package eu.europa.ec.fisheries.uvms.spatial.util;

import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ZipExtractorT {

    private static final String EEZ_ZIP_FILE = "zip/eez.zip";
    private static final String RFMO_ZIP_FILE = "zip/rfmo.zip";
    private static final String PREFIX = "temp";

    private ZipExtractor zipExtractor = new ZipExtractor();

    public void shouldUnzipEezFile() throws Exception {
        // given
        Path zipFilePath = getAbsoluteZipPath(EEZ_ZIP_FILE);
        Path outputFolderPath = Files.createTempDirectory(PREFIX);

        // when
        try {
            Map<SupportedFileExtensions, Path> filesNames = zipExtractor.unZipFile(zipFilePath, outputFolderPath);
            assertEquals(3, filesNames.size());
            assertEquals(Paths.get("eez.shp"), filesNames.get(SupportedFileExtensions.SHP).getFileName());
            assertEquals(Paths.get("eez.dbf"), filesNames.get(SupportedFileExtensions.DBF).getFileName());
            assertEquals(Paths.get("eez.shx"), filesNames.get(SupportedFileExtensions.SHX).getFileName());
        } catch (Exception ex) {
            fail("Should not throw exception");
        }

        //then
        System.out.println("Pass. OK");
    }

    public void shouldUnzipRfmoFile() throws Exception {
        // given
        Path zipFilePath = getAbsoluteZipPath(RFMO_ZIP_FILE);
        Path outputFolderPath = Files.createTempDirectory(PREFIX);

        // when
        try {
            Map<SupportedFileExtensions, Path> filesNames = zipExtractor.unZipFile(zipFilePath, outputFolderPath);
            assertEquals(3, filesNames.size());
            assertEquals(Paths.get("rfmo.shp"), filesNames.get(SupportedFileExtensions.SHP).getFileName());
            assertEquals(Paths.get("rfmo.dbf"), filesNames.get(SupportedFileExtensions.DBF).getFileName());
            assertEquals(Paths.get("rfmo.shx"), filesNames.get(SupportedFileExtensions.SHX).getFileName());
        } catch (Exception ex) {
            fail("Should not throw exception");
        }

        //then
        System.out.println("Pass. OK");
    }

    private Path getAbsoluteZipPath(String zipFile) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(zipFile).getPath());
        return file.toPath();
    }
}