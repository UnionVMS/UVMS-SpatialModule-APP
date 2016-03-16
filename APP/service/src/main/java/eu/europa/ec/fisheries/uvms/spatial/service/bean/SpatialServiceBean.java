package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.dao.GisFunction;
import eu.europa.ec.fisheries.uvms.spatial.dao.PostGres;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.PortsEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.area.SystemAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.ClosestAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.FilterAreasDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.util.MeasurementUnit;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.geotools.geometry.jts.WKTReader2;
import org.geotools.geometry.jts.WKTWriter2;
import org.geotools.referencing.GeodeticCalculator;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import static com.google.common.collect.Lists.newArrayList;
import static eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialUtils.convertToPointInWGS84;
import static eu.europa.ec.fisheries.uvms.spatial.util.ColumnAliasNameHelper.getFieldMap;
import static eu.europa.ec.fisheries.uvms.spatial.util.SpatialTypeEnum.getEntityClassByType;
import static eu.europa.ec.fisheries.uvms.spatial.util.SpatialTypeEnum.getNativeQueryByType;
import static org.apache.commons.collections.CollectionUtils.isEmpty;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * This class groups all the spatial operations on the spatial database.
 */
@Stateless
@Local(SpatialService.class)
@Transactional
@Slf4j
public class SpatialServiceBean implements SpatialService {

    private static final String TYPE_NAMES = "typeNames";
    private static final String TYPE_NAME = "typeName";

    private @PersistenceContext(unitName = "spatialPU") EntityManager em;
    private @EJB SpatialRepository repository;

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public List<Location> getClosestLocationByLocationType(final ClosestLocationSpatialRQ request) throws ServiceException {

        try {

            final List<Location> closestLocations = new ArrayList<>();
            final Double latitude = request.getPoint().getLatitude();
            final Double longitude = request.getPoint().getLongitude();
            final Integer crs = request.getPoint().getCrs();
            final UnitType unit = request.getUnit();
            final MeasurementUnit measurementUnit = MeasurementUnit.getMeasurement(unit.name());

            List<AreaLocationTypesEntity> locationTypesEntities = repository.findEntityByNamedQuery(AreaLocationTypesEntity.class, AreaLocationTypesEntity.FIND_ALL_LOCATIONS);

            Map<String, String> locationMap = Maps.newHashMap();

            for (AreaLocationTypesEntity location : locationTypesEntities) {
                locationMap.put(location.getTypeName().toUpperCase(), location.getAreaDbTable());
            }

            final GisFunction gisFunction = new PostGres();
            final WKTReader2 wktReader2 = new WKTReader2();
            final GeodeticCalculator calc = new GeodeticCalculator();

            for (LocationType locationType : request.getLocationTypes().getLocationTypes()) {

                Location closestLocation = null;
                Double closestDistance = Double.MAX_VALUE;

                final String areaDbTable = locationMap.get(locationType.value());

                final String queryString = "SELECT gid, code, name, "+ gisFunction.toWkt("geom")+ ", " + gisFunction.stDistance(longitude, latitude, crs) + " AS distance " +
                        "FROM spatial." + areaDbTable + " WHERE enabled = 'Y' ORDER BY distance ASC " + gisFunction.limit(15);

                final Query emNativeQuery = em.createNativeQuery(queryString);
                final List records = emNativeQuery.getResultList();

                for (Object record : records) {

                    final Object[] result = (Object[]) record;
                    final String wkt = result[3].toString();
                    final Geometry geometry = wktReader2.read(wkt);
                    final Point centroid = geometry.getCentroid();
                    calc.setStartingGeographicPoint(centroid.getX(), centroid.getY());
                    calc.setDestinationGeographicPoint(longitude, latitude);
                    Double orthodromicDistance = calc.getOrthodromicDistance();

                    if (closestDistance > orthodromicDistance) {
                        closestDistance = orthodromicDistance;
                        closestLocation = new Location();
                        closestLocation.setDistance(orthodromicDistance);
                        closestLocation.setId(result[0].toString());
                        closestLocation.setDistance(orthodromicDistance / measurementUnit.getRatio());
                        closestLocation.setUnit(unit);
                        closestLocation.setCode(result[1].toString());
                        closestLocation.setName(result[2].toString());
                        closestLocation.setLocationType(locationType);
                    }

                }

                if (closestLocation != null){
                    closestLocations.add(closestLocation);
                }

            }

            return closestLocations;

        } catch (ParseException ex) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR, ex);
        }
    }

    @Override
    @SneakyThrows
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public List<AreaExtendedIdentifierType> getAreaTypesByLocation(final AreaByLocationSpatialRQ request) {

        final Integer crs = request.getPoint().getCrs();
        final double latitude = request.getPoint().getLatitude();
        final double longitude = request.getPoint().getLongitude();

        List<AreaLocationTypesEntity> systemAreaTypes = repository.findEntityByNamedQuery(AreaLocationTypesEntity.class, QueryNameConstants.FIND_SYSTEM_AREAS);

        List<AreaExtendedIdentifierType> areaTypes = Lists.newArrayList();

        PostGres function = new PostGres();

        for (AreaLocationTypesEntity areaType : systemAreaTypes) {

            String areaDbTable = areaType.getAreaDbTable();

            String queryString = "SELECT gid, name, code FROM spatial." + areaDbTable +
                    " WHERE " + function.stIntersects(latitude, longitude, crs) + " AND enabled = 'Y'";

            Query emNativeQuery = em.createNativeQuery(queryString);

            emNativeQuery.unwrap(SQLQuery.class)
                    .addScalar("gid", StandardBasicTypes.INTEGER)
                    .addScalar("code", StandardBasicTypes.STRING)
                    .addScalar("name", StandardBasicTypes.STRING)
                    .setResultTransformer(Transformers.aliasToBean(SystemAreaDto.class));

            List<SystemAreaDto> resultList = emNativeQuery.getResultList();

            for (SystemAreaDto area : resultList) {
                AreaExtendedIdentifierType areaIdentifier = new AreaExtendedIdentifierType(String.valueOf(area.getGid()), AreaType.valueOf(areaType.getTypeName()), area.getCode(), area.getName());
                areaTypes.add(areaIdentifier);
            }
        }
        return areaTypes;
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public List<Area> getClosestAreas(ClosestAreaSpatialRQ request) throws ServiceException {

        Point point = convertToPointInWGS84(request.getPoint());

        MeasurementUnit measurementUnit = MeasurementUnit.getMeasurement(request.getUnit().name());

        Map<String, String> areaType2TableName = new HashMap<>();

        List<AreaLocationTypesEntity> areas = repository.findEntityByNamedQuery(AreaLocationTypesEntity.class, QueryNameConstants.FIND_ALL_AREAS);

        for (AreaLocationTypesEntity area : areas) {
            areaType2TableName.put(area.getTypeName().toUpperCase(), area.getAreaDbTable());
        }

        List<Area> closestAreas = newArrayList();

        for (AreaType areaType : request.getAreaTypes().getAreaTypes()) {
            if (areaType!= null) {
                String areaDbTable = areaType2TableName.get(areaType.value());

                List<ClosestAreaDto> closestAreaList = repository.findClosestArea(point, measurementUnit, areaDbTable);

                if (closestAreaList.size() > 1) {
                    throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
                }

                ClosestAreaDto closestAreaDto = closestAreaList.get(0);

                if (closestAreaDto != null) {
                    Area closestAreaEntry = new Area();
                    closestAreaEntry.setId(closestAreaDto.getId());
                    closestAreaEntry.setDistance(closestAreaDto.getDistance());
                    closestAreaEntry.setUnit(request.getUnit());
                    closestAreaEntry.setCode(closestAreaDto.getCode());
                    closestAreaEntry.setName(closestAreaDto.getName());
                    closestAreaEntry.setAreaType(areaType);
                    closestAreas.add(closestAreaEntry);
                }
            }
        }

        return closestAreas;
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public FilterAreasSpatialRS filterAreas(final FilterAreasSpatialRQ request) throws ServiceException {

        final UserAreasType userAreas = request.getUserAreas();
        final ScopeAreasType scopeAreas = request.getScopeAreas();

        if ((userAreas == null || isEmpty(userAreas.getUserAreas()))
                && (scopeAreas == null || isEmpty(scopeAreas.getScopeAreas()))) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
        }

        Function<AreaIdentifierType, String> extractAreaType = new Function<AreaIdentifierType, String>() {

            @Override
            public String apply(AreaIdentifierType area) {
                if (area != null && area.getAreaType() != null) {
                    return area.getAreaType().toString().toUpperCase();
                }
                return StringUtils.EMPTY;
            }
        };

        Function<AreaIdentifierType, String> extractAreaId = new Function<AreaIdentifierType, String>() {

            @Override
            public String apply(AreaIdentifierType area) {

                if (area != null) {
                    return area.getId();
                }
                return StringUtils.EMPTY;
            }
        };

        List<String> userAreaTypes = Collections.emptyList();
        List<String> userAreaIds = Collections.emptyList();
        List<String> scopeAreaTypes = Collections.emptyList();
        List<String> scopeAreaIds = Collections.emptyList();

        if (userAreas != null) {
            userAreaTypes = Lists.transform(userAreas.getUserAreas(), extractAreaType);
            userAreaIds = Lists.transform(userAreas.getUserAreas(), extractAreaId);
        }
        if (scopeAreas != null) {
            scopeAreaTypes = Lists.transform(scopeAreas.getScopeAreas(), extractAreaType);
            scopeAreaIds = Lists.transform(scopeAreas.getScopeAreas(), extractAreaId);
        }

        Map<String, List<String>> parameters = ImmutableMap.<String, List<String>>builder().put(TYPE_NAMES, userAreaTypes).build();

        List<AreaLocationTypesEntity> areaEntities = repository.findEntityByNamedQuery(AreaLocationTypesEntity.class, QueryNameConstants.FIND_TYPE_BY_NAMES, parameters);

        final Map<String, String> areaType2TableName = Maps.newHashMap();
        for (AreaLocationTypesEntity areaEntity : areaEntities) {
            areaType2TableName.put(areaEntity.getTypeName(), areaEntity.getAreaDbTable());
        }

        for (String areaType : userAreaTypes) {
            String userAreaTableName = areaType2TableName.get(areaType);
            if (isBlank(userAreaTableName)) {
                throw new SpatialServiceException(SpatialServiceErrors.INVALID_AREA_TYPE, areaType);
            }
        }

        List<String> userAreaTables = Lists.transform(userAreaTypes, new Function<String, String>() {
            @Override
            public String apply(String areaType) {
                return areaType2TableName.get(areaType);
            }
        });

        List<String> scopeAreaTables = Lists.transform(scopeAreaTypes, new Function<String, String>() {
            @Override
            public String apply(String areaType) {
                return areaType2TableName.get(areaType);
            }
        });

        // FIXME Oracle problem
        FilterAreasDto result = repository.filterAreas(userAreaTables, userAreaIds, scopeAreaTables, scopeAreaIds);

        FilterAreasSpatialRS response = new FilterAreasSpatialRS();
        response.setGeometry(result.getWktgeometry());
        response.setCode(result.getResultcode());
        return response;

    }

    @Override
    @Transactional
    public List<SystemAreaDto> getAreasByFilter(final String tableName, final String filter) throws ServiceException {

        try {
            AreaLocationTypesEntity areaLocationType = repository.findAreaLocationTypeByTypeName(tableName.toUpperCase());

            if (areaLocationType == null) {
                throw new SpatialServiceException(SpatialServiceErrors.INVALID_AREA_LOCATION_TYPE, areaLocationType);
            }

            GisFunction gisFunction = new PostGres();
            final String toUpperCase = filter.toUpperCase();
            final String queryString= "SELECT gid, name, code, CAST(" + gisFunction.toWkt("geom") +" AS " + gisFunction.castAsUnlimitedLength() + ")" +
                    " FROM spatial." + tableName + " WHERE UPPER(name) LIKE '%" + toUpperCase + "%' OR code LIKE '%" + toUpperCase + "%' GROUP BY gid";

            final Query emNativeQuery = em.createNativeQuery(queryString);
            final List records = emNativeQuery.getResultList();
            Iterator it = records.iterator();

            final ArrayList<SystemAreaDto> systemAreaByFilterRecords = new ArrayList<>();
            final WKTReader2 wktReader2 = new WKTReader2();
            final WKTWriter2 wktWriter2 = new WKTWriter2();

            while (it.hasNext( )) {
                final Object[] result = (Object[])it.next();
                final String wkt = result[3].toString();
                Geometry geometry = wktReader2.read(wkt);
                final Geometry envelope = geometry.getEnvelope();

                final String wktEnvelope = wktWriter2.write(envelope);
                systemAreaByFilterRecords.add(
                        new SystemAreaDto(Integer.valueOf(result[0].toString()),
                                result[2].toString(), tableName.toUpperCase(), wktEnvelope, result[1].toString()));
            }

            return systemAreaByFilterRecords;

        } catch (ParseException e) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR, e.getMessage());
        }
    }


    @Override
    @Transactional
    public LocationDetails getLocationDetails(LocationTypeEntry locationTypeEntry) throws ServiceException {

        final GisFunction gisFunction = new PostGres();
        final WKTReader2 wktReader2 = new WKTReader2();
        final GeodeticCalculator calc = new GeodeticCalculator();

        String id = locationTypeEntry.getId();
        String locationType = locationTypeEntry.getLocationType();

        Map<String, String> parameters = ImmutableMap.<String, String>builder().put(TYPE_NAME, locationType.toUpperCase()).build();
        List<AreaLocationTypesEntity> areasLocationTypes =
                repository.findEntityByNamedQuery(AreaLocationTypesEntity.class, AreaLocationTypesEntity.FIND_TYPE_BY_NAME, parameters, 1);//Fixme greg replace with dao call
        if (areasLocationTypes.isEmpty()) {
            throw new SpatialServiceException(SpatialServiceErrors.INVALID_AREA_LOCATION_TYPE, areasLocationTypes);
        }

        AreaLocationTypesEntity locationTypesEntity = areasLocationTypes.get(0);
        Map<String, Object> properties;

        if (locationTypeEntry.getId() != null) {

            if (!StringUtils.isNumeric(id)) {
                throw new SpatialServiceException(SpatialServiceErrors.INVALID_ID_TYPE, id);
            }

            Object object = repository.findEntityById(getEntityClassByType(locationTypesEntity.getTypeName()), Long.parseLong(locationTypeEntry.getId()));

            if (object == null) {
                throw new SpatialServiceException(SpatialServiceErrors.ENTITY_NOT_FOUND, locationTypesEntity.getTypeName());
            }

            properties = getFieldMap(object);

        } else {

            Map<String, Object> fieldMap = new HashMap();

            Double longitude = locationTypeEntry.getLongitude();
            Double latitude = locationTypeEntry.getLatitude();
            Integer crs = locationTypeEntry.getCrs();

            List list = new ArrayList();

            if (locationType.equals("PORT")){

                final String queryString = "SELECT * FROM spatial.port WHERE enabled = 'Y' ORDER BY " + gisFunction.stDistance(longitude, latitude, crs) + " ASC " + gisFunction.limit(15); // FIXME this can and should be replaces by NamedQuery
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

        List<LocationProperty> locationProperties = new ArrayList<>();

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
