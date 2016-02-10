package eu.europa.ec.fisheries.uvms.spatial.service.mapfish;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.transcoder.SVGAbstractTranscoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

public class SVGUtil {

    private SVGUtil(){};

    public static BufferedImage convertSVGToPNG(URL url, String elementId, String color) throws Exception {

        Document documentFromSvg = createDocumentFromSvg(url);
        documentFromSvg.getElementById(elementId).getAttributes().getNamedItem("style").getFirstChild().setNodeValue("fill:#" + color);

        ByteArrayOutputStream resultByteStream = new ByteArrayOutputStream();

        TranscoderInput transcoderInput = new TranscoderInput(documentFromSvg);
        TranscoderOutput transcoderOutput = new TranscoderOutput(resultByteStream);

        PNGTranscoder pngTranscoder = new PNGTranscoder();
        pngTranscoder.addTranscodingHint(SVGAbstractTranscoder.KEY_HEIGHT, 256f);
        pngTranscoder.addTranscodingHint(SVGAbstractTranscoder.KEY_WIDTH, 256f);
        pngTranscoder.transcode(transcoderInput, transcoderOutput);

        resultByteStream.flush();

        return ImageIO.read(new ByteArrayInputStream(resultByteStream.toByteArray()));
    }

    private static Document createDocumentFromSvg(URL url) throws Exception {

        try {
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
            return f.createDocument(url.getPath());
        } catch (IOException ex) {
            throw new Exception();
        }
    }
}
