package eu.europa.ec.fisheries.uvms.spatial.rest.resources;

import javax.ejb.EJB;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ExceptionInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.UserAreaService;
import lombok.extern.slf4j.Slf4j;

@Path("/")
@Slf4j
public class UserAreaResource extends UnionVMSResource {
	
	@EJB
	private UserAreaService userAreaService;
	
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/userarealayers")
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response getSystemAreaLayerMapping(@Context HttpServletRequest request, @HeaderParam("scopeName") String scopeName) {
    	log.debug("UserName from security : " + request.getRemoteUser());
    	return createSuccessResponse(userAreaService.getUserAreaLayerDefination(request.getRemoteUser(), scopeName));
    }
    
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/userareadetails")
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response getUserAreaDetails(@Context HttpServletRequest request, @HeaderParam("scopeName") String scopeName) {
    	return createSuccessResponse(userAreaService.getUserAreaDetails(request.getRemoteUser(), scopeName));
    }
}
