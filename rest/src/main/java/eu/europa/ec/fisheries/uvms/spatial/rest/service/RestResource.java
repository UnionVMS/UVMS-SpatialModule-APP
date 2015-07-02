package eu.europa.ec.fisheries.uvms.spatial.rest.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.schema.spatial.search.v1.MovementListQuery;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.spatial.service.CombinedMovementVesselService;
import eu.europa.ec.fisheries.uvms.spatial.service.MovementService;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.MovementListResponseDto;
import eu.europa.ec.fisheries.uvms.spatial.service.exception.MovementServiceException;

/**
 *
 * @author jojoha
 */
@Path("/spatial")
@Stateless
public class RestResource {

    final static Logger LOG = LoggerFactory.getLogger(RestResource.class);

    @EJB
    MovementService serviceLayer;

    @EJB
    CombinedMovementVesselService combinedService;

    /**
     *
     * @responseMessage 200 Movement list successfully retreived
     * @responseMessage 500 Error when retrieveing the list values for
     * transponders
     *
     * @summary Gets a list of spatials filtered by a query
     *
     */
    @POST
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/list")
    public ResponseDto<MovementListResponseDto> getListByQuery(MovementListQuery query) {
        LOG.info("Get list invoked in rest layer");
        try {
            return new ResponseDto(combinedService.getList(query), ResponseCode.OK);
        } catch (MovementServiceException | NullPointerException ex) {
            LOG.error("[ Error when geting list. ]", ex);
            return new ResponseDto(ex.getMessage(), ResponseCode.ERROR);
        }
    }

    @GET
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/{id}")
    public ResponseDto getById(@PathParam(value = "id") final Long id) {
        LOG.info("Get by id invoked in rest layer");
        try {
            return new ResponseDto(serviceLayer.getById(id), ResponseCode.OK);
        } catch (MovementServiceException | NullPointerException ex) {
            LOG.error("[ Error when geting by id. ] ", ex);
            return new ResponseDto(ex.getMessage(), ResponseCode.ERROR);
        }
    }

    @PUT
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public ResponseDto update(final Object data) {
        LOG.info("Update invoked in rest layer");
        try {
            return new ResponseDto(serviceLayer.update(data), ResponseCode.OK);
        } catch (MovementServiceException | NullPointerException ex) {
            LOG.error("[ Error when updating. ] {} ", ex.getStackTrace());
            return new ResponseDto(ResponseCode.ERROR);
        }
    }

}
