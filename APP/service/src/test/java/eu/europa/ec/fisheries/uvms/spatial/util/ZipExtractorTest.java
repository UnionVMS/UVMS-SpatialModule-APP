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

public class ZipExtractorTest {

    private static final String EEZ_ZIP_FILE = "zip/eez.zip";
    private static final String PREFIX = "temp";

    private ZipExtractor zipExtractor = new ZipExtractor();

    public void shouldUnzipFile() throws Exception {
        // given
        Path zipFilePath = getAbsoluteZipPath();
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

    private Path getAbsoluteZipPath() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(EEZ_ZIP_FILE).getPath());
        return file.toPath();
    }
}