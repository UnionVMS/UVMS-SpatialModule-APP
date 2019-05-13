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

import javax.ws.rs.Path;

import static eu.europa.ec.fisheries.uvms.spatial.rest.constants.RestConstants.SERVICE_LAYER_PATH;

/**
 * @implicitParam roleName|string|header|true||||||
 * @implicitParam scopeName|string|header|true|EC|||||
 * @implicitParam authorization|string|header|true||||||jwt token
 */
@Path(SERVICE_LAYER_PATH)
public class ServiceLayerResource extends UnionVMSResource {

    private static final Logger log = LoggerFactory.getLogger(ServiceLayerResource.class);

    //@EJB
    //private USMService usmService;

    //@EJB
    //private ServiceLayerService service;

    //@EJB
    //private AreaTypeNamesService areaTypeService;

    /*
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

     */
/*
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


 */

    /*@PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response updateServiceLayer(ServiceLayer serviceLayer, @PathParam("id") Long id){
        Response response = createSuccessResponse();
        serviceLayer.setId(id);
        try {
            //service.update(serviceLayer);
        } catch (Exception ex) {
            String error = "[ Error when updating resource layer. ] ";
            log.error(error, ex);
            response = createErrorResponse(error);
        }
        return response;
    }*/

}