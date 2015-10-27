package eu.europa.ec.fisheries.uvms.spatial.rest.resources;

import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ExceptionInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.MapConfigService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.MapConfig;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.interceptor.Interceptors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/config")
@Slf4j
public class MapConfigResource extends UnionVMSResource {

    @EJB
    private MapConfigService mapConfigService;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("{id}")
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response getExclusiveEconomicZoneById(@PathParam("id") int id) {
        log.info("Getting map configuration for report with id = {}", id);
        MapConfig mapConfig = mapConfigService.getMockReportConfig(id);
        return createSuccessResponse(mapConfig);
    }

}
