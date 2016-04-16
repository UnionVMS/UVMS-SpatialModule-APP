package eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.spatial.model.constants.USMSpatial;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ExceptionInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.geojson.PortAreaGeoJsonDto;
import lombok.extern.slf4j.Slf4j;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/ports")
@Slf4j
@Stateless
public class AreaPortResource extends UnionVMSResource {

    @EJB
    private AreaService areaService;

    @PUT
    @Path("/portarea")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response updatePortArea(@Context HttpServletRequest request,
                                   PortAreaGeoJsonDto portAreaGeoJsonDto,
                                   @HeaderParam(USMSpatial.SCOPE_NAME) String scopeName) throws ServiceException {
        String userName = request.getRemoteUser();
        log.info("{} is requesting updatePortArea(...)", userName);
        long gid = areaService.updatePortArea(portAreaGeoJsonDto);
        return createSuccessResponse(gid);
    }

    @DELETE
    @Path("/portarea/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response deleteUserArea(@Context HttpServletRequest request,
                                   @PathParam("id") Long portAreaId,
                                   @HeaderParam(USMSpatial.SCOPE_NAME) String scopeName) throws ServiceException {
        String userName = request.getRemoteUser();
        log.info("{} is requesting deletePortArea(...), with a ID={}", userName, portAreaId);
        areaService.deletePortArea(portAreaId);
        return createSuccessResponse();
    }

}
