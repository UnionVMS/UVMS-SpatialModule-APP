package eu.europa.ec.fisheries.uvms.spatial.rest.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.europa.ec.fisheries.uvms.rest.FeatureToGeoJsonMapper;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.error.ErrorHandler;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.EezService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.EezDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/eez")
public class EezResource {

    final static Logger LOG = LoggerFactory.getLogger(EezResource.class);

    @EJB
    private EezService eezService;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("{id}")
    @SuppressWarnings("unchecked")
    public ResponseDto getExclusiveEconomicZoneById(@PathParam("id") int id) throws IOException {
        try {
            LOG.info("Getting eez with {}", id);
            EezDto eezDto = eezService.getEezByIdRest(id);
            String geojson = feature2String(eezDto);
            return new ResponseDto(string2Json(geojson), ResponseCode.OK);
        } catch (Exception e) {
            LOG.error("[ Error when getting eez list. ] ", e);
            return ErrorHandler.getFault(e);
        }
    }

    private String feature2String(EezDto eezDto) {
        return new FeatureToGeoJsonMapper().convert(eezDto.toFeature());
    }

    private JsonNode string2Json(String geojson) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(geojson);
    }
}
