/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.rest.util;

import lombok.Data;
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
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

@Slf4j
public class ImageEncoderFactory {

    private static int LINE_HEIGHT = 12;
    private static Font FONT_NORMAL = new Font("Arial", Font.PLAIN, LINE_HEIGHT);
    private static final String STROKE_DASH_ARRAY = "stroke-dasharray";
    private static final String STROKE = "stroke";
    private static final String POSITION = "position";
    private static final String ALARM = "alarm";
    private static final String STYLE = "style";
    private static final String LINE = "line";
    private static final String POSITION_SVG = "/position.svg";
    private static final String ALARM_SVG = "/alarm.svg";
    private static final String SEGMENT_SVG = "/line.svg";
    private static final String CLUSTER_SVG = "/cluster.svg";
    private static final String TRANSFORM = "transform";
    private static final String FILL = "fill:";
    public static final String CIRCLE = "circle";
    private static final int LINE_SPACING = 13;
    private static final int TITLE_OFFSET = 10;
    private static final int WIDTH = 200;

    private ImageEncoderFactory(){}

    public static BufferedImage renderCluster(String backGroundColor, String borderColor) throws TranscoderException, IOException {

        log.debug("Rendering cluster icon with color {} ", borderColor);
        Document cluster = createDocument(CLUSTER_SVG);
        NamedNodeMap attributes = cluster.getElementById(CIRCLE).getAttributes();
        attributes.getNamedItem("stroke").getFirstChild().setNodeValue(borderColor);
        return getBufferedImage(cluster);
    }

    public static BufferedImage renderSegment(String hexColor, String strokeDashArray, String scale) throws TranscoderException, IOException {

        log.debug("Rendering segment icon with color {} ", hexColor);
        Document line = createDocument(SEGMENT_SVG);
        NamedNodeMap attributes = line.getElementById(LINE).getAttributes();
        attributes.getNamedItem(TRANSFORM).getFirstChild().setNodeValue(scale);
        attributes.getNamedItem(STROKE).getFirstChild().setNodeValue(hexColor);
        attributes.getNamedItem(STROKE_DASH_ARRAY).getFirstChild().setNodeValue(strokeDashArray);
        return getBufferedImage(line);
    }

    public static BufferedImage renderPosition(String hexColor, String scale) throws IOException, TranscoderException {

        log.debug("Rendering position icon with color {} ", hexColor);
        Document position = createDocument(POSITION_SVG);
        position.getElementById("scale").getAttributes().getNamedItem(TRANSFORM).getFirstChild().setNodeValue(scale);
        position.getElementById(POSITION).getAttributes().getNamedItem(STYLE).getFirstChild().setNodeValue(FILL + hexColor);
        return getBufferedImage(position);
    }

    public static BufferedImage renderPosition(String hexColor) throws IOException, TranscoderException {

        log.debug("Rendering position icon with color {} ", hexColor);
        Document position = createDocument(POSITION_SVG);
        position.getElementById(POSITION).getAttributes().getNamedItem(STYLE).getFirstChild().setNodeValue(FILL + hexColor);
        return getBufferedImage(position);
    }

    public static BufferedImage renderAlarm(String hexColor) throws IOException, TranscoderException {

        log.debug("Rendering alarm icon with color {} ", hexColor);
        Document alarm = createDocument(ALARM_SVG);
        alarm.getElementById(ALARM).getAttributes().getNamedItem("fill").getFirstChild().setNodeValue(hexColor);
        return getBufferedImage(alarm);
    }

    public static BufferedImage getBufferedImage(Document document) throws TranscoderException, IOException {

        ByteArrayOutputStream resultByteStream = new ByteArrayOutputStream();
        TranscoderInput transcoderInput = new TranscoderInput(document);
        TranscoderOutput transcoderOutput = new TranscoderOutput(resultByteStream);
        PNGTranscoder pngTranscoder = new PNGTranscoder();
        pngTranscoder.addTranscodingHint(SVGAbstractTranscoder.KEY_HEIGHT, 56f);
        pngTranscoder.addTranscodingHint(SVGAbstractTranscoder.KEY_WIDTH, 35f);
        pngTranscoder.transcode(transcoderInput, transcoderOutput);
        resultByteStream.flush();
        return ImageIO.read(new ByteArrayInputStream(resultByteStream.toByteArray()));
    }

    public static void embedIcon(BufferedImage mainImage, BufferedImage icon, int imageX, int imageY, int reductionScale){
        for (int i=0;i<icon.getWidth();i=i+reductionScale){
            for (int j=0;j<icon.getHeight();j=j+reductionScale){
                int localRGB = icon.getRGB(i, j);
                if(imageX + i < mainImage.getWidth() && imageY + j < mainImage.getHeight())
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
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static BufferedImage renderLegend(List<LegendEntry> legendEntries, String title, int iconAndTextOffset) {

        int height = 20 + (LINE_HEIGHT+ LINE_SPACING) * legendEntries.size();

        // TYPE_INT_ARGB specifies the image format: 8-bit RGBA packed
        // into integer pixels
        BufferedImage mainImage = new BufferedImage(WIDTH, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D ig2 = mainImage.createGraphics();
        ig2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        ig2.setFont(FONT_NORMAL);

        int increment = TITLE_OFFSET + 2 * LINE_SPACING;

        ig2.setPaint(Color.black);
        int OFFSET = 0;
        ig2.drawString(title, OFFSET, TITLE_OFFSET);

        for (LegendEntry entry:legendEntries){
            FontMetrics fontMetrics = ig2.getFontMetrics();
            int stringHeight = fontMetrics.getAscent();

            if(entry.icon != null) // must be placed higher then the image by font height
                embedIcon(mainImage,entry.icon, OFFSET, increment-stringHeight, 1);

            ig2.setFont(FONT_NORMAL);
            ig2.drawString(entry.msg, OFFSET + iconAndTextOffset, increment);

            increment += stringHeight + LINE_SPACING;
        }

        return mainImage;

    }

    @Data
    public static class LegendEntry {
        private BufferedImage icon;
        private String msg;
    }
}