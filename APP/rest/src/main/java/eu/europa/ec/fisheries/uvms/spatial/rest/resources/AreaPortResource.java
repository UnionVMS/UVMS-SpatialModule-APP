package eu.europa.ec.fisheries.uvms.spatial.rest.resources;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ExceptionInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.PortAreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.PortAreaDto;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/ports")
@Slf4j
@Stateless
public class AreaPortResource extends UnionVMSResource {

    @EJB
    private PortAreaService portAreaService;

    @PUT
    @Path("/portarea")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response updatePortArea(@Context HttpServletRequest request,
                                   PortAreaDto portAreaDto,
                                   @HeaderParam("scopeName") String scopeName) throws ServiceException {
        String userName = request.getRemoteUser();
        log.info("{} is requesting updatePortArea(...)", userName);
        long gid = portAreaService.updatePortArea(portAreaDto);
        return createSuccessResponse(gid);
    }

    @DELETE
    @Path("/portarea/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response deleteUserArea(@Context HttpServletRequest request,
                                   @PathParam("id") Long portAreaId,
                                   @HeaderParam("scopeName") String scopeName) throws ServiceException {
        String userName = request.getRemoteUser();
        log.info("{} is requesting deletePortArea(...), with a ID={}", userName, portAreaId);
        portAreaService.deletePortArea(portAreaId);
        return createSuccessResponse();
    }

}
