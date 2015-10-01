package eu.europa.ec.fisheries.uvms.spatial.rest.resources;

import java.util.List;

import javax.ejb.EJB;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.europa.ec.fisheries.uvms.rest.FeatureToGeoJsonMapper;
import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.service.interceptor.ValidationInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.AreaDetailsDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.AreaFilterDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.AreaTypeDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.error.ErrorHandler;
import eu.europa.ec.fisheries.uvms.spatial.rest.mapper.AreaLocationDtoMapper;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ExceptionInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ValidationUtils;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaByLocationService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaDetailsService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaTypeNamesService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.ClosestAreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.SearchAreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.ClosestAreaDto;
import lombok.extern.slf4j.Slf4j;

@Path("/")
@Slf4j
public class AreaResource extends UnionVMSResource {

    @EJB
    private AreaTypeNamesService areaTypeService;

    @EJB
    private AreaByLocationService areaByLocationService;

    @EJB
    private ClosestAreaService closestAreaService;
    
    @EJB
    private AreaDetailsService areaDetailsService;
    
	@EJB
	private SearchAreaService searchAreaService;
    
    private AreaLocationDtoMapper mapper = AreaLocationDtoMapper.INSTANCE;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/areatypes")
    public ResponseDto getAreaTypes() {
        try {
            log.info("Getting user areas list");
            List<String> areaTypes = areaTypeService.listAllAreaTypeNames();
            return new ResponseDto(areaTypes, ResponseCode.OK);
        } catch (Exception ex) {
            log.error("[ Error when getting area types list. ] ", ex);
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
            log.info("Getting spatial enrichment by location");
            ValidationUtils.validateInputParameters(lat, lon);
            List<AreaDto> areasByLocation = areaByLocationService.getAreaTypesByLocation(lat, lon, crs);
            return new ResponseDto(areasByLocation, ResponseCode.OK);
        } catch (Exception ex) {
            log.error("[ Error when getting areas by location. ] ", ex);
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
            @DefaultValue("Meters") @QueryParam(value = "unit") String unit,
            @QueryParam(value = "type") List<String> areaTypes) {
        try {
            log.info("Getting closest areas");
            validateInputParameters(lat, lon, areaTypes);
            List<ClosestAreaDto> closestAreas = closestAreaService.getClosestAreas(lat, lon, crs, unit, areaTypes);
            return new ResponseDto(closestAreas, ResponseCode.OK);
        } catch (Exception ex) {
            log.error("[ Error when getting closest areas. ] ", ex);
            return ErrorHandler.getFault(ex);
        }
    }
    
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/areadetails")
    @Interceptors(value = {ValidationInterceptor.class, ExceptionInterceptor.class})
    public Response getAreaDetails(AreaTypeDto areaDto) throws Exception {
    	AreaDetails areaDetails = areaDetailsService.getAreaDetails(mapper.getAreaTypeEntry(areaDto));
    	AreaDetailsDto areaDetailsDto = mapper.getAreaDetailsDto(areaDetails);
    	if (!areaDto.getIsGeom()) {
    		areaDetailsDto.removeGeometry();
        	return createSuccessResponse(areaDetailsDto.getProperties());
    	}    	
    	String geojson = new FeatureToGeoJsonMapper().convert(areaDetailsDto.toFeature());
    	return createSuccessResponse(new ObjectMapper().readTree(geojson));
    }
   
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/arealayers")
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response getSystemAreaLayerMapping() {
    	return createSuccessResponse(areaTypeService.listSystemAreaLayerMapping());
    }
    
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/areasbyfilter")
    @Interceptors(value = {ValidationInterceptor.class, ExceptionInterceptor.class})
    public Response getAreasByFilter(AreaFilterDto areaFilterDto) {
    	return createSuccessResponse(searchAreaService.getAreasByFilter(areaFilterDto.getAreaType(), areaFilterDto.getFilter()));
    }

    public void validateInputParameters(Double lat, Double lon, List<String> areaTypes) {
        ValidationUtils.validateCoordinates(lat, lon);
        ValidationUtils.validateAreaTypes(areaTypes);
    }

}
