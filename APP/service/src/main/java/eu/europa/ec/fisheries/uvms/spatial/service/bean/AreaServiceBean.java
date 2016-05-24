package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.interceptors.SimpleTracingInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.dao.util.DAOFactory;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.BaseSpatialEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.CountryEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaProperty;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.service.AreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.AreaTypeNamesService;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import eu.europa.ec.fisheries.uvms.spatial.util.FileSaver;
import eu.europa.ec.fisheries.uvms.spatial.util.ShapeFileReader;
import eu.europa.ec.fisheries.uvms.spatial.util.SpatialTypeEnum;
import eu.europa.ec.fisheries.uvms.spatial.util.SupportedFileExtensions;
import eu.europa.ec.fisheries.uvms.spatial.util.ZipExtractor;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
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

    private @PersistenceContext(unitName = "spatialPU") EntityManager em;

    private static final String AREA_ZIP_FILE = "AreaFile.zip";
    private static final String PREFIX = "temp";
    private static final String GID = "gid";
    private static final String AREA_TYPE = "areaType";

    private @EJB AreaTypeNamesService areaTypeService;
    private @EJB AreaService areaService;
    private @EJB SpatialRepository repository;

    @Override
    public Map<String, String> getAllCountriesDesc() throws ServiceException {
        Map<String, String> stringStringMap = new HashMap<>();
        List<CountryEntity> entityByNamedQuery = repository.findAllCountries();
        for (CountryEntity country : entityByNamedQuery){
            stringStringMap.put(country.getCode(), country.getName());
        }
        return stringStringMap;
    }

    @Override
    @Transactional
    public List<Map<String, String>> getSelectedAreaColumns(final List<AreaTypeEntry> areaTypes) throws ServiceException {

        List<Map<String, String>> columnMapList = new ArrayList<>();
        for(AreaTypeEntry areaTypeEntry : areaTypes) { // TODO @Greg get rid of the loop
            String gid = areaTypeEntry.getId();
            String areaType = areaTypeEntry.getAreaType().value();

            if (!StringUtils.isNumeric(gid)) {
                throw new SpatialServiceException(SpatialServiceErrors.INVALID_ID_TYPE, gid);
            }

            String namedQuery = null;
            for (SpatialTypeEnum type : SpatialTypeEnum.values()) {
                if(type.getType().equalsIgnoreCase(areaType)) {
                    namedQuery = type.getNamedQuery();
                }
            }
            List<Map<String, String>> selectedAreaColumns = repository.findSelectedAreaColumns(namedQuery, Long.parseLong(gid));

            Map<String, String> columnMap;

            if (!selectedAreaColumns.isEmpty()) {
                columnMap = selectedAreaColumns.get(0);
                columnMap.put(GID, gid);
                columnMap.put(AREA_TYPE, areaType.toUpperCase());
                columnMapList.add(columnMap);
            }
        }
        return columnMapList;
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @Interceptors(SimpleTracingInterceptor.class)
    public void uploadArea(byte[] content, String areaTypeString, int crsCode) {

        try {

            CoordinateReferenceSystem sourceCRS = validate(content, crsCode);

            AreaLocationTypesEntity typeName = repository.findAreaLocationTypeByTypeName(areaTypeString.toUpperCase());
            Path absolutePath = Files.createTempDirectory(PREFIX);
            Path zipFilePath = Paths.get(absolutePath + File.separator + AREA_ZIP_FILE);
            FileSaver fileSaver = new FileSaver();
            fileSaver.saveContentToFile(content, zipFilePath);
            ZipExtractor zipExtractor = new ZipExtractor();
            Map<SupportedFileExtensions, Path> fileNames = zipExtractor.unZipFile(zipFilePath, absolutePath);
            ShapeFileReader shapeFileReader = new ShapeFileReader();
            Map<String, List<Property>> features = shapeFileReader.readShapeFile(fileNames.get(SupportedFileExtensions.SHP), sourceCRS);
            DAOFactory.getAbstractSpatialDao(em, typeName.getTypeName()).bulkInsert(features);
            FileUtils.deleteDirectory(new File(absolutePath.toString()));

        } catch (Exception ex) {
            throw new SpatialServiceException(SpatialServiceErrors.INVALID_UPLOAD_AREA_DATA, ex);
        }
    }

    private CoordinateReferenceSystem validate(byte[] content, int crsCode) {

        CoordinateReferenceSystem sourceCRS;

        try {

            if (content.length == 0) {
                throw new IllegalArgumentException("File is empty.");
            }

            sourceCRS = CRS.decode(ShapeFileReader.EPSG + crsCode);

        } catch (FactoryException e) {
            throw new IllegalArgumentException("CrsCode is wrong.");
        }

        return sourceCRS;
    }

    @Override
    @Transactional
    public AreaDetails getAreaDetailsById(AreaTypeEntry areaTypeEntry) throws ServiceException {

        if (areaTypeEntry.getAreaType() == null) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR, StringUtils.EMPTY);
        }

        if (!StringUtils.isNumeric(areaTypeEntry.getId())) {
            throw new SpatialServiceException(SpatialServiceErrors.INVALID_ID_TYPE, areaTypeEntry.getId());
        }

        AreaLocationTypesEntity areaLocationTypesEntity =
                repository.findAreaLocationTypeByTypeName(areaTypeEntry.getAreaType().value().toUpperCase());

        Long id = Long.parseLong(areaTypeEntry.getId());
        BaseSpatialEntity area = DAOFactory.getAbstractSpatialDao(em, areaLocationTypesEntity.getTypeName()).findOne(id);

        if (area == null) {
            throw new SpatialServiceException(SpatialServiceErrors.ENTITY_NOT_FOUND, areaLocationTypesEntity.getTypeName());
        }

        Map<String, Object> properties = area.getFieldMap();

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

}