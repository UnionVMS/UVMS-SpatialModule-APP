package eu.europa.ec.fisheries.uvms.spatial.rest.util;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.transcoder.SVGAbstractTranscoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageFactory {

    static Font font = new Font("TimesRoman", Font.BOLD, 20);
    static int hOffset = 100;

    public static BufferedImage renderLine(String color, String strokeDashArray) throws TranscoderException, IOException {

        Document line = createDocument("/line.svg");
        line.getElementById("line").getAttributes().getNamedItem("stroke").getFirstChild().setNodeValue("#" + color);
        line.getElementById("line").getAttributes().getNamedItem("stroke-dasharray").getFirstChild().setNodeValue(strokeDashArray);
        return getBufferedImage(line);
    }

    public static BufferedImage renderPosition(String color) throws Exception {

        Document position = createDocument("/position.svg");
        position.getElementById("position").getAttributes().getNamedItem("style").getFirstChild().setNodeValue("fill:#" + color);
        return getBufferedImage(position);
    }

    private static BufferedImage getBufferedImage(Document line) throws TranscoderException, IOException {

        ByteArrayOutputStream resultByteStream = new ByteArrayOutputStream();
        TranscoderInput transcoderInput = new TranscoderInput(line);
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
                mainImage.setRGB(imageX+i/reductionScale,  imageY+j/reductionScale-20, localRGB);
            }
        }
    }

    private static Document createDocument(String pathToSvg) {

        try {

            InputStream is = ImageFactory.class.getResourceAsStream(pathToSvg);
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
            return f.createDocument("http://www.w3.org/2000/svg", is);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    static public BufferedImage renderLegend(List<LegendEntry> legendEntries, String title) throws Exception {
        int width = 500, height = (500 + 50*legendEntries.size());
        // TYPE_INT_ARGB specifies the image format: 8-bit RGBA packed
        // into integer pixels
        BufferedImage mainImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D ig2 = mainImage.createGraphics();

        ig2.setFont(font);

        int increment = 150;

        ig2.setPaint(Color.black);
        ig2.drawString(title, hOffset, 25);

        for (LegendEntry entry:legendEntries){
            FontMetrics fontMetrics = ig2.getFontMetrics();
            int stringWidth = fontMetrics.stringWidth(entry.msg);
            int stringHeight = fontMetrics.getAscent();
            ig2.drawString(entry.msg, hOffset+30, increment+stringHeight);

            if(entry.icon!=null)
                embedIcon(mainImage,entry.icon, 0,increment+stringHeight, 10);

            increment = increment+ stringHeight+10;
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
