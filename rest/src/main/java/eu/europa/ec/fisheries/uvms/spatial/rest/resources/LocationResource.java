package eu.europa.ec.fisheries.uvms.spatial.rest.resources;

import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.error.ErrorHandler;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ValidationUtils;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaByLocationService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaTypeNamesService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.ClosestAreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.ClosestLocationService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.ClosestAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.ClosestLocationDto;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static eu.europa.ec.fisheries.uvms.spatial.rest.util.ValidationUtils.validateInputParameters;

/**
 * Created by Michal Kopyczok on 03-Sep-15.
 */
@Path("/")
@Slf4j
public class LocationResource {
    @EJB
    private ClosestLocationService closestLocationService;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/closestlocations")
    public ResponseDto closestArea(
            @QueryParam(value = "lat") Double lat,
            @QueryParam(value = "lon") Double lon,
            @DefaultValue("4326") @QueryParam(value = "crs") int crs,
            @DefaultValue("Meter") @QueryParam(value = "unit") String unit,
            @QueryParam(value = "type") List<String> locationTypes) {
        try {
            log.info("Getting closest locations");
            validateInputParameters(lat, lon, locationTypes);
            List<ClosestLocationDto> closestLocations = closestLocationService.getClosestLocationsRest(lat, lon, crs, unit, locationTypes);
            return new ResponseDto(closestLocations, ResponseCode.OK);
        } catch (Exception ex) {
            log.error("[ Error when getting closest locations. ] ", ex);
            return ErrorHandler.getFault(ex);
        }
    }

    public void validateInputParameters(Double lat, Double lon, List<String> locationTypes) {
        ValidationUtils.validateCoordinates(lat, lon);
        ValidationUtils.validateLocationTypes(locationTypes);
    }
}
