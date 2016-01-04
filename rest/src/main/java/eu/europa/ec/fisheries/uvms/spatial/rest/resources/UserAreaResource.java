package eu.europa.ec.fisheries.uvms.spatial.rest.resources;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.service.interceptor.ValidationInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Coordinate;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.FilterDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.GeoCoordinateDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.mapper.AreaLocationDtoMapper;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ExceptionInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.UserAreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaGeomDto;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
@Slf4j
@Stateless
public class UserAreaResource extends UnionVMSResource {

    @EJB
    private UserAreaService userAreaService;

    private AreaLocationDtoMapper mapper = AreaLocationDtoMapper.mapper();

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/userarea")
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response storeUserArea(@Context HttpServletRequest request,
                                  UserAreaGeomDto userAreaGeomDto,
                                  @HeaderParam("scopeName") String scopeName) throws ServiceException {
        String userName = request.getRemoteUser();
        log.info("{} is requesting storeUserArea(...)", userName);
        userAreaService.storeUserArea(userAreaGeomDto, userName, scopeName);
        return createSuccessResponse();
    }

    @PUT
    @Path("userarea")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response updateUserArea(@Context HttpServletRequest request,
                                   UserAreaGeomDto userAreaGeomDto,
                                   @HeaderParam("scopeName") String scopeName) throws ServiceException {
        String userName = request.getRemoteUser();
        log.info("{} is requesting updateUserArea(...), with a ID={}", userName, userAreaGeomDto.getGid());
        userAreaService.updateUserArea(userAreaGeomDto, request.getRemoteUser(), scopeName);
        return createSuccessResponse();
    }

    @DELETE
    @Path("/userarea/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response deleteUserArea(@Context HttpServletRequest request,
                                   @PathParam("id") Long userAreaId,
                                   @HeaderParam("scopeName") String scopeName) throws ServiceException {
        String userName = request.getRemoteUser();
        log.info("{} is requesting deleteUserArea(...), with a ID={} and scopeName={}", userName, userAreaId, scopeName);
        userAreaService.deleteUserArea(userAreaId, userName, scopeName);
        return createSuccessResponse();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/userarealayers")
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response getSystemAreaLayerMapping(@Context HttpServletRequest request, @HeaderParam("scopeName") String scopeName) {
        log.debug("UserName from security : " + request.getRemoteUser());
        return createSuccessResponse(userAreaService.getUserAreaLayerDefination(request.getRemoteUser(), scopeName));
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/userareadetails")
    @Interceptors(value = {ValidationInterceptor.class, ExceptionInterceptor.class})
    public Response getUserAreaDetails(GeoCoordinateDto geoCoordinateDto, @Context HttpServletRequest request, @HeaderParam("scopeName") String scopeName) {
        Coordinate coordinate = mapper.getCoordinateFromDto(geoCoordinateDto);
        return createSuccessResponse(userAreaService.getUserAreaDetails(coordinate, request.getRemoteUser(), scopeName));
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/userareasbyfilter")
    @Interceptors(value = {ValidationInterceptor.class, ExceptionInterceptor.class})
    public Response searchUserAreas(FilterDto filter, @Context HttpServletRequest request, @HeaderParam("scopeName") String scopeName) {
        return createSuccessResponse(userAreaService.searchUserAreasByCriteria(request.getRemoteUser(), scopeName, filter.getFilter()));
    }
}
