/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.rest.service;

import eu.europa.ec.fisheries.uvms.BaseUnitilsTest;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ImageEncoderFactory;
import lombok.SneakyThrows;
import org.apache.batik.transcoder.TranscoderException;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageEncoderFactoryTest extends BaseUnitilsTest {

    @Test
    @SneakyThrows
    public void test() {

        BufferedImage position = getPosition("scale(0.3)");
        BufferedImage line = getLine("scale(1.3)");
        BufferedImage cluster = getCluster();

        List<ImageEncoderFactory.LegendEntry> entries = new ArrayList<>();
        ImageEncoderFactory.LegendEntry legendEntry = new ImageEncoderFactory.LegendEntry();
        legendEntry.setMsg("Hello world!");
        legendEntry.setIcon(position);
        entries.add(legendEntry);

        legendEntry = new ImageEncoderFactory.LegendEntry();
        legendEntry.setMsg("Hello Cluster!");
        legendEntry.setIcon(cluster);
        entries.add(legendEntry);

        legendEntry = new ImageEncoderFactory.LegendEntry();
        legendEntry.setMsg("Hello Greg");
        legendEntry.setIcon(position);
        entries.add(legendEntry);

        legendEntry = new ImageEncoderFactory.LegendEntry();
        legendEntry.setMsg("Speedy Gonzales");
        legendEntry.setIcon(line);
        entries.add(legendEntry);

        BufferedImage image = ImageEncoderFactory.renderLegend(entries, "TITLE", 40);

        File outputfile = File.createTempFile("test2", ".png");
        ImageIO.write(image, "PNG", outputfile);

    }

    private BufferedImage getCluster() throws IOException, TranscoderException {
        Document cluster = ImageEncoderFactory.createDocument("/cluster.svg");
        NamedNodeMap attributes = cluster.getElementById("circle").getAttributes();
        attributes.getNamedItem("stroke").getFirstChild().setNodeValue("#EE0000");
        return ImageEncoderFactory.getBufferedImage(cluster);
    }

    @Test
    @SneakyThrows
    public void test3() {
        BufferedImage position = getPosition("scale(1)");
        File outputfile = File.createTempFile("test3", ".png");
        ImageIO.write(position, "PNG", outputfile);
    }

    private BufferedImage getPosition(String scale) throws Exception {
        Document position = ImageEncoderFactory.createDocument("/position.svg");
        position.getElementById("scale").getAttributes().getNamedItem("transform").getFirstChild().setNodeValue(scale);
        position.getElementById("position").getAttributes().getNamedItem("style").getFirstChild().setNodeValue("fill:" + "#DFSFQS");
        return ImageEncoderFactory.getBufferedImage(position);
    }

    private BufferedImage getLine(String scale) throws IOException, TranscoderException {
        Document segment = ImageEncoderFactory.createDocument("/line.svg");
        segment.getElementById("line").getAttributes().getNamedItem("transform").getFirstChild().setNodeValue(scale);
        return ImageEncoderFactory.getBufferedImage(segment);
    }
}