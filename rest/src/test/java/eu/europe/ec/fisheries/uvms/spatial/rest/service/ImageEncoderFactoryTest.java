package eu.europe.ec.fisheries.uvms.spatial.rest.service;

import eu.europa.ec.fisheries.uvms.spatial.rest.util.ImageEncoderFactory;
import lombok.SneakyThrows;
import org.apache.batik.transcoder.TranscoderException;
import org.junit.Test;
import org.w3c.dom.Document;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageEncoderFactoryTest {

    @Test
    @SneakyThrows
    public void test() {

        BufferedImage position = getPosition();
        BufferedImage line = getLine();

        List<ImageEncoderFactory.LegendEntry> entries = new ArrayList<>();
        ImageEncoderFactory.LegendEntry legendEntry = new ImageEncoderFactory.LegendEntry();
        legendEntry.setMsg("Hello world!");
        legendEntry.setIcon(position);
        entries.add(legendEntry);

        legendEntry = new ImageEncoderFactory.LegendEntry();
        legendEntry.setMsg("Hello Greg");
        legendEntry.setIcon(position);
        entries.add(legendEntry);

        legendEntry = new ImageEncoderFactory.LegendEntry();
        legendEntry.setMsg("Speedy Gonzales");
        legendEntry.setIcon(line);
        entries.add(legendEntry);

        BufferedImage image = ImageEncoderFactory.renderLegend(entries, "TITLE", 55);

        File outputfile = new File("test2.png");
        ImageIO.write(image, "PNG", outputfile);

    }

    private BufferedImage getPosition() throws IOException, TranscoderException {
        Document document = ImageEncoderFactory.createDocument("/position.svg");
        return ImageEncoderFactory.getBufferedImage(document);
    }

    private BufferedImage getLine() throws IOException, TranscoderException {
        Document document = ImageEncoderFactory.createDocument("/line.svg");
        return ImageEncoderFactory.getBufferedImage(document);
    }
}
