package eu.europa.ec.fisheries.uvms.spatial.rest.resources;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.MapConfigurationType;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.MapConfigService;
import lombok.extern.slf4j.Slf4j;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/report")
@Slf4j
public class ReportResource extends UnionVMSResource {

    @EJB
    private MapConfigService mapConfigService;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("mapconfig/{id}")
    public Response getMapConfigBy(@PathParam("id") Integer reportId) {

        log.info("Getting map settings for report with id = {}", reportId);

        Response response;

        try {

            MapConfigurationType mapConfigurationType = mapConfigService.getMapConfigurationType((long) reportId);

            response = createSuccessResponse(mapConfigurationType);

        } catch (ServiceException ex) {

            log.error("[ Error when getting map settings. ] ", ex);

            response = createErrorResponse(ex.getMessage());

        }

        return response;

    }
}
