package eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured;

import static org.apache.commons.lang3.StringUtils.*;

import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.spatial.model.mapfish.request.Class;
import eu.europa.ec.fisheries.uvms.spatial.model.mapfish.request.Icons;
import eu.europa.ec.fisheries.uvms.spatial.model.mapfish.response.ImageResponse;
import eu.europa.ec.fisheries.uvms.spatial.rest.resources.unsecured.LegendResource;
import eu.europa.ec.fisheries.uvms.spatial.rest.resources.unsecured.SegmentResource;
import eu.europa.ec.fisheries.uvms.spatial.rest.resources.unsecured.PositionResource;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ImageEncoderFactory;
import lombok.extern.slf4j.Slf4j;
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

@Path("/image")
@Slf4j
public class ImageResource extends UnionVMSResource {

    public static final String SCALE_1_3 = "scale(1.3)";
    public static final String SCALE_0_3 = "scale(0.3)";

    @Path("/position")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listPositionEntryKeys() throws IOException {
        return createSuccessResponse(PositionResource.positionEntries.keySet());
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response renderImages(@Context HttpServletRequest request, Icons icons) throws Exception {

        ImageResponse response = new ImageResponse();
        response.getLegend().withBase("/spatial/image/legend/");

        handlePositions(icons, response);

        handleSegments(icons, response);

        return createSuccessResponse(response);

    }

    private void handlePositions(Icons icons, ImageResponse response) throws Exception {

        response.getMap().getVmspos().withBase("/spatial/image/position/");
        List<ImageEncoderFactory.LegendEntry> temp = new ArrayList<>();

        for (Class clazz : icons.getPositions().getClasses()) { // TODO check hex value

            ImageEncoderFactory.LegendEntry legendEntry = new ImageEncoderFactory.LegendEntry();
            legendEntry.setMsg(clazz.getText());

            if (PositionResource.positionEntries.get(clazz.getColor()) == null){
                BufferedImage positionForMapIcon = ImageEncoderFactory.renderPosition(clazz.getColor());
                PositionResource.positionEntries.put(clazz.getColor().replace("#", ""), positionForMapIcon);
            }

            BufferedImage iconForLegend = ImageEncoderFactory.renderPosition(clazz.getColor(), SCALE_0_3);
            legendEntry.setIcon(iconForLegend);

            response.getMap().getVmspos().getColors().add(clazz.getColor().replace("#", EMPTY));
            temp.add(legendEntry);
        }

        String guid = UUID.randomUUID().toString();
        response.getLegend().withPositions(guid);
        LegendResource.legendEntries.put(guid, ImageEncoderFactory.renderLegend(temp, icons.getPositions().getTitle(), 25));
    }

    private void handleSegments(Icons icons, ImageResponse response) throws Exception {

        String lineStyle = icons.getSegments().getLineStyle();
        List<ImageEncoderFactory.LegendEntry> temp = new ArrayList<>();

        for (Class clazz : icons.getSegments().getClasses()) {

            ImageEncoderFactory.LegendEntry legendEntry = new ImageEncoderFactory.LegendEntry();
            legendEntry.setMsg(clazz.getText());

            if (SegmentResource.segmentEntries.get(clazz.getColor()) == null){
                BufferedImage segmentIconForMap = ImageEncoderFactory.renderSegment(clazz.getColor(), lineStyle);
                SegmentResource.segmentEntries.put(clazz.getColor().replace("#", EMPTY), segmentIconForMap);
            }

            BufferedImage segmentIconForLegend = ImageEncoderFactory.renderSegment(clazz.getColor(), lineStyle, SCALE_1_3);
            legendEntry.setIcon(segmentIconForLegend);
            temp.add(legendEntry);
        }

        String guid = UUID.randomUUID().toString();
        response.getLegend().withSegments(guid);
        LegendResource.legendEntries.put(guid, ImageEncoderFactory.renderLegend(temp, icons.getSegments().getTitle(), 40));
    }

}