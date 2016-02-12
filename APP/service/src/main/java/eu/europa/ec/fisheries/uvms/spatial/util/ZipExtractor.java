package eu.europa.ec.fisheries.uvms.spatial.util;

import com.google.common.collect.Maps;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipExtractor {

    public Map<SupportedFileExtensions, Path> unZipFile(Path zipFilePath, Path outputFolderPath) throws IOException {
        Map<SupportedFileExtensions, Path> fileNames = Maps.newHashMap();

        byte[] buffer = new byte[2048];

        File folder = outputFolderPath.toFile();
        if (!folder.exists()) {
            folder.mkdir();
        }

        ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath.toFile()));
        ZipEntry entry = zis.getNextEntry();

        while (entry != null) {
            Path outputFilePath = Paths.get(outputFolderPath + File.separator + entry.getName());
            addFileNameToResultMap(fileNames, outputFilePath);

            File newFile = outputFilePath.toFile();

            //create all non exists folders else you will hit FileNotFoundException for compressed folder
            new File(newFile.getParent()).mkdirs();

            FileOutputStream fos = new FileOutputStream(newFile);

            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }

            fos.close();
            entry = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();

        return fileNames;
    }

    private void addFileNameToResultMap(Map<SupportedFileExtensions, Path> fileNames, Path outputFilePath) {
        String extension = FilenameUtils.getExtension(outputFilePath.toString());
        SupportedFileExtensions supportedExtension = SupportedFileExtensions.fromValue(extension);

        if (supportedExtension != null) {
            switch (supportedExtension) {
                case SHP:
                    fileNames.put(SupportedFileExtensions.SHP, outputFilePath);
                    break;
                case DBF:
                    fileNames.put(SupportedFileExtensions.DBF, outputFilePath);
                    break;
                case SHX:
                    fileNames.put(SupportedFileExtensions.SHX, outputFilePath);
                    break;
            }
        }
    }


}
