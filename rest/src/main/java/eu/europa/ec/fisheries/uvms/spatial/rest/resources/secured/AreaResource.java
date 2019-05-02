/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vividsolutions.jts.geom.MultiPolygon;
import eu.europa.ec.fisheries.uvms.commons.geometry.mapper.GeometryMapper;
import eu.europa.ec.fisheries.uvms.commons.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.commons.service.interceptor.ValidationInterceptor;
import eu.europa.ec.fisheries.uvms.constants.AuthConstants;
import eu.europa.ec.fisheries.uvms.rest.security.bean.USMService;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
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
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeatureType;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
    public Response getLocationByPointOrById(LocationQueryDto query) throws ServiceException {
        try {
            String id = query.getId();
            Boolean isGeom = query.getIsGeom();
            String locationType = query.getLocationType();
            Integer crs = query.getCrs();
            Double latitude = query.getLatitude();
            Double longitude = query.getLongitude();

            Map<String, Object> locationDetails;
            if (id != null){
                locationDetails = areaService.getAreaById(Long.valueOf(id), AreaType.valueOf(locationType));
            }

            else {
                ClosestLocationSpatialRQ closestLocationSpatialRQ = new ClosestLocationSpatialRQ();
                ClosestLocationSpatialRQ.LocationTypes locationTypes = new ClosestLocationSpatialRQ.LocationTypes();
                locationTypes.getLocationTypes().add(LocationType.PORT);
                closestLocationSpatialRQ.setLocationTypes(locationTypes);
                PointType pointType = new PointType();
                pointType.setCrs(crs);
                pointType.setLatitude(latitude);
                pointType.setLongitude(longitude);
                closestLocationSpatialRQ.setPoint(pointType);
                closestLocationSpatialRQ.setUnit(UnitType.NAUTICAL_MILES);
                List<Location> closestPointByPoint = areaService.getClosestPointByPoint(closestLocationSpatialRQ);
                Location location = closestPointByPoint.get(0);
                ObjectMapper oMapper = new ObjectMapper();
                locationDetails = oMapper.convertValue(location, Map.class);

            }
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

    @POST
    @Produces(value = {MediaType.APPLICATION_XML})
    @Consumes(value = {MediaType.APPLICATION_XML})
    @Path("/location/details")
    public ClosestLocationSpatialRS getLocationByPoint(ClosestLocationSpatialRQ request) throws ServiceException {

        ClosestLocationSpatialRS response = new ClosestLocationSpatialRS();
        List<Location> closestLocations = areaService.getClosestPointByPoint(request);

        if (closestLocations != null){
            ClosestLocationsType locationType = new ClosestLocationsType();
            locationType.getClosestLocations().addAll(closestLocations);
            response.setClosestLocations(locationType);
        }

        return response;

    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/properties")
    @Interceptors(value = {ValidationInterceptor.class, ExceptionInterceptor.class})
    public Response getAreaProperties(List<AreaCoordinateType> areaDtoList) throws ServiceException {
        List<AreaTypeEntry> areaTypeEntryList = null;

        if (CollectionUtils.isNotEmpty(areaDtoList)) {
            areaTypeEntryList = new ArrayList<>( areaDtoList.size() );
            for ( AreaCoordinateType areaCoordinateType : areaDtoList ) {

                AreaTypeEntry areaTypeEntry = new AreaTypeEntry();

                areaTypeEntry.setLongitude( areaCoordinateType.getLongitude() );
                areaTypeEntry.setLatitude( areaCoordinateType.getLatitude() );
                areaTypeEntry.setCrs( areaCoordinateType.getCrs() );
                areaTypeEntry.setId( areaCoordinateType.getId() );

                areaTypeEntry.setAreaType(  Enum.valueOf( AreaType.class, areaCoordinateType.getAreaType().toUpperCase()));

                areaTypeEntryList.add(areaTypeEntry);
            }
        }

        List<Map<String, Object>> selectedAreaColumns = areaService.getAreasByIds(areaTypeEntryList);
        return createSuccessResponse(selectedAreaColumns);
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
    public Response getAreasByPointOrById(AreaCoordinateType areaDto, @Context HttpServletRequest servletRequest) throws ServiceException {
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
                    if(!entrySet.getKey().equals("extent") && !entrySet.getKey().equals("centroid")) // TODO check if really needed in webapp
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

    @POST
    @Produces(value = {MediaType.APPLICATION_XML})
    @Consumes(value = {MediaType.APPLICATION_XML})
    @Path("/details")
    public AreaByLocationSpatialRS getAreasByPoint(AreaByLocationSpatialRQ request) throws ServiceException {

        AreaByLocationSpatialRS response = new AreaByLocationSpatialRS();
        List<AreaExtendedIdentifierType> areaTypesByLocation = areaService.getAreasByPoint(request);

        if(areaTypesByLocation != null){
            AreasByLocationType areasByLocationType = new AreasByLocationType();
            areasByLocationType.getAreas().addAll(areaTypesByLocation);
            response.setAreasByLocation(areasByLocationType);
        }

        return response;

    }

    @POST
    @Produces(value = {MediaType.APPLICATION_XML})
    @Consumes(value = {MediaType.APPLICATION_XML})
    @Path("/closest")
    public ClosestAreaSpatialRS getClosestAreasToPointByType(ClosestAreaSpatialRQ request) throws ServiceException {
        ClosestAreaSpatialRS response = new ClosestAreaSpatialRS();
        Double lat = request.getPoint().getLatitude();
        Double lon = request.getPoint().getLongitude();
        Integer crs = request.getPoint().getCrs();
        UnitType unit = request.getUnit();
        List<Area> closestAreas = areaService.getClosestArea(lon, lat, crs, unit);
        if (closestAreas != null) {
            ClosestAreasType closestAreasType = new ClosestAreasType();
            closestAreasType.getClosestAreas().addAll(closestAreas);
            response.setClosestArea(closestAreasType);
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
        return createSuccessResponse(areaService.getAreasByCode(request));
    }
}