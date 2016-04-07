package eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vividsolutions.jts.io.ParseException;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.rest.FeatureToGeoJsonJacksonMapper;
import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.service.interceptor.ValidationInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.model.constants.USMSpatial;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationDetails;
import eu.europa.ec.fisheries.uvms.spatial.rest.type.geocoordinate.AreaCoordinateType;
import eu.europa.ec.fisheries.uvms.spatial.rest.type.geocoordinate.LocationCoordinateType;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaDetailsService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaTypeNamesService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.SearchAreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.UserAreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.geojson.AreaDetailsGeoJsonDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.type.AreaFilterType;
import eu.europa.ec.fisheries.uvms.spatial.rest.type.ResponseCode;
import eu.europa.ec.fisheries.uvms.spatial.rest.type.ResponseDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.error.ErrorHandler;
import eu.europa.ec.fisheries.uvms.spatial.rest.mapper.AreaLocationDtoMapper;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ExceptionInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.geojson.LocationDetailsGeoJsonDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.AreaServiceLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.LayerSubTypeEnum;
import lombok.extern.slf4j.Slf4j;

@Path("/")
@Slf4j
@Stateless
public class AreaResource extends UnionVMSResource {

    private @EJB AreaTypeNamesService areaTypeService;
    private @EJB AreaDetailsService areaDetailsService;
	private @EJB SearchAreaService searchAreaService;
    private @EJB UserAreaService userAreaService;
    private @EJB SpatialService spatialService;

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

            response = createSuccessResponse(new FeatureToGeoJsonJacksonMapper().convert(areaDetailsGeoJsonDto.toFeature()));

    	} else {
            List<AreaDetails> areaDetailsList = areaDetailsService.getAreaDetailsByLocation(mapper.getAreaTypeEntry(areaDto));
            AreaDetailsGeoJsonDto areaDetailsGeoJsonDto = mapper.getAreaDetailsDtoForAllAreas(areaDetailsList, areaDto);
            if (!areaDto.getIsGeom()) {
                areaDetailsGeoJsonDto.removeGeometryAllAreas();
                return createSuccessResponse(areaDetailsGeoJsonDto.getAllAreaProperties());
            }

            List<ObjectNode> nodeList = new ArrayList<>();

            for (Map<String, Object> featureMap : areaDetailsGeoJsonDto.getAllAreaProperties()) {
                ObjectNode convert = new FeatureToGeoJsonJacksonMapper().convert(areaDetailsGeoJsonDto.toFeature(featureMap));
                nodeList.add(convert);
            }
            response = createSuccessResponse(nodeList);
    	}

        return response;
    }
    
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/areaproperties")
    @Interceptors(value = {ValidationInterceptor.class, ExceptionInterceptor.class})
    public Response getAreaProperties(List<AreaCoordinateType> areaDtoList) throws ServiceException {

        List<AreaTypeEntry> areaTypeEntryList = mapper.getAreaTypeEntryList(areaDtoList);
        List<Map<String, String>> selectedAreaColumns = searchAreaService.getSelectedAreaColumns(areaTypeEntryList);
        return createSuccessResponse(selectedAreaColumns);
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
    public Response searchAreasByNameOrCode(AreaFilterType areaFilterType) throws ServiceException {
    	return createSuccessResponse(spatialService.searchAreasByNameOrCode(areaFilterType.getAreaType(), areaFilterType.getFilter()));
    }

    @GET
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/servicelayers/{layerType}")
    public Response getServiceLayersByType(@PathParam("layerType") String layerType, @HeaderParam(USMSpatial.SCOPE_NAME) String scopeName, @Context HttpServletRequest request) throws ServiceException {
        LayerSubTypeEnum layerTypeEnum = LayerSubTypeEnum.value(layerType);
        if (layerTypeEnum.equals(LayerSubTypeEnum.USERAREA) || layerTypeEnum.equals(LayerSubTypeEnum.AREAGROUP)) {
            List<AreaServiceLayerDto> areaServiceLayerDtos = areaTypeService.getAllAreasLayerDescription(layerTypeEnum, request.getRemoteUser(), scopeName);
            return createSuccessResponse(areaServiceLayerDtos);
        } else {
            return createSuccessResponse(areaTypeService.getAreaLayerDescription(layerTypeEnum));
        }
    }


    // TODO check this it looks like DUPLICATE functionality as 'areadetails' at first sight only the output looks a bit different
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/locationdetails")
    @Interceptors(value = {ValidationInterceptor.class, ExceptionInterceptor.class})
    public Response getLocationDetails(LocationCoordinateType locationDto) throws IOException, ParseException, ServiceException {
        LocationDetails locationDetails = spatialService.getLocationDetails(mapper.getLocationTypeEntry(locationDto));
        LocationDetailsGeoJsonDto locationDetailsGeoJsonDto = mapper.getLocationDetailsDto(locationDetails);
        ObjectNode nodes = new FeatureToGeoJsonJacksonMapper().convert(locationDetailsGeoJsonDto.toFeature());
        return createSuccessResponse(nodes);
    }

}
