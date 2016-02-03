package eu.europa.ec.fisheries.uvms.spatial.rest.resources;

import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaGroupService;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by padhyad on 1/25/2016.
 */
@Path("/")
@Slf4j
@Stateless
public class AreaGroupResource extends UnionVMSResource {

    @EJB
    private AreaGroupService areaGroupService;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/areagroup/list")
    public Response getAreaGroups(@Context HttpServletRequest request) {
        return createSuccessResponse(areaGroupService.getAreaGroups(request.getRemoteUser()));
    }

    @DELETE
    @Path("/areagroup/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAreaGroup(@PathParam("id") Long groupId) {
        areaGroupService.deleteAreaGroup(groupId);
        return createSuccessResponse();
    }
}
