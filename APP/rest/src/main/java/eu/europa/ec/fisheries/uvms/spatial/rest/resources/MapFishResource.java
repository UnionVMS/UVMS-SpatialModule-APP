package eu.europa.ec.fisheries.uvms.spatial.rest.resources;

import eu.europa.ec.fisheries.uvms.constants.AuthConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.mapfish.Color;
import eu.europa.ec.fisheries.uvms.spatial.service.mapfish.MapFishService;
import lombok.extern.slf4j.Slf4j;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

@Path("/mapfish")
@Slf4j
public class MapFishResource {


    @EJB
    private MapFishService mapFishService;

    @POST
    public void test(@Context HttpServletRequest request,
                     @HeaderParam(AuthConstants.HTTP_HEADER_SCOPE_NAME) String scopeName,
                     @HeaderParam(AuthConstants.HTTP_HEADER_ROLE_NAME) String roleName,
                     Color color) throws Exception {


        mapFishService.saveVesselIconsWithColor( color.getVessel());

        System.out.println("");

    }
}
