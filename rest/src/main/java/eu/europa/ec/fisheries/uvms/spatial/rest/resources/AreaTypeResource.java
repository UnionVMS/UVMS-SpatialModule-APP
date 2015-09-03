package eu.europa.ec.fisheries.uvms.spatial.rest.resources;

import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.error.ErrorHandler;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaByLocationService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaTypeNamesService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.ClosestAreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.ClosestAreaDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Path("/")
public class AreaTypeResource {

    final static Logger LOG = LoggerFactory.getLogger(AreaTypeResource.class);

    @EJB
    private AreaTypeNamesService areaTypeService;

    @EJB
    private AreaByLocationService areaByLocationService;

    @EJB
    private ClosestAreaService closestAreaService;

    public AreaTypeResource() {
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/areatypes")
    public ResponseDto getAreaTypes() {
        try {
            LOG.info("Getting user areas list");
            List<String> areaTypes = areaTypeService.getAreaTypesRest();
            return new ResponseDto(areaTypes, ResponseCode.OK);
        } catch (Exception ex) {
            LOG.error("[ Error when getting area types list. ] ", ex);
            return ErrorHandler.getFault(ex);
        }
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/areasbylocation")
    public ResponseDto getAreasByLocation(
            @QueryParam(value = "lat") double lat,
            @QueryParam(value = "lon") double lon,
            @DefaultValue("4326") @QueryParam(value = "crs") int crs) {
        try {
            LOG.info("Getting areas by location");
            List<AreaDto> areasByLocation = areaByLocationService.getAreasByLocationRest(lat, lon, crs);
            return new ResponseDto(areasByLocation, ResponseCode.OK);
        } catch (Exception ex) {
            LOG.error("[ Error when getting areas by location. ] ", ex);
            return ErrorHandler.getFault(ex);
        }
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/closestareas")
    public ResponseDto closestArea(
            @QueryParam(value = "lat") double lat,
            @QueryParam(value = "lon") double lon,
            @DefaultValue("4326") @QueryParam(value = "crs") int crs,
            @QueryParam(value = "unit") String unit,
            @QueryParam(value = "areaTypes") String areaTypes) {
        try {
            LOG.info("Getting closest areas");
            ArrayList<String> types = newArrayList(areaTypes);
            List<ClosestAreaDto> closestAreas = closestAreaService.getClosestAreasRest(lat, lon, crs, unit, types);
            return new ResponseDto(closestAreas, ResponseCode.OK);
        } catch (Exception ex) {
            LOG.error("[ Error when getting closest areas. ] ", ex);
            return ErrorHandler.getFault(ex);
        }
    }
}
