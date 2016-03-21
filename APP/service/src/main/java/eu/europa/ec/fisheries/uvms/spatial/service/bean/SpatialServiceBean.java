package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.simplify.DouglasPeuckerSimplifier;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.interceptors.TracingInterceptor;
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
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.GeometryMapper;
import eu.europa.ec.fisheries.uvms.spatial.util.SpatialFunctionFactory;
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
        spatialFunction = new SpatialFunctionFactory(properties).getInstance();
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public List<Location> getClosestPointToPointByType(final ClosestLocationSpatialRQ request) throws ServiceException {
        // TODO convert point

        final Map<String, Location> distancePerTypeMap = new HashMap<>();
        final Double latitude = request.getPoint().getLatitude();
        final Double longitude = request.getPoint().getLongitude();
        final Integer crs = request.getPoint().getCrs();
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
                    .append(spatialFunction.stDistance(longitude, latitude, crs)).append(" AS distance ")
                    .append("FROM spatial.").append(next.getAreaDbTable())
                    .append(" WHERE enabled = 'Y' ORDER BY distance ASC ")
                    .append(spatialFunction.limit(10)).append(")");
            it.remove(); // avoids a ConcurrentModificationException
            if (it.hasNext()) {
                sb.append(" UNION ALL ");
            }
        }

        final Query emNativeQuery = em.createNativeQuery(sb.toString());
        emNativeQuery.unwrap(SQLQuery.class).addScalar("type", StringType.INSTANCE).addScalar(GID, IntegerType.INSTANCE)
                .addScalar(CODE, StringType.INSTANCE).addScalar(NAME, StringType.INSTANCE).addScalar(GEOM, GeometryType.INSTANCE)
                .addScalar("distance", DoubleType.INSTANCE);

        final List records = emNativeQuery.getResultList();

        for (Object record : records) {
            final Object[] result = (Object[]) record;
            final Point centroid = ((Geometry) result[4]).getCentroid();
            calc.setStartingGeographicPoint(centroid.getX(), centroid.getY());
            calc.setDestinationGeographicPoint(longitude, latitude);
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
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public List<Area> getClosestAreasToPointByType(final ClosestAreaSpatialRQ request) throws ServiceException {

        final Point point = convertToPointInWGS84(request.getPoint());

        final MeasurementUnit measurementUnit = MeasurementUnit.getMeasurement(request.getUnit().name());
        final Map<String, String> areaType2TableName = new HashMap<>();
        final List<AreaLocationTypesEntity> areas = repository.findAllIsLocation(false);
        final List<Area> closestAreas = new ArrayList<>();

        for (AreaLocationTypesEntity area : areas) {
            areaType2TableName.put(area.getTypeName().toUpperCase(), area.getAreaDbTable());
        }

        for (AreaType areaType : request.getAreaTypes().getAreaTypes()) {

            String areaDbTable = areaType2TableName.get(areaType.value());
            final eu.europa.ec.fisheries.uvms.spatial.model.schemas.GeometryType geometryType = GeometryMapper.INSTANCE.geometryToWKT(point);
            final Integer crs = point.getSRID();
            final Double unitRatio = measurementUnit.getRatio();

            // FIXME native query alert
            String queryString = "WITH prox_query AS (SELECT gid, code, name, " + spatialFunction.stClosestPoint(point.getY(), point.getX(), crs) + " AS closestPoint " +
                    "FROM spatial." + areaDbTable + " " +
                    "WHERE NOT ST_IsEmpty(geom) AND enabled = 'Y' " +
                    "ORDER BY geom <#> st_geomfromtext(CAST('" + geometryType.getGeometry() + "' AS text), " + crs + ") limit 30) " +
                    "SELECT gid, code, name, st_length_spheroid(st_makeline(closestPoint, st_geomfromtext(CAST('" + geometryType.getGeometry() + "' AS text), " + crs + ")), 'SPHEROID[\"WGS 84\",6378137,298.257223563]') /" + unitRatio + " AS distance " +
                    "FROM prox_query " +
                    "ORDER BY distance " + spatialFunction.limit(1);

            Query emNativeQuery = em.createNativeQuery(queryString);
            emNativeQuery.unwrap(SQLQuery.class)
                    .addScalar(GID, IntegerType.INSTANCE)
                    .addScalar(CODE, StringType.INSTANCE)
                    .addScalar(NAME, StringType.INSTANCE)
                    .addScalar("distance", DoubleType.INSTANCE);

            final List records  = emNativeQuery.getResultList();
            final Iterator it = records.iterator();

            Area area = new Area();

            while (it.hasNext( )) {
                final Object[] result = (Object[])it.next();
                it.remove(); // avoids a ConcurrentModificationException
                area.setId(String.valueOf(result[0]));
                area.setCode(String.valueOf(result[1]));
                area.setName(String.valueOf(result[2]));
                area.setDistance(Double.valueOf(String.valueOf(result[3])));
                area.setUnit(request.getUnit());
                area.setAreaType(areaType);
                closestAreas.add(area);
            }
        }

        return closestAreas;
    }

    @Override
    public List<Area> getClosestAreasToPointByTypeGeneric(final ClosestAreaSpatialRQ request) throws ServiceException {

        final Point point = convertToPointInWGS84(request.getPoint());
        final MeasurementUnit measurementUnit = MeasurementUnit.getMeasurement(request.getUnit().name());
        final List<AreaLocationTypesEntity> typesEntities = repository.findAllIsLocation(false);
        final StringBuilder sb = new StringBuilder();
        final Map<String, Location> distancePerTypeMap = new HashMap<>();

        Iterator<AreaLocationTypesEntity> it = typesEntities.iterator();
        while (it.hasNext()) {
            AreaLocationTypesEntity next = it.next();
            final String areaDbTable = next.getAreaDbTable();
            final String typeName = next.getTypeName();
            sb.append("SELECT '" + typeName + "' AS type, gid, code, name, " + spatialFunction.stClosestPoint(point.getY(), point.getX(), point.getSRID()) + " AS closest " +
                    "FROM spatial." + areaDbTable + " " +
                    "WHERE NOT ST_IsEmpty(geom) AND enabled = 'Y' " +
                    "ORDER BY " + spatialFunction.stDistance(point.getY(), point.getX(), point.getSRID()) + spatialFunction.limit(10));
            it.remove(); // avoids a ConcurrentModificationException
            if (it.hasNext()) {
                sb.append(" UNION ALL ");
            }
        }

        Query emNativeQuery = em.createNativeQuery(sb.toString());

        emNativeQuery.unwrap(SQLQuery.class).addScalar(TYPE, StandardBasicTypes.STRING)
                .addScalar(GID, StandardBasicTypes.INTEGER).addScalar(CODE, StandardBasicTypes.STRING)
                .addScalar(NAME, StandardBasicTypes.STRING).addScalar("closest", GeometryType.INSTANCE);

        List records = emNativeQuery.getResultList();

        for (Object record : records) {
            final Object[] result = (Object[]) record;

        }
        return null;
    }

    @Override
    public List<AreaExtendedIdentifierType> getAreasByPoint(final AreaByLocationSpatialRQ request) throws ServiceException {

        // TODO convert point
        final Integer crs = request.getPoint().getCrs();
        final double latitude = request.getPoint().getLatitude();
        final double longitude = request.getPoint().getLongitude();
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
                    .append(spatialFunction.stIntersects(latitude, longitude, crs))
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

        if ((userAreas == null || isEmpty(userAreas.getUserAreas()))
                && (scopeAreas == null || isEmpty(scopeAreas.getScopeAreas()))) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
        }

        try {

            buildQuery(scopeAreas.getScopeAreas(), sb, "scope");
            if (StringUtils.isNotEmpty(sb.toString())){
                sb.append(" UNION ALL ");
            }
            buildQuery(userAreas.getUserAreas(), sb, "user");

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
            GeometryCollection userUnion = (GeometryCollection) geometryFactory.buildGeometry(userGeometryList).union();
            GeometryCollection scopeUnion = (GeometryCollection) geometryFactory.buildGeometry(scopeGeometryList).union();

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

    private void buildQuery(List<AreaIdentifierType> typeList, StringBuilder sb, String type) {

        Iterator<AreaIdentifierType> it = typeList.iterator();

        while (it.hasNext()) {
            AreaIdentifierType next = it.next();
            final String id = next.getId();
            final AreaType areaType = next.getAreaType();
            sb.append("SELECT '").append(type).append("' as type ,geom FROM spatial.").append(areaType.value())
                    .append(" spatial WHERE spatial.gid = ").append(id);
            it.remove(); // avoids a ConcurrentModificationException
            if (it.hasNext()) {
                sb.append(" UNION ALL ");
            }
        }
    }

    @Override
    @Transactional
    public List<GenericSystemAreaDto> searchAreasByNameOrCode(final String tableName, final String filter) throws ServiceException {

        final AreaLocationTypesEntity areaLocationType = repository.findAreaLocationTypeByTypeName(tableName.toUpperCase());
        final String toUpperCase = filter.toUpperCase();
        final ArrayList<GenericSystemAreaDto> systemAreaByFilterRecords = new ArrayList<>();
        final WKTWriter2 wktWriter2 = new WKTWriter2();
        final StringBuilder sb = new StringBuilder();

        if (areaLocationType == null) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR, areaLocationType);
        }

        sb.append("SELECT gid, name, code, geom FROM spatial.")
                .append(tableName).append(" ").append("WHERE UPPER(name) LIKE '%")
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
                            result[2].toString(), tableName.toUpperCase(), wktWriter2.write(envelope), result[1].toString()));
        }

        return systemAreaByFilterRecords;

    }

    @Override
    @Transactional
    public LocationDetails getLocationDetails(final LocationTypeEntry locationTypeEntry) throws ServiceException {
        // TODO convert point?
        final GeodeticCalculator calc = new GeodeticCalculator();
        final Map<String, Object> properties;
        final String id = locationTypeEntry.getId();
        final String locationType = locationTypeEntry.getLocationType();
        final List<LocationProperty> locationProperties = new ArrayList<>();
        final AreaLocationTypesEntity locationTypesEntity =
                repository.findAreaLocationTypeByTypeName(locationType.toUpperCase());

        if (locationTypeEntry.getId() != null) {

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

            Double longitude = locationTypeEntry.getLongitude();
            Double latitude = locationTypeEntry.getLatitude();
            Integer crs = locationTypeEntry.getCrs();

            List list = new ArrayList();

            if (locationType.equals("PORT")){

                final String queryString = "SELECT * FROM spatial.port WHERE enabled = 'Y' ORDER BY " + spatialFunction.stDistance(longitude, latitude, crs) + " ASC " + spatialFunction.limit(15); // FIXME this can and should be replaces by NamedQuery
                Query emNativeQuery = em.createNativeQuery(queryString, PortsEntity.class);
                List<PortsEntity> records = emNativeQuery.getResultList();

                PortsEntity closestLocation = null;
                Double closestDistance = Double.MAX_VALUE;
                for (PortsEntity portsEntity : records) {

                    final Geometry geometry = portsEntity.getGeom();
                    final Point centroid = geometry.getCentroid();
                    calc.setStartingGeographicPoint(centroid.getX(), centroid.getY());
                    calc.setDestinationGeographicPoint(longitude, latitude);
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
                Point point = convertToPointInWGS84(longitude, latitude, crs);
                list = repository.findAreaOrLocationByCoordinates(point, getNativeQueryByType(locationTypesEntity.getTypeName()));
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
