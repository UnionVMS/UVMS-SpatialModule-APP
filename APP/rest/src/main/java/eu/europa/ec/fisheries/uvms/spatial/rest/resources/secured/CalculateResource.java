package eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured;

import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.spatial.service.CalculateService;
import lombok.extern.slf4j.Slf4j;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("/calculate")
@Slf4j
public class CalculateResource extends UnionVMSResource {

    @EJB
    private CalculateService service;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/buffer")
    public Response buffer(Map<String, Object> payload){

        Response response;

        try {
            Double latitude = Double.valueOf(String.valueOf(payload.get("latitude")));
            Double longitude = Double.valueOf(String.valueOf(payload.get("longitude")));
            Double buffer = Double.valueOf(String.valueOf(payload.get("buffer")));
            response = createSuccessResponse(service.calculateBuffer(latitude, longitude, buffer));
        }

        catch (Exception ex){
            String error = "[ Error when calculating buffer. ] ";
            log.error(error, ex);
            response = createErrorResponse(error);
        }

        return response;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/translate")
    public Response translate(Map<String, Object> payload){

        Response response;

        try {
            Double latitude = Double.valueOf(String.valueOf(payload.get("x")));
            Double longitude = Double.valueOf(String.valueOf(payload.get("y")));
            Double buffer = Double.valueOf(String.valueOf(payload.get("wkt")));
            response = createSuccessResponse(service.calculateBuffer(latitude, longitude, buffer));
        }

        catch (Exception ex){
            String error = "[ Error when calculating buffer. ] ";
            log.error(error, ex);
            response = createErrorResponse(error);
        }

        return response;
    }
}


