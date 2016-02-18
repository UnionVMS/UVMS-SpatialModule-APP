package eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.vividsolutions.jts.io.ParseException;
import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.service.interceptor.ValidationInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationDetails;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.geojson.LocationDetailsGeoJsonDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.geocoordinate.LocationTypeDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.error.ErrorHandler;
import eu.europa.ec.fisheries.uvms.spatial.rest.mapper.AreaLocationDtoMapper;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ExceptionInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ValidationUtils;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.ClosestLocationService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.LocationDetailsService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.ClosestLocationDto;
import lombok.extern.slf4j.Slf4j;

@Path("/")
@Slf4j
@Stateless
public class LocationResource extends UnionVMSResource {
    @EJB
    private ClosestLocationService closestLocationService;
    
    @EJB
    private LocationDetailsService locationDetailsService;
    
    private AreaLocationDtoMapper mapper = AreaLocationDtoMapper.mapper();

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/closestlocations")
    public ResponseDto closestLocation(
            @QueryParam(value = "lat") Double lat,
            @QueryParam(value = "lon") Double lon,
            @DefaultValue("4326") @QueryParam(value = "crs") int crs,
            @DefaultValue("meters") @QueryParam(value = "unit") String unit,
            @QueryParam(value = "type") List<String> locationTypes) {
        try {
            log.info("Getting closest locations");
            validateInputParameters(lat, lon, locationTypes);
            List<ClosestLocationDto> closestLocations = closestLocationService.getClosestLocations(lat, lon, crs, unit, locationTypes);
            return new ResponseDto(closestLocations, ResponseCode.OK);
        } catch (Exception ex) {
            log.error("[ Error when getting closest locations. ] ", ex);
            return ErrorHandler.getFault(ex);
        }
    }
    
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/locationdetails")
    @Interceptors(value = {ValidationInterceptor.class, ExceptionInterceptor.class})
    public Response getLocationDetails(LocationTypeDto locationDto) throws IOException, ParseException {
    	LocationDetails locationDetails = locationDetailsService.getLocationDetails(mapper.getLocationTypeEntry(locationDto));
    	LocationDetailsGeoJsonDto locationDetailsGeoJsonDto = mapper.getLocationDetailsDto(locationDetails);
    	return createSuccessResponse(locationDetailsGeoJsonDto.convert());
    }

    private void validateInputParameters(Double lat, Double lon, List<String> locationTypes) {
        ValidationUtils.validateCoordinates(lat, lon);
        ValidationUtils.validateLocationTypes(locationTypes);
    }
}
