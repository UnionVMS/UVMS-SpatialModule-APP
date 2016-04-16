package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.service.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import eu.europa.ec.fisheries.uvms.spatial.util.FileSaver;
import eu.europa.ec.fisheries.uvms.spatial.util.ShapeFileReader;
import eu.europa.ec.fisheries.uvms.spatial.util.SupportedFileExtensions;
import eu.europa.ec.fisheries.uvms.spatial.util.ZipExtractor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Stateless
@Local(AreaUploadService.class)
@Transactional
@Slf4j
public class AreaUploadServiceBean implements AreaUploadService {

    private static final String AREA_ZIP_FILE = "AreaFile.zip";
    private static final String PREFIX = "temp";

    private @EJB AreaTypeNamesService areaTypeService;
    private @EJB SpatialRepository repository;
    private @EJB AreaService areaService;

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
                    areaService.replaceEezArea(features);
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

}
