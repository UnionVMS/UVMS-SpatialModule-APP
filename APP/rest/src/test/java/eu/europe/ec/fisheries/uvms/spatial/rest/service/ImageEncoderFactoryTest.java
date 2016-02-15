package eu.europe.ec.fisheries.uvms.spatial.rest.service;

import com.google.common.io.Resources;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ImageEncoderFactory;
import lombok.SneakyThrows;
import org.apache.batik.transcoder.TranscoderException;
import org.junit.Test;
import org.w3c.dom.Document;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ImageEncoderFactoryTest {

    @Test
    @SneakyThrows
    public void test() {

        BufferedImage position = getPosition();
      //  BufferedImage icon1 = getIcon1();

        List<ImageEncoderFactory.LegendEntry> entries = new ArrayList<>();
        ImageEncoderFactory.LegendEntry legendEntry = new ImageEncoderFactory.LegendEntry();
        legendEntry.setMsg("Hello world!");
        legendEntry.setIcon(position);
        entries.add(legendEntry);

        /*legendEntry = new ImageEncoderFactory.LegendEntry();
        legendEntry.msg="Hello Greg";
        legendEntry.icon = icon1;
        entries.add(legendEntry);

        legendEntry = new ImageEncoderFactory.LegendEntry();
        legendEntry.msg="Speedy Gonzales";
        legendEntry.icon = icon;
        entries.add(legendEntry);

        legendEntry = new ImageEncoderFactory.LegendEntry();
        legendEntry.msg="A"	;
        legendEntry.icon = icon;
        entries.add(legendEntry);

        legendEntry = new ImageEncoderFactory.LegendEntry();
        legendEntry.msg="B";
        legendEntry.icon = icon1;
        entries.add(legendEntry);

        legendEntry = new ImageEncoderFactory.LegendEntry();
        legendEntry.msg="C";
        legendEntry.icon = icon;
        entries.add(legendEntry);*/
        BufferedImage image = ImageEncoderFactory.renderLegend(entries, "TITLE");

        File outputfile = new File("fuck.png");
        ImageIO.write(image, "PNG", outputfile);

    }

    private BufferedImage getPosition() throws IOException, TranscoderException {
        Document document = ImageEncoderFactory.createDocument("/position.svg");
        return ImageEncoderFactory.getBufferedImage(document);
    }

   /* private BufferedImage getIcon1(){
        BufferedImage img;
        try {
            img = ImageIO.read(new File("c:\\vessel1.png"));
        } catch (IOException e) {
        }
        return img;
    }*/
}
