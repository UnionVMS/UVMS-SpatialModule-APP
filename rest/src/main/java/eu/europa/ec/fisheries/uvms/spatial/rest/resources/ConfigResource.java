package eu.europa.ec.fisheries.uvms.spatial.rest.resources;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.europa.ec.fisheries.uvms.constants.AuthConstants;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.rest.security.bean.USMService;
import eu.europa.ec.fisheries.uvms.service.interceptor.ValidationInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ExceptionInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.MapConfigService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.MapConfigDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.ProjectionDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm.ConfigurationDto;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@Path("/config")
@Slf4j
public class ConfigResource extends UnionVMSResource {

    private static final String DEFAULT_CONFIG = "DEFAULT_CONFIG";

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
                                       @HeaderParam(AuthConstants.HTTP_HEADER_AUTHORIZATION) String jwtToken,
                                       @PathParam("id") int id) throws ServiceException {
        final String username = request.getRemoteUser();
        String applicationName = request.getServletContext().getInitParameter("usmApplication");
        String adminPref = usmService.getOptionDefaultValue(DEFAULT_CONFIG, applicationName);
        String userPref = usmService.getUserPreference(DEFAULT_CONFIG, username, applicationName, roleName, scopeName, jwtToken);
        log.info("Getting map configuration for report with id = {}", id);
        MapConfigDto mapConfig = mapConfigService.getReportConfig(id, userPref, adminPref);
        return createSuccessResponse(mapConfig);
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/admin")
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response getAdminPreferences(@Context HttpServletRequest request) throws ServiceException, IOException {
        String applicationName = request.getServletContext().getInitParameter("usmApplication");
        String adminConfig = usmService.getOptionDefaultValue(DEFAULT_CONFIG, applicationName);
        return createSuccessResponse(mapConfigService.convertToAdminConfiguration(adminConfig));
    }

    @POST
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/admin/save")
    @Interceptors(value = {ExceptionInterceptor.class, ValidationInterceptor.class})
    public Response saveAdminPreferences(@Context HttpServletRequest request, ConfigurationDto configurationDto) throws ServiceException, IOException {
        String applicationName = request.getServletContext().getInitParameter("usmApplication");
        String defaultConfig = usmService.getOptionDefaultValue(DEFAULT_CONFIG, applicationName);
        usmService.setOptionDefaultValue(DEFAULT_CONFIG, mapConfigService.convertToAdminJson(configurationDto, defaultConfig), applicationName);
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
