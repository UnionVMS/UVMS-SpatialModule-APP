package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.Maps;
import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.BaseAreaEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.PortAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.PortEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.RfmoEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaProperty;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.service.AreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.AreaTypeNamesService;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.PortAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.PortLocationDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.geojson.PortAreaGeoJsonDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.PortAreaMapper;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.PortLocationMapper;
import eu.europa.ec.fisheries.uvms.spatial.util.FileSaver;
import eu.europa.ec.fisheries.uvms.spatial.util.ShapeFileReader;
import eu.europa.ec.fisheries.uvms.spatial.util.SpatialTypeEnum;
import eu.europa.ec.fisheries.uvms.spatial.util.SupportedFileExtensions;
import eu.europa.ec.fisheries.uvms.spatial.util.ZipExtractor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.geotools.referencing.CRS;
import org.opengis.feature.Property;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
@Local(AreaService.class)
@Slf4j
public class AreaServiceBean implements AreaService {

    private static final String CODE = "code";
    private static final String NAME = "name";
    private static final String AREA_ZIP_FILE = "AreaFile.zip";
    private static final String PREFIX = "temp";
    private static final String GID = "gid";
    private static final String AREA_TYPE = "areaType";

    private @EJB AreaTypeNamesService areaTypeService;
    private @EJB AreaService areaService;
    private @EJB SpatialRepository repository;

    @Override
    public Map<String, String> getAllCountriesDesc() {
        Map<String, String> countries = new HashMap<>();
        List<Map<String, String>> countryList = repository.findAllCountriesDesc();
        for (Map<String, String> country : countryList) {
            countries.put(country.get(CODE), country.get(NAME));
        }
        return countries;
    }

    @Override
    @Transactional
    public void replaceEez(Map<String, List<Property>> features) {
        try {
            repository.disableAllEezAreas();
            for (List<Property> properties : features.values()) {
                repository.create(new EezEntity(properties));
            }
        } catch (Exception e) {
            throw new SpatialServiceException(SpatialServiceErrors.INVALID_UPLOAD_AREA_DATA, e);
        }
    }

    @Override
    @Transactional
    public void replaceRfmo(Map<String, List<Property>> features) {
        try {
            repository.disableAllRfmoAreas();
            for (List<Property> properties : features.values()) {
                repository.create(new RfmoEntity(properties));
            }
        } catch (Exception e) {
            throw new SpatialServiceException(SpatialServiceErrors.INVALID_UPLOAD_AREA_DATA, e);
        }
    }

    @Override
    @Transactional
    public List<Map<String, String>> getSelectedAreaColumns(final List<AreaTypeEntry> areaTypes) throws ServiceException {

        List<Map<String, String>> columnMapList = new ArrayList<>();
        for(AreaTypeEntry areaTypeEntry : areaTypes) {
            String gid = areaTypeEntry.getId();
            String areaType = areaTypeEntry.getAreaType().value();
            if (!StringUtils.isNumeric(gid)) {
                throw new SpatialServiceException(SpatialServiceErrors.INVALID_ID_TYPE, gid);
            }
            List<AreaLocationTypesEntity> areasLocationTypes = repository.findAreaLocationTypeByTypeName(areaType.toUpperCase());

            if (areasLocationTypes.isEmpty()) {
                throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR, areasLocationTypes);
            }
            String namedQuery = null;
            for (SpatialTypeEnum type : SpatialTypeEnum.values()) {
                if(type.getType().equalsIgnoreCase(areaType)) {
                    namedQuery = type.getNamedQuery();
                }
            }
            List<Map<String, String>> selectedAreaColumns = repository.findSelectedAreaColumns(namedQuery, Long.parseLong(gid));
            if (selectedAreaColumns.isEmpty()) {
                throw new SpatialServiceException(SpatialServiceErrors.ENTITY_NOT_FOUND);
            }
            Map<String, String> columnMap = selectedAreaColumns.get(0);
            columnMap.put(GID, gid);
            columnMap.put(AREA_TYPE, areaType.toUpperCase());
            columnMapList.add(columnMap);
        }
        return columnMapList;
    }

    @Override
    public void uploadArea(byte[] content, String areaTypeString, int crsCode) {
        try {
            AreaType areaType = AreaType.fromValue(areaTypeString);
            CoordinateReferenceSystem sourceCRS = validate(content, areaTypeString, crsCode);

            Path absolutePath = getTempPath();
            Path zipFilePath = Paths.get(absolutePath + File.separator + AREA_ZIP_FILE);

            FileSaver fileSaver = new FileSaver();
            fileSaver.saveContentToFile(content, zipFilePath);

            ZipExtractor zipExtractor = new ZipExtractor();
            Map<SupportedFileExtensions, Path> fileNames = zipExtractor.unZipFile(zipFilePath, absolutePath);

            ShapeFileReader shapeFileReader = new ShapeFileReader();
            Map<String, List<Property>> features = shapeFileReader.readShapeFile(fileNames.get(SupportedFileExtensions.SHP), sourceCRS);

            switch (areaType) {
                case EEZ:
                    areaService.replaceEez(features);
                    break;
                case RFMO:
                    areaService.replaceRfmo(features);
                    break;
                case PORT:
                    areaService.replacePort(features);
                    break;
                case PORTAREA:
                    areaService.replacePortArea(features);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported area type.");
            }

            FileUtils.deleteDirectory(new File(absolutePath.toString()));

            log.debug("Finished areas upload.");
        } catch (IOException ex) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
        }
    }

    private CoordinateReferenceSystem validate(byte[] content, String areaType, int crsCode) {
        if (content.length == 0) {
            throw new IllegalArgumentException("File is empty.");
        }
        CoordinateReferenceSystem sourceCRS;
        try {
            sourceCRS = CRS.decode(ShapeFileReader.EPSG + crsCode);
        } catch (FactoryException e) {
            throw new IllegalArgumentException("CrsCode is wrong.");
        }
        List<String> areaTypes = areaTypeService.listAllAreaAndLocationTypeNames();
        if (!areaTypes.contains(areaType.toUpperCase())) {
            throw new IllegalArgumentException("Unsupported area type.");
        }
        return sourceCRS;
    }

    private Path getTempPath() throws IOException {
        return Files.createTempDirectory(PREFIX);
    }

    private enum AreaType {
        EEZ("eez"),
        RFMO("rfmo"),
        PORT("port"),
        PORTAREA("portarea");

        private final String value;

        AreaType(String value) {
            this.value = value;
        }

        public static AreaType fromValue(String value) {
            for (AreaType areaType : values()) {
                if (areaType.value.equalsIgnoreCase(value)) {
                    return areaType;
                }
            }
            throw new IllegalArgumentException("Unsupported area type");
        }

    }

    @Override
    @Transactional
    public void replacePort(Map<String, List<Property>> features) {
        try {
            repository.disableAllPortLocations();

            Date enabledOn = new Date();// TODO move code to entity like eez
            for (List<Property> properties : features.values()) {
                Map<String, Object> values = createAttributesMap(properties);
                PortLocationDto portLocationDto = new PortLocationDto();
                portLocationDto.setGeometry((Geometry) values.get("the_geom"));
                portLocationDto.setCode(readStringProperty(values, "code"));
                portLocationDto.setName(readStringProperty(values, "name"));
                portLocationDto.setCountryCode(readStringProperty(values, "country_co"));
                portLocationDto.setFishingPort(readStringProperty(values, "fishing_po"));
                portLocationDto.setLandingPlace(readStringProperty(values, "landing_pl"));
                portLocationDto.setCommercialPort(readStringProperty(values, "commercial"));
                portLocationDto.setEnabled(true);
                portLocationDto.setEnabledOn(enabledOn);

                PortEntity portsEntity = PortLocationMapper.INSTANCE.portLocationDtoToPortsEntity(portLocationDto);
                repository.createEntity(portsEntity);
            }
        } catch (Exception e) {
            throw new SpatialServiceException(SpatialServiceErrors.INVALID_UPLOAD_AREA_DATA, e);
        }
    }

    @Override
    @Transactional
    public void replacePortArea(Map<String, List<Property>> features) {
        try {
            repository.disableAllPortAreas();
            Date enabledOn = new Date();// TODO move code to entity like eez
            for (List<Property> properties : features.values()) {
                Map<String, Object> values = createAttributesMap(properties);
                PortAreaDto portAreaDto = new PortAreaDto();
                portAreaDto.setGeometry((Geometry) values.get("the_geom"));
                portAreaDto.setCode(readStringProperty(values, "code"));
                portAreaDto.setName(readStringProperty(values, "name"));
                portAreaDto.setEnabled(true);
                portAreaDto.setEnabledOn(enabledOn);

                PortAreasEntity portAreasEntity = PortAreaMapper.INSTANCE.portAreaDtoToPortAreasEntity(portAreaDto);
                repository.createEntity(portAreasEntity);
            }
        } catch (Exception e) {
            throw new SpatialServiceException(SpatialServiceErrors.INVALID_UPLOAD_AREA_DATA, e);
        }
    }

    private Map<String, Object> createAttributesMap(List<Property> properties) { // TODO move code to entity like eez
        Map<String, Object> resultMap = Maps.newHashMap();
        for (Property property : properties) {
            String name = property.getName().toString();
            Object value = property.getValue();
            resultMap.put(name, value);
        }
        return resultMap;
    }

    private String readStringProperty(Map<String, Object> values, String propertyName) throws UnsupportedEncodingException { // TODO move code to entity like eez
        return new String(((String) values.get(propertyName)).getBytes("ISO-8859-1"), "UTF-8");
    }

    @Override
    @Transactional
    public AreaDetails getAreaDetailsById(AreaTypeEntry areaTypeEntry) throws ServiceException {

        eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType areaType = areaTypeEntry.getAreaType();

        if (areaType == null) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR, StringUtils.EMPTY);
        }

        if (!StringUtils.isNumeric(areaTypeEntry.getId())) {
            throw new SpatialServiceException(SpatialServiceErrors.INVALID_ID_TYPE, areaTypeEntry.getId());
        }

        List<AreaLocationTypesEntity> areasLocationTypes =
                repository.findAreaLocationTypeByTypeName(areaTypeEntry.getAreaType().value().toUpperCase());

        if (areasLocationTypes.isEmpty()) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR, areasLocationTypes);
        }

        AreaLocationTypesEntity areaLocationTypesEntity = areasLocationTypes.get(0);

        Integer id = Integer.parseInt(areaTypeEntry.getId());

        BaseAreaEntity areaEntity = repository.findAreaByTypeAndId(areaLocationTypesEntity.getTypeName(), id.longValue());

        if (areaEntity == null) {
            throw new SpatialServiceException(SpatialServiceErrors.ENTITY_NOT_FOUND, areaLocationTypesEntity.getTypeName());
        }
        Map<String, Object> properties = areaEntity.getFieldMap();

        return createAreaDetailsSpatialResponse(properties, areaTypeEntry);

    }

    private AreaDetails createAreaDetailsSpatialResponse(Map<String, Object> properties, AreaTypeEntry areaTypeEntry) {
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
        return areaDetails;
    }

    @Override
    public Long updatePortArea(PortAreaGeoJsonDto portAreaGeoJsonDto) throws ServiceException {
        Long id = portAreaGeoJsonDto.getId();

        if (id == null) {
            throw new SpatialServiceException(SpatialServiceErrors.MISSING_PORT_AREA_ID);
        }

        List<PortAreasEntity> persistentPortAreas = repository.findPortAreaById(id);

        if (CollectionUtils.isEmpty(persistentPortAreas)) {
            throw new SpatialServiceException(SpatialServiceErrors.PORT_AREA_DOES_NOT_EXIST, id);
        }

        PortAreasEntity persistentPortArea = persistentPortAreas.get(0);
        persistentPortArea.setGeom( portAreaGeoJsonDto.getGeometry());

        PortAreasEntity persistedUpdatedEntity = repository.update(persistentPortArea);
        return persistedUpdatedEntity.getGid();

    }

    @Override
    public void deletePortArea(Long portAreaId) throws ServiceException {

        List<PortAreasEntity> persistentPortAreas = repository.findPortAreaById(portAreaId);

        if (CollectionUtils.isEmpty(persistentPortAreas)) {
            throw new SpatialServiceException(SpatialServiceErrors.PORT_AREA_DOES_NOT_EXIST, portAreaId);
        }

        PortAreasEntity persistentPortArea = persistentPortAreas.get(0);
        persistentPortArea.setGeom(null);

        PortAreasEntity persistedUpdatedEntity = repository.update(persistentPortArea);
        persistedUpdatedEntity.getGid();
    }
}