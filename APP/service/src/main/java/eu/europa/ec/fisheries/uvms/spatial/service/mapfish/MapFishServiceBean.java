package eu.europa.ec.fisheries.uvms.spatial.service.mapfish;

import eu.europa.ec.fisheries.uvms.spatial.service.MapFishService;
import lombok.extern.slf4j.Slf4j;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

@Stateless
@Local(MapFishService.class)
@Slf4j
public class MapFishServiceBean implements MapFishService{

    @Override
    public void saveVesselIconsWithColor(List<String> colors) throws Exception {

        File path = new File("app/mapfish/");
        String property = System.getProperty("user.dir");
        for (String color :  colors){
            // TODO check hex value
            File file = new File(path, "/vessel.svg");
            File outputfile = new File(path, "vessel_#" + color + ".png");

            if (outputfile.createNewFile()) {
                BufferedImage bufferedImage = SVGUtil.convertSVGToPNG(file.toURI().toURL(), "polygon", color);
                ImageIO.write(bufferedImage, "png", outputfile);
            }
        }

    }
}
