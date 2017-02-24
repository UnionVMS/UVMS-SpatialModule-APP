/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.bean.impl;

import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.simplify.DouglasPeuckerSimplifier;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.interceptors.TracingInterceptor;
import eu.europa.ec.fisheries.uvms.mapper.GeometryMapper;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.AbstractAreaDao;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.util.DAOFactory;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.util.DatabaseDialect;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.util.DatabaseDialectFactory;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.BaseAreaEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.PortEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.UserAreasEntity;
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
import eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialService;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.area.GenericSystemAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.area.SystemAreaNamesDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.area.UserAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.enums.MeasurementUnit;
import eu.europa.ec.fisheries.uvms.spatial.service.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.exception.SpatialServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.CRS;
import org.geotools.referencing.GeodeticCalculator;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.vividsolutions.jts.operation.distance.DistanceOp.nearestPoints;
import static eu.europa.ec.fisheries.uvms.spatial.service.bean.impl.GeometryUtils.*;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;
import static org.geotools.geometry.jts.JTS.orthodromicDistance;
import static org.geotools.geometry.jts.JTS.toGeographic;

/**
 * This class groups all the spatial operations on the spatial database.
 */
@Stateless
@Transactional
@Slf4j
@Interceptors(TracingInterceptor.class)
public class SpatialServiceBean implements SpatialService {

    private static final String EPSG = "EPSG:";
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

        if (request == null){
            throw new ServiceException("REQUEST CAN NOT BE NULL");
        }
        if (request.getPoint() == null){
            throw new ServiceException("POINT CAN NOT BE NULL");
        }

        Double lat = request.getPoint().getLatitude();
        Double lon = request.getPoint().getLongitude();
        Integer crs = request.getPoint().getCrs();

        if (lat == null || lon == null || crs == null){
            throw new ServiceException("MISSING MANDATORY FIELDS");
        }

        final Point incoming = toWgs84Point(lat, lon, crs);
        final Double incomingLatitude = incoming.getY();
        final Double incomingLongitude = incoming.getX();
        final Map<String, Location> distancePerTypeMap = new HashMap<>();
        final UnitType unit = request.getUnit();
        final MeasurementUnit measurementUnit = MeasurementUnit.getMeasurement(unit.name());
        final GeodeticCalculator calc = new GeodeticCalculator(DEFAULT_CRS);
        final List<AreaLocationTypesEntity> typeEntities = repository.findAllIsPointIsSystemWide(true, true);

        List records = repository.closestPoint(typeEntities, databaseDialect, incoming);

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

        Point point = toWgs84Point(areaTypeEntry.getLatitude(),areaTypeEntry.getLongitude(), areaTypeEntry.getCrs());

        List list = DAOFactory.getAbstractSpatialDao(em, areaLocationTypesEntity.getTypeName()).findByIntersect(point);
        List<AreaDetails> areaDetailsList = new ArrayList<>();

        for (Object area : list) {
            Map<String, Object> properties = ((BaseAreaEntity)area).getFieldMap();

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
                areaProperty.setPropertyValue(String.valueOf(((BaseAreaEntity) area).getId()));
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

        Point point = toWgs84Point(coordinate.getLatitude(), coordinate.getLongitude(), coordinate.getCrs());

        List<UserAreasEntity> userAreaDetailsWithExtentByLocation = repository.findUserAreaDetailsByLocation(userName, point);

        List<UserAreaDto> userAreaDtos = new ArrayList<>();
        for (UserAreasEntity userAreaDetails : userAreaDetailsWithExtentByLocation){
            UserAreaDto userAreaDto = new UserAreaDto();
            userAreaDto.setGid(userAreaDetails.getId());
            userAreaDto.setDesc(userAreaDetails.getAreaDesc());
            userAreaDto.setExtent(GeometryMapper.INSTANCE.geometryToWkt(userAreaDetails.getGeom().getEnvelope()).getValue());
            userAreaDto.setName(userAreaDetails.getName());
            userAreaDto.setAreaType(userAreaDetails.getType());
            userAreaDtos.add(userAreaDto);
        }

        return userAreaDtos;
    }

    @Override
    @Transactional
    public List<AreaDetails> getUserAreaDetailsByLocation(AreaTypeEntry areaTypeEntry, String userName) throws ServiceException {
        Point point = toWgs84Point(areaTypeEntry.getLatitude(), areaTypeEntry.getLongitude(), areaTypeEntry.getCrs());
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
        if (geometry != null) {
            properties.put("centroid", GeometryUtils.wktToCentroidWkt(String.valueOf(geometry)));
        }
    }

    @Override
    public List<Area> getClosestArea(final ClosestAreaSpatialRQ request) throws ServiceException {

        final Map<String, Area> distancePerTypeMap = new HashMap<>();

        try {

            if (request == null){
                throw new ServiceException("REQUEST CAN NOT BE NULL");
            }
            if (request.getPoint() == null){
                throw new ServiceException("POINT CAN NOT BE NULL");
            }

            Double lat = request.getPoint().getLatitude();
            Double lon = request.getPoint().getLongitude();
            Integer crs = request.getPoint().getCrs();

            if (lat == null || lon == null || crs == null){
                throw new ServiceException("MISSING MANDATORY FIELDS");
            }

            final Point incoming = toWgs84Point(lat, lon, crs);

            final MeasurementUnit measurementUnit = MeasurementUnit.getMeasurement(request.getUnit().name());
            final UnitType unit = request.getUnit();
            final List<AreaLocationTypesEntity> typesEntities = repository.findAllIsPointIsSystemWide(false, true);
            List records = repository.closestArea(typesEntities, databaseDialect, incoming);

            for (Object record : records) {

                final Object[] result = (Object[]) record;
                Geometry geom = (Geometry) result[4];

                if (geom.isEmpty()){
                    continue;
                }

                final com.vividsolutions.jts.geom.Coordinate[] coordinates = nearestPoints(geom, incoming);
                final Double orthodromicDistance = orthodromicDistance(coordinates[0], coordinates[1], DEFAULT_CRS);

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

        if (request == null){
            throw new ServiceException("REQUEST CAN NOT BE NULL");
        }
        if (request.getPoint() == null){
            throw new ServiceException("POINT CAN NOT BE NULL");
        }

        Double lat = request.getPoint().getLatitude();
        Double lon = request.getPoint().getLongitude();
        Integer crs = request.getPoint().getCrs();

        if (lat == null || lon == null || crs == null){
            throw new ServiceException("MISSING MANDATORY FIELDS");
        }

        final Point incoming = toWgs84Point(lat, lon, crs);
        final List<AreaLocationTypesEntity> typesEntities = repository.findAllIsPointIsSystemWide(false, true);
        final List<AreaExtendedIdentifierType> areaTypes = new ArrayList<>();

        List records = repository.intersectingArea(typesEntities, databaseDialect, incoming);

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

            if(userAreas != null){
                if ( CollectionUtils.isNotEmpty(userAreas.getUserAreas()) && StringUtils.isNotEmpty(sb.toString())){
                    sb.append(" UNION ALL ");
                }
                buildQuery(userAreas.getUserAreas(), sb, "user", typesEntityMap);
            }

            log.debug("{} QUERY => {}", sb.toString());

            List records = repository.listBaseAreaList(sb.toString());

            final List<Geometry> scopeGeometryList = new ArrayList<>();
            final List<Geometry> userGeometryList = new ArrayList<>();

            for (Object record : records) {
                final Object[] result = (Object[]) record;
                final String type = (String) result[0];
                Geometry geometry = (Geometry) result[1];
                if (!isDefaultCrs(geometry.getSRID())){
                    geometry = toGeographic(geometry, CRS.decode(EPSG + Integer.toString(geometry.getSRID()), true));
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
                response.setGeometry(GeometryMapper.INSTANCE.geometryToWkt(intersection).getValue());
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

        List<BaseAreaEntity> baseEntities = DAOFactory.getAbstractSpatialDao(em, areaLocationType.getTypeName()).searchEntity(filter);
        for (BaseAreaEntity entity : baseEntities) {
            Geometry geometry = entity.getGeom();
            Geometry envelope = geometry.getGeometryType().equalsIgnoreCase(MULTIPOINT) ? geometry : entity.getGeom().getEnvelope();
            systemAreaByFilterRecords.add(
                    new GenericSystemAreaDto(entity.getId().intValue(), entity.getCode(),areaType.toUpperCase(),
                            GeometryMapper.INSTANCE.geometryToWkt(envelope).getValue(), entity.getName()));
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
        List<BaseAreaEntity> baseEntities = DAOFactory.getAbstractSpatialDao(em, areaLocationType.getTypeName()).searchNameByCode(filter);

        List<SystemAreaNamesDto> systemAreas = new ArrayList<>();
        for (BaseAreaEntity baseEntity : baseEntities) {
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
            systemAreaNamesDto.setExtent(GeometryMapper.INSTANCE.geometryToWkt(unionGeom.getEnvelope()).getValue());
        }

        return systemAreas;
    }

    @Override
    @Transactional
    public LocationDetails getLocationDetails(final LocationTypeEntry locationTypeEntry) throws ServiceException {

        final GeodeticCalculator calc = new GeodeticCalculator(DEFAULT_CRS);
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

            AbstractAreaDao dao = DAOFactory.getAbstractSpatialDao(em, locationTypesEntity.getTypeName());
            BaseAreaEntity areaEntity = dao.findOne(Long.parseLong(locationTypeEntry.getId()));

            if (areaEntity == null) {
                throw new SpatialServiceException(SpatialServiceErrors.ENTITY_NOT_FOUND, locationTypesEntity.getTypeName());
            }

            properties = areaEntity.getFieldMap();

        }

        else {

            Map<String, Object> fieldMap = new HashMap<>();
            List list = new ArrayList();
            Point point = toWgs84Point(incomingLatitude, incomingLongitude, locationTypeEntry.getCrs());

            List<PortEntity> records = repository.listClosestPorts(point, 5);
            PortEntity closestLocation = null;
            Double closestDistance = Double.MAX_VALUE;
            for (PortEntity portsEntity : records) {

                final Geometry geometry = portsEntity.getGeom();
                final Point centroid = geometry.getCentroid();
                calc.setStartingGeographicPoint(centroid.getX(), centroid.getY());
                calc.setDestinationGeographicPoint(point.getX(), point.getY());
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
        return GeometryMapper.INSTANCE.geometryToWkt(geometry).getValue();

    }

}