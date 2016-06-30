package eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vividsolutions.jts.io.ParseException;
import eu.europa.ec.fisheries.uvms.constants.AuthConstants;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.rest.FeatureToGeoJsonJacksonMapper;
import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.rest.security.bean.USMService;
import eu.europa.ec.fisheries.uvms.service.interceptor.ValidationInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.model.constants.USMSpatial;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationDetails;
import eu.europa.ec.fisheries.uvms.spatial.rest.mapper.AreaLocationDtoMapper;
import eu.europa.ec.fisheries.uvms.spatial.rest.type.AreaFilterType;
import eu.europa.ec.fisheries.uvms.spatial.rest.type.geocoordinate.AreaCoordinateType;
import eu.europa.ec.fisheries.uvms.spatial.rest.type.geocoordinate.LocationCoordinateType;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ExceptionInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.AreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.AreaTypeNamesService;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialService;
import eu.europa.ec.fisheries.uvms.spatial.service.UserAreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.geojson.AreaDetailsGeoJsonDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.geojson.LocationDetailsGeoJsonDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.LayerSubTypeEnum;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.ServiceLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.util.ServiceLayerUtils;
import eu.europa.ec.fisheries.wsdl.user.types.DatasetExtension;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

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
import java.io.IOException;
import java.util.*;

@Path("/area")
@Slf4j
@Stateless
public class AreaResource extends UnionVMSResource {

    private @EJB AreaTypeNamesService areaTypeService;
    private @EJB AreaService areaService;
    private @EJB UserAreaService userAreaService;
    private @EJB SpatialService spatialService;
    private @EJB USMService usmService;

    private AreaLocationDtoMapper mapper = AreaLocationDtoMapper.mapper();

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/types")
    public Response getAreaTypes() {

        Response response;

        try {
            log.info("Getting user areas list");
            List<String> areaTypes = areaTypeService.listAllAreaTypeNames();
            response = createSuccessResponse(areaTypes);
        } catch (Exception ex) {
            log.error("[ Error when getting area types list. ] ", ex);
            return createErrorResponse("[ Error when getting area types list. ] " + ex.getMessage());
        }
        return response;
    }
    
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/details")
    @Interceptors(value = {ValidationInterceptor.class, ExceptionInterceptor.class})
    public Response getAreaDetails(AreaCoordinateType areaDto) throws IOException, ParseException, ServiceException {

        Response response;

        if (areaDto.getId() != null) {

            AreaDetails areaDetails = areaService.getAreaDetailsById(mapper.getAreaTypeEntry(areaDto));
            AreaDetailsGeoJsonDto areaDetailsGeoJsonDto = mapper.getAreaDetailsDto(areaDetails);
            if (!areaDto.getIsGeom()) {
                areaDetailsGeoJsonDto.removeGeometry();
                return createSuccessResponse(areaDetailsGeoJsonDto.getProperties());
            }

            response = createSuccessResponse(new FeatureToGeoJsonJacksonMapper().convert(areaDetailsGeoJsonDto.toFeature()));

    	} else {
            List<AreaDetails> areaDetailsList = spatialService.getAreaDetailsByLocation(mapper.getAreaTypeEntry(areaDto));
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
    @Path("/properties")
    @Interceptors(value = {ValidationInterceptor.class, ExceptionInterceptor.class})
    public Response getAreaProperties(List<AreaCoordinateType> areaDtoList) throws ServiceException {

        List<AreaTypeEntry> areaTypeEntryList = mapper.getAreaTypeEntryList(areaDtoList);
        List<Map<String, String>> selectedAreaColumns = areaService.getSelectedAreaColumns(areaTypeEntryList);
        return createSuccessResponse(selectedAreaColumns);
    }
   
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/layers")
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response getSystemAreaLayerMapping(@Context HttpServletRequest request,
                                              @HeaderParam(AuthConstants.HTTP_HEADER_SCOPE_NAME) String scopeName,
                                              @HeaderParam(AuthConstants.HTTP_HEADER_ROLE_NAME) String roleName) throws ServiceException {
        final String username = request.getRemoteUser();
        Collection<String> permittedLayersNames = ServiceLayerUtils.getUserPermittedLayersNames(usmService, username, roleName, scopeName);
    	return createSuccessResponse(areaTypeService.listSystemAreaLayerMapping(permittedLayersNames));
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/locationlayers")
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response getSystemAreaAndLocationLayerMapping(@Context HttpServletRequest request,
                                                         @HeaderParam(AuthConstants.HTTP_HEADER_SCOPE_NAME) String scopeName,
                                                         @HeaderParam(AuthConstants.HTTP_HEADER_ROLE_NAME) String roleName) throws ServiceException {
        final String username = request.getRemoteUser();
        Collection<String> permittedLayersNames = ServiceLayerUtils.getUserPermittedLayersNames(usmService, username, roleName, scopeName);
        return createSuccessResponse(areaTypeService.listSystemAreaAndLocationLayerMapping(permittedLayersNames));
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/byfilter")
    @Interceptors(value = {ValidationInterceptor.class, ExceptionInterceptor.class})
    public Response searchAreasByNameOrCode(AreaFilterType areaFilterType) throws ServiceException {
    	return createSuccessResponse(spatialService.searchAreasByNameOrCode(areaFilterType.getAreaType(), areaFilterType.getFilter()));
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/bycode")
    @Interceptors(value = {ValidationInterceptor.class, ExceptionInterceptor.class})
    public Response searchAreaNamesByCode(AreaFilterType areaFilterType) throws ServiceException {
        return createSuccessResponse(spatialService.searchAreasByCode(areaFilterType.getAreaType(), areaFilterType.getFilter()));
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

    @POST
    @Consumes((MediaType.APPLICATION_JSON))
    @Produces((MediaType.APPLICATION_JSON))
    @Path("/datasets/{areaType}/{areaGid}/{datasetName}")
    @Interceptors(value = {ExceptionInterceptor.class})
        public Response createDataset(@PathParam("areaType") String areaType,
                                      @PathParam("areaGid") String  areaGid,
                                      @PathParam("datasetName") String   datasetName,
                                      @Context HttpServletRequest request ) throws ServiceException {
        if (!request.isUserInRole("CREATE_USER_AREA_DATASET")) {
            return createErrorResponse("user_area_dataset_creation_not_allowed");
        }

        if (StringUtils.isNotBlank(datasetName)) {
            usmService.createDataset(USMSpatial.APPLICATION_NAME, datasetName,  areaType + USMSpatial.DELIMITER + areaGid, USMSpatial.USM_DATASET_CATEGORY, USMSpatial.USM_DATASET_DESCRIPTION);
        } else {
            throw new IllegalArgumentException("datasetName is missing");
        }
        return createSuccessResponse();
    }

    @GET
    @Produces((MediaType.APPLICATION_JSON))
    @Path("/datasets/{areaType}/{areaGid}")
    @Interceptors(value = { ExceptionInterceptor.class})
    public Response findDatasets(@PathParam("areaType") String areaType, @PathParam("areaGid") String areaGid,@Context HttpServletRequest request ) throws ServiceException {

       List<DatasetExtension> datasets = usmService.findDatasetsByDiscriminator(USMSpatial.APPLICATION_NAME,  areaType + USMSpatial.DELIMITER + areaGid);
       return createSuccessResponse(datasets);
    }

}
