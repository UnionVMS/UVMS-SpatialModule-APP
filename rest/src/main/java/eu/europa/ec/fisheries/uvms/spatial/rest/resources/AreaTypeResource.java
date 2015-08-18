package eu.europa.ec.fisheries.uvms.spatial.rest.resources;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaByLocationSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ResponseMessageType;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaByLocationService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/")
public class AreaTypeResource extends AbstractResource {

    final static Logger LOG = LoggerFactory.getLogger(AreaTypeResource.class);

    @EJB
    private AreaTypeService areaTypeService;

    @EJB
    private AreaByLocationService areaByLocationService;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/areatypes")
    public ResponseDto getAreaTypes() {
        try {
            LOG.info("Getting user areas list");
            AreaTypeSpatialRS getAreaTypesRS = areaTypeService.getAreaTypes();

            ResponseMessageType responseMessage = getAreaTypesRS.getResponseMessage();
            if (isSuccess(responseMessage)) {
                return new ResponseDto(getAreaTypesRS.getAreaTypes(), ResponseCode.OK);
            } else {
                return createErrorResponse(responseMessage);
            }
        } catch (Exception ex) {
            LOG.error("[ Error when getting area types list. ] ", ex);
            return new ResponseDto(ex.getMessage(), ResponseCode.ERROR);
        }
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/areasbylocation")
    public ResponseDto areasByLocation(
            @QueryParam(value = "lat") double lat,
            @QueryParam(value = "lon") double lon,
            @DefaultValue("4326") @QueryParam(value = "crs") int crs) {
        try {
            LOG.info("Getting areas by location");
            AreaByLocationSpatialRS areasByLocation = areaByLocationService.getAreasByLocation(lat, lon, crs);

            ResponseMessageType responseMessage = areasByLocation.getResponseMessage();
            if (isSuccess(responseMessage)) {
                return new ResponseDto(areasByLocation.getAreasType(), ResponseCode.OK);
            } else {
                return createErrorResponse(responseMessage);
            }
        } catch (Exception ex) {
            LOG.error("[ Error when getting areas by location. ] ", ex);
            return new ResponseDto(ex.getMessage(), ResponseCode.ERROR);
        }
    }
}
