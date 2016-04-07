package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.simplify.DouglasPeuckerSimplifier;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.interceptors.TracingInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.dao.Oracle;
import eu.europa.ec.fisheries.uvms.spatial.dao.util.SpatialFunction;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.PortsEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Area;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaByLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaExtendedIdentifierType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.FilterAreasSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.FilterAreasSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Location;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationProperty;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ScopeAreasType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.UnitType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.UserAreasType;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.GenericSystemAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.util.MeasurementUnit;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import eu.europa.ec.fisheries.uvms.spatial.util.DatabaseDialectFactory;
import eu.europa.ec.fisheries.uvms.spatial.util.PropertiesBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.WKTWriter2;
import org.geotools.referencing.CRS;
import org.geotools.referencing.GeodeticCalculator;
import org.hibernate.SQLQuery;
import org.hibernate.spatial.GeometryType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.StringType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialUtils.DEFAULT_CRS;
import static eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialUtils.convertToPointInWGS84;
import static eu.europa.ec.fisheries.uvms.spatial.util.ColumnAliasNameHelper.getFieldMap;
import static eu.europa.ec.fisheries.uvms.spatial.util.SpatialTypeEnum.getEntityClassByType;
import static eu.europa.ec.fisheries.uvms.spatial.util.SpatialTypeEnum.getNativeQueryByType;
import static org.apache.commons.collections.CollectionUtils.isEmpty;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

/**
 * This class groups all the spatial operations on the spatial database.
 */
@Stateless
@Local(SpatialService.class)
@Transactional
@Slf4j
@Interceptors(TracingInterceptor.class)
public class SpatialServiceBean implements SpatialService {

    private static final String EPSG = "EPSG:";
    private static final String GEOM = "geom";
    private static final String GID = "gid";
    private static final String NAME = "name";
    private static final String CODE = "code";
    private static final String TYPE = "type";

    private @PersistenceContext(unitName = "spatialPU") EntityManager em;
    private @EJB SpatialRepository repository;
    private @EJB PropertiesBean properties;
    private SpatialFunction spatialFunction;

    @PostConstruct
    public void init(){
        spatialFunction = new DatabaseDialectFactory(properties).getInstance();
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public List<Location> getClosestPointToPointByType(final ClosestLocationSpatialRQ request) throws ServiceException {

        final Point incomingPoint = SpatialUtils.convertToPointInWGS84(request.getPoint());
        final Double incomingLatitude = incomingPoint.getY();
        final Double incomingLongitude = incomingPoint.getX();
        final Map<String, Location> distancePerTypeMap = new HashMap<>();
        final UnitType unit = request.getUnit();
        final MeasurementUnit measurementUnit = MeasurementUnit.getMeasurement(unit.name());
        final GeodeticCalculator calc = new GeodeticCalculator();
        final List<AreaLocationTypesEntity> typeEntities = repository.findAllIsPointIsSystemWide(true, true);
        final StringBuilder sb = new StringBuilder();

        Iterator<AreaLocationTypesEntity> it = typeEntities.iterator();
        while (it.hasNext()) {
            AreaLocationTypesEntity next = it.next();
            String typeName = next.getTypeName();
            sb.append("(SELECT '").append(typeName).append("' as type, gid, code, name, geom, ")
                    .append(spatialFunction.stDistance(incomingLatitude, incomingLongitude)).append(" AS distance ")
                    .append("FROM spatial.").append(next.getAreaDbTable())
                    .append(" WHERE enabled = 'Y'");

                    if (spatialFunction instanceof Oracle){
                        sb.append(" AND ");
                        sb.append(spatialFunction.limit(10)).append(")");
                        sb.append(" ORDER BY distance ASC ");
                    } else {
                        sb.append(" ORDER BY distance ASC ");
                        sb.append(spatialFunction.limit(10)).append(")");
                    }

            it.remove(); // avoids a ConcurrentModificationException
            if (it.hasNext()) {
                sb.append(" UNION ALL ");
            }
        }

        log.debug("{} QUERY => {}", spatialFunction.getClass().getSimpleName().toUpperCase(), sb.toString());

        final Query emNativeQuery = em.createNativeQuery(sb.toString());
        emNativeQuery.unwrap(SQLQuery.class).addScalar("type", StringType.INSTANCE).addScalar(GID, IntegerType.INSTANCE)
                .addScalar(CODE, StringType.INSTANCE).addScalar(NAME, StringType.INSTANCE).addScalar(GEOM, GeometryType.INSTANCE)
                .addScalar("distance", DoubleType.INSTANCE);

        final List records = emNativeQuery.getResultList();

        for (Object record : records) {
            final Object[] result = (Object[]) record;
            final Point centroid = ((Geometry) result[4]).getCentroid();
            calc.setStartingGeographicPoint(centroid.getX(), centroid.getY());
            calc.setDestinationGeographicPoint(incomingLongitude, incomingLatitude);
            Double orthodromicDistance = calc.getOrthodromicDistance();
            final String type = result[0].toString();
            Location closest = distancePerTypeMap.get(type);

            if (closest == null || orthodromicDistance / measurementUnit.getRatio() < closest.getDistance()) {

                if (closest == null) {
                    closest = new Location();
                }

                closest.setDistance(orthodromicDistance);
                closest.setId(result[1].toString());
                closest.setDistance(orthodromicDistance / measurementUnit.getRatio());
                closest.setUnit(unit);
                closest.setCode(result[2].toString());
                closest.setName(result[3].toString());
                closest.setLocationType(LocationType.fromValue(type));
                distancePerTypeMap.put(type, closest);
            }
        }
        return new ArrayList<>(distancePerTypeMap.values());
    }

    @Override
    public List<Area> getClosestAreasToPointByType(final ClosestAreaSpatialRQ request) throws ServiceException {

        final Point incomingPoint = convertToPointInWGS84(request.getPoint());
        final Double incomingLatitude = incomingPoint.getY();
        final Double incomingLongitude = incomingPoint.getX();
        final MeasurementUnit measurementUnit = MeasurementUnit.getMeasurement(request.getUnit().name());
        final UnitType unit = request.getUnit();
        final List<AreaLocationTypesEntity> typesEntities = repository.findAllIsPointIsSystemWide(false, true);
        final StringBuilder sb = new StringBuilder();
        final Map<String, Area> distancePerTypeMap = new HashMap<>();
        final GeodeticCalculator calc = new GeodeticCalculator();

        Iterator<AreaLocationTypesEntity> it = typesEntities.iterator();
        while (it.hasNext()) {
            AreaLocationTypesEntity next = it.next();
            final String areaDbTable = next.getAreaDbTable();
            final String typeName = next.getTypeName();
            sb.append("(SELECT '").append(typeName).append("' AS type, gid, code, name, ")
                    .append(spatialFunction.stClosestPoint(incomingLatitude, incomingLongitude))
                    .append(" AS closest ").append("FROM spatial.").append(areaDbTable).append(" ")
                    .append("WHERE NOT ST_IsEmpty(geom) AND enabled = 'Y' ").append("ORDER BY ") // TODO check isEmpty in oracl"
                    .append(spatialFunction.stDistance(incomingLatitude, incomingLongitude)).append(" ")
                    .append(spatialFunction.limit(10)).append(")");
            it.remove(); // avoids a ConcurrentModificationException
            if (it.hasNext()) {
                sb.append(" UNION ALL ");
            }
        }

        log.debug("{} QUERY => {}", spatialFunction.getClass().getSimpleName().toUpperCase(), sb.toString());

        Query emNativeQuery = em.createNativeQuery(sb.toString());

        emNativeQuery.unwrap(SQLQuery.class).addScalar(TYPE, StandardBasicTypes.STRING)
                .addScalar(GID, StandardBasicTypes.INTEGER).addScalar(CODE, StandardBasicTypes.STRING)
                .addScalar(NAME, StandardBasicTypes.STRING).addScalar("closest", GeometryType.INSTANCE);

        List records = emNativeQuery.getResultList();

        for (Object record : records) {
            final Object[] result = (Object[]) record;
            final Point closestPoint = (Point) result[4];
            calc.setStartingGeographicPoint(closestPoint.getX(), closestPoint.getY());
            calc.setDestinationGeographicPoint(incomingLongitude, incomingLatitude);
            Double orthodromicDistance = calc.getOrthodromicDistance();

            final String type = result[0].toString();
            Area closest = distancePerTypeMap.get(type);

            if (closest == null || orthodromicDistance / measurementUnit.getRatio() < closest.getDistance()) {

                if (closest == null) {
                    closest = new Area();
                }

                closest.setDistance(orthodromicDistance);
                closest.setId(result[1].toString());
                closest.setDistance(orthodromicDistance / measurementUnit.getRatio());
                closest.setUnit(unit);
                closest.setCode(result[2].toString());
                closest.setName(result[3].toString());
                closest.setAreaType(AreaType.valueOf(type));
                distancePerTypeMap.put(type, closest);
            }

        }
        return new ArrayList<>(distancePerTypeMap.values());
    }

    @Override
    public List<AreaExtendedIdentifierType> getAreasByPoint(final AreaByLocationSpatialRQ request) throws ServiceException {

        final Point incomingPoint = convertToPointInWGS84(request.getPoint());
        final Double incomingLatitude = incomingPoint.getY();
        final Double incomingLongitude = incomingPoint.getX();
        final StringBuilder sb = new StringBuilder();
        final List<AreaLocationTypesEntity> typesEntities = repository.findAllIsPointIsSystemWide(false, true);
        final List<AreaExtendedIdentifierType> areaTypes = new ArrayList<>();

        Iterator<AreaLocationTypesEntity> it = typesEntities.iterator();
        while (it.hasNext()) {
            AreaLocationTypesEntity next = it.next();
            final String areaDbTable = next.getAreaDbTable();
            final String typeName = next.getTypeName();
            sb.append("SELECT '").append(typeName).append("' as type, gid, name, code FROM spatial.")
                    .append(areaDbTable).append(" WHERE ")
                    .append(spatialFunction.stIntersects(incomingLatitude, incomingLongitude))
                    .append(" AND enabled = 'Y'");
            it.remove(); // avoids a ConcurrentModificationException
            if (it.hasNext()) {
                sb.append(" UNION ALL ");
            }
        }

        Query emNativeQuery = em.createNativeQuery(sb.toString());

        emNativeQuery.unwrap(SQLQuery.class)
                .addScalar(TYPE, StandardBasicTypes.STRING)
                .addScalar(GID, StandardBasicTypes.INTEGER)
                .addScalar(CODE, StandardBasicTypes.STRING)
                .addScalar(NAME, StandardBasicTypes.STRING);

        List records = emNativeQuery.getResultList();

        for (Object record : records) {
            final Object[] result = (Object[]) record;
            AreaExtendedIdentifierType area = new AreaExtendedIdentifierType();
            area.setAreaType(AreaType.valueOf(String.valueOf(result[0])));
            area.setId(String.valueOf(result[1]));
            area.setName(String.valueOf(result[2]));
            area.setCode(String.valueOf(result[3]));
            areaTypes.add(area);
        }

        return areaTypes;
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public FilterAreasSpatialRS computeAreaFilter(final FilterAreasSpatialRQ request) throws ServiceException {

        final UserAreasType userAreas = request.getUserAreas();
        final ScopeAreasType scopeAreas = request.getScopeAreas();
        final StringBuilder sb = new StringBuilder();
        final List<AreaLocationTypesEntity> typesEntities = repository.findAllIsLocation(false);
        final Map<String, AreaLocationTypesEntity> typesEntityMap = new HashMap<>();

        for (AreaLocationTypesEntity typesEntity : typesEntities){
            typesEntityMap.put(typesEntity.getTypeName(), typesEntity);
        }

        if ((userAreas == null || isEmpty(userAreas.getUserAreas()))
                && (scopeAreas == null || isEmpty(scopeAreas.getScopeAreas()))) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
        }

        try {

            buildQuery(scopeAreas.getScopeAreas(), sb, "scope", typesEntityMap);
            if (StringUtils.isNotEmpty(sb.toString())){
                sb.append(" UNION ALL ");
            }
            buildQuery(userAreas.getUserAreas(), sb, "user", typesEntityMap);

            Query emNativeQuery = em.createNativeQuery(sb.toString());
            emNativeQuery.unwrap(SQLQuery.class)
                    .addScalar(TYPE, StringType.INSTANCE)
                    .addScalar(GEOM, GeometryType.INSTANCE);
            List records = emNativeQuery.getResultList();

            final List<Geometry> scopeGeometryList = new ArrayList<>();
            final List<Geometry> userGeometryList = new ArrayList<>();

            for (Object record : records) {
                final Object[] result = (Object[]) record;
                final String type = (String) result[0];
                Geometry geometry = (Geometry) result[1];
                if (!SpatialUtils.isDefaultCrs(geometry.getSRID())){
                    geometry = JTS.toGeographic(geometry,
                            CRS.decode(EPSG + Integer.toString(geometry.getSRID()), true));
                }
                if ("user".equals(type)){
                    userGeometryList.add(geometry);
                } else {
                    scopeGeometryList.add(geometry);
                }
            }

            GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

            Geometry userUnion = geometryFactory.buildGeometry(userGeometryList).union();
            Geometry scopeUnion = geometryFactory.buildGeometry(scopeGeometryList).union();

            FilterAreasSpatialRS response = new FilterAreasSpatialRS(null,0);
            Geometry intersection = null;

            if (!userUnion.isEmpty() && !scopeUnion.isEmpty()){
                intersection = userUnion.intersection(scopeUnion);
                response.setCode(3);
                if (intersection.isEmpty()){
                    intersection = scopeUnion;
                    response.setCode(4);
                }
            }
            else if(!userUnion.isEmpty()){
                intersection = userUnion;
                response.setCode(1);
            }
            else if(!scopeUnion.isEmpty()){
                intersection = scopeUnion;
                response.setCode(2);
            }

            assert intersection != null;
            if (intersection.getNumPoints() > 20000){
                intersection = DouglasPeuckerSimplifier.simplify(intersection, 0.5);
            }

            response.setGeometry(new WKTWriter2().write(intersection));
            return response;

        } catch (TransformException | FactoryException ex) {
            throw new ServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR.toString(), ex);
        }

    }

    private void buildQuery(List<AreaIdentifierType> typeList, StringBuilder sb, String type, Map<String, AreaLocationTypesEntity> typesEntityMap ) {

        Iterator<AreaIdentifierType> it = typeList.iterator();

        while (it.hasNext()) {
            AreaIdentifierType next = it.next();
            final String id = next.getId();
            final AreaType areaType = next.getAreaType();
            final AreaLocationTypesEntity locationTypesEntity = typesEntityMap.get(areaType.value());
            sb.append("SELECT '").append(type).append("' as type ,geom FROM spatial.").append(locationTypesEntity.getAreaDbTable())
                    .append(" spatial WHERE spatial.gid = ").append(id);
            it.remove(); // avoids a ConcurrentModificationException
            if (it.hasNext()) {
                sb.append(" UNION ALL ");
            }
        }
    }

    @Override
    @Transactional
    public List<GenericSystemAreaDto> searchAreasByNameOrCode(final String areaType, final String filter) throws ServiceException {

        AreaLocationTypesEntity areaLocationType = null;
        final String toUpperCase = filter.toUpperCase();
        final ArrayList<GenericSystemAreaDto> systemAreaByFilterRecords = new ArrayList<>();
        final WKTWriter2 wktWriter2 = new WKTWriter2();
        final StringBuilder sb = new StringBuilder();

        List<AreaLocationTypesEntity> areaLocationTypeByTypeName = repository.findAreaLocationTypeByTypeName(areaType.toUpperCase());

        if (areaLocationTypeByTypeName != null && !areaLocationTypeByTypeName.isEmpty()){
            areaLocationType = areaLocationTypeByTypeName.get(0);
        }

        if (areaLocationType == null) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
        }

        sb.append("SELECT gid, name, code, geom FROM spatial.")
                .append(areaLocationType.getAreaDbTable()).append(" ").append("WHERE UPPER(name) LIKE '%")
                .append(toUpperCase).append("%' OR code LIKE '%").append(toUpperCase).append("%' GROUP BY gid");

        final Query emNativeQuery = em.createNativeQuery(sb.toString());
        emNativeQuery.unwrap(SQLQuery.class)
                .addScalar(GID, IntegerType.INSTANCE)
                .addScalar(NAME, StringType.INSTANCE)
                .addScalar(CODE, StringType.INSTANCE)
                .addScalar(GEOM, GeometryType.INSTANCE);

        final List records = emNativeQuery.getResultList();
        Iterator it = records.iterator();

        while (it.hasNext( )) {
            final Object[] result = (Object[])it.next();
            it.remove(); // avoids a ConcurrentModificationException
            final Geometry envelope = ((Geometry)result[3]).getEnvelope();
            systemAreaByFilterRecords.add(
                    new GenericSystemAreaDto(Integer.valueOf(result[0].toString()),
                            result[2].toString(), areaType.toUpperCase(), wktWriter2.write(envelope), result[1].toString()));
        }

        return systemAreaByFilterRecords;

    }

    @Override
    @Transactional
    public LocationDetails getLocationDetails(final LocationTypeEntry locationTypeEntry) throws ServiceException {

        final GeodeticCalculator calc = new GeodeticCalculator();
        final Map<String, Object> properties;
        final String id = locationTypeEntry.getId();
        final String locationType = locationTypeEntry.getLocationType();
        final List<LocationProperty> locationProperties = new ArrayList<>();
        AreaLocationTypesEntity locationTypesEntity = null;

        List<AreaLocationTypesEntity> areaLocationTypeByTypeName =
                repository.findAreaLocationTypeByTypeName(locationType.toUpperCase());

        if(areaLocationTypeByTypeName != null && !areaLocationTypeByTypeName.isEmpty()){
            locationTypesEntity = areaLocationTypeByTypeName.get(0);
        }

        if (locationTypesEntity != null && locationTypeEntry.getId() != null) {

            if (!StringUtils.isNumeric(id)) {
                throw new SpatialServiceException(SpatialServiceErrors.INVALID_ID_TYPE, id);
            }
            // FIXME @Greg DAO
            Object object = repository.findEntityById(getEntityClassByType(locationTypesEntity.getTypeName()), Long.parseLong(locationTypeEntry.getId()));

            if (object == null) {
                throw new SpatialServiceException(SpatialServiceErrors.ENTITY_NOT_FOUND, locationTypesEntity.getTypeName());
            }

            properties = getFieldMap(object);

        } else {

            Map<String, Object> fieldMap = new HashMap<>();

            final Point incomingPoint =
                    convertToPointInWGS84(locationTypeEntry.getLongitude(), locationTypeEntry.getLatitude(), DEFAULT_CRS);
            final Double incomingLatitude = incomingPoint.getY();
            final Double incomingLongitude = incomingPoint.getX();

            List list = new ArrayList();

            if (locationType.equals("PORT")){

                final String queryString = "SELECT * FROM spatial.port WHERE enabled = 'Y' ORDER BY " +
                        spatialFunction.stDistance(incomingLongitude, incomingLatitude) + " ASC " + spatialFunction.limit(15); // FIXME this can and should be replaces by NamedQuery
                Query emNativeQuery = em.createNativeQuery(queryString, PortsEntity.class);
                List<PortsEntity> records = emNativeQuery.getResultList();

                PortsEntity closestLocation = null;
                Double closestDistance = Double.MAX_VALUE;
                for (PortsEntity portsEntity : records) {

                    final Geometry geometry = portsEntity.getGeom();
                    final Point centroid = geometry.getCentroid();
                    calc.setStartingGeographicPoint(centroid.getX(), centroid.getY());
                    calc.setDestinationGeographicPoint(incomingLongitude, incomingLatitude);
                    Double orthodromicDistance = calc.getOrthodromicDistance();

                    if (closestDistance > orthodromicDistance) {
                        closestDistance = orthodromicDistance;
                        closestLocation = portsEntity;
                    }
                }

                if (closestLocation != null){
                    list.add(closestLocation);
                }
            }
            else {
                list = repository.findAreaOrLocationByCoordinates(incomingPoint, getNativeQueryByType(locationTypesEntity.getTypeName()));
            }

            if (isNotEmpty(list)) {
                fieldMap = getFieldMap(list.get(0));
            }

            properties = fieldMap;
        }

        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            LocationProperty locationProperty = new LocationProperty();
            locationProperty.setPropertyName(entry.getKey());
            locationProperty.setPropertyValue(entry.getValue()!=null?entry.getValue().toString():null);
            locationProperties.add(locationProperty);
        }

        LocationDetails locationDetails = new LocationDetails();
        locationDetails.setLocationType(locationTypeEntry);
        locationDetails.getLocationProperties().addAll(locationProperties);

        return locationDetails;

    }

}
