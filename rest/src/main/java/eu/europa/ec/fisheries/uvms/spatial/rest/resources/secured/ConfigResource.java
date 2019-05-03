/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured;

import eu.europa.ec.fisheries.uvms.commons.rest.resource.UnionVMSResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;

/**
 * @implicitParam roleName|string|header|true||||||
 * @implicitParam scopeName|string|header|true|EC|||||
 * @implicitParam authorization|string|header|true||||||jwt token
 */
//@Path("/config")
@Stateless
public class ConfigResource extends UnionVMSResource {

    private static final Logger log = LoggerFactory.getLogger(ConfigResource.class);


    private static final String DEFAULT_CONFIG = "DEFAULT_CONFIG";
    private static final String USER_CONFIG = "USER_CONFIG";
/*
    @EJB
    private MapConfigService mapConfigService;

    @EJB
    private USMService usmService;

    @POST
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Path("{id}")
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response getReportMapConfig(@PathParam("id") int id, ConfigResourceDto config, @HeaderParam("roleName") String roleName, @HeaderParam("scopeName") String scopeName, @Context HttpServletRequest servletRequest) throws Exception {
        final String username = servletRequest.getRemoteUser();
        String applicationName = servletRequest.getServletContext().getInitParameter("usmApplication");
        String adminPref = usmService.getOptionDefaultValue(DEFAULT_CONFIG, applicationName);
        String userPref = usmService.getUserPreference(USER_CONFIG, username, applicationName, roleName, scopeName);
        log.info("Getting mapDefaultSRIDToEPSG configuration for report with id = {}", id);

        Collection<String> permittedLayersNames = ServiceLayerUtils.getUserPermittedLayersNames(usmService, username, roleName, scopeName);

        MapConfigDto mapConfig = mapConfigService.getReportConfig(id, userPref, adminPref, username, scopeName, config.getTimeStamp(), permittedLayersNames);
        return createSuccessResponse(mapConfig);
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/basic")
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response getBasicReportMapConfig(@HeaderParam("roleName") String roleName, @HeaderParam("scopeName") String scopeName, @Context HttpServletRequest servletRequest) throws ServiceException {
        final String username = servletRequest.getRemoteUser();
        String applicationName = servletRequest.getServletContext().getInitParameter("usmApplication");
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
    public Response getReportMapConfigWithoutSave(ConfigurationDto configurationDto, @HeaderParam("roleName") String roleName, @HeaderParam("scopeName") String scopeName, @Context HttpServletRequest servletRequest) throws ServiceException {
        final String username = servletRequest.getRemoteUser();
        Collection<String> permittedLayersNames = ServiceLayerUtils.getUserPermittedLayersNames(usmService, username, roleName, scopeName);


        MapConfigDto mapConfig = mapConfigService.getReportConfigWithoutSave(configurationDto, servletRequest.getRemoteUser(), scopeName, permittedLayersNames);
        return createSuccessResponse(mapConfig);
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/report")
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response getReportConfig(@HeaderParam("roleName") String roleName, @HeaderParam("scopeName") String scopeName, @Context HttpServletRequest servletRequest) throws ServiceException {
        final String username = servletRequest.getRemoteUser();
        String applicationName = servletRequest.getServletContext().getInitParameter("usmApplication");
        String adminPref = usmService.getOptionDefaultValue(DEFAULT_CONFIG, applicationName);
        String userPref = usmService.getUserPreference(USER_CONFIG, username, applicationName, roleName, scopeName);
        return createSuccessResponse(mapConfigService.getReportConfigWithoutMap(userPref, adminPref));
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/admin")
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response getAdminPreferences(@HeaderParam("roleName") String roleName, @HeaderParam("scopeName") String scopeName, @Context HttpServletRequest servletRequest) throws ServiceException, IOException {
        String applicationName = servletRequest.getServletContext().getInitParameter("usmApplication");
        String adminConfig = usmService.getOptionDefaultValue(DEFAULT_CONFIG, applicationName);
        Collection<String> permittedLayersNames = ServiceLayerUtils.getUserPermittedLayersNames(usmService, servletRequest.getRemoteUser(), roleName, scopeName);
        return createSuccessResponse(mapConfigService.retrieveAdminConfiguration(adminConfig, permittedLayersNames));
    }

    @POST
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/admin/save")
    @Interceptors(value = {ExceptionInterceptor.class, ValidationInterceptor.class})
    public Response saveAdminPreferences(ConfigurationDto configurationDto, @HeaderParam("roleName") String roleName, @HeaderParam("scopeName") String scopeName, @Context HttpServletRequest servletRequest) throws ServiceException, IOException {
        Response response;

        if (servletRequest.isUserInRole(SpatialFeaturesEnum.MANAGE_SYSTEM_SPATIAL_CONFIGURATIONS.toString())) {
            String applicationName = servletRequest.getServletContext().getInitParameter("usmApplication");
            String defaultConfig = usmService.getOptionDefaultValue(DEFAULT_CONFIG, applicationName);
            Collection<String> permittedLayersNames = ServiceLayerUtils.getUserPermittedLayersNames(usmService, servletRequest.getRemoteUser(), roleName, scopeName);
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
    public Response getUserPreferences(@HeaderParam("roleName") String roleName, @HeaderParam("scopeName") String scopeName, @Context HttpServletRequest servletRequest) throws ServiceException, IOException {
        String applicationName = servletRequest.getServletContext().getInitParameter("usmApplication");
        final String username = servletRequest.getRemoteUser();
        String adminPref = usmService.getOptionDefaultValue(DEFAULT_CONFIG, applicationName);
        String userPref = usmService.getUserPreference(USER_CONFIG, username, applicationName, roleName, scopeName);
        Collection<String> permittedLayersNames = ServiceLayerUtils.getUserPermittedLayersNames(usmService, username, roleName, scopeName);
        return createSuccessResponse(mapConfigService. retrieveUserConfiguration(userPref, adminPref, username, permittedLayersNames));
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
    public Response resetUserPreferences(ConfigurationDto configurationDto, @HeaderParam("roleName") String roleName, @HeaderParam("scopeName") String scopeName, @Context HttpServletRequest servletRequest) throws ServiceException {
        String applicationName = servletRequest.getServletContext().getInitParameter("usmApplication");
        final String username = servletRequest.getRemoteUser();
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


 */
}