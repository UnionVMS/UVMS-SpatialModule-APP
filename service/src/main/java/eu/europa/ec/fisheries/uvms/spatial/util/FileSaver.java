package eu.europa.ec.fisheries.uvms.spatial.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileSaver {

    public void saveContentToFile(byte[] content, String fileName) throws IOException {
        File file = new File(fileName);

        if (!file.exists()) {
            file.createNewFile();
        }

        FileOutputStream fop = new FileOutputStream(file);

        fop.write(content);
        fop.flush();
        fop.close();
    }

}
