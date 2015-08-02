package eu.europa.ec.fisheries.uvms.spatial.rest.resources;

import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.CrudService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * //TODO create test
 */
@Path("/areatype")
@Stateless
public class AreaTypeResource {

    final static Logger LOG = LoggerFactory.getLogger(AreaTypeResource.class);

    @EJB
    private CrudService crudService;

    @POST
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("getAreaTypes")
    public ResponseDto getAreaTypes() {
        try {
            LOG.info("Getting user areas list");
            return null;//new ResponseDto(spatialService.getAreaTypes(), ResponseCode.OK);
        } catch (Exception e) {
            LOG.error("[ Error when getting vessel list. ] ", e); //TODO veesel list?
            throw new RuntimeException("Please fix it");
        }
    }
}
