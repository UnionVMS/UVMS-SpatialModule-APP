package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.simplify.DouglasPeuckerSimplifier;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.interceptors.TracingInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.dao.util.Oracle;
import eu.europa.ec.fisheries.uvms.spatial.dao.util.SpatialFunction;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.BaseAreaEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.PortEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.UserAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Coordinate;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Location;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.GenericSystemAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.UserAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.util.MeasurementUnit;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import eu.europa.ec.fisheries.uvms.spatial.dao.util.DatabaseDialectFactory;
import eu.europa.ec.fisheries.uvms.spatial.util.PropertiesBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.WKTReader2;
import org.geotools.geometry.jts.WKTWriter2;
import org.geotools.referencing.CRS;
import org.geotools.referencing.GeodeticCalculator;
import org.hibernate.SQLQuery;
import org.hibernate.spatial.GeometryType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.mapstruct.ap.internal.util.Collections;
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
    @Transactional
    public List<AreaDetails> getAreaDetailsByLocation(AreaTypeEntry areaTypeEntry) throws ServiceException {

        AreaType areaType = areaTypeEntry.getAreaType();

        if (areaType == null) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR, StringUtils.EMPTY);
        }

        List<AreaLocationTypesEntity> areasLocationTypes =
                repository.findAreaLocationTypeByTypeName(areaTypeEntry.getAreaType().value().toUpperCase());

        if (areasLocationTypes.isEmpty()) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR, areasLocationTypes);
        }

        AreaLocationTypesEntity areaLocationTypesEntity = areasLocationTypes.iterator().next();

        Point point = SpatialUtils.convertToPointInWGS84(areaTypeEntry.getLongitude(), areaTypeEntry.getLatitude(), areaTypeEntry.getCrs());

        List allAreas = Collections.newArrayList();

        switch (areaLocationTypesEntity.getTypeName().toUpperCase()){
            case "EEZ" :
                allAreas = repository.findEezByIntersect(point);
                break;
            case "PORTAREA" :
                allAreas = repository.findPortAreaByIntersect(point);
                break;
            case "RFMO" :
                allAreas = repository.findRfmoByIntersect(point);
                break;
            case "USERAREA" :
                allAreas = repository.findUserAreaByIntersect(point);
                break;
            case "PORT" :
                allAreas = repository.findUserAreaByIntersect(point);
                break;
            default:
                throw new IllegalArgumentException("Unsupported area type.");
        }
        // more areas here

        List<AreaDetails> areaDetailsList = new ArrayList<>();

        for (Object allArea : allAreas) {
            Map<String, Object> properties = ((BaseAreaEntity)allArea).getFieldMap();

            List<AreaProperty> areaProperties = new ArrayList<>();
            for (Map.Entry<String, Object> entry : properties.entrySet()) {
                AreaProperty areaProperty = new AreaProperty();
                areaProperty.setPropertyName(entry.getKey());
                areaProperty.setPropertyValue(entry.getValue());
                areaProperties.add(areaProperty);
            }

            AreaDetails areaDetails = new AreaDetails();
            areaDetails.setAreaType(areaTypeEntry);
            areaDetails.getAreaProperties().addAll(areaProperties);
            areaDetailsList.add(areaDetails);
        }
        return areaDetailsList;

    }

    @Override
    @Transactional
    public List<UserAreaDto> getUserAreaDetailsWithExtentByLocation(Coordinate coordinate, String userName) throws ServiceException {
        Point point = convertToPointInWGS84(coordinate.getLongitude(), coordinate.getLatitude(), coordinate.getCrs());

        List<UserAreasEntity> userAreaDetailsWithExtentByLocation = repository.findUserAreaDetailsByLocation(userName, point);

        List<UserAreaDto> userAreaDtos = new ArrayList<>();
        for (UserAreasEntity userAreaDetails : userAreaDetailsWithExtentByLocation){
            UserAreaDto userAreaDto = new UserAreaDto();
            userAreaDto.setGid(userAreaDetails.getGid());
            userAreaDto.setDesc(userAreaDetails.getAreaDesc());
            userAreaDto.setExtent(new WKTWriter2().write(userAreaDetails.getGeom().getEnvelope()));
            userAreaDto.setName(userAreaDetails.getName());
            userAreaDto.setAreaType(userAreaDetails.getType());
            userAreaDtos.add(userAreaDto);
        }

        return userAreaDtos;
    }

    @Override
    @Transactional
    public List<AreaDetails> getUserAreaDetailsByLocation(AreaTypeEntry areaTypeEntry, String userName) throws ServiceException {
        Point point = convertToPointInWGS84(areaTypeEntry.getLongitude(), areaTypeEntry.getLatitude(), areaTypeEntry.getCrs());
        List<UserAreasEntity> userAreaDetails = repository.findUserAreaDetailsByLocation(userName, point);
        try {
            List<AreaDetails> areaDetailsList = Lists.newArrayList();
            for (UserAreasEntity userAreaDetail : userAreaDetails) {
                Map<String, Object> properties = userAreaDetail.getFieldMap();
                addCentroidToProperties(properties);

                List<AreaProperty> areaProperties = new ArrayList<>();
                for (Map.Entry<String, Object> entry : properties.entrySet()) {
                    AreaProperty areaProperty = new AreaProperty();
                    areaProperty.setPropertyName(entry.getKey());
                    areaProperty.setPropertyValue(entry.getValue());
                    areaProperties.add(areaProperty);
                }

                AreaDetails areaDetails = new AreaDetails();
                areaDetails.setAreaType(areaTypeEntry);
                areaDetails.getAreaProperties().addAll(areaProperties);
                areaDetailsList.add(areaDetails);
            }
            return areaDetailsList;

        } catch (ParseException e) {
            String error = "Error while trying to parse geometry";
            log.error(error);
            throw new ServiceException(error);
        }
    }

    private void addCentroidToProperties(Map<String, Object> properties) throws ParseException {
        Object geometry = properties.get("geometry");
        if(geometry != null){
            Geometry centroid = new WKTReader2().read(String.valueOf(geometry)).getCentroid();
            properties.put("centroid", new WKTWriter2().write(centroid));
        }
    }

    @Override
    public List<Area> getClosestAreasToPointByType(final ClosestAreaSpatialRQ request) throws ServiceException {

        final Point incomingPoint = convertToPointInWGS84(request.getPoint());
        final Double incomingLatitude = incomingPoint.getY();
        final Double incomingLongitude = incomingPoint.getX();
        final MeasurementUnit measurementUnit = MeasurementUnit.getMeasurement(request.getUnit().name());
        final UnitType unit = request.getUnit();
        final List<AreaLocationTypesEntity> typesEntities = repository.findAllIsPointIsSystemWide(false, true);
        final Map<String, Area> distancePerTypeMap = new HashMap<>();
        final GeodeticCalculator calc = new GeodeticCalculator();

        List records = repository.closestArea(typesEntities, spatialFunction, incomingPoint);

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
        final List<AreaLocationTypesEntity> typesEntities = repository.findAllIsPointIsSystemWide(false, true);
        final List<AreaExtendedIdentifierType> areaTypes = new ArrayList<>();

        List records = repository.intersectingArea(typesEntities, spatialFunction, incomingPoint);

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

        if (id != null && !StringUtils.isNumeric(id)) {
            throw new SpatialServiceException(SpatialServiceErrors.INVALID_ID_TYPE, id);
        }

        List<AreaLocationTypesEntity> areaLocationTypeByTypeName =
                repository.findAreaLocationTypeByTypeName(locationType.toUpperCase());

        if(CollectionUtils.isNotEmpty(areaLocationTypeByTypeName)){
            locationTypesEntity = areaLocationTypeByTypeName.iterator().next();
        }

        if (locationTypesEntity != null && locationTypeEntry.getId() != null) {

            BaseAreaEntity areaEntity = repository.findAreaByTypeAndId(locationTypesEntity.getTypeName(), Long.parseLong(locationTypeEntry.getId()));

            if (areaEntity == null) {
                throw new SpatialServiceException(SpatialServiceErrors.ENTITY_NOT_FOUND, locationTypesEntity.getTypeName());
            }

            properties = areaEntity.getFieldMap();

        } else {

            Map<String, Object> fieldMap = new HashMap<>();

            final Point incomingPoint =
                    convertToPointInWGS84(locationTypeEntry.getLongitude(), locationTypeEntry.getLatitude(), DEFAULT_CRS);
            final Double incomingLatitude = incomingPoint.getY();
            final Double incomingLongitude = incomingPoint.getX();

            List list = new ArrayList();

            if (locationType.equals("PORT")){

                final String queryString = "SELECT * FROM spatial.port WHERE enabled = 'Y' ORDER BY " +
                        spatialFunction.stDistance(incomingLongitude, incomingLatitude) + " ASC " + spatialFunction.limit(15); // FIXME this can be replaces by NamedQuery
                Query emNativeQuery = em.createNativeQuery(queryString, PortEntity.class);
                List<PortEntity> records = emNativeQuery.getResultList();

                PortEntity closestLocation = null;
                Double closestDistance = Double.MAX_VALUE;
                for (PortEntity portsEntity : records) {

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

                assert locationTypesEntity != null;
                switch (locationTypesEntity.getTypeName().toUpperCase()){
                    case "EEZ":
                        list = repository.findEezByIntersect(incomingPoint);
                    break;
                    case "RFMO":
                        list = repository.findRfmoByIntersect(incomingPoint);
                        break;
                    case "USERAREA":
                        list = repository.findUserAreaByIntersect(incomingPoint);
                        break;
                    case "PORTAREA":
                        list = repository.findPortAreaByIntersect(incomingPoint);
                        break;
                    default:
                        throw new IllegalArgumentException("Unsupported area type.");
                }
            }

            if (isNotEmpty(list)) {
                fieldMap = ((BaseAreaEntity)list.iterator().next()).getFieldMap();
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

    @Override
    public String calculateBuffer(final Double latitude, final Double longitude, final Double buffer) {

        GeometryFactory gf = new GeometryFactory();
        Point point = gf.createPoint(new com.vividsolutions.jts.geom.Coordinate(longitude, latitude));
        Geometry geometry = point.buffer(buffer);
        return new WKTWriter2().write(geometry);

    }

    @Override
    public String translate(final Double tx, final Double ty, final String wkt) throws ServiceException {

        try {

            Geometry geometry = new WKTReader2().read(wkt);
            Geometry translate = SpatialUtils.translate(tx, ty, geometry);
            return new WKTWriter2().write(translate);

        } catch (Exception ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

}
