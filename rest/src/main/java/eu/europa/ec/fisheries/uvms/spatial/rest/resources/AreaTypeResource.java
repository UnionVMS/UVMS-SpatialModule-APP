package eu.europa.ec.fisheries.uvms.spatial.rest.resources;

import eu.europa.ec.fisheries.schema.spatial.source.GetAreaTypesSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
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

    @GET
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/areasByLocation")
    public ResponseDto areasByLocation(
            @PathParam(value = "lat") double lat,
            @PathParam(value = "lon") double lon,
            @DefaultValue("4326") @PathParam(value = "crs") int crs) {
        try {
            LOG.info("Getting areas by location");
            return new ResponseDto(areaService.getAreasByLocation(lat, lon, crs), ResponseCode.OK);
        } catch (Exception ex) {
            LOG.error("[ Error when getting areas by location. ] ", ex);
            return new ResponseDto(ex.getMessage(), ResponseCode.ERROR);
        }
    }
}
