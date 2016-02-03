package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.util.FileSaver;
import eu.europa.ec.fisheries.uvms.spatial.util.ShapeFileReader;
import eu.europa.ec.fisheries.uvms.spatial.util.ZipExtractor;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Stateless
@Local(AreaUploadService.class)
@Transactional
@Slf4j
public class AreaUploadServiceBean implements AreaUploadService {

    private static final String AREA_ZIP_FILE = "AreaFile.zip";
    private static final String PREFIX = "temp";

    @Override
    public void uploadArea(byte[] content, String areaType, String crsCode) throws IOException {
        String absolutePath = getTempPath();
        String zipFilePath = absolutePath + AREA_ZIP_FILE;

        FileSaver fileSaver = new FileSaver();
        fileSaver.saveContentToFile(content, zipFilePath);

        ZipExtractor zipExtractor = new ZipExtractor();
        zipExtractor.unZipFile(zipFilePath, absolutePath);

        //TODO Not finished yet!
        ShapeFileReader shapeFileReader = new ShapeFileReader();
        shapeFileReader.readShapeFile(absolutePath);
    }

    private String getTempPath() throws IOException {
        Path tempPath = Files.createTempDirectory(PREFIX);
        return tempPath.toString() + File.separator;
    }

}
