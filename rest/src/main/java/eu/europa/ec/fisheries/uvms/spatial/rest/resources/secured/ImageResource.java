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

import static org.apache.commons.lang3.StringUtils.EMPTY;

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

import eu.europa.ec.fisheries.uvms.commons.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.rest.resources.unsecured.LegendResource;
import eu.europa.ec.fisheries.uvms.spatial.rest.resources.unsecured.PositionResource;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ExceptionInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ImageEncoderFactory;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.impl.PropertiesBean;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.mapfish.request.Class;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.mapfish.request.Cluster;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.mapfish.request.Icons;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.mapfish.response.ImageResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.batik.transcoder.TranscoderException;

/**
 * @implicitParam roleName|string|header|true||||||
 * @implicitParam scopeName|string|header|true|EC|||||
 * @implicitParam authorization|string|header|true||||||jwt token
 */
@Path("/image")
@Slf4j
public class ImageResource extends UnionVMSResource {

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
    public Response renderImages(@Context HttpServletRequest request, Icons payload) throws ServiceException {

        ImageResponse response = new ImageResponse();
        response.getLegend().withBase("/" + sanitizeUrl(propertiesBean.getProperty("context.root")) + "/spatial/image/legend/");

        if (payload.getPositions() != null){
            handlePositions(payload, response);
        }

        if (payload.getSegments() != null) {
            handleSegments(payload, response);
        }

        if (payload.getAlarms() != null) {
            handleAlarms(payload, response);
        }

        if (payload.getActivities() != null) {
            handleActivities(payload, response);
        }

        return createSuccessResponse(response);

    }

    private void handleActivities(Icons payload, ImageResponse response) throws ServiceException {

        List<ImageEncoderFactory.LegendEntry> temp = new ArrayList<>();
        Cluster cluster = payload.getActivities().getCluster();
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageEncoderFactory.renderActivity(cluster.getBgcolor(), cluster.getBordercolor());
        } catch (TranscoderException | IOException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        ImageEncoderFactory.LegendEntry clusterEntry = new ImageEncoderFactory.LegendEntry();
        clusterEntry.setMsg(cluster.getText());
        clusterEntry.setIcon(bufferedImage);
        temp.add(clusterEntry);

        String guid = UUID.randomUUID().toString();
        response.getLegend().withActivities(guid);
        LegendResource.getLegendEntries().put(guid, ImageEncoderFactory.renderLegend(temp, payload.getActivities().getTitle(), 25));

    }

    private void handleAlarms(Icons payload, ImageResponse response) throws ServiceException {
        try {
            List<ImageEncoderFactory.LegendEntry> temp = new ArrayList<>();

            for (Class clazz : payload.getAlarms().getClasses()) {

                ImageEncoderFactory.LegendEntry legendEntry = new ImageEncoderFactory.LegendEntry();
                legendEntry.setMsg(clazz.getText());
                BufferedImage alarmIconForLegend = ImageEncoderFactory.renderAlarm(clazz.getColor());
                legendEntry.setIcon(alarmIconForLegend);
                temp.add(legendEntry);
            }

            String guid = UUID.randomUUID().toString();
            response.getLegend().withAlarms(guid);
            LegendResource.getLegendEntries().put(guid, ImageEncoderFactory.renderLegend(temp, payload.getAlarms().getTitle(), 25));
        } catch (TranscoderException | IOException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    private void handlePositions(Icons payload, ImageResponse response) throws ServiceException {
        try {
            response.getMap().getVmspos().withBase("/" + sanitizeUrl(propertiesBean.getProperty("context.root")) + "/spatial/image/position/");
            List<ImageEncoderFactory.LegendEntry> temp = new ArrayList<>();

            for (Class clazz : payload.getPositions().getClasses()) { // TODO validate hex value

                ImageEncoderFactory.LegendEntry legendEntry = new ImageEncoderFactory.LegendEntry();
                legendEntry.setMsg(clazz.getText());

                if (PositionResource.getpositionEntries().get(clazz.getColor().replace("#", EMPTY)) == null){
                    BufferedImage positionForMapIcon = ImageEncoderFactory.renderPosition(clazz.getColor());
                    PositionResource.getpositionEntries().put(clazz.getColor().replace("#", EMPTY), positionForMapIcon);
                }

                BufferedImage iconForLegend = ImageEncoderFactory.renderPosition(clazz.getColor(), SCALE_0_3);
                legendEntry.setIcon(iconForLegend);

                response.getMap().getVmspos().getColors().add(clazz.getColor().replace("#", EMPTY));
                temp.add(legendEntry);
            }

            Cluster cluster = payload.getPositions().getCluster();

            if (cluster != null){

                BufferedImage bufferedImage = ImageEncoderFactory.renderCluster(cluster.getBgcolor(), cluster.getBordercolor());
                ImageEncoderFactory.LegendEntry clusterEntry = new ImageEncoderFactory.LegendEntry();
                clusterEntry.setMsg(cluster.getText());
                clusterEntry.setIcon(bufferedImage);
                temp.add(clusterEntry);
            }

            String guid = UUID.randomUUID().toString();
            response.getLegend().withPositions(guid);
            LegendResource.getLegendEntries().put(guid, ImageEncoderFactory.renderLegend(temp, payload.getPositions().getTitle(), 25));

        } catch (TranscoderException | IOException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    private void handleSegments(Icons payload, ImageResponse response) throws ServiceException {
        try {
            String lineStyle = payload.getSegments().getLineStyle();
            List<ImageEncoderFactory.LegendEntry> temp = new ArrayList<>();

            for (eu.europa.ec.fisheries.uvms.spatial.service.dto.mapfish.request.Class clazz : payload.getSegments().getClasses()) {

                ImageEncoderFactory.LegendEntry legendEntry = new ImageEncoderFactory.LegendEntry();
                legendEntry.setMsg(clazz.getText());
                BufferedImage segmentIconForLegend = ImageEncoderFactory.renderSegment(clazz.getColor(), lineStyle, SCALE_1_3);
                legendEntry.setIcon(segmentIconForLegend);
                temp.add(legendEntry);
            }

            String guid = UUID.randomUUID().toString();
            response.getLegend().withSegments(guid);
            LegendResource.getLegendEntries().put(guid, ImageEncoderFactory.renderLegend(temp, payload.getSegments().getTitle(), 40));

        } catch (TranscoderException | IOException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    private String sanitizeUrl(String url) {
        String sanitizedUrl = url;
        if (sanitizedUrl.startsWith("/")) {
            sanitizedUrl = sanitizedUrl.substring(1);
        }
        if (sanitizedUrl.endsWith("/")){
            return url.substring(0,url.length()-1);
        }
        return sanitizedUrl;
    }
}