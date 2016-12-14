/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.rest.security.bean.USMService;
import eu.europa.ec.fisheries.uvms.spatial.model.constants.USMSpatial;
import eu.europa.ec.fisheries.uvms.spatial.model.layer.ServiceLayer;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.model.views.Views;
import eu.europa.ec.fisheries.uvms.spatial.rest.constants.RestConstants;
import eu.europa.ec.fisheries.uvms.spatial.rest.constants.View;
import eu.europa.ec.fisheries.uvms.spatial.service.AreaTypeNamesService;
import eu.europa.ec.fisheries.uvms.spatial.service.ServiceLayerService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.LayerSubTypeEnum;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.ServiceLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.util.ServiceLayerUtils;
import lombok.extern.slf4j.Slf4j;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static eu.europa.ec.fisheries.uvms.spatial.rest.constants.RestConstants.*;

@Path(SERVICE_LAYER_PATH)
@Slf4j
public class ServiceLayerResource extends UnionVMSResource {

    private @EJB USMService usmService;
    private @EJB ServiceLayerService service;
    private @EJB AreaTypeNamesService areaTypeService;

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
    public Response getServiceLayersByType(@PathParam("layerType") String layerType, @HeaderParam(USMSpatial.SCOPE_NAME) String scopeName, @HeaderParam(USMSpatial.ROLE_NAME) String roleName,@Context HttpServletRequest request) throws ServiceException {
        LayerSubTypeEnum layerTypeEnum = LayerSubTypeEnum.value(layerType);
        List<? extends ServiceLayerDto> areaServiceLayerDtos ;

        if (layerTypeEnum.equals(LayerSubTypeEnum.USERAREA) || layerTypeEnum.equals(LayerSubTypeEnum.AREAGROUP)) {
            areaServiceLayerDtos = areaTypeService.getAllAreasLayerDescription(layerTypeEnum, request.getRemoteUser(), scopeName);
        } else {
            areaServiceLayerDtos = areaTypeService.getAreaLayerDescription(layerTypeEnum);
        }

        //filter those that the user is not allowed to see
        Collection<String> permittedLayersNames = ServiceLayerUtils.getUserPermittedLayersNames(usmService, request.getRemoteUser(), roleName, scopeName);
        Iterator<? extends ServiceLayerDto> iterator = areaServiceLayerDtos.iterator();
        while (iterator.hasNext()) {
            ServiceLayerDto serviceLayer = iterator.next();

            if (!permittedLayersNames.contains(serviceLayer.getAreaLocationTypeName())) {
                iterator.remove();
            }
        }
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