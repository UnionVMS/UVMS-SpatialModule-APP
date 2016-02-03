package eu.europa.ec.fisheries.uvms.spatial.util;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.fail;

public class ZipExtractorTest {

    private static final String ZIP_DIR = "zip/";
    private static final String EEZ_ZIP_FILE = "eez.zip";
    private static final String PREFIX = "temp";

    private ZipExtractor zipExtractor = new ZipExtractor();

    @Test
    public void shouldUnzipFile() throws Exception {
        // given
        String absoluteZipPath = getAbsoluteZipPath();
        String zipFilePath = absoluteZipPath + EEZ_ZIP_FILE;
        String outputFolderPath = getTempPath();

        // when
        try {
            zipExtractor.unZipFile(zipFilePath, outputFolderPath);
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