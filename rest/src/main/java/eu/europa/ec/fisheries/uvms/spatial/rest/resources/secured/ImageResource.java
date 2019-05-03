/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured;

import eu.europa.ec.fisheries.uvms.commons.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.spatial.rest.resources.unsecured.LegendResource;
import eu.europa.ec.fisheries.uvms.spatial.rest.resources.unsecured.PositionResource;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ExceptionInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ImageEncoderFactory;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.impl.PropertiesBean;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.mapfish.request.Class;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.mapfish.request.Cluster;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.mapfish.request.Icons;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.mapfish.response.ImageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// TODO  fix legendEntry
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * @implicitParam roleName|string|header|true||||||
 * @implicitParam scopeName|string|header|true|EC|||||
 * @implicitParam authorization|string|header|true||||||jwt token
 */
@Path("/image")
public class ImageResource extends UnionVMSResource {

    private final static Logger log = LoggerFactory.getLogger(ImageResource.class);

    public static final String SCALE_1_3 = "scale(1.3)";
    public static final String SCALE_0_3 = "scale(0.3)";

    @EJB
    private PropertiesBean propertiesBean;

    @Path("/position")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listPositionEntryKeys() throws IOException {
        return createSuccessResponse(PositionResource.getpositionEntries().keySet());
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Interceptors(value = { ExceptionInterceptor.class})
    public Response renderImages(@Context HttpServletRequest request, Icons payload)  {

        ImageResponse response = new ImageResponse();
        response.getLegend().withBase("/" + propertiesBean.getProperty("context.root") + "/spatial/image/legend/");

        if (payload.getPositions() != null){
            handlePositions(payload, response);
        }

        if (payload.getSegments() != null) {
            handleSegments(payload, response);
        }

        if (payload.getAlarms() != null) {
            handleAlarms(payload, response);
        }

        return createSuccessResponse(response);

    }

    private void handleAlarms(Icons payload, ImageResponse response)  {
        try {
            List<ImageEncoderFactory.LegendEntry> temp = new ArrayList<>();

            for (Class clazz : payload.getAlarms().getClasses()) {

                ImageEncoderFactory.LegendEntry legendEntry = new ImageEncoderFactory.LegendEntry();
//                legendEntry.setMsg(clazz.getText());
                BufferedImage alarmIconForLegend = ImageEncoderFactory.renderAlarm(clazz.getColor());
//                legendEntry.setIcon(alarmIconForLegend);
                temp.add(legendEntry);
            }

            String guid = UUID.randomUUID().toString();
            response.getLegend().withAlarms(guid);
            LegendResource.getLegendEntries().put(guid, ImageEncoderFactory.renderLegend(temp, payload.getAlarms().getTitle(), 25));
        } catch ( IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void handlePositions(Icons payload, ImageResponse response) {
        try {
            response.getMap().getVmspos().withBase("/" + propertiesBean.getProperty("context.root") + "/spatial/image/position/");
            List<ImageEncoderFactory.LegendEntry> temp = new ArrayList<>();

            for (Class clazz : payload.getPositions().getClasses()) { // TODO validate hex value

                ImageEncoderFactory.LegendEntry legendEntry = new ImageEncoderFactory.LegendEntry();
 //               legendEntry.setMsg(clazz.getText());

                if (PositionResource.getpositionEntries().get(clazz.getColor().replace("#", EMPTY)) == null){
                    BufferedImage positionForMapIcon = ImageEncoderFactory.renderPosition(clazz.getColor());
                    PositionResource.getpositionEntries().put(clazz.getColor().replace("#", EMPTY), positionForMapIcon);
                }

                BufferedImage iconForLegend = ImageEncoderFactory.renderPosition(clazz.getColor(), SCALE_0_3);
//                legendEntry.setIcon(iconForLegend);

                response.getMap().getVmspos().getColors().add(clazz.getColor().replace("#", EMPTY));
                temp.add(legendEntry);
            }

            Cluster cluster = payload.getPositions().getCluster();

            if (cluster != null){

                BufferedImage bufferedImage = ImageEncoderFactory.renderCluster(cluster.getBgcolor(), cluster.getBordercolor());
                ImageEncoderFactory.LegendEntry clusterEntry = new ImageEncoderFactory.LegendEntry();
//                clusterEntry.setMsg(cluster.getText());
//                clusterEntry.setIcon(bufferedImage);
                temp.add(clusterEntry);
            }

            String guid = UUID.randomUUID().toString();
            response.getLegend().withPositions(guid);
            LegendResource.getLegendEntries().put(guid, ImageEncoderFactory.renderLegend(temp, payload.getPositions().getTitle(), 25));

        } catch ( IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void handleSegments(Icons payload, ImageResponse response)  {
        try {
            String lineStyle = payload.getSegments().getLineStyle();
            List<ImageEncoderFactory.LegendEntry> temp = new ArrayList<>();

            for (eu.europa.ec.fisheries.uvms.spatial.service.dto.mapfish.request.Class clazz : payload.getSegments().getClasses()) {

                ImageEncoderFactory.LegendEntry legendEntry = new ImageEncoderFactory.LegendEntry();
  //              legendEntry.setMsg(clazz.getText());
                BufferedImage segmentIconForLegend = ImageEncoderFactory.renderSegment(clazz.getColor(), lineStyle, SCALE_1_3);
//                legendEntry.setIcon(segmentIconForLegend);
                temp.add(legendEntry);
            }

            String guid = UUID.randomUUID().toString();
            response.getLegend().withSegments(guid);
            LegendResource.getLegendEntries().put(guid, ImageEncoderFactory.renderLegend(temp, payload.getSegments().getTitle(), 40));

        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}