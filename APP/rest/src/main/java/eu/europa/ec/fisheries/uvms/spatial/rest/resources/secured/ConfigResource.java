package eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured;

import eu.europa.ec.fisheries.uvms.constants.AuthConstants;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.rest.security.bean.USMService;
import eu.europa.ec.fisheries.uvms.service.interceptor.ValidationInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialFeaturesEnum;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ExceptionInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.MapConfigService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.ConfigResourceDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.MapConfigDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.ProjectionDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm.ConfigurationDto;
import eu.europa.ec.fisheries.uvms.spatial.util.ServiceLayerUtils;
import lombok.extern.slf4j.Slf4j;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Path("/config")
@Slf4j
@Stateless
public class ConfigResource extends UnionVMSResource {

    private static final String DEFAULT_CONFIG = "DEFAULT_CONFIG";
    private static final String USER_CONFIG = "USER_CONFIG";

    @EJB
    private MapConfigService mapConfigService;

    @EJB
    private USMService usmService;

    @POST
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Path("{id}")
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response getReportMapConfig(@Context HttpServletRequest request,
                                       @HeaderParam(AuthConstants.HTTP_HEADER_SCOPE_NAME) String scopeName,
                                       @HeaderParam(AuthConstants.HTTP_HEADER_ROLE_NAME) String roleName,
                                       @PathParam("id") int id, ConfigResourceDto config) throws ServiceException {
        final String username = request.getRemoteUser();
        String applicationName = request.getServletContext().getInitParameter("usmApplication");
        String adminPref = usmService.getOptionDefaultValue(DEFAULT_CONFIG, applicationName);
        String userPref = usmService.getUserPreference(USER_CONFIG, username, applicationName, roleName, scopeName);
        log.info("Getting map configuration for report with id = {}", id);

        Collection<String> permittedLayersNames = ServiceLayerUtils.getUserPermittedLayersNames(usmService, username, roleName, scopeName);

        MapConfigDto mapConfig = mapConfigService.getReportConfig(id, userPref, adminPref, username, scopeName, config.getTimeStamp(), permittedLayersNames);
        return createSuccessResponse(mapConfig);
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/basic")
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response getBasicReportMapConfig(@Context HttpServletRequest request,
                                            @HeaderParam(AuthConstants.HTTP_HEADER_SCOPE_NAME) String scopeName,
                                            @HeaderParam(AuthConstants.HTTP_HEADER_ROLE_NAME) String roleName) throws ServiceException {
        final String username = request.getRemoteUser();
        String applicationName = request.getServletContext().getInitParameter("usmApplication");
        String adminPref = usmService.getOptionDefaultValue(DEFAULT_CONFIG, applicationName);
        String userPref = usmService.getUserPreference(USER_CONFIG, username, applicationName, roleName, scopeName);
        MapConfigDto mapConfig = mapConfigService.getBasicReportConfig(userPref, adminPref);
        return createSuccessResponse(mapConfig);
    }

    @POST
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Path("/fromreport")
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response getReportMapConfigWithoutSave(@Context HttpServletRequest request,
                                                  @HeaderParam(AuthConstants.HTTP_HEADER_SCOPE_NAME) String scopeName,
                                                  @HeaderParam(AuthConstants.HTTP_HEADER_ROLE_NAME) String roleName,
                                                  ConfigurationDto configurationDto) throws ServiceException {
        final String username = request.getRemoteUser();
        Collection<String> permittedLayersNames = ServiceLayerUtils.getUserPermittedLayersNames(usmService, username, roleName, scopeName);


        MapConfigDto mapConfig = mapConfigService.getReportConfigWithoutSave(configurationDto, request.getRemoteUser(), scopeName, permittedLayersNames);
        return createSuccessResponse(mapConfig);
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/report")
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response getReportConfig(@Context HttpServletRequest request,
                                    @HeaderParam(AuthConstants.HTTP_HEADER_SCOPE_NAME) String scopeName,
                                    @HeaderParam(AuthConstants.HTTP_HEADER_ROLE_NAME) String roleName) throws ServiceException {
        final String username = request.getRemoteUser();
        String applicationName = request.getServletContext().getInitParameter("usmApplication");
        String adminPref = usmService.getOptionDefaultValue(DEFAULT_CONFIG, applicationName);
        String userPref = usmService.getUserPreference(USER_CONFIG, username, applicationName, roleName, scopeName);
        return createSuccessResponse(mapConfigService.getReportConfigWithoutMap(userPref, adminPref));
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/admin")
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response getAdminPreferences(@Context HttpServletRequest request,
                                        @HeaderParam(AuthConstants.HTTP_HEADER_SCOPE_NAME) String scopeName,
                                        @HeaderParam(AuthConstants.HTTP_HEADER_ROLE_NAME) String roleName) throws ServiceException, IOException {
        String applicationName = request.getServletContext().getInitParameter("usmApplication");
        String adminConfig = usmService.getOptionDefaultValue(DEFAULT_CONFIG, applicationName);
        Collection<String> permittedLayersNames = ServiceLayerUtils.getUserPermittedLayersNames(usmService, request.getRemoteUser(), roleName, scopeName);
        return createSuccessResponse(mapConfigService.retrieveAdminConfiguration(adminConfig, permittedLayersNames));
    }

    @POST
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/admin/save")
    @Interceptors(value = {ExceptionInterceptor.class, ValidationInterceptor.class})
    public Response saveAdminPreferences(@Context HttpServletRequest request, ConfigurationDto configurationDto,
                                         @HeaderParam(AuthConstants.HTTP_HEADER_SCOPE_NAME) String scopeName,
                                         @HeaderParam(AuthConstants.HTTP_HEADER_ROLE_NAME) String roleName) throws ServiceException, IOException {
        Response response;

        if (request.isUserInRole(SpatialFeaturesEnum.MANAGE_SYSTEM_SPATIAL_CONFIGURATIONS.toString())) {
            String applicationName = request.getServletContext().getInitParameter("usmApplication");
            String defaultConfig = usmService.getOptionDefaultValue(DEFAULT_CONFIG, applicationName);
            Collection<String> permittedLayersNames = ServiceLayerUtils.getUserPermittedLayersNames(usmService, request.getRemoteUser(), roleName, scopeName);
            String json = mapConfigService.saveAdminJson(configurationDto, defaultConfig, permittedLayersNames);
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
        Collection<String> permittedLayersNames = ServiceLayerUtils.getUserPermittedLayersNames(usmService, username, roleName, scopeName);
        return createSuccessResponse(mapConfigService.retrieveUserConfiguration(userPref, adminPref, username, permittedLayersNames));
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

        String adminConfig = usmService.getOptionDefaultValue(DEFAULT_CONFIG, applicationName);
        Collection<String> permittedLayersNames = ServiceLayerUtils.getUserPermittedLayersNames(usmService, username, roleName, scopeName);
        ConfigurationDto defaultConfigurationDto = mapConfigService.getNodeDefaultValue(configurationDto, adminConfig, username, permittedLayersNames);
        return createSuccessResponse(defaultConfigurationDto);
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
