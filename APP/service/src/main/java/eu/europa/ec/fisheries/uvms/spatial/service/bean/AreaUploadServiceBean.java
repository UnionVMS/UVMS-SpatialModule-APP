package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.handler.EezSaverHandler;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.handler.RfmoSaverHandler;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.handler.SaverHandler;
import eu.europa.ec.fisheries.uvms.spatial.util.FileSaver;
import eu.europa.ec.fisheries.uvms.spatial.util.ShapeFileReader;
import eu.europa.ec.fisheries.uvms.spatial.util.SupportedExtensions;
import eu.europa.ec.fisheries.uvms.spatial.util.ZipExtractor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opengis.feature.Property;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Stateless
@Local(AreaUploadService.class)
@Transactional
@Slf4j
public class AreaUploadServiceBean implements AreaUploadService {

    private static final String AREA_ZIP_FILE = "AreaFile.zip";
    private static final String PREFIX = "temp";

    @EJB
    private AreaTypeNamesService areaTypeService;

    @EJB
    private EezSaverHandler eezSaverHandler;

    @EJB
    private RfmoSaverHandler rmfoSaverHandler;

    @Override
    public void uploadArea(byte[] content, String areaTypeString, String crsCode) throws IOException, ServiceException {
        AreaType areaType = validate(content, areaTypeString, crsCode);

        String absolutePath = getTempPath();
        String zipFilePath = absolutePath + AREA_ZIP_FILE;

        FileSaver fileSaver = new FileSaver();
        fileSaver.saveContentToFile(content, zipFilePath);

        ZipExtractor zipExtractor = new ZipExtractor();
        Map<SupportedExtensions, String> fileNames = zipExtractor.unZipFile(zipFilePath, absolutePath);

        ShapeFileReader shapeFileReader = new ShapeFileReader();
        Map<String, List<Property>> features = shapeFileReader.readShapeFile(absolutePath, fileNames.get(SupportedExtensions.SHP));

        saveAreas(areaType, features);

        log.debug("Finished upload areas.");
    }

    private void saveAreas(AreaType areaType, Map<String, List<Property>> features) throws ServiceException {
        SaverHandler saverHandler = getHandler(areaType);
        if (saverHandler != null) {
            saverHandler.save(features);
        } else {
            throw new IllegalArgumentException("Unsupported area type.");
        }
    }

    private SaverHandler getHandler(AreaType areaType) {
        switch (areaType) {
            case EEZ:
                return eezSaverHandler;
            case RFMO:
                return eezSaverHandler;
            default:
                throw new IllegalArgumentException("Unsupported area type.");
        }
    }

    private AreaType validate(byte[] content, String areaType, String crsCode) {
        AreaType area = AreaType.fromValue(areaType);

        if (content.length == 0 || !StringUtils.isNumeric(crsCode)) {
            throw new IllegalArgumentException("File is empty or crsCode is wrong.");
        }
        List<String> areaTypes = areaTypeService.listAllAreaTypeNames();
        if (!areaTypes.contains(areaType.toUpperCase())) {
            throw new IllegalArgumentException("Unsupported area type.");
        }
        return area;
    }

    private String getTempPath() throws IOException {
        Path tempPath = Files.createTempDirectory(PREFIX);
        return tempPath.toString() + File.separator;
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
