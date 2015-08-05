package eu.europa.ec.fisheries.uvms.spatial.rest.resources;

import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.spatial.service.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * //TODO create test
 */
@Path("areatypes")
@Stateless
public class AreaTypeResource {

    final static Logger LOG = LoggerFactory.getLogger(AreaTypeResource.class);

    @EJB
    private AreaService areaService;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public ResponseDto getAreaTypes() {
        try {
            LOG.info("Getting user areas list");
            return new ResponseDto(areaService.getAreaTypes(), ResponseCode.OK);
        } catch (Exception e) {
            LOG.error("[ Error when getting area types list. ] ", e);
            throw new RuntimeException("Please fix it");
        }
    }
}
