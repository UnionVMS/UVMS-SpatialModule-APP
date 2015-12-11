package eu.europa.ec.fisheries.uvms.spatial.rest.resources;

import eu.europa.ec.fisheries.uvms.constants.AuthConstants;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.rest.security.bean.USMService;
import eu.europa.ec.fisheries.uvms.service.interceptor.ValidationInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialFeaturesEnum;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ExceptionInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.MapConfigService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.MapConfigDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.ProjectionDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm.ConfigurationDto;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Path("/config")
@Slf4j
public class ConfigResource extends UnionVMSResource {

    private static final String DEFAULT_CONFIG = "DEFAULT_CONFIG";

    private static final String USER_CONFIG = "USER_CONFIG";

    @EJB
    private MapConfigService mapConfigService;

    @EJB
    private USMService usmService;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("{id}")
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response getReportMapConfig(@Context HttpServletRequest request,
                                       @HeaderParam(AuthConstants.HTTP_HEADER_SCOPE_NAME) String scopeName,
                                       @HeaderParam(AuthConstants.HTTP_HEADER_ROLE_NAME) String roleName,
                                       @PathParam("id") int id) throws ServiceException {
        final String username = request.getRemoteUser();
        String applicationName = request.getServletContext().getInitParameter("usmApplication");
        String adminPref = usmService.getOptionDefaultValue(DEFAULT_CONFIG, applicationName);
        String userPref = usmService.getUserPreference(USER_CONFIG, username, applicationName, roleName, scopeName);
        log.info("Getting map configuration for report with id = {}", id);
        MapConfigDto mapConfig = mapConfigService.getReportConfig(id, userPref, adminPref);
        return createSuccessResponse(mapConfig);
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/admin")
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response getAdminPreferences(@Context HttpServletRequest request) throws ServiceException, IOException {

        Response response;

        if (request.isUserInRole(SpatialFeaturesEnum.MANAGE_SYSTEM_SPATIAL_CONFIGURATIONS.toString())) {
            String applicationName = request.getServletContext().getInitParameter("usmApplication");
            String adminConfig = usmService.getOptionDefaultValue(DEFAULT_CONFIG, applicationName);
            response = createSuccessResponse(mapConfigService.retrieveAdminConfiguration(adminConfig));
        } else {
            response = createErrorResponse("User not authorized to access Spatial System Configurations.");
        }

        return response;
    }

    @POST
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/admin/save")
    @Interceptors(value = {ExceptionInterceptor.class, ValidationInterceptor.class})
    public Response saveAdminPreferences(@Context HttpServletRequest request, ConfigurationDto configurationDto) throws ServiceException, IOException {
        Response response;

        if (request.isUserInRole(SpatialFeaturesEnum.MANAGE_SYSTEM_SPATIAL_CONFIGURATIONS.toString())) {
            String applicationName = request.getServletContext().getInitParameter("usmApplication");
            String defaultConfig = usmService.getOptionDefaultValue(DEFAULT_CONFIG, applicationName);
            String json = mapConfigService.saveAdminJson(configurationDto, defaultConfig);
            usmService.setOptionDefaultValue(DEFAULT_CONFIG, json, applicationName);
            response = createSuccessResponse();
        } else {
            response = createErrorResponse("User not authorized to access Spatial System Configurations.");
        }

        return response;
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/user")
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response getUserPreferences(@Context HttpServletRequest request,
                                       @HeaderParam(AuthConstants.HTTP_HEADER_SCOPE_NAME) String scopeName,
                                       @HeaderParam(AuthConstants.HTTP_HEADER_ROLE_NAME) String roleName) throws ServiceException, IOException {
        String applicationName = request.getServletContext().getInitParameter("usmApplication");
        final String username = request.getRemoteUser();
        String adminPref = usmService.getOptionDefaultValue(DEFAULT_CONFIG, applicationName);
        String userPref = usmService.getUserPreference(USER_CONFIG, username, applicationName, roleName, scopeName);
        return createSuccessResponse(mapConfigService.retrieveUserConfiguration(userPref, adminPref));
    }

    @POST
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/user/save")
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response saveUserPreferences(@Context HttpServletRequest request,
                                        @HeaderParam(AuthConstants.HTTP_HEADER_SCOPE_NAME) String scopeName,
                                        @HeaderParam(AuthConstants.HTTP_HEADER_ROLE_NAME) String roleName,
                                        ConfigurationDto configurationDto) throws ServiceException, IOException {
        String applicationName = request.getServletContext().getInitParameter("usmApplication");
        final String username = request.getRemoteUser();
        String userPref = usmService.getUserPreference(USER_CONFIG, username, applicationName, roleName, scopeName);
        String json = mapConfigService.saveUserJson(configurationDto, userPref);
        usmService.putUserPreference(USER_CONFIG, json, applicationName, scopeName, roleName, username);
        return createSuccessResponse();
    }

    @POST
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/user/reset")
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response resetUserPreferences(@Context HttpServletRequest request,
                                         @HeaderParam(AuthConstants.HTTP_HEADER_SCOPE_NAME) String scopeName,
                                         @HeaderParam(AuthConstants.HTTP_HEADER_ROLE_NAME) String roleName,
                                         ConfigurationDto configurationDto) throws ServiceException {
        String applicationName = request.getServletContext().getInitParameter("usmApplication");
        final String username = request.getRemoteUser();
        String userPref = usmService.getUserPreference(USER_CONFIG, username, applicationName, roleName, scopeName);
        String json = mapConfigService.resetUserJson(configurationDto, userPref);
        usmService.putUserPreference(USER_CONFIG, json, applicationName, scopeName, roleName, username);
        return createSuccessResponse();
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/projections")
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response getAllProjections() {
        log.info("Getting all projections");
        List<ProjectionDto> projections = mapConfigService.getAllProjections();
        return createSuccessResponse(projections);
    }
}
