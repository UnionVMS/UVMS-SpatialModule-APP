package eu.europa.ec.fisheries.uvms.spatial.rest.resources.unsecured;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Path("/alarm")
@SuppressWarnings("unchecked")
@Slf4j
public class AlarmResource {

    public static Map<String, BufferedImage> alarmEntries = Collections.synchronizedMap(new LinkedHashMap() {

        private static final int MAX_ENTRIES = 1000;

        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {

            return size() > MAX_ENTRIES;
        }
    });

    @GET
    @Path("/{key}")
    public void getPositionEntry(@PathParam("key") String key,
                                 @Context HttpServletResponse response) throws IOException {
        BufferedImage bi = alarmEntries.get(key);
        if (bi != null) {
            OutputStream out = response.getOutputStream();
            ImageIO.write(bi, "png", out);
            out.close();
        }
    }

}
