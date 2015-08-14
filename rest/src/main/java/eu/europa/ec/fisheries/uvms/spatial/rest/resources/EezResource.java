package eu.europa.ec.fisheries.uvms.spatial.rest.resources;

import eu.europa.ec.fisheries.uvms.spatial.rest.dto.EezDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.mapper.EezDtoMapper;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.ExclusiveEconomicZoneService;
import eu.schemas.GetEezSpatialRS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/eez")
public class EezResource {

    final static Logger LOG = LoggerFactory.getLogger(EezResource.class);

    @EJB
    private ExclusiveEconomicZoneService exclusiveEconomicZoneService;

    @Inject
    private EezDtoMapper eezDtoMapper;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/{id}")
    @SuppressWarnings("unchecked")
    public ResponseDto getExclusiveEconomicZoneById(@PathParam("id") int eezId) {
        try {
            LOG.info("Getting eez with {}", eezId);
            GetEezSpatialRS eez = exclusiveEconomicZoneService.getExclusiveEconomicZoneById(eezId);
            EezDto eezDto = eezDtoMapper.eezSchemaToDto(eez.getEez());
            return new ResponseDto(eezDto, ResponseCode.OK);
        } catch (Exception ex) {
            if (LOG.isDebugEnabled()) {
                LOG.error("[ Error when getting eez with id " + eezId + ". ] ", ex);
            }
            return new ResponseDto(ex.getMessage(), ResponseCode.ERROR);
        }
    }
}
