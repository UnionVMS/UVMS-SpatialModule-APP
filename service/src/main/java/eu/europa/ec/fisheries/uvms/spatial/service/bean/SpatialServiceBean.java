package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.operation.distance.DistanceOp;
import com.vividsolutions.jts.simplify.DouglasPeuckerSimplifier;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.interceptors.TracingInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.dao.AbstractSpatialDao;
import eu.europa.ec.fisheries.uvms.spatial.dao.util.DAOFactory;
import eu.europa.ec.fisheries.uvms.spatial.dao.util.DatabaseDialect;
import eu.europa.ec.fisheries.uvms.spatial.dao.util.DatabaseDialectFactory;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.BaseSpatialEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.PortEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.UserAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Area;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaByLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaExtendedIdentifierType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaProperty;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Coordinate;
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
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.GenericSystemAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.SystemAreaNamesDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.UserAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.util.MeasurementUnit;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
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
import java.util.*;

import static eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialUtils.convertToPointInWGS84;
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
    private static final String TYPE = "type";
    private static final String GEOM = "geom";
    private static final String MULTIPOINT = "MULTIPOINT";

    private @PersistenceContext(unitName = "spatialPU") EntityManager em;
    private @EJB SpatialRepository repository;
    private @EJB PropertiesBean properties;
    private DatabaseDialect databaseDialect;

    @PostConstruct
    public void init(){
        databaseDialect = new DatabaseDialectFactory(properties).getInstance();
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
        final GeodeticCalculator calc = new GeodeticCalculator(SpatialUtils.getDefaultCrs());
        final List<AreaLocationTypesEntity> typeEntities = repository.findAllIsPointIsSystemWide(true, true);

        List records = repository.closestPoint(typeEntities, databaseDialect, incomingPoint);

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

        AreaLocationTypesEntity areaLocationTypesEntity = repository.findAreaLocationTypeByTypeName(areaTypeEntry.getAreaType().value().toUpperCase());

        Point point = SpatialUtils.convertToPointInWGS84(areaTypeEntry.getLongitude(), areaTypeEntry.getLatitude(), areaTypeEntry.getCrs());

        List list = DAOFactory.getAbstractSpatialDao(em, areaLocationTypesEntity.getTypeName()).findByIntersect(point);
        List<AreaDetails> areaDetailsList = new ArrayList<>();

        for (Object area : list) {
            Map<String, Object> properties = ((BaseSpatialEntity)area).getFieldMap();

            List<AreaProperty> areaProperties = new ArrayList<>();
            for (Map.Entry<String, Object> entry : properties.entrySet()) {
                AreaProperty areaProperty = new AreaProperty();
                areaProperty.setPropertyName(entry.getKey());
                areaProperty.setPropertyValue(entry.getValue());
                areaProperties.add(areaProperty);
            }
            if (!properties.isEmpty()) {
                AreaProperty areaProperty = new AreaProperty();
                areaProperty.setPropertyName("gid");
                areaProperty.setPropertyValue(String.valueOf(((BaseSpatialEntity) area).getId()));
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
            userAreaDto.setGid(userAreaDetails.getId());
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
    public List<Area> getClosestArea(final ClosestAreaSpatialRQ request) throws ServiceException {

        final Map<String, Area> distancePerTypeMap = new HashMap<>();

        try {

            final Point incomingPoint = convertToPointInWGS84(request.getPoint());
            final MeasurementUnit measurementUnit = MeasurementUnit.getMeasurement(request.getUnit().name());
            final UnitType unit = request.getUnit();
            final List<AreaLocationTypesEntity> typesEntities = repository.findAllIsPointIsSystemWide(false, true);
            List records = repository.closestArea(typesEntities, databaseDialect, incomingPoint);

            for (Object record : records) {

                final Object[] result = (Object[]) record;
                Geometry geom = (Geometry) result[4];

                if (geom.isEmpty()){
                    continue;
                }

                final com.vividsolutions.jts.geom.Coordinate[] coordinates =
                        DistanceOp.nearestPoints(geom, incomingPoint);
                final Double orthodromicDistance =
                        JTS.orthodromicDistance(coordinates[0], coordinates[1], SpatialUtils.getDefaultCrs());

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
                    closest.setCode(result[2] != null ? result[2].toString() : null);
                    closest.setName(result[3] != null ? result[3].toString() : null);
                    closest.setAreaType(AreaType.valueOf(type));
                    distancePerTypeMap.put(type, closest);
                }

            }
        }
        catch (TransformException e) {
            throw new ServiceException("ERROR WHILE CALCULATING DISTANCE", e);
        }

        return new ArrayList<>(distancePerTypeMap.values());
    }

    @Override
    public List<AreaExtendedIdentifierType> getAreasByPoint(final AreaByLocationSpatialRQ request) throws ServiceException {

        final Point incomingPoint = convertToPointInWGS84(request.getPoint());
        final List<AreaLocationTypesEntity> typesEntities = repository.findAllIsPointIsSystemWide(false, true);
        final List<AreaExtendedIdentifierType> areaTypes = new ArrayList<>();

        List records = repository.intersectingArea(typesEntities, databaseDialect, incomingPoint);

        for (Object record : records) {
            final Object[] result = (Object[]) record;
            AreaExtendedIdentifierType area = new AreaExtendedIdentifierType();
            area.setAreaType(AreaType.valueOf(String.valueOf(result[0])));
            area.setId(String.valueOf(result[1]));
            area.setCode(String.valueOf(result[2]));
            area.setName(String.valueOf(result[3]));
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

        try {

            buildQuery(scopeAreas.getScopeAreas(), sb, "scope", typesEntityMap);
            if (CollectionUtils.isNotEmpty(userAreas.getUserAreas()) && StringUtils.isNotEmpty(sb.toString())){
                sb.append(" UNION ALL ");
            }
            buildQuery(userAreas.getUserAreas(), sb, "user", typesEntityMap);

            log.debug("{} QUERY => {}", databaseDialect.getClass().getSimpleName().toUpperCase(), sb.toString());

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

            if (intersection != null){
                if (intersection.getNumPoints() > 20000){
                    intersection = DouglasPeuckerSimplifier.simplify(intersection, 0.5);
                }
                response.setGeometry(new WKTWriter2().write(intersection));
            }

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
            sb.append("(SELECT '").append(type).append("' as type ,geom FROM spatial.").append(locationTypesEntity.getAreaDbTable())
                    .append(" spatial WHERE spatial.gid = ").append(id).append(" AND enabled = 'Y')");
            it.remove(); // avoids a ConcurrentModificationException
            if (it.hasNext()) {
                sb.append(" UNION ALL ");
            }
        }
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public List<GenericSystemAreaDto> searchAreasByNameOrCode(final String areaType, final String filter) throws ServiceException {

        AreaLocationTypesEntity areaLocationType = repository.findAreaLocationTypeByTypeName(areaType.toUpperCase());

        if (areaLocationType == null) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
        }
        final ArrayList<GenericSystemAreaDto> systemAreaByFilterRecords = new ArrayList<>();
        final WKTWriter2 wktWriter2 = new WKTWriter2();

        List<BaseSpatialEntity> baseEntities = DAOFactory.getAbstractSpatialDao(em, areaLocationType.getTypeName()).searchEntity(filter);
        for (BaseSpatialEntity entity : baseEntities) {
            Geometry geometry = entity.getGeom();
            Geometry envelope = geometry.getGeometryType().equalsIgnoreCase(MULTIPOINT) ? geometry : entity.getGeom().getEnvelope();
            systemAreaByFilterRecords.add(new GenericSystemAreaDto(entity.getId().intValue(), entity.getCode(),areaType.toUpperCase(), wktWriter2.write(envelope), entity.getName()));
        }

        return systemAreaByFilterRecords;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SystemAreaNamesDto> searchAreasByCode(String areaType, String filter) throws ServiceException {
        AreaLocationTypesEntity areaLocationType = repository.findAreaLocationTypeByTypeName(areaType.toUpperCase());

        if (areaLocationType == null) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
        }
        List<BaseSpatialEntity> baseEntities = DAOFactory.getAbstractSpatialDao(em, areaLocationType.getTypeName()).searchNameByCode(filter);

        List<SystemAreaNamesDto> systemAreas = new ArrayList<>();
        for (BaseSpatialEntity baseEntity : baseEntities) {
            boolean isAdded = false;
            for (SystemAreaNamesDto systemAreaNamesDto : systemAreas) {
                if (systemAreaNamesDto.getCode().equalsIgnoreCase(baseEntity.getCode())) {
                    systemAreaNamesDto.getAreaNames().add(baseEntity.getName());
                    List<Geometry> geometries = systemAreaNamesDto.getGeoms();
                    Geometry geometry = baseEntity.getGeom();
                    geometries.add(geometry);
                    isAdded = true;
                }
            }
            if (!isAdded) {
                systemAreas.add(new SystemAreaNamesDto(baseEntity.getCode(), new HashSet<String>(Arrays.asList(baseEntity.getName())), new ArrayList(Arrays.asList(baseEntity.getGeom()))));
            }
        }

        for (SystemAreaNamesDto systemAreaNamesDto : systemAreas) {
            GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
            Geometry unionGeom = geometryFactory.buildGeometry(systemAreaNamesDto.getGeoms()).union();
            systemAreaNamesDto.setExtent(new WKTWriter2().write(unionGeom.getEnvelope()));
        }

        return systemAreas;
    }

    @Override
    @Transactional
    public LocationDetails getLocationDetails(final LocationTypeEntry locationTypeEntry) throws ServiceException {

        final GeodeticCalculator calc = new GeodeticCalculator(SpatialUtils.getDefaultCrs());
        final Map<String, Object> properties;
        final String id = locationTypeEntry.getId();
        final String locationType = locationTypeEntry.getLocationType();
        final List<LocationProperty> locationProperties = new ArrayList<>();
        AreaLocationTypesEntity locationTypesEntity;
        final Double incomingLatitude = locationTypeEntry.getLatitude();
        final Double incomingLongitude = locationTypeEntry.getLongitude();

        if (id != null && !StringUtils.isNumeric(id)) {
            throw new SpatialServiceException(SpatialServiceErrors.INVALID_ID_TYPE, id);
        }

        locationTypesEntity = repository.findAreaLocationTypeByTypeName(locationType.toUpperCase());

        if (locationTypesEntity == null){
            throw new ServiceException("TYPE CANNOT BE NULL");
        }
        else if (!locationTypesEntity.getIsLocation()){
            throw new ServiceException(locationTypesEntity.getTypeName() + " IS NOT A LOCATION");
        }

        if (locationTypeEntry.getId() != null) {

            AbstractSpatialDao dao = DAOFactory.getAbstractSpatialDao(em, locationTypesEntity.getTypeName());
            BaseSpatialEntity areaEntity = dao.findOne(Long.parseLong(locationTypeEntry.getId()));

            if (areaEntity == null) {
                throw new SpatialServiceException(SpatialServiceErrors.ENTITY_NOT_FOUND, locationTypesEntity.getTypeName());
            }

            properties = areaEntity.getFieldMap();

        }

        else {

            Map<String, Object> fieldMap = new HashMap<>();
            List list = new ArrayList();
            Point point = convertToPointInWGS84(incomingLongitude, incomingLatitude, locationTypeEntry.getCrs());

            List<PortEntity> records = repository.listClosestPorts(point, 5);
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
            if (isNotEmpty(list)) {
                fieldMap = ((BaseSpatialEntity)list.iterator().next()).getFieldMap();
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
