package eu.europa.ec.fisheries.uvms.spatial.rest.resources;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.GetAreaTypesSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/")
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

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/areasbylocation")
    public ResponseDto areasByLocation(
            @QueryParam(value = "lat") double lat,
            @QueryParam(value = "lon") double lon,
            @DefaultValue("4326") @QueryParam(value = "crs") int crs) {
        try {
            LOG.info("Getting areas by location");
            return new ResponseDto(areaService.getAreasByLocation(lat, lon, crs), ResponseCode.OK);
        } catch (Exception ex) {
            LOG.error("[ Error when getting areas by location. ] ", ex);
            return new ResponseDto(ex.getMessage(), ResponseCode.ERROR);
        }
    }
}
