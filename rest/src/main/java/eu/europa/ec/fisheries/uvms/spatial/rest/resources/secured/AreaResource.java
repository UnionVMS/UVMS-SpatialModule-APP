package eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured;

import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.vividsolutions.jts.io.ParseException;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.service.interceptor.ValidationInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.rest.type.geocoordinate.AreaCoordinateType;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaByLocationService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaDetailsService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaTypeNamesService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.SearchAreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.UserAreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.geojson.AreaDetailsGeoJsonDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.type.AreaFilterType;
import eu.europa.ec.fisheries.uvms.spatial.rest.type.ResponseCode;
import eu.europa.ec.fisheries.uvms.spatial.rest.type.ResponseDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.error.ErrorHandler;
import eu.europa.ec.fisheries.uvms.spatial.rest.mapper.AreaLocationDtoMapper;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ExceptionInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ValidationUtils;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.AreaServiceLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.LayerSubTypeEnum;
import lombok.extern.slf4j.Slf4j;

@Path("/")
@Slf4j
@Stateless
public class AreaResource extends UnionVMSResource {

    private @EJB AreaTypeNamesService areaTypeService;
    private @EJB AreaByLocationService areaByLocationService;
    private @EJB AreaService areaService;
    private @EJB AreaDetailsService areaDetailsService;
	private @EJB SearchAreaService searchAreaService;
    private @EJB UserAreaService userAreaService;
    
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
    public Response getAreasByFilter(AreaFilterType areaFilterType) throws ServiceException {
    	return createSuccessResponse(searchAreaService.getAreasByFilter(areaFilterType.getAreaType(), areaFilterType.getFilter()));
    }

    @GET
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/servicelayers/{layerType}")
    public Response getServiceLayersByType(@PathParam("layerType") String layerType, @Context HttpServletRequest request) throws ServiceException {
        LayerSubTypeEnum layerTypeEnum = LayerSubTypeEnum.value(layerType);
        if (layerTypeEnum.equals(LayerSubTypeEnum.USERAREA) || layerTypeEnum.equals(LayerSubTypeEnum.AREAGROUP)) {
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
