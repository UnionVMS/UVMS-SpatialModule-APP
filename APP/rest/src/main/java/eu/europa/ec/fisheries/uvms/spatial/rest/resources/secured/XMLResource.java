package eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.FilterAreasSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.FilterAreasSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialEnrichmentRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialEnrichmentRS;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.FilterAreasService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialEnrichmentService;
import lombok.extern.slf4j.Slf4j;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * This class is a resource to call services exposed tru the JMS channel
 */
@Path("/xml")
@Slf4j
public class XMLResource {

    private @EJB SpatialEnrichmentService enrichmentService;
    private @EJB FilterAreasService filterAreasService;

    @POST
    @Produces(value = {MediaType.APPLICATION_XML})
    @Consumes(value = {MediaType.APPLICATION_XML})
    @Path("/enrichment")
    public SpatialEnrichmentRS spatialEnrichment(SpatialEnrichmentRQ request) throws ServiceException {

        return enrichmentService.getSpatialEnrichment(request);

    }


    @POST
    @Produces(value = {MediaType.APPLICATION_XML})
    @Consumes(value = {MediaType.APPLICATION_XML})
    @Path("/filter-areas")
    public FilterAreasSpatialRS filterAreas(FilterAreasSpatialRQ request){

        return  filterAreasService.filterAreas(request);

    }

}
