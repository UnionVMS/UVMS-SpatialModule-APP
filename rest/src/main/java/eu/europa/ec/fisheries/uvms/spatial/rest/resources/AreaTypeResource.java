package eu.europa.ec.fisheries.uvms.spatial.rest.resources;

import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.error.ErrorHandler;
import eu.europa.ec.fisheries.uvms.spatial.service.rest.AreaByLocationRestService;
import eu.europa.ec.fisheries.uvms.spatial.service.rest.AreaTypeRestService;
import eu.europa.ec.fisheries.uvms.spatial.service.rest.dto.AreaDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/")
public class AreaTypeResource {

    final static Logger LOG = LoggerFactory.getLogger(AreaTypeResource.class);

    @EJB
    private AreaTypeRestService areaTypeService;

    @EJB
    private AreaByLocationRestService areaByLocationService;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/areatypes")
    public ResponseDto getAreaTypes() {
        try {
            LOG.info("Getting user areas list");
            List<String> areaTypes = areaTypeService.getAreaTypes();
            return new ResponseDto(areaTypes, ResponseCode.OK);
        } catch (Exception ex) {
            LOG.error("[ Error when getting area types list. ] ", ex);
            return ErrorHandler.getFault(ex);
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
            List<AreaDto> areasByLocation = areaByLocationService.getAreasByLocation(lat, lon, crs);
            return new ResponseDto(areasByLocation, ResponseCode.OK);
        } catch (Exception ex) {
            LOG.error("[ Error when getting areas by location. ] ", ex);
            return ErrorHandler.getFault(ex);
        }
    }
}
