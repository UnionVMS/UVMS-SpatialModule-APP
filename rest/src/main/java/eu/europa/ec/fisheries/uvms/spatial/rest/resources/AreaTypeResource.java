package eu.europa.ec.fisheries.uvms.spatial.rest.resources;

import eu.europa.ec.fisheries.schema.spatial.source.GetAreaTypesSpatialRS;
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

@Path("/")
@Stateless
public class AreaTypeResource {

    final static Logger LOG = LoggerFactory.getLogger(AreaTypeResource.class);

    @EJB
    private AreaService areaService;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/areatypes")
    public ResponseDto getAreaTypes() {
        try {
            LOG.info("Getting user areas list");
            GetAreaTypesSpatialRS areaTypes = areaService.getAreaTypes();
            return new ResponseDto(areaTypes, ResponseCode.OK);
        } catch (Exception ex) {
            LOG.error("[ Error when getting area types list. ] ", ex);
            return new ResponseDto(ex.getMessage(), ResponseCode.ERROR);
        }
    }
}
