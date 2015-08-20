package eu.europa.ec.fisheries.uvms.spatial.rest.resources;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ResponseMessageType;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.EezService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/eez")
public class EezResource extends AbstractResource {

    final static Logger LOG = LoggerFactory.getLogger(EezResource.class);

    @EJB
    private EezService eezService;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/{id}")
    @SuppressWarnings("unchecked")
    public ResponseDto getExclusiveEconomicZoneById(@PathParam("id") int eezId) {
        try {
            LOG.info("Getting getEezRS with {}", eezId);

            EezSpatialRS getEezRS = eezService.getEezById(createRequest(eezId));

            ResponseMessageType responseMessage = getEezRS.getResponseMessage();
            if (isSuccess(responseMessage)) {
                return new ResponseDto(getEezRS.getEez(), ResponseCode.OK);
            } else {
                return createErrorResponse(responseMessage);
            }
        } catch (Exception ex) {
            if (LOG.isDebugEnabled()) {
                LOG.error("[ Error when getting eez with id " + eezId + ". ] ", ex);
            }
            return new ResponseDto(ex.getMessage(), ResponseCode.ERROR);
        }
    }

    private EezSpatialRQ createRequest(int eezId) {
        return new EezSpatialRQ(String.valueOf(eezId));
    }
}
