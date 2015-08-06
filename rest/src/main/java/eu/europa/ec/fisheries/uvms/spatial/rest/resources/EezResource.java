package eu.europa.ec.fisheries.uvms.spatial.rest.resources;

import eu.europa.ec.fisheries.schema.spatial.source.GetEezSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * //TODO create test
 */
@Path("/eez")
@Stateless
public class EezResource {

    final static Logger LOG = LoggerFactory.getLogger(EezResource.class);

    @EJB
    private SpatialService spatialService;

    @POST
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("{id}")
    public ResponseDto getExclusiveEconomicZoneById(@PathParam("id") long eezId) {
        try {
            LOG.info("Getting eez with {}", eezId);
            GetEezSpatialRS eez = spatialService.getExclusiveEconomicZoneById(eezId);
            return new ResponseDto(eez, ResponseCode.OK);
        } catch (Exception ex) {
            if (LOG.isDebugEnabled()) {
                LOG.error("[ Error when getting eez with id " + eezId + ". ] ", ex);
            }
            return new ResponseDto(ex.getMessage(), ResponseCode.ERROR);
        }
    }
}
