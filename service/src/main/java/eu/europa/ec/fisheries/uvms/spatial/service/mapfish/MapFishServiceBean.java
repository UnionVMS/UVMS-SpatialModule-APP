/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.mapfish;

import eu.europa.ec.fisheries.uvms.spatial.service.MapFishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.batik.transcoder.TranscoderException;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Stateless
@Local(MapFishService.class)
@Slf4j
public class MapFishServiceBean implements MapFishService{

    @Override
    public void saveVesselIconsWithColor(List<String> colors) throws IOException, TranscoderException {

        File path = new File("app/mapfish/");

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