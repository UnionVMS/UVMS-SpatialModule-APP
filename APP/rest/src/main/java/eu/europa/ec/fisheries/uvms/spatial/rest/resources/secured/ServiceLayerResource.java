package eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.spatial.model.layer.ServiceLayer;
import eu.europa.ec.fisheries.uvms.spatial.model.views.Views;
import eu.europa.ec.fisheries.uvms.spatial.rest.constants.RestConstants;
import eu.europa.ec.fisheries.uvms.spatial.rest.constants.View;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.ServiceLayerService;
import lombok.extern.slf4j.Slf4j;
import javax.ejb.EJB;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static eu.europa.ec.fisheries.uvms.spatial.rest.constants.RestConstants.*;

@Path(SERVICE_LAYER_PATH)
@Slf4j
public class ServiceLayerResource extends UnionVMSResource {

    @EJB
    private ServiceLayerService service;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{name}")
    public Response getServiceLayerByName(
            @PathParam(NAME) String name,
            @DefaultValue(RestConstants.DETAIL) @QueryParam(value = VIEW) String view) {

        Response response = createErrorResponse("Service layer not found");

        try {

            final ServiceLayer serviceLayer = service.findBy(name);

            if (serviceLayer != null){

                ObjectMapper mapper = new ObjectMapper();
                mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
                String json;

                switch (View.valueOf(view.toUpperCase())){

                    case DETAIL:
                        json = mapper.writerWithView(Views.Detail.class).writeValueAsString(serviceLayer);
                        break;

                    default:
                        json = mapper.writeValueAsString(serviceLayer);
                }

                response = createSuccessResponse(mapper.readTree(json));

            }

        }

        catch (Exception ex){
            String error = "[ Error when getting service layer. ] ";
            log.error(error, ex);
            response = createErrorResponse(error);
        }

        return response;
    }

}
