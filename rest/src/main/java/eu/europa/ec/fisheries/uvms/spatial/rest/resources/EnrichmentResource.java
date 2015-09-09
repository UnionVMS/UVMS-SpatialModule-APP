package eu.europa.ec.fisheries.uvms.spatial.rest.resources;

import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.error.ErrorHandler;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ValidationUtils;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialEnrichmentService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.EnrichmentDto;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Michal Kopyczok on 09-Sep-15.
 */
@Path("/")
@Slf4j
public class EnrichmentResource {

    @EJB
    private SpatialEnrichmentService enrichmentService;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/enrichment")
    public ResponseDto spatialEnrichment(
            @QueryParam(value = "lat") Double lat,
            @QueryParam(value = "lon") Double lon,
            @DefaultValue("4326") @QueryParam(value = "crs") int crs,
            @DefaultValue("Meter") @QueryParam(value = "unit") String unit,
            @QueryParam(value = "locType") List<String> locationTypes,
            @QueryParam(value = "areaType") List<String> areaTypes) {
        try {
            log.info("Getting spatial enrichment");
            validateInputParameters(lat, lon, areaTypes, locationTypes);
            EnrichmentDto enrichment = enrichmentService.getSpatialEnrichment(lat, lon, crs, unit, areaTypes, locationTypes);
            return new ResponseDto(enrichment, ResponseCode.OK);
        } catch (Exception ex) {
            log.error("[ Error when getting spatial enrichment. ] ", ex);
            return ErrorHandler.getFault(ex);
        }
    }

    public void validateInputParameters(Double lat, Double lon, List<String> areaTypes, List<String> locationTypes) {
        ValidationUtils.validateCoordinates(lat, lon);
        ValidationUtils.validateAreaTypes(areaTypes);
        ValidationUtils.validateLocationTypes(locationTypes);
    }

}
