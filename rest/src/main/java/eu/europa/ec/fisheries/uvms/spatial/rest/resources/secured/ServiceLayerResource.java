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
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.List;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.europa.ec.fisheries.uvms.commons.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.rest.security.bean.USMService;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.rest.constants.RestConstants;
import eu.europa.ec.fisheries.uvms.spatial.rest.constants.View;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.bean.AreaTypeNamesService;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.bean.ServiceLayerService;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto.Views;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto.layer.ServiceLayer;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto.layer.ServiceLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.enums.LayerSubTypeEnum;
import eu.europa.ec.fisheries.uvms.spatial.service.util.ServiceLayerUtils;
import lombok.extern.slf4j.Slf4j;
import static eu.europa.ec.fisheries.uvms.spatial.rest.constants.RestConstants.SERVICE_LAYER_PATH;
import static eu.europa.ec.fisheries.uvms.spatial.rest.constants.RestConstants.SYSTEM_AREA_TYPE;
import static eu.europa.ec.fisheries.uvms.spatial.rest.constants.RestConstants.VIEW;

/**
 * @implicitParam roleName|string|header|true||||||
 * @implicitParam scopeName|string|header|true|EC|||||
 * @implicitParam authorization|string|header|true||||||jwt token
 */
@Path(SERVICE_LAYER_PATH)
@Slf4j
public class ServiceLayerResource extends UnionVMSResource {

    @EJB
    private USMService usmService;

    @EJB
    private ServiceLayerService service;

    @EJB
    private AreaTypeNamesService areaTypeService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{systemAreaType}")
    public Response getServiceLayerBySystemAreaType(
            @PathParam(SYSTEM_AREA_TYPE) String systemAreaType,
            @DefaultValue(RestConstants.PUBLIC) @QueryParam(value = VIEW) String view) {
        Response response = createErrorResponse("Service layer not found");
        try {
            AreaType type = AreaType.valueOf(systemAreaType);
            final ServiceLayer serviceLayer = service.findBy(type);
            if (serviceLayer != null){
                ObjectMapper mapper = new ObjectMapper();
                mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
                String json;
                switch (View.valueOf(view.toUpperCase())){
                    case PUBLIC:
                        json = mapper.writerWithView(Views.Public.class).writeValueAsString(serviceLayer);
                        break;
                    default:
                        json = mapper.writeValueAsString(serviceLayer);
                }
                response = createSuccessResponse(mapper.readTree(json));
            }
        }
        catch (Exception ex){
            String error = "[ Error when getting resource layer. ] ";
            log.error(error, ex);
            response = createErrorResponse(error);
        }
        return response;
    }

    @GET
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/layer/{layerType}")
    public Response getServiceLayersByType(@PathParam("layerType") String layerType, @HeaderParam("scopeName") String scopeName, @HeaderParam("roleName") String roleName, @Context HttpServletRequest servletRequest) throws ServiceException {
        LayerSubTypeEnum layerTypeEnum = LayerSubTypeEnum.value(layerType);
        List<? extends ServiceLayerDto> areaServiceLayerDtos ;

        if (layerTypeEnum.equals(LayerSubTypeEnum.USERAREA) || layerTypeEnum.equals(LayerSubTypeEnum.AREAGROUP)) {
            areaServiceLayerDtos = areaTypeService.getAllAreasLayerDescription(layerTypeEnum, servletRequest.getRemoteUser(), scopeName);
        } else {
            areaServiceLayerDtos = areaTypeService.getAreaLayerDescription(layerTypeEnum);
        }

        //filter those that the user is not allowed to see
        Collection<String> permittedLayersNames = ServiceLayerUtils.getUserPermittedLayersNames(usmService, servletRequest.getRemoteUser(), roleName, scopeName);
        areaServiceLayerDtos.removeIf(serviceLayer -> !permittedLayersNames.contains(serviceLayer.getAreaLocationTypeName()));
        return createSuccessResponse(areaServiceLayerDtos);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response updateServiceLayer(ServiceLayer serviceLayer, @PathParam("id") Long id){
        Response response = createSuccessResponse();
        serviceLayer.setId(id);
        try {
            service.update(serviceLayer);
        } catch (Exception ex) {
            String error = "[ Error when updating resource layer. ] ";
            log.error(error, ex);
            response = createErrorResponse(error);
        }
        return response;
    }

}