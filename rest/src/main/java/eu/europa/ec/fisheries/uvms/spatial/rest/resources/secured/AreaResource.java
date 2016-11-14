/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vividsolutions.jts.io.ParseException;
import eu.europa.ec.fisheries.uvms.constants.AuthConstants;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.mapper.GeometryMapper;
import eu.europa.ec.fisheries.uvms.rest.FeatureToGeoJsonJacksonMapper;
import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.rest.security.bean.USMService;
import eu.europa.ec.fisheries.uvms.service.interceptor.ValidationInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.model.area.AreaByCodeJsonPayload;
import eu.europa.ec.fisheries.uvms.spatial.model.area.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.model.constants.USMSpatial;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
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
import eu.europa.ec.fisheries.uvms.spatial.util.ServiceLayerUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

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
    @Interceptors(value = {ExceptionInterceptor.class})
    @Path("/types")
    public Response getAreaTypes() {
        log.info("Getting user areas list");
        List<String> areaTypes = areaTypeService.listAllAreaTypeNames();
        return createSuccessResponse(areaTypes);
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Interceptors(value = {ExceptionInterceptor.class})
    @Path("/location/details")
    public Response getLocationDetails(LocationCoordinateType locationDto) throws ServiceException {
        try {
            LocationTypeEntry locationTypeEntry = mapper.getLocationTypeEntry(locationDto);
            LocationDetails locationDetails = spatialService.getLocationDetails(locationTypeEntry);
            LocationDetailsGeoJsonDto locationDetailsGeoJsonDto = mapper.getLocationDetailsDto(locationDetails);
            if(!locationDto.getIsGeom()) {
                locationDetailsGeoJsonDto.removeGeometry();
                return createSuccessResponse(locationDetailsGeoJsonDto.getProperties());
            }
            StringWriter writer = new StringWriter();
            GeometryMapper.INSTANCE.simpleFeatureToGeoJson(locationDetailsGeoJsonDto.toFeature(), writer);
            return Response.ok(writer.toString()).build();
        } catch (ServiceException | ParseException | IOException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

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

                AreaDetails areaDetails = areaService.getAreaDetailsById(mapper.getAreaTypeEntry(areaDto));
                AreaDetailsGeoJsonDto areaDetailsGeoJsonDto = mapper.getAreaDetailsDto(areaDetails);
                if (!areaDto.getIsGeom()) {
                    areaDetailsGeoJsonDto.removeGeometry();
                    return createSuccessResponse(areaDetailsGeoJsonDto.getProperties());
                }
                GeometryMapper.INSTANCE.simpleFeatureToGeoJson(areaDetailsGeoJsonDto.toFeature(), writer);
                response = Response.ok(writer.toString()).build();

            } else {
                List<AreaDetails> areaDetailsList = spatialService.getAreaDetailsByLocation(mapper.getAreaTypeEntry(areaDto));
                AreaDetailsGeoJsonDto areaDetailsGeoJsonDto = mapper.getAreaDetailsDtoForAllAreas(areaDetailsList, areaDto);
                if (!areaDto.getIsGeom()) {
                    areaDetailsGeoJsonDto.removeGeometryAllAreas();
                    return createSuccessResponse(areaDetailsGeoJsonDto.getAllAreaProperties());
                }
                List<ObjectNode> nodeList = new ArrayList<>();

                for (Map<String, Object> featureMap : areaDetailsGeoJsonDto.getAllAreaProperties()) {
                    ObjectNode convert = new FeatureToGeoJsonJacksonMapper().convert(areaDetailsGeoJsonDto.toFeature(featureMap)); // TODO remove JacksonMapper
                    nodeList.add(convert);
                }
                response = createSuccessResponse(nodeList);
            }
        } catch (ServiceException | ParseException | IOException e) {
            throw new ServiceException(e.getMessage(), e);
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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/type/code")
    @Interceptors(value = { ExceptionInterceptor.class})
    public Response byCode(AreaByCodeJsonPayload payload) throws ServiceException {

        List<AreaSimpleType> request = new ArrayList<>();
        List<AreaType> areaTypeList = payload.getAreaTypes();
        for (AreaType areaType : areaTypeList){
            request.add(new AreaSimpleType(areaType.getAreaType(), areaType.getAreaCode(), null));
        }
        List<AreaSimpleType> response = areaService.byCode(request);
        return createSuccessResponse(response);
    }

}