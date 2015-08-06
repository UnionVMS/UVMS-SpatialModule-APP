package eu.europa.ec.fisheries.uvms.spatial.rest.resources;

import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by kopyczmi on 06-Aug-15.
 */
@Path("/")
@Stateless
public class SpatialResource {
    final static Logger LOG = LoggerFactory.getLogger(EezResource.class);

    @EJB
    private SpatialService spatialService;

    @POST
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/areasByLocation")
    public ResponseDto getExclusiveEconomicZoneById(
            @PathParam(value = "lat") double lat,
            @PathParam(value = "lon") double lon,
            @DefaultValue("4326") @PathParam(value = "crs") int crs) {
        try {
            LOG.info("Getting areas by location");
            return new ResponseDto(spatialService.getAreasByLocation(lat, lon, crs), ResponseCode.OK);
        } catch (Exception ex) {
            LOG.error("[ Error when getting areas by location. ] ", ex);
            return new ResponseDto(ex.getMessage(), ResponseCode.ERROR);
        }
    }
}
