package eu.europa.ec.fisheries.uvms.spatial.rest.resources;

import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.error.ErrorHandler;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaByLocationService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaTypeNamesService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.ClosestAreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaIdentifierDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.ClosestAreaDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static eu.europa.ec.fisheries.uvms.spatial.rest.util.ValidationUtils.validateInputParameters;

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
            List<String> areaTypes = areaTypeService.listAllAreaTypeNames();
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
            @QueryParam(value = "lat") Double lat,
            @QueryParam(value = "lon") Double lon,
            @DefaultValue("4326") @QueryParam(value = "crs") int crs) {
        try {
            LOG.info("Getting areas by location");
            validateInputParameters(lat, lon);
            List<AreaIdentifierDto> areasByLocation = areaByLocationService.getAreaTypesByLocation(lat, lon, crs);
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
            @QueryParam(value = "lat") Double lat,
            @QueryParam(value = "lon") Double lon,
            @DefaultValue("4326") @QueryParam(value = "crs") int crs,
            @DefaultValue("Meter") @QueryParam(value = "unit") String unit,
            @QueryParam(value = "type") List<String> areaTypes) {
        try {
            LOG.info("Getting closest areas");
            validateInputParameters(lat, lon);
            List<ClosestAreaDto> closestAreas = closestAreaService.getClosestAreas(lat, lon, crs, unit, areaTypes);
            return new ResponseDto(closestAreas, ResponseCode.OK);
        } catch (Exception ex) {
            LOG.error("[ Error when getting closest areas. ] ", ex);
            return ErrorHandler.getFault(ex);
        }
    }

}
