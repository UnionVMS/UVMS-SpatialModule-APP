package eu.europa.ec.fisheries.uvms.spatial.util;

import com.google.common.collect.Maps;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipExtractor {

    public Map<SupportedFileExtensions, String> unZipFile(String zipFilePath, Path outputFolderPath) throws IOException {
        Map<SupportedFileExtensions, String> fileNames = Maps.newHashMap();

        byte[] buffer = new byte[2048];

        File folder = new File(outputFolderPath.toString() + File.separator);
        if (!folder.exists()) {
            folder.mkdir();
        }

        ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zis.getNextEntry();

        while (entry != null) {
            String fileName = entry.getName();
            addFileNameToResultMap(fileNames, fileName);

            File newFile = new File(outputFolderPath + File.separator + fileName);

            System.out.println("file unzip : " + newFile.getAbsoluteFile());

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

    private void addFileNameToResultMap(Map<SupportedFileExtensions, String> fileNames, String fileName) {
        String extension = FilenameUtils.getExtension(fileName);
        SupportedFileExtensions supportedExtension = SupportedFileExtensions.fromValue(extension);

        if (supportedExtension != null) {
            switch (supportedExtension) {
                case SHP:
                    fileNames.put(SupportedFileExtensions.SHP, fileName);
                    break;
                case DBF:
                    fileNames.put(SupportedFileExtensions.DBF, fileName);
                    break;
                case SHX:
                    fileNames.put(SupportedFileExtensions.SHX, fileName);
                    break;
            }
        }
    }


}
