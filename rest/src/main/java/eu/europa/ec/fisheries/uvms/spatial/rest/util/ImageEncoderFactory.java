package eu.europa.ec.fisheries.uvms.spatial.rest.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.transcoder.SVGAbstractTranscoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

@Slf4j
public class ImageEncoderFactory {

    private ImageEncoderFactory(){}

    private static final int LINE_HEIGHT = 22;
    private static Font FONT_BOLD = new Font("Arial", Font.BOLD, LINE_HEIGHT);
    private static Font FONT_NORMAL = new Font("Arial", Font.PLAIN, LINE_HEIGHT);

    private static int hOffset = 0;

    public static BufferedImage renderSegment(String hexColor, String strokeDashArray) throws TranscoderException, IOException {

        log.debug("Rendering segment");
        Document line = createDocument("/line.svg");
        NamedNodeMap attributes = line.getElementById("line").getAttributes();
        attributes.getNamedItem("stroke").getFirstChild().setNodeValue(hexColor);
        attributes.getNamedItem("stroke-dasharray").getFirstChild().setNodeValue(strokeDashArray);
        return getBufferedImage(line);
    }

    public static BufferedImage renderPosition(String hexColor) throws Exception {

        Document position = createDocument("/position.svg");
        position.getElementById("position").getAttributes().getNamedItem("style").getFirstChild().setNodeValue("fill:" + hexColor);
        return getBufferedImage(position);
    }

    public static BufferedImage getBufferedImage(Document document) throws TranscoderException, IOException {

        ByteArrayOutputStream resultByteStream = new ByteArrayOutputStream();
        TranscoderInput transcoderInput = new TranscoderInput(document);
        TranscoderOutput transcoderOutput = new TranscoderOutput(resultByteStream);
        PNGTranscoder pngTranscoder = new PNGTranscoder();
        pngTranscoder.addTranscodingHint(SVGAbstractTranscoder.KEY_HEIGHT, 256f);
        pngTranscoder.addTranscodingHint(SVGAbstractTranscoder.KEY_WIDTH, 256f);
        pngTranscoder.transcode(transcoderInput, transcoderOutput);
        resultByteStream.flush();
        return ImageIO.read(new ByteArrayInputStream(resultByteStream.toByteArray()));
    }

    static public void embedIcon(BufferedImage mainImage, BufferedImage icon, int imageX, int imageY, int reductionScale){
        for (int i=0;i<icon.getWidth();i=i+reductionScale){
            for (int j=0;j<icon.getHeight();j=j+reductionScale){
                int localRGB = icon.getRGB(i, j);
                mainImage.setRGB(imageX+i,  imageY+j, localRGB);
            }
        }
    }

    public static Document createDocument(String pathToSvg) {

        try {

            InputStream is = ImageEncoderFactory.class.getResourceAsStream(pathToSvg);
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
            return f.createDocument("http://www.w3.org/2000/svg", is);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    static public BufferedImage renderLegend(List<LegendEntry> legendEntries, String title, int offset) throws Exception {
        int width = 500, height = (500 + 50*legendEntries.size());
        // TYPE_INT_ARGB specifies the image format: 8-bit RGBA packed
        // into integer pixels
        BufferedImage mainImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D ig2 = mainImage.createGraphics();
        ig2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        ig2.setFont(FONT_BOLD);

        int increment = 40;

        ig2.setPaint(Color.black);
        ig2.drawString(title, hOffset, 20);

        for (LegendEntry entry:legendEntries){
            FontMetrics fontMetrics = ig2.getFontMetrics();
            int stringWidth = fontMetrics.stringWidth(entry.msg);
            int stringHeight = fontMetrics.getAscent();

            if(entry.icon!=null)
                embedIcon(mainImage,entry.icon, 0,increment+stringHeight-10, 1);

            ig2.setFont(FONT_NORMAL);

            ig2.drawString(entry.msg, hOffset+offset, increment+2*stringHeight-10);

            increment = increment+ stringHeight+20;
        }

        return mainImage;

    }

    public static class LegendEntry {

        private BufferedImage icon;
        private String msg;

        public void setIcon(BufferedImage icon) {

            this.icon = icon;
        }

        public void setMsg(String msg) {

            this.msg = msg;
        }
    }
}
