package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.interceptors.SimpleTracingInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.BaseAreaEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.PortAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaProperty;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.service.AreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.AreaTypeNamesService;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.geojson.PortAreaGeoJsonDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
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
import javax.interceptor.Interceptors;
import javax.transaction.Transactional;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @Interceptors(SimpleTracingInterceptor.class)
    public void uploadArea(byte[] content, String areaTypeString, int crsCode) {
        try {
            AreaType areaType = AreaType.fromValue(areaTypeString);
            CoordinateReferenceSystem sourceCRS = validate(content, areaTypeString, crsCode);

            Path absolutePath = Files.createTempDirectory(PREFIX);
            Path zipFilePath = Paths.get(absolutePath + File.separator + AREA_ZIP_FILE);

            FileSaver fileSaver = new FileSaver();
            fileSaver.saveContentToFile(content, zipFilePath);

            ZipExtractor zipExtractor = new ZipExtractor();
            Map<SupportedFileExtensions, Path> fileNames = zipExtractor.unZipFile(zipFilePath, absolutePath);

            ShapeFileReader shapeFileReader = new ShapeFileReader();
            Map<String, List<Property>> features = shapeFileReader.readShapeFile(fileNames.get(SupportedFileExtensions.SHP), sourceCRS);

            switch (areaType) {
                case EEZ:
                    repository.replaceEez(features);
                    break;
                case RFMO:
                    repository.replaceRfmo(features);
                    break;
                case PORT:
                    repository.replacePort(features);
                    break;
                case PORTAREA:
                    repository.replacePortArea(features);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported area type.");
            }

            FileUtils.deleteDirectory(new File(absolutePath.toString()));

        } catch (Exception ex) {
            throw new SpatialServiceException(SpatialServiceErrors.INVALID_UPLOAD_AREA_DATA, ex);
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