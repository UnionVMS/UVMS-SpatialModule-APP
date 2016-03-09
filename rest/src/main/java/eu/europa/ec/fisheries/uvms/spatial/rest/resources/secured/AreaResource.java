package eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured;

import java.io.IOException;
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
import eu.europa.ec.fisheries.uvms.spatial.rest.type.geocoordinate.AreaCoordinateType;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.geojson.AreaDetailsGeoJsonDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.type.AreaFilterType;
import eu.europa.ec.fisheries.uvms.spatial.rest.type.ResponseCode;
import eu.europa.ec.fisheries.uvms.spatial.rest.type.ResponseDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.error.ErrorHandler;
import eu.europa.ec.fisheries.uvms.spatial.rest.mapper.AreaLocationDtoMapper;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ExceptionInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ValidationUtils;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.*;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.AreaExtendedIdentifierDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.ClosestAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.AreaServiceLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.LayerTypeEnum;
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
    public Response getAreaDetails(AreaCoordinateType areaDto) throws IOException, ParseException, ServiceException {

        Response response;

        if (areaDto.getId() != null) {

            AreaDetails areaDetails = areaDetailsService.getAreaDetailsById(mapper.getAreaTypeEntry(areaDto));
            AreaDetailsGeoJsonDto areaDetailsGeoJsonDto = mapper.getAreaDetailsDto(areaDetails);
            if (!areaDto.getIsGeom()) {
                areaDetailsGeoJsonDto.removeGeometry();
                return createSuccessResponse(areaDetailsGeoJsonDto.getProperties());
            }
            response = createSuccessResponse(areaDetailsGeoJsonDto.convert());

    	} else {
            List<AreaDetails> areaDetailsList = areaDetailsService.getAreaDetailsByLocation(mapper.getAreaTypeEntry(areaDto));
            AreaDetailsGeoJsonDto areaDetailsGeoJsonDto = mapper.getAreaDetailsDtoForAllAreas(areaDetailsList, areaDto);
            if (!areaDto.getIsGeom()) {
                areaDetailsGeoJsonDto.removeGeometryAllAreas();
                return createSuccessResponse(areaDetailsGeoJsonDto.getAllAreaProperties());
            }
            response = createSuccessResponse(areaDetailsGeoJsonDto.convertAll());
    	}

        return response;
    }
    
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/areaproperties")
    @Interceptors(value = {ValidationInterceptor.class, ExceptionInterceptor.class})
    public Response getAreaProperties(List<AreaCoordinateType> areaDtoList) {
    	return createSuccessResponse(searchAreaService.getSelectedAreaColumns(mapper.getAreaTypeEntryList(areaDtoList)));
    }
   
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/arealayers")
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response getSystemAreaLayerMapping() {
    	return createSuccessResponse(areaTypeService.listSystemAreaLayerMapping());
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/arealocationlayers")
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response getSystemAreaAndLocationLayerMapping() {
        return createSuccessResponse(areaTypeService.listSystemAreaAndLocationLayerMapping());
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/areasbyfilter")
    @Interceptors(value = {ValidationInterceptor.class, ExceptionInterceptor.class})
    public Response getAreasByFilter(AreaFilterType areaFilterType) {
    	return createSuccessResponse(searchAreaService.getAreasByFilter(areaFilterType.getAreaType(), areaFilterType.getFilter()));
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
}
