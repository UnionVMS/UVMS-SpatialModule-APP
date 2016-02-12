package eu.europa.ec.fisheries.uvms.spatial.util;

import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ZipExtractorTest {

    private static final String ZIP_DIR = "zip/";
    private static final String EEZ_ZIP_FILE = "eez.zip";
    private static final String PREFIX = "temp";

    private ZipExtractor zipExtractor = new ZipExtractor();

    public void shouldUnzipFile() throws Exception {
        // given
        String absoluteZipPath = getAbsoluteZipPath();
        String zipFilePath = absoluteZipPath + EEZ_ZIP_FILE;
        Path outputFolderPath = Paths.get(getTempPath());

        // when
        try {
            Map<SupportedFileExtensions, String> filesNames = zipExtractor.unZipFile(zipFilePath, outputFolderPath);
            assertEquals(3, filesNames.size());
            assertEquals("eez.shp", filesNames.get(SupportedFileExtensions.SHP));
            assertEquals("eez.dbf", filesNames.get(SupportedFileExtensions.DBF));
            assertEquals("eez.shx", filesNames.get(SupportedFileExtensions.SHX));
        } catch (Exception ex) {
            fail("Should not throw exception");
        }

        //then
        System.out.println("Pass. OK");
    }

    private String getTempPath() throws IOException {
        Path temp = Files.createTempDirectory(PREFIX);
        return temp.toString() + File.separator;
    }

    private String getAbsoluteZipPath() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(ZIP_DIR + EEZ_ZIP_FILE).getFile());
        return file.getParent() + File.separator;
    }
}