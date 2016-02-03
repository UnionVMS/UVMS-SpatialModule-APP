package eu.europa.ec.fisheries.uvms.spatial.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipExtractor {

    public void unZipFile(String zipFilePath, String outputFolderPath) throws IOException {

        byte[] buffer = new byte[2048];

        File folder = new File(outputFolderPath);
        if (!folder.exists()) {
            folder.mkdir();
        }

        ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zis.getNextEntry();

        while (entry != null) {
            String fileName = entry.getName();
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
    }

}
