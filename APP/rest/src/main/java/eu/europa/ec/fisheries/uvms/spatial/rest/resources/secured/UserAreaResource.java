package eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured;

import com.vividsolutions.jts.io.ParseException;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.rest.constants.ErrorCodes;
import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.service.interceptor.ValidationInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.model.constants.USMSpatial;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Coordinate;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialFeaturesEnum;
import eu.europa.ec.fisheries.uvms.spatial.rest.mapper.AreaLocationDtoMapper;
import eu.europa.ec.fisheries.uvms.spatial.rest.type.FilterType;
import eu.europa.ec.fisheries.uvms.spatial.rest.type.geocoordinate.UserAreaCoordinateType;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ExceptionInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.UserAreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.UserAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.geojson.AreaDetailsGeoJsonDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.geojson.UserAreaGeoJsonDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Path("/")
@Slf4j
@Stateless
public class UserAreaResource extends UnionVMSResource {

    @EJB
    private UserAreaService userAreaService;

    private AreaLocationDtoMapper areaLocationMapper = AreaLocationDtoMapper.mapper();

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/userarea")
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response storeUserArea(@Context HttpServletRequest request,
                                  UserAreaGeoJsonDto userAreaGeoJsonDto,
                                  @HeaderParam(USMSpatial.SCOPE_NAME) String scopeName) throws ServiceException {
        if (request.isUserInRole("MANAGE_USER_DEFINED_AREAS")) {
            String userName = request.getRemoteUser();
            log.info("{} is requesting storeUserArea(...)", userName);
            if (StringUtils.isNotBlank(userAreaGeoJsonDto.getDatasetName()) && !request.isUserInRole("CREATE_USER_AREA_DATASET")) {
                return createErrorResponse("user_area_dataset_creation_not_allowed");
            }

            if (userAreaGeoJsonDto.getScopeSelection() != null && !userAreaGeoJsonDto.getScopeSelection().isEmpty() && !request.isUserInRole("SHARE_USER_DEFINED_AREAS")) {
                return createErrorResponse("user_area_sharing_not_allowed");
            }

            long gid = userAreaService.storeUserArea(userAreaGeoJsonDto, userName);
            return createSuccessResponse(gid);
        } else {
            return createErrorResponse("user_areas_management_not_allowed");
        }
    }


    @PUT
    @Path("userarea")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response updateUserArea(@Context HttpServletRequest request,
                                   UserAreaGeoJsonDto userAreaGeoJsonDto,
                                   @HeaderParam(USMSpatial.SCOPE_NAME) String scopeName) throws ServiceException {
        if (request.isUserInRole(SpatialFeaturesEnum.MANAGE_USER_DEFINED_AREAS.toString())) {
            String userName = request.getRemoteUser();
            log.info("{} is requesting storeUserArea(...)", userName);

            if (StringUtils.isNotBlank(userAreaGeoJsonDto.getDatasetName()) && !request.isUserInRole(SpatialFeaturesEnum.CREATE_USER_AREA_DATASET.toString())) {
                return createErrorResponse("user_area_dataset_creation_not_allowed");
            }

            if (userAreaGeoJsonDto.getScopeSelection() != null && !userAreaGeoJsonDto.getScopeSelection().isEmpty() && !request.isUserInRole(SpatialFeaturesEnum.SHARE_USER_DEFINED_AREAS.toString())) {
                return createErrorResponse("user_area_sharing_not_allowed");
            }

            boolean isPowerUser = isPowerUser(request);
            log.info("{} is requesting updateUserArea(...), with a ID={}. Spatial power user: {}", userName, Long.toString(userAreaGeoJsonDto.getId()), isPowerUser);

            long gid = userAreaService.updateUserArea(userAreaGeoJsonDto, request.getRemoteUser(), isPowerUser, scopeName);
            return createSuccessResponse(gid);
        } else {
            return createErrorResponse("user_areas_management_not_allowed");
        }
    }

    @DELETE
    @Path("/userarea/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response deleteUserArea(@Context HttpServletRequest request,
                                   @PathParam("id") Long userAreaId,
                                   @HeaderParam(USMSpatial.SCOPE_NAME) String scopeName) throws ServiceException {
        String userName = request.getRemoteUser();
        log.info("{} is requesting deleteUserArea(...), with a ID={} and scopeName={}", userName, userAreaId, scopeName);

        boolean isPowerUser = isPowerUser(request);
        userAreaService.deleteUserArea(userAreaId, userName, isPowerUser, null); // we pass NULL for scope because deletion shouldn't happen on shared areas, unless you are a power user
        return createSuccessResponse();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/userarealayers")
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response getUserAreaLayerMapping(@Context HttpServletRequest request, @HeaderParam(USMSpatial.SCOPE_NAME) String scopeName) {
        log.debug("UserName from security : " + request.getRemoteUser());
        return createSuccessResponse(userAreaService.getUserAreaLayerDefination(request.getRemoteUser(), scopeName));
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/userareadetails")
    @Interceptors(value = {ValidationInterceptor.class, ExceptionInterceptor.class})
    public Response getUserAreaDetails(UserAreaCoordinateType userAreaTypeDto, @Context HttpServletRequest request, @HeaderParam(USMSpatial.SCOPE_NAME) String scopeName) throws IOException, ParseException, ServiceException {
        Response response;
        boolean isPowerUser = isPowerUser(request);
        try {
            if (userAreaTypeDto.getId() != null) {
                response = getUserAreaDetailsById(userAreaTypeDto, request.getRemoteUser(), isPowerUser, scopeName);
            } else {
                response = getUserAreaDetailsByLocation(userAreaTypeDto, request.getRemoteUser());
            }
        }
        catch (Exception ex){
            response = createErrorResponse(ex.getMessage());
        }

        return response;
    }

    private boolean isPowerUser(HttpServletRequest request) {
        return request.isUserInRole("MANAGE_ANY_USER_AREA");
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/userareatypes")
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response getUserAreaTypes(@Context HttpServletRequest request, @HeaderParam(USMSpatial.SCOPE_NAME) String scopeName) throws ServiceException {
        log.debug("UserName from security : " + request.getRemoteUser());
        boolean isPowerUser = false;

        if (request.isUserInRole(SpatialFeaturesEnum.MANAGE_ANY_USER_AREA.value())) {
            isPowerUser = true;
        }

        return createSuccessResponse(userAreaService.getUserAreaTypes(request.getRemoteUser(), scopeName, isPowerUser));
    }

    private Response getUserAreaDetailsById(UserAreaCoordinateType userAreaTypeDto, String userName, boolean isPowerUser, String scopeName) throws ServiceException, IOException, ParseException {
        if (!userAreaTypeDto.getIsGeom()) {
            AreaTypeEntry areaTypeEntry = areaLocationMapper.getAreaTypeEntry(userAreaTypeDto);
            List<AreaDetails> userAreaDetailsWithExtentById = userAreaService.getUserAreaDetailsWithExtentById(areaTypeEntry, userName, isPowerUser, scopeName);
            AreaDetailsGeoJsonDto areaDetailsGeoJsonDto = areaLocationMapper.getAreaDetailsDto(userAreaDetailsWithExtentById.get(0));
            areaDetailsGeoJsonDto.removeGeometry();
            return createSuccessResponse(areaDetailsGeoJsonDto.getProperties());
        } else {
            AreaTypeEntry areaTypeEntry = AreaLocationDtoMapper.mapper().getAreaTypeEntry(userAreaTypeDto);
            List<AreaDetails> userAreaDetails = userAreaService.getUserAreaDetailsById(areaTypeEntry, userName, isPowerUser, scopeName);
            AreaDetailsGeoJsonDto areaDetailsGeoJsonDto = areaLocationMapper.getAreaDetailsDtoForAllAreas(userAreaDetails, userAreaTypeDto);
            return createSuccessResponse(areaDetailsGeoJsonDto.convertAll());
        }
    }

    private Response getUserAreaDetailsByLocation(UserAreaCoordinateType userAreaTypeDto, String userName) throws Exception {

        if (!userAreaTypeDto.getIsGeom()) {
            Coordinate coordinate = areaLocationMapper.getCoordinateFromDto(userAreaTypeDto);
            List<UserAreaDto> userAreaDetails = userAreaService.getUserAreaDetailsWithExtentByLocation(coordinate, userName);
            return createSuccessResponse(userAreaDetails);
        } else {
            AreaTypeEntry areaTypeEntry = AreaLocationDtoMapper.mapper().getAreaTypeEntry(userAreaTypeDto);
            List<AreaDetails> userAreaDetails = userAreaService.getUserAreaDetailsByLocation(areaTypeEntry, userName);
            AreaDetailsGeoJsonDto areaDetailsGeoJsonDto = areaLocationMapper.getAreaDetailsDtoForAllAreas(userAreaDetails, userAreaTypeDto);
            return createSuccessResponse(areaDetailsGeoJsonDto.convertAll());
        }
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/userareasbyfilter") // TODO test with postman
    @Interceptors(value = {ValidationInterceptor.class, ExceptionInterceptor.class})
    public Response searchUserAreas(FilterType filter, @Context HttpServletRequest request, @HeaderParam(USMSpatial.SCOPE_NAME) String scopeName) throws ServiceException {
        return createSuccessResponse(userAreaService.searchUserAreasByCriteria(request.getRemoteUser(), scopeName, filter.getFilter(), isPowerUser(request)));
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/userareaslist") // TODO test with postman
    public Response listUserAreas(@Context HttpServletRequest request, @HeaderParam(USMSpatial.SCOPE_NAME) String scopeName) throws ServiceException {
        Response response;

        if (request.isUserInRole(SpatialFeaturesEnum.MANAGE_USER_DEFINED_AREAS.toString())) {
            response = createSuccessResponse(userAreaService.searchUserAreasByCriteria(request.getRemoteUser(), scopeName, StringUtils.EMPTY, isPowerUser(request)));
        } else {
            response = createErrorResponse(ErrorCodes.NOT_AUTHORIZED);
        }
        return response;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/userareaslist/{type}")
    public Response listUserAreas(@Context HttpServletRequest request, @HeaderParam(USMSpatial.SCOPE_NAME) String scopeName, @PathParam("type") String userAreaType) throws ServiceException {
        Response response;
        if (request.isUserInRole(SpatialFeaturesEnum.MANAGE_USER_DEFINED_AREAS.toString())) {
            List<UserAreaGeoJsonDto> userAreas = userAreaService.searchUserAreasByType(request.getRemoteUser(), scopeName, userAreaType, isPowerUser(request));
            response = createSuccessResponse(userAreas);
        } else {
            response = createErrorResponse(ErrorCodes.NOT_AUTHORIZED);
        }
        return response;
    }
}
