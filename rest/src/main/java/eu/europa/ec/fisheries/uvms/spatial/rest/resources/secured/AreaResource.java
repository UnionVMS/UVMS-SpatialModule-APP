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
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.vividsolutions.jts.geom.MultiPolygon;
import eu.europa.ec.fisheries.uvms.commons.geometry.mapper.GeometryMapper;
import eu.europa.ec.fisheries.uvms.commons.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.commons.service.interceptor.ValidationInterceptor;
import eu.europa.ec.fisheries.uvms.constants.AuthConstants;
import eu.europa.ec.fisheries.uvms.rest.security.bean.USMService;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaSimpleType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.AreaCoordinateType;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.AreaFilterType;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.LocationQueryDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.mapper.AreaLocationMapper;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ExceptionInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaTypeNamesService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.UserAreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.area.AreaByCodeJsonPayload;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.usm.USMSpatial;
import eu.europa.ec.fisheries.uvms.spatial.service.util.ServiceLayerUtils;
import lombok.extern.slf4j.Slf4j;
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
@Path("/area")
@Slf4j
@Stateless
public class AreaResource extends UnionVMSResource {

    @EJB
    private AreaTypeNamesService areaTypeService;

    @EJB
    private AreaService areaService;

    @EJB
    private UserAreaService userAreaService;

    @EJB
    private SpatialService spatialService;

    @EJB
    private USMService usmService;

    @Context
    private HttpServletRequest servletRequest;

    private AreaLocationMapper mapper = AreaLocationMapper.mapper();

    /**
     * Return the list of all area types.
     *
     *
     * @responseMessage 200 ok
     * @responseMessage 404 not found
     */
    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Interceptors(value = {ExceptionInterceptor.class})
    @Path("/types")
    public Response getAreaTypes() {
        log.info("Getting user areas list");
        List<String> areaTypes = areaTypeService.listAllAreaTypeNames();
        return createSuccessResponse(areaTypes);
    }

    /**
     * Endpoint to get location details for given coordinate
     *
     * @param query
     *
     * @see LocationQueryDto
     *
     * @responseMessage 200 ok
     * @responseMessage 404 not found
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Interceptors(value = {ExceptionInterceptor.class})
    @Path("/location/details")
    public Response getLocationDetails(LocationQueryDto query) throws ServiceException {
        try {
            String id = query.getId();
            Boolean isGeom = query.getIsGeom();
            String locationType = query.getLocationType();
            Integer crs = query.getCrs();
            String gid = query.getGid();
            Double latitude = query.getLatitude();
            Double longitude = query.getLongitude();

            Map<String, Object> locationDetails;

            if (id != null){
                locationDetails = areaService.getAreaById(Long.valueOf(id), AreaType.valueOf(locationType));
            }

            else {
                locationDetails = areaService.getClosestPointByPoint(longitude, latitude, crs);
            }
            //LocationTypeEntry locationTypeEntry = mapper.getLocationTypeEntry(query);
            if (!isGeom) {
                return createSuccessResponse(locationDetails);
            }
            StringWriter writer = new StringWriter();
            SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(build(MultiPolygon.class, locationDetails, "geometry"));

            for (Map.Entry<String, Object> entrySet : locationDetails.entrySet()) {
                if (!entrySet.getKey().equals("extent") && !entrySet.getKey().equals("centroid")) // TODO check with HUGO if really necessary
                    featureBuilder.set(entrySet.getKey(), entrySet.getValue());
            }
            GeometryMapper.INSTANCE.simpleFeatureToGeoJson(featureBuilder.buildFeature(null), writer);
            return Response.ok(writer.toString()).build();
        } catch (ServiceException  | IOException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    /**
     *
     * @param areaDto
     *
     * @see
     * @return
     * @throws ServiceException
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/details")
    @Interceptors(value = {ValidationInterceptor.class, ExceptionInterceptor.class})
    public Response getAreaDetails(AreaCoordinateType areaDto) throws ServiceException {
        Response response;
        StringWriter writer = new StringWriter();
        try {
            if (areaDto.getId() != null) {

                Map<String, Object> areaDetailsById =
                        areaService.getAreaById(Long.valueOf(areaDto.getId()), AreaType.valueOf(areaDto.getAreaType()));
                if (!areaDto.getIsGeom()) {
                    return createSuccessResponse(areaDetailsById);
                }

                SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(build(MultiPolygon.class, areaDetailsById, "geometry"));

                for (Map.Entry<String, Object> entrySet : areaDetailsById.entrySet()) {
                    if(!entrySet.getKey().equals("extent") && !entrySet.getKey().equals("centroid")) // TODO check with HUGO if really necessary
                        featureBuilder.set(entrySet.getKey(), entrySet.getValue());
                }

                GeometryMapper.INSTANCE.simpleFeatureToGeoJson(featureBuilder.buildFeature(null), writer);
                response = Response.ok(writer.toString()).build();

            } else {
                List<Map<String, Object>> areaDetailsByLocation = areaService.getAreasByPoint(areaDto.getLatitude(), areaDto.getLongitude(),areaDto.getCrs(), servletRequest.getRemoteUser(), AreaType.valueOf(areaDto.getAreaType()));
                response = createSuccessResponse(areaDetailsByLocation);
            }
        } catch (ServiceException | IOException e) {
            throw new ServiceException(e.getMessage(), e);
        }

        return response;
    }

    private SimpleFeatureType build(Class geometryType, Map<String, Object> properties, String geometryFieldName) {
        SimpleFeatureTypeBuilder sb = new SimpleFeatureTypeBuilder();
        sb.setCRS(DefaultGeographicCRS.WGS84);
        sb.setName("MULTIPOLIGON");
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
        return sb.buildFeatureType();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/properties")
    @Interceptors(value = {ValidationInterceptor.class, ExceptionInterceptor.class})
    public Response getAreaProperties(List<AreaCoordinateType> areaDtoList) throws ServiceException {
        List<AreaTypeEntry> areaTypeEntryList = mapper.getAreaTypeEntryList(areaDtoList);
        List<Map<String, Object>> selectedAreaColumns = areaService.getSelectedAreaColumns(areaTypeEntryList);
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
    	return createSuccessResponse(areaService.searchAreasByNameOrCode(areaFilterType.getAreaType(), areaFilterType.getFilter()));
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/bycode")
    @Interceptors(value = {ValidationInterceptor.class, ExceptionInterceptor.class})
    public Response searchAreaNamesByCode(AreaFilterType areaFilterType) throws ServiceException {
        return createSuccessResponse(areaService.searchAreasByCode(areaFilterType.getAreaType(), areaFilterType.getFilter()));
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/datasets/{areaType}/{areaGid}/{datasetName}")
    @Interceptors(value = {ExceptionInterceptor.class})
        public Response createDataset(@PathParam("areaType") String areaType,
                                      @PathParam("areaGid") String  areaGid,
                                      @PathParam("datasetName") String dataSetName,
                                      @Context HttpServletRequest request ) throws ServiceException {
        if (!request.isUserInRole("CREATE_USER_AREA_DATASET")) {
            return createErrorResponse("user_area_dataset_creation_not_allowed");
        }
        if (StringUtils.isNotBlank(dataSetName)) {
            usmService.createDataset(USMSpatial.APPLICATION_NAME, dataSetName,  areaType + USMSpatial.DELIMITER + areaGid, USMSpatial.USM_DATASET_CATEGORY, USMSpatial.USM_DATASET_DESCRIPTION);
        } else {
            throw new IllegalArgumentException("datasetName is missing");
        }
        return createSuccessResponse();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/type/code")
    @Interceptors(value = { ExceptionInterceptor.class})
    public Response byCode(AreaByCodeJsonPayload payload) throws ServiceException {

        List<AreaSimpleType> request = new ArrayList<>();
        List<eu.europa.ec.fisheries.uvms.spatial.service.dto.area.AreaType> areaTypeList = payload.getAreaTypes();
        for (eu.europa.ec.fisheries.uvms.spatial.service.dto.area.AreaType areaType : areaTypeList){
            request.add(new AreaSimpleType(areaType.getAreaType(), areaType.getAreaCode(), null));
        }
        List<AreaSimpleType> response = areaService.getAreasByCode(request);
        return createSuccessResponse(response);
    }

}