package eu.europa.ec.fisheries.uvms.spatial.mapfish;

import com.google.common.io.Resources;
import eu.europa.ec.fisheries.uvms.spatial.service.mapfish.SVGUtil;
import lombok.SneakyThrows;
import org.junit.Test;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

public class SVGUtilTest {

    @Test
    @SneakyThrows
    public void test() {

        BufferedImage bufferedImage = SVGUtil.convertSVGToPNG(Resources.getResource("mapfish/vessel.svg"), "polygon", "FF33FF");

        URL resource = this.getClass().getResource("/");
        File outputfile = new File(resource + "/eu/europa/ec/fisheries/uvms/spatial/mapfish/image.png");
        new File(outputfile.getParent()).mkdirs();
        ImageIO.write(bufferedImage, "png", outputfile);
    }
}
