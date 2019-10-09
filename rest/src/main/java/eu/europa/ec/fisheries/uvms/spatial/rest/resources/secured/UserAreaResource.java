/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.JsonNode;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.io.ParseException;
import eu.europa.ec.fisheries.uvms.commons.geometry.mapper.FeatureToGeoJsonJacksonMapper;
import eu.europa.ec.fisheries.uvms.commons.rest.constants.ErrorCodes;
import eu.europa.ec.fisheries.uvms.commons.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.commons.service.interceptor.ValidationInterceptor;
import eu.europa.ec.fisheries.uvms.rest.security.bean.USMService;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialFeaturesEnum;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.FilterType;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.UserAreaCoordinateType;
import eu.europa.ec.fisheries.uvms.spatial.rest.mapper.AreaLocationMapper;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ExceptionInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.UserAreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.area.UserAreaUpdateDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.geojson.UserAreaGeoJsonDto;
import eu.europa.ec.fisheries.wsdl.user.types.ContextSet;
import eu.europa.ec.fisheries.wsdl.user.types.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * @implicitParam roleName|string|header|true||||||
 * @implicitParam scopeName|string|header|true|EC|||||
 * @implicitParam authorization|string|header|true||||||jwt token
 */
@Path("/userarea")
@Slf4j
@Stateless
public class UserAreaResource extends UnionVMSResource {

    @EJB
    private USMService usmService;

    @EJB
    private UserAreaService userAreaService;

    @EJB
    private AreaService areaService;

    @EJB
    private SpatialService spatialService;

    private AreaLocationMapper areaLocationMapper = AreaLocationMapper.mapper();

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/")
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response storeUserArea(UserAreaGeoJsonDto userAreaGeoJsonDto, @Context HttpServletRequest servletRequest) throws ServiceException {
        if (servletRequest.isUserInRole("MANAGE_USER_DEFINED_AREAS")) {
            String userName = servletRequest.getRemoteUser();
            log.info("{} is requesting createUserArea(...)", userName);

            if (StringUtils.isNotBlank(userAreaGeoJsonDto.getDatasetName()) && !servletRequest.isUserInRole("CREATE_USER_AREA_DATASET")) {
                return createErrorResponse("user_area_dataset_creation_not_allowed");
            }

            List<String> scopeSelection = userAreaGeoJsonDto.getScopeSelection();

            if (isAllowedToShareScopes(userName, scopeSelection, servletRequest)) {
                long gid = userAreaService.createUserArea(userAreaGeoJsonDto, userName);
                return createSuccessResponse(gid);
            }
            else {
                return createErrorResponse("user_area_sharing_not_allowed");
            }
        }

        return createErrorResponse("user_areas_management_not_allowed");

    }

    private Boolean isAllowedToShareScopes(String userName, List<String> scopeSelection, HttpServletRequest servletRequest) throws ServiceException {

        Boolean isAllowedToShareScopes = false;

        if (CollectionUtils.isEmpty(scopeSelection)){
            isAllowedToShareScopes = true;
        }

        else if (servletRequest.isUserInRole("SHARE_USER_DEFINED_AREAS")) {

            UserContext usmApplication = usmService.getFullUserContext(userName, servletRequest.getServletContext().getInitParameter("usmApplication"));

            ContextSet contextSet = usmApplication.getContextSet();

            List<eu.europa.ec.fisheries.wsdl.user.types.Context> contexts = contextSet.getContexts();

            List<String> userScopes = new ArrayList<>();

            if (CollectionUtils.isNotEmpty(contexts)){
                for (eu.europa.ec.fisheries.wsdl.user.types.Context context : contexts) {
                    userScopes.add(context.getScope().getScopeName());
                }
            }

            isAllowedToShareScopes = CollectionUtils.containsAny(userScopes, scopeSelection);

        }
        return isAllowedToShareScopes;
    }

    private SimpleFeatureType build(Class geometryType, Map<String, Object> properties, String geometryFieldName) {
        SimpleFeatureTypeBuilder sb = new SimpleFeatureTypeBuilder();
        sb.setCRS(DefaultGeographicCRS.WGS84);
        sb.setName("MULTIPOLIGON");
        if (MapUtils.isNotEmpty(properties)){
            for (String key : properties.keySet()) {
                if (key.equalsIgnoreCase(geometryFieldName)) {
                    sb.add(key, geometryType);
                } else {
                    Class propClass = String.class;
                    Object propValue = properties.get(key);
                    if (propValue != null) {
                        propClass = propValue.getClass();
                    }
                    sb.add(key, propClass);
                }
            }
        }
        return sb.buildFeatureType();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response deleteUserArea(@PathParam("id") Long userAreaId, @HeaderParam("scopeName") String scopeName,  @Context HttpServletRequest servletRequest) throws ServiceException {
        String userName = servletRequest.getRemoteUser();
        log.info("{} is requesting deleteUserArea(...), with a ID={} and scopeName={}", userName, userAreaId, scopeName);

        boolean isPowerUser = isPowerUser(servletRequest);
        userAreaService.deleteUserArea(userAreaId, userName, isPowerUser, null); // we pass NULL for scope because deletion shouldn't happen on shared areas, unless you are a power user
        return createSuccessResponse();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/layers")
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response getUserAreaLayerMapping(@HeaderParam("scopeName") String scopeName, @Context HttpServletRequest servletRequest) {
        log.debug("UserName from security : " + servletRequest.getRemoteUser());
        return createSuccessResponse(userAreaService.getUserAreaLayerDefinition(servletRequest.getRemoteUser(), scopeName));
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/details")
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response getUserAreaDetails(UserAreaCoordinateType userAreaTypeDto, @HeaderParam("scopeName") String scopeName, @Context HttpServletRequest servletRequest) throws IOException, ParseException, ServiceException {
        Response response;
        boolean isPowerUser = isPowerUser(servletRequest);
        if (userAreaTypeDto.getId() != null) {
            response = getUserAreaDetailsById(userAreaTypeDto, servletRequest.getRemoteUser(), isPowerUser, scopeName);
        } else {
            response = getUserAreaDetailsByLocation(userAreaTypeDto, servletRequest.getRemoteUser());
        }
        return response;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/types")
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response getUserAreaTypes(@HeaderParam("scopeName") String scopeName, @Context HttpServletRequest servletRequest) throws ServiceException {
        log.debug("UserName from security : " + servletRequest.getRemoteUser());
        boolean isPowerUser = false;

        if (servletRequest.isUserInRole(SpatialFeaturesEnum.MANAGE_ANY_USER_AREA.value())) {
            isPowerUser = true;
        }

        return createSuccessResponse(userAreaService.getUserAreaTypes(servletRequest.getRemoteUser(), scopeName, isPowerUser));
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/updatedate")
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response updateUserAreaDates(UserAreaUpdateDto userAreaUpdateDto, @Context HttpServletRequest servletRequest) throws ServiceException {
        boolean isPowerUser = false;
        if (servletRequest.isUserInRole(SpatialFeaturesEnum.MANAGE_ANY_USER_AREA.value())) {
            isPowerUser = true;
        }
        userAreaService.updateUserAreaDates(servletRequest.getRemoteUser(), userAreaUpdateDto.getStartDate(), userAreaUpdateDto.getEndDate(), userAreaUpdateDto.getType(), isPowerUser);
        return createSuccessResponse();
    }

    private boolean isPowerUser(HttpServletRequest request) {
        return request.isUserInRole("MANAGE_ANY_USER_AREA");
    }

    private Response getUserAreaDetailsById(UserAreaCoordinateType userAreaTypeDto, String userName, boolean isPowerUser, String scopeName) throws ServiceException, IOException, ParseException {
        if (!userAreaTypeDto.getIsGeom()) {
            AreaTypeEntry areaTypeEntry = areaLocationMapper.getAreaTypeEntry(userAreaTypeDto);
            Map<String, Object> userAreaDetailsWithExtentById = userAreaService.getUserAreaDetailsWithExtentById(areaTypeEntry, userName, isPowerUser, scopeName);
            return createSuccessResponse(userAreaDetailsWithExtentById);
        } else {
            AreaTypeEntry areaTypeEntry = AreaLocationMapper.mapper().getAreaTypeEntry(userAreaTypeDto);
            Map<String, Object> userAreaDetailsWithExtentById = userAreaService.getUserAreaDetailsWithExtentById(areaTypeEntry, userName, isPowerUser, scopeName);

            SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(build(MultiPolygon.class, userAreaDetailsWithExtentById, "geometry"));

           for (Map.Entry<String, Object> entrySet : userAreaDetailsWithExtentById.entrySet()) {
                if(!entrySet.getKey().equals("extent") && !entrySet.getKey().equals("centroid")){
                    featureBuilder.set(entrySet.getKey(), entrySet.getValue());
                } // TODO check with HUGO if really necessary
           }

            List<JsonNode> nodeList = new ArrayList<>();

            JsonNode jsonNode = new FeatureToGeoJsonJacksonMapper().convert(featureBuilder.buildFeature(null));
            nodeList.add(jsonNode);

            return createSuccessResponse(nodeList);
        }
    }

    private Response getUserAreaDetailsByLocation(UserAreaCoordinateType userAreaTypeDto, String userName) throws ServiceException {

        try {
            String areaType = userAreaTypeDto.getAreaType();
            Integer crs = userAreaTypeDto.getCrs();
            Boolean isGeom = userAreaTypeDto.getIsGeom();
            Double latitude = userAreaTypeDto.getLatitude();
            Double longitude = userAreaTypeDto.getLongitude();
            List<Map<String, Object>> userAreaDetails = areaService.getAreasByPoint(latitude, longitude, crs, userName, AreaType.USERAREA);

            if (!isGeom) {
                return createSuccessResponse(userAreaDetails);
            } else {
                List<List<JsonNode>> lists = new ArrayList<>();

                for (Map<String, Object> stringObjectMap : userAreaDetails){

                    List<JsonNode> nodeList = new ArrayList<>();

                    SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(build(MultiPolygon.class, stringObjectMap, "geometry"));

                    for (Map.Entry<String, Object> entrySet : stringObjectMap.entrySet()) {
                        if(!entrySet.getKey().equals("extent") && !entrySet.getKey().equals("centroid")){
                            featureBuilder.set(entrySet.getKey(), entrySet.getValue());
                        } // TODO check with HUGO if really necessary
                    }

                    JsonNode jsonNode = new FeatureToGeoJsonJacksonMapper().convert(featureBuilder.buildFeature(null));
                    nodeList.add(jsonNode);
                    lists.add(nodeList);
                }
                return createSuccessResponse(lists);
            }
        } catch (IOException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @PUT
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response updateUserArea(UserAreaGeoJsonDto userAreaGeoJsonDto, @HeaderParam("scopeName") String scopeName, @Context HttpServletRequest servletRequest) throws ServiceException {
        if (servletRequest.isUserInRole(SpatialFeaturesEnum.MANAGE_ANY_USER_AREA.toString())) {
            String userName = servletRequest.getRemoteUser();
            log.info("{} is requesting updateUserArea(...)", userName);

            if (StringUtils.isNotBlank(userAreaGeoJsonDto.getDatasetName()) && !servletRequest.isUserInRole(SpatialFeaturesEnum.CREATE_USER_AREA_DATASET.toString())) {
                return createErrorResponse("user_area_dataset_creation_not_allowed");
            }

            List<String> scopeSelection = userAreaGeoJsonDto.getScopeSelection();
            boolean isPowerUser = isPowerUser(servletRequest);

            if (isAllowedToShareScopes(userName, scopeSelection, servletRequest)) {
                long gid = userAreaService.updateUserArea(userAreaGeoJsonDto, servletRequest.getRemoteUser(), isPowerUser, scopeName);
                log.info("{} is requesting updateUserArea(...), with a ID={}. Spatial power user: {}", userName, Long.toString(userAreaGeoJsonDto.getId()), isPowerUser);
                return createSuccessResponse(gid);
            }
            else {
                return createErrorResponse("user_area_sharing_not_allowed");
            }

        } else {
            return createErrorResponse("user_areas_management_not_allowed");
        }
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/byfilter")
    @Interceptors(value = {ValidationInterceptor.class, ExceptionInterceptor.class})
    public Response searchUserAreas(FilterType filter, @HeaderParam("scopeName") String scopeName, @Context HttpServletRequest servletRequest) throws ServiceException {
        Response response;

        if (servletRequest.isUserInRole(SpatialFeaturesEnum.MANAGE_USER_DEFINED_AREAS.toString())) {
            response = createSuccessResponse(userAreaService.searchUserAreasByCriteria(servletRequest.getRemoteUser(), scopeName, filter.getFilter(), isPowerUser(servletRequest)));
        }
        else {
            response = createErrorResponse(ErrorCodes.NOT_AUTHORIZED);
        }
        return response;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/list")
    public Response listUserAreas(@HeaderParam("scopeName") String scopeName, @Context HttpServletRequest servletRequest) throws ServiceException {
        Response response;

        if (servletRequest.isUserInRole(SpatialFeaturesEnum.MANAGE_USER_DEFINED_AREAS.toString())) {
            response = createSuccessResponse(userAreaService.searchUserAreasByCriteria(servletRequest.getRemoteUser(), scopeName, StringUtils.EMPTY, isPowerUser(servletRequest)));
        } else {
            response = createErrorResponse(ErrorCodes.NOT_AUTHORIZED);
        }
        return response;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/list/{type}")
    public Response listUserAreasByType(@PathParam("type") String userAreaType, @HeaderParam("scopeName") String scopeName, @Context HttpServletRequest servletRequest) throws ServiceException {
        Response response;
        if (servletRequest.isUserInRole(SpatialFeaturesEnum.MANAGE_USER_DEFINED_AREAS.toString())) {
            List<UserAreaGeoJsonDto> userAreas = userAreaService.searchUserAreasByType(servletRequest.getRemoteUser(), scopeName, userAreaType, isPowerUser(servletRequest));
            response = createSuccessResponse(userAreas);
        } else {
            response = createErrorResponse(ErrorCodes.NOT_AUTHORIZED);
        }
        return response;
    }
}