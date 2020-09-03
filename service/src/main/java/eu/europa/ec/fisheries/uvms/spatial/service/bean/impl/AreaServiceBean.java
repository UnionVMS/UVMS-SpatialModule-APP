/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.spatial.service.bean.impl;

import static com.vividsolutions.jts.operation.distance.DistanceOp.nearestPoints;
import static eu.europa.ec.fisheries.uvms.commons.geometry.utils.GeometryUtils.isDefaultEpsgSRID;
import static org.geotools.geometry.jts.JTS.orthodromicDistance;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.simplify.DouglasPeuckerSimplifier;
import eu.europa.ec.fisheries.uvms.commons.domain.BaseEntity;
import eu.europa.ec.fisheries.uvms.commons.geometry.mapper.GeometryMapper;
import eu.europa.ec.fisheries.uvms.commons.geometry.utils.GeometryUtils;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.commons.service.fileutils.ZipExtractor;
import eu.europa.ec.fisheries.uvms.commons.service.interceptor.SimpleTracingInterceptor;
import eu.europa.ec.fisheries.uvms.commons.service.interceptor.ValidationInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Area;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaByLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaExtendedIdentifierType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaSimpleType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.FilterAreasSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.FilterAreasSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Location;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ScopeAreasType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.UnitType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.UserAreasType;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaTypeNamesService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.AbstractAreaDao;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.util.DAOFactory;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.util.DatabaseDialect;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.util.DatabaseDialectFactory;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.util.Oracle;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.area.GenericSystemAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.area.SystemAreaNamesDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.upload.UploadMapping;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.upload.UploadMetadata;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.upload.UploadProperty;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.BaseAreaEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.CountryEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.EntityFactory;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.PortEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.UserAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.enums.MeasurementUnit;
import eu.europa.ec.fisheries.uvms.spatial.service.enums.SpatialTypeEnum;
import eu.europa.ec.fisheries.uvms.spatial.service.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.exception.SpatialServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.util.UploadUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.CRS;
import org.geotools.referencing.GeodeticCalculator;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

@Stateless
@Slf4j
public class AreaServiceBean implements AreaService {

    private static final String MULTIPOINT = "MULTIPOINT";

    private static final String GID = "gid";
    private static final String AREA_TYPE = "areaType";

    private EntityManager em;

    @PersistenceContext(unitName = "spatialPUpostgres")
    private EntityManager postgres;

    @PersistenceContext(unitName = "spatialPUoracle")
    private EntityManager oracle;

    @EJB
    private AreaTypeNamesService areaTypeService;

    @EJB
    private AreaService areaService;

    @EJB
    private SpatialRepository repository;

    @EJB
    private PropertiesBean properties;

    private DatabaseDialect databaseDialect;

    public void initEntityManager() {
        String dbDialect = System.getProperty("db.dialect");
        if ("oracle".equalsIgnoreCase(dbDialect)) {
            em = oracle;
        } else {
            em = postgres;
        }
    }

    @PostConstruct
    public void init() {
        initEntityManager();
        databaseDialect = new DatabaseDialectFactory(properties).getInstance();
    }

    @Override
    public Map<String, String> getAllCountriesDesc() throws ServiceException {
        Map<String, String> stringStringMap = new HashMap<>();
        List<CountryEntity> entityByNamedQuery = repository.findAllCountries();
        for (CountryEntity country : entityByNamedQuery) {
            stringStringMap.put(country.getCode(), country.getName());
        }
        return stringStringMap;
    }

    @Override
    // FIXME rewrite me please and re use existing code
    public List<Map<String, Object>> getAreasByIds(final List<AreaTypeEntry> areaTypes) throws ServiceException {

        List<Map<String, Object>> areaColumnsList = new ArrayList<>();
        Map<String, List<Long>> areaTypeGidMap = new HashMap<>();

        for (AreaTypeEntry areaTypeEntry : areaTypes) {
            String gid = areaTypeEntry.getId();
            String areaType = areaTypeEntry.getAreaType().value();

            if (!StringUtils.isNumeric(gid)) {
                throw new SpatialServiceException(SpatialServiceErrors.INVALID_ID_TYPE, gid);
            }

            boolean isAdded = false;
            for (Map.Entry<String, List<Long>> entry : areaTypeGidMap.entrySet()) {
                if (entry.getKey().equalsIgnoreCase(areaType)) {
                    entry.getValue().add(Long.parseLong(gid));
                    isAdded = true;
                }
            }
            if (!isAdded) {
                areaTypeGidMap.put(areaType, new ArrayList(Arrays.asList(Long.parseLong(gid))));
            }
        }


        for (Map.Entry<String, List<Long>> entry : areaTypeGidMap.entrySet()) { // FIXME looping and querying should be avoided
            String namedQuery = null;
            for (SpatialTypeEnum type : SpatialTypeEnum.values()) {
                if (type.getType().equalsIgnoreCase(entry.getKey())) {
                    namedQuery = type.getNamedQuery();
                }
            }
            if (namedQuery != null) {
                List<Map<String, Object>> selectedAreaColumns = repository.getAreasByIds(namedQuery, entry.getValue());
                for (Map<String, Object> columnMap : selectedAreaColumns) {
                    columnMap.put(AREA_TYPE, entry.getKey().toUpperCase());
                }
                areaColumnsList.addAll(selectedAreaColumns);
            }
        }
        return areaColumnsList;
    }


    @Override
    @Interceptors(SimpleTracingInterceptor.class)
    public void upload(final UploadMapping mapping, final String type, final Integer epsg) throws ServiceException {

        String ref = (String) mapping.getAdditionalProperties().get("ref");

        try {
            Integer srid = repository.mapEpsgToSRID(epsg);
            AreaLocationTypesEntity typeName = repository.findAreaLocationTypeByTypeName(type.toUpperCase());

            if (typeName == null) {
                throw new ServiceException("TYPE NOT SUPPORTED");
            }

            Map<String, List<Property>> features = readShapeFile(Paths.get(ref + File.separator + typeName.getAreaDbTable() + ".shp"), srid);
            DAOFactory.getAbstractSpatialDao(em, typeName.getTypeName()).bulkInsert(features, mapping.getMapping());
            org.apache.commons.io.FileUtils.deleteDirectory(Paths.get(ref).getParent().toFile());

            repository.makeGeomValid(typeName.getAreaDbTable(), databaseDialect);

        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    private Map<String, List<Property>> readShapeFile(Path shapeFilePath, Integer srid) throws IOException, ServiceException {

        Map<String, Object> map = new HashMap<>();
        map.put("url", shapeFilePath.toUri().toURL());
        DataStore dataStore = DataStoreFinder.getDataStore(map);
        Map<String, List<Property>> geometries = new HashMap<>();
        String typeName = dataStore.getTypeNames()[0];
        FeatureSource<SimpleFeatureType, SimpleFeature> source = dataStore.getFeatureSource(typeName);
        FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures(Filter.INCLUDE);
        FeatureIterator<SimpleFeature> iterator = collection.features();

        try {
            CoordinateReferenceSystem sourceCRS = GeometryUtils.toCoordinateReferenceSystem(srid);
            CoordinateReferenceSystem coordinateReferenceSystem = GeometryUtils.toDefaultCoordinateReferenceSystem();
            MathTransform transform = CRS.findMathTransform(sourceCRS, coordinateReferenceSystem);
            while (iterator.hasNext()) {
                final SimpleFeature feature = iterator.next();
                geometries.put(feature.getID(), new ArrayList<>(feature.getProperties()));
                Geometry targetGeometry = (Geometry) feature.getDefaultGeometry();
                if (targetGeometry != null) {
                    if (coordinateReferenceSystem.getName().equals(sourceCRS.getName())) {
                        targetGeometry = JTS.transform(targetGeometry, transform);
                    }
                } else {
                    throw new ServiceException("TARGET GEOMETRY CANNOT BE NULL");
                }
                targetGeometry.setSRID(databaseDialect.defaultSRID());
                feature.setDefaultGeometry(targetGeometry);
            }
            return geometries;

        } catch (FactoryException | TransformException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        } finally {
            iterator.close();
            dataStore.dispose();
        }
    }


    @Override
    @Interceptors(SimpleTracingInterceptor.class)
    public UploadMetadata metadata(byte[] bytes, String type) {

        UploadMetadata response = new UploadMetadata();

        try {
            if (bytes.length == 0) {
                throw new IllegalArgumentException("File is empty.");
            }
            AreaLocationTypesEntity typeName = repository.findAreaLocationTypeByTypeName(type.toUpperCase());

            if (typeName == null) {
                throw new ServiceException("TYPE NOT SUPPORTED");
            }

            BaseEntity instance = EntityFactory.getInstance(typeName.getTypeName());
            List<Field> properties = instance.listMembers();

            for (Field field : properties) {
                UploadProperty uploadProperty = new UploadProperty();
                uploadProperty.withName(field.getName()).withType(field.getType().getSimpleName());
                response.getDomain().add(uploadProperty);
            }

            Path uploadPath = Paths.get("app/upload/spatial");
            uploadPath.toFile().mkdirs();
            Path absolutePath = Files.createTempDirectory(uploadPath, null, new FileAttribute[0]);
            ZipExtractor.unZipFile(bytes, absolutePath);
            ZipExtractor.renameFiles(absolutePath, typeName.getTypeName());
            List<UploadProperty> propertyList = UploadUtil.readAttribute(absolutePath);
            response.getFile().addAll(propertyList);
            response.withAdditionalProperty("ref", absolutePath.toString());

        } catch (Exception ex) {
            throw new SpatialServiceException(SpatialServiceErrors.INVALID_UPLOAD_AREA_DATA, ex);
        }
        return response;
    }

    @Override
    @Interceptors(ValidationInterceptor.class)
    public Map<String, Object> getAreaById(@NotNull Long id, @NotNull AreaType areaType) throws ServiceException {

        BaseAreaEntity area = repository.findAreaById(id, areaType);

        if (area == null) {
            throw new SpatialServiceException(SpatialServiceErrors.ENTITY_NOT_FOUND, areaType.value());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        @SuppressWarnings("unchecked")
        Map<String, Object> map = objectMapper.convertValue(area, Map.class);
        return map;
    }

    @Override
    public List<AreaSimpleType> getAreasByCode(List<AreaSimpleType> areaSimpleTypeList) throws ServiceException {

        List records = repository.areaByCode(areaSimpleTypeList);
        List<AreaSimpleType> simpleTypeList = new ArrayList<>();

        for (Object record : records) {

            final Object[] result = (Object[]) record;
            String type = (String) result[0];
            String code = (String) result[1];
            Geometry geom = (Geometry) result[2];
            simpleTypeList.add(new AreaSimpleType(type, code, GeometryMapper.INSTANCE.geometryToWkt(geom).getValue()));
        }

        return simpleTypeList;
    }

    @Override
    public List<Location> getClosestPointByPoint(final ClosestLocationSpatialRQ request) throws ServiceException {

        Double lat = request.getPoint().getLatitude();
        Double lon = request.getPoint().getLongitude();
        Integer crs = request.getPoint().getCrs();
        UnitType unit = request.getUnit();

        Point incoming = (Point) GeometryUtils.toGeographic(lat, lon, crs);
        Double incomingLatitude = incoming.getY();
        Double incomingLongitude = incoming.getX();
        Map<String, Location> distancePerTypeMap = new HashMap<>();
        MeasurementUnit measurementUnit = MeasurementUnit.getMeasurement(unit.name());
        GeodeticCalculator calc = new GeodeticCalculator(GeometryUtils.toDefaultCoordinateReferenceSystem());
        List<AreaLocationTypesEntity> typeEntities = repository.findByIsLocationAndIsSystemWide(true, true); // Can thi query be avoided?

        List records = repository.closestPointByPoint(typeEntities, databaseDialect, incoming);

        for (Object record : records) {
            Object[] result = (Object[]) record;
            Geometry geometry = (Geometry) result[4];
            Point centroid = geometry.getCentroid();
            calc.setStartingGeographicPoint(centroid.getX(), centroid.getY());
            calc.setDestinationGeographicPoint(incomingLongitude, incomingLatitude);
            Double orthodromicDistance = calc.getOrthodromicDistance();
            String type = result[0].toString();
            Location closest = distancePerTypeMap.get(type);

            if (closest == null || orthodromicDistance / measurementUnit.getRatio() < closest.getDistance()) {
                if (closest == null) {
                    closest = new Location();
                }
                closest.setDistance(orthodromicDistance);
                closest.setCentroid(centroid.toText());
                closest.setExtent(GeometryMapper.INSTANCE.geometryToWkt(geometry.getEnvelope()).getValue());
                String id = result[1].toString();
                closest.setId(id);
                closest.setGid(id);
                closest.setDistance(orthodromicDistance / measurementUnit.getRatio());
                closest.setUnit(unit);
                closest.setEnabled(true);
                closest.setWkt(GeometryMapper.INSTANCE.geometryToWkt(geometry).getValue());
                closest.setCode(result[2].toString());
                closest.setName(result[3].toString());
                closest.setLocationType(LocationType.fromValue(type));
                distancePerTypeMap.put(type, closest);
                // TODO enrich with MDR LOCATION LISt DATA
            }
        }
        return new ArrayList<>(distancePerTypeMap.values());
    }

    @Override
    @Interceptors(ValidationInterceptor.class) // TODO add isLocation and IsSystemWide as parameters
    public List<Area> getClosestArea(@NotNull Double longitude, @NotNull Double latitude, @NotNull Integer crs, @NotNull UnitType unit) throws ServiceException {
        final Map<String, Area> distancePerTypeMap = new HashMap<>();
        try {
            Point incoming = (Point) GeometryUtils.toGeographic(latitude, longitude, crs);
            MeasurementUnit measurementUnit = MeasurementUnit.getMeasurement(unit.name());
            List<AreaLocationTypesEntity> typesEntities = repository.findByIsLocationAndIsSystemWide(false, true);
            List records = repository.closestAreaByPoint(typesEntities, databaseDialect, incoming);
            if (databaseDialect instanceof Oracle){
                for (Object record : records) {
                    Object[] result = (Object[]) record;
                    Geometry geom = (Geometry) result[4];
                    if (geom.isEmpty()){
                        continue;
                    }
                    com.vividsolutions.jts.geom.Coordinate[] coordinates = nearestPoints(geom, incoming);
                    Double orthodromicDistance = orthodromicDistance(coordinates[0], coordinates[1], GeometryUtils.toDefaultCoordinateReferenceSystem());
                    String type = result[0].toString();
                    Area closest = distancePerTypeMap.get(type);
                    if (closest == null || orthodromicDistance / measurementUnit.getRatio() < closest.getDistance()) {
                        if (closest == null) {
                            closest = new Area();
                        }
                        closest.setId(result[1].toString());
                        closest.setDistance(orthodromicDistance / measurementUnit.getRatio());
                        closest.setUnit(unit);
                        closest.setCode(result[2] != null ? result[2].toString() : null);
                        closest.setName(result[3] != null ? result[3].toString() : null);
                        closest.setAreaType(AreaType.valueOf(type));
                        distancePerTypeMap.put(type, closest);
                    }
                }
            } else {
                for (Object record : records) {
                    Object[] result = (Object[]) record;
                    String type = result[0].toString();
                    Area closest = new Area();
                    closest.setDistance((Double) result[5] / measurementUnit.getRatio());
                    closest.setId(result[1].toString());
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
        buildQuery(scopeAreas.getScopeAreas(), sb, "scope", typesEntityMap);
        if (userAreas != null) {
            if (CollectionUtils.isNotEmpty(userAreas.getUserAreas()) && StringUtils.isNotEmpty(sb.toString())) {
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
            Integer epsgSRID = repository.mapToEpsgSRID(geometry.getSRID());
            if (!isDefaultEpsgSRID(epsgSRID)) {
                geometry = GeometryUtils.toGeographic(geometry, epsgSRID);
            }
            if ("user".equals(type)) {
                userGeometryList.add(geometry);
            } else {
                scopeGeometryList.add(geometry);
            }
        }
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        Geometry userUnion = geometryFactory.buildGeometry(userGeometryList).union();
        Geometry scopeUnion = geometryFactory.buildGeometry(scopeGeometryList).union();
        FilterAreasSpatialRS response = new FilterAreasSpatialRS(null, 0);
        Geometry intersection = null;
        if (!userUnion.isEmpty() && !scopeUnion.isEmpty()) {
            intersection = userUnion.intersection(scopeUnion);
            response.setCode(3);
            if (intersection.isEmpty()) {
                intersection = scopeUnion;
                response.setCode(4);
            }
        } else if (!userUnion.isEmpty()) {
            intersection = userUnion;
            response.setCode(1);
        } else if (!scopeUnion.isEmpty()) {
            intersection = scopeUnion;
            response.setCode(2);
        }
        if (intersection != null) {
            if (intersection.getNumPoints() > 20000) {
                intersection = DouglasPeuckerSimplifier.simplify(intersection, 0.5);
            }
            response.setGeometry(GeometryMapper.INSTANCE.geometryToWkt(intersection).getValue());
        }
        return response;
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

    @Override
    @Interceptors(ValidationInterceptor.class)
    public List<GenericSystemAreaDto> searchAreasByNameOrCode(@NotNull String areaType, @NotNull String filter) throws ServiceException {
        AreaLocationTypesEntity areaLocationType = repository.findAreaLocationTypeByTypeName(areaType.toUpperCase());
        final ArrayList<GenericSystemAreaDto> systemAreaByFilterRecords = new ArrayList<>();
        //FIXME use generic dao
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

    @Override
    public Map<String, Object> getLocationDetails(final LocationTypeEntry locationTypeEntry) throws ServiceException {
        BaseAreaEntity match = null;
        GeodeticCalculator calc = new GeodeticCalculator(GeometryUtils.toDefaultCoordinateReferenceSystem());
        String id = locationTypeEntry.getId();
        String locationType = locationTypeEntry.getLocationType();
        AreaLocationTypesEntity locationTypesEntity;
        Double incomingLatitude = locationTypeEntry.getLatitude();
        Double incomingLongitude = locationTypeEntry.getLongitude();

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
            match = areaEntity;
        }
        else {
            Point point = (Point) GeometryUtils.toGeographic(incomingLatitude, incomingLongitude, locationTypeEntry.getCrs());
            List<PortEntity> records = repository.listClosestPorts(point, 5);
            Double closestDistance = Double.MAX_VALUE;
            for (PortEntity portsEntity : records) {
                Geometry geometry = portsEntity.getGeom();
                Point centroid = geometry.getCentroid();
                calc.setStartingGeographicPoint(centroid.getX(), centroid.getY());
                calc.setDestinationGeographicPoint(point.getX(), point.getY());
                Double orthodromicDistance = calc.getOrthodromicDistance();
                if (closestDistance > orthodromicDistance) {
                    closestDistance = orthodromicDistance;
                    match = portsEntity;
                }
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.convertValue(match, Map.class);
        return map;
    }

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
                systemAreas.add(new SystemAreaNamesDto(baseEntity.getCode(), new HashSet<>(Arrays.asList(baseEntity.getName())), new ArrayList(Arrays.asList(baseEntity.getGeom()))));
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
    @SneakyThrows
    @Interceptors(ValidationInterceptor.class)
    public List<Map<String, Object>> getAreasByPoint(@NotNull Double latitude, @NotNull Double longitude, @NotNull Integer crs, @NotNull String userName, @NotNull AreaType areaType) throws ServiceException {

        AreaLocationTypesEntity areaLocationTypesEntity = repository.findAreaLocationTypeByTypeName(areaType.value().toUpperCase());

        Point point = (Point) GeometryUtils.toGeographic(latitude, longitude, crs);

        // FIXME try using ethod of repo remove DAOFactory stuff
        List byIntersect = DAOFactory.getAbstractSpatialDao(em, areaLocationTypesEntity.getTypeName()).findByIntersect(point);

        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> objectAsMap = new ArrayList<>();

        for(Object o : byIntersect){
            objectAsMap.add(objectMapper.convertValue(o, Map.class));
        }

        return objectAsMap;
    }
    
    @Override
    public List<AreaExtendedIdentifierType> getUserAreasByPoint(Date activeDate, Double lon, Double lat, Integer crs) throws ServiceException {
        if (lat == null || lon == null || crs == null){
            throw new ServiceException("MISSING MANDATORY FIELDS");
        }
        final Point incoming = (Point) GeometryUtils.toGeographic(lat, lon, crs);
        final List<AreaExtendedIdentifierType> areaTypes = new ArrayList<>();
        List<UserAreasEntity> records = repository.intersectingUserAreas(incoming,activeDate);
        for (UserAreasEntity result : records) {
            AreaExtendedIdentifierType area = new AreaExtendedIdentifierType();
            area.setAreaType(AreaType.USERAREA);
            area.setId(String.valueOf(result.getId()));
            area.setCode(result.getCode());
            area.setName(result.getName());
            areaTypes.add(area);
        }
        return areaTypes;
    }
    
    @Override // FIXME is kind off a duplicate of List<Map<String, Object>> getAreasByPoint
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

        final Point incoming = (Point) GeometryUtils.toGeographic(lat, lon, crs);
        final List<AreaLocationTypesEntity> typesEntities = repository.findByIsLocationAndIsSystemWide(false, true);
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

    @Override // FIXME is kind off a duplicate of List<Map<String, Object>> getAreasByPoint
    public List<AreaExtendedIdentifierType> getPortAreasByPoint(Point incoming) throws ServiceException {

        Integer crs = 4326;

        AreaLocationTypesEntity typesEntity = repository.findAreaLocationTypeByTypeName("PORTAREA");
        final List<AreaLocationTypesEntity> typesEntities = new ArrayList<>();
        typesEntities.add(typesEntity);
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
    public String getGeometryForPort(String portCode){
        if(portCode ==null){
            return null;
        }
        log.debug("Port code received in getGeometryForPort :"+portCode);
        String geomWkt=null;
        try {
            List<BaseAreaEntity> baseEntities = DAOFactory.getAbstractSpatialDao(em, "port").searchNameByCode(portCode);

            if(CollectionUtils.isNotEmpty(baseEntities)){
                for(BaseAreaEntity baseAreaEntity : baseEntities){
                    if(baseAreaEntity.getGeom() !=null){
                        Geometry geometry = baseAreaEntity.getGeom();
                        geomWkt = GeometryMapper.INSTANCE.geometryToWkt(geometry).getValue();
                        log.debug("geomWkt received :"+geomWkt);
                        break;
                    }
                }
            }
        } catch (ServiceException e) {
            log.error("Could not fetch geometry for the portCode:"+portCode,e);
        }


        return geomWkt;
    }

}