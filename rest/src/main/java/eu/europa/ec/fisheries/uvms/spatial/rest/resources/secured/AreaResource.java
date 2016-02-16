package eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.vividsolutions.jts.io.ParseException;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.service.interceptor.ValidationInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaDetailsDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.AreaFilterDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.AreaTypeDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.error.ErrorHandler;
import eu.europa.ec.fisheries.uvms.spatial.rest.mapper.AreaLocationDtoMapper;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ExceptionInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ValidationUtils;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.*;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaExtendedIdentifierDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.ClosestAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaGroup.AreaGroupDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaGroup.AreaGroupTypeDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.AreaServiceLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.LayerTypeEnum;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.ServiceLayerDto;
import lombok.extern.slf4j.Slf4j;

@Path("/")
@Slf4j
@Stateless
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

    @EJB
    private UserAreaService userAreaService;
    
    private AreaLocationDtoMapper mapper = AreaLocationDtoMapper.mapper();

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
            List<AreaExtendedIdentifierDto> areasByLocation = areaByLocationService.getAreaTypesByLocation(lat, lon, crs);
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
    public Response getAreaDetails(AreaTypeDto areaDto) throws IOException, ParseException {
    	if (areaDto.getId() != null) {
    		return getAreaDetailsById(areaDto);
    	} else {
    		return getAreaDetailsByLocation(areaDto);
    	}
    }
    
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/areaproperties")
    @Interceptors(value = {ValidationInterceptor.class, ExceptionInterceptor.class})
    public Response getAreaProperties(List<AreaTypeDto> areaDtoList) {
    	return createSuccessResponse(searchAreaService.getSelectedAreaColumns(mapper.getAreaTypeEntryList(areaDtoList)));
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

    @GET
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/servicelayers/{layerType}")
    public Response getServiceLayersByType(@PathParam("layerType") String layerType, @Context HttpServletRequest request) throws ServiceException {
        LayerTypeEnum layerTypeEnum = LayerTypeEnum.value(layerType);
        if (layerTypeEnum.equals(LayerTypeEnum.USERAREA)) {
            List<AreaServiceLayerDto> areaServiceLayerDtos = areaTypeService.getAllAreasLayerDescription(layerTypeEnum, request.getRemoteUser());
            return createSuccessResponse(areaServiceLayerDtos);
        } else {
            return createSuccessResponse(areaTypeService.getAreaLayerDescription(layerTypeEnum));
        }
    }

    public void validateInputParameters(Double lat, Double lon, List<String> areaTypes) {
        ValidationUtils.validateCoordinates(lat, lon);
        ValidationUtils.validateAreaTypes(areaTypes);
    }
    
    private Response getAreaDetailsById(AreaTypeDto areaDto) throws IOException, ParseException {
    	AreaDetails areaDetails = areaDetailsService.getAreaDetailsById(mapper.getAreaTypeEntry(areaDto));
    	AreaDetailsDto areaDetailsDto = mapper.getAreaDetailsDto(areaDetails);
    	if (!areaDto.getIsGeom()) {
    		areaDetailsDto.removeGeometry();
        	return createSuccessResponse(areaDetailsDto.getProperties());
    	}  
    	return createSuccessResponse(areaDetailsDto.convert());
    }
    
    private Response getAreaDetailsByLocation(AreaTypeDto areaDto) throws IOException, ParseException {
    	List<AreaDetails> areaDetailsList = areaDetailsService.getAreaDetailsByLocation(mapper.getAreaTypeEntry(areaDto));
		AreaDetailsDto areaDetailsDto = mapper.getAreaDetailsDtoForAllAreas(areaDetailsList, areaDto);    		
		if (!areaDto.getIsGeom()) {
    		areaDetailsDto.removeGeometryAllAreas();
        	return createSuccessResponse(areaDetailsDto.getAllAreaProperties());
    	}
		return createSuccessResponse(areaDetailsDto.convertAll());
    }
}