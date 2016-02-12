package eu.europa.ec.fisheries.uvms.spatial.rest.resources;

import eu.europa.ec.fisheries.uvms.constants.AuthConstants;
import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.spatial.model.mapfish.request.Class;
import eu.europa.ec.fisheries.uvms.spatial.model.mapfish.request.Icons;
import eu.europa.ec.fisheries.uvms.spatial.model.mapfish.response.ImageResponse;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ImageFactory;
import lombok.extern.slf4j.Slf4j;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Path("/icon")
@Slf4j
@SuppressWarnings("unchecked")
public class IconResource extends UnionVMSResource {

    public static Map<String, BufferedImage> positionEntries = Collections.synchronizedMap(new LinkedHashMap() {
        private static final int MAX_ENTRIES = 1000;
        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size() > MAX_ENTRIES;
        }
    });

    public static Map<String, BufferedImage> lineEntries = Collections.synchronizedMap(new LinkedHashMap() {
        private static final int MAX_ENTRIES = 1000;
        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size() > MAX_ENTRIES;
        }
    });

    @Path("/pos/{hex}")
    public void getPositionEntry(@PathParam("hex") String key,
                            @Context HttpServletResponse response) throws IOException {
        BufferedImage bi = positionEntries.get(key);
        OutputStream out = response.getOutputStream();
        ImageIO.write(bi, "png", out);
        out.close();
    }

    @Path("/line/{hex}")
    public void getLineEntry(@PathParam("hex") String key,
                                 @Context HttpServletResponse response) throws IOException {
        BufferedImage bi = lineEntries.get(key);
        OutputStream out = response.getOutputStream();
        ImageIO.write(bi, "png", out);
        out.close();
    }

    @Path("/positions")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<String> listPositionEntryKeys() throws IOException {
        return positionEntries.keySet();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response renderImages(@Context HttpServletRequest request,
                     @HeaderParam(AuthConstants.HTTP_HEADER_SCOPE_NAME) String scopeName,
                     @HeaderParam(AuthConstants.HTTP_HEADER_ROLE_NAME) String roleName,
                     Icons icons) throws Exception {

        ImageResponse response = new ImageResponse();
        response.getLegend().withBase("/spatial/rest/legend/");
        handlePositionIcons(icons, response);
        handleSegmentIcons(icons, response);

        return createSuccessResponse(response);

    }

    private void handleSegmentIcons(Icons icons, ImageResponse response) throws Exception {

        String lineStyle = icons.getSegments().getLineStyle();
        List<ImageFactory.LegendEntry> legendEntries = new ArrayList<>();

        for (Class clazz : icons.getSegments().getClasses()) {

            ImageFactory.LegendEntry legendEntry = new ImageFactory.LegendEntry();
            legendEntry.setMsg(clazz.getText());

            if (lineEntries.get(clazz.getColor()) == null){
                BufferedImage icon = ImageFactory.renderLine(clazz.getColor(), lineStyle);
                lineEntries.put(clazz.getColor(), icon);
                legendEntry.setIcon(icon);
            }
            else {
                legendEntry.setIcon(lineEntries.get(clazz.getColor()));
            }

            legendEntries.add(legendEntry);
        }

        String guid = UUID.randomUUID().toString();
        response.getLegend().withSegments(guid);
        LegendResource.legendEntries.put(guid, ImageFactory.renderLegend(legendEntries, icons.getSegments().getTitle()));
    }

    private void handlePositionIcons(Icons icons, ImageResponse response) throws Exception {

        response.getMap().getVmspos().withBase("/spatial/rest/icon/pos/");
        List<ImageFactory.LegendEntry> legendEntries = new ArrayList<>();

        for (Class clazz : icons.getPositions().getClasses()) { // TODO check hex value

            ImageFactory.LegendEntry legendEntry = new ImageFactory.LegendEntry();
            legendEntry.setMsg(clazz.getText());

            if (positionEntries.get(clazz.getColor()) == null){
                BufferedImage icon = ImageFactory.renderPosition(clazz.getColor());
                positionEntries.put(clazz.getColor(), icon);
                legendEntry.setIcon(icon);
            }
            else {
                legendEntry.setIcon(positionEntries.get(clazz.getColor()));
            }

            response.getMap().getVmspos().getColors().add(clazz.getColor());
            legendEntries.add(legendEntry);
        }

        String guid = UUID.randomUUID().toString();
        response.getLegend().withPositions(guid);
        LegendResource.legendEntries.put(guid, ImageFactory.renderLegend(legendEntries, icons.getPositions().getTitle()));
    }
}
