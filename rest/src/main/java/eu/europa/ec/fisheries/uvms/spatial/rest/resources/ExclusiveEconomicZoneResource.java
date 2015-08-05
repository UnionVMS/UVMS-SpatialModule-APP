package eu.europa.ec.fisheries.uvms.spatial.rest.resources;

import eu.europa.ec.fisheries.uvms.spatial.entity.ExclusiveEconomicZone;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.spatial.dao.CrudDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * //TODO create test
 */
@Path("/eez")
@Stateless
public class ExclusiveEconomicZoneResource {

    final static Logger LOG = LoggerFactory.getLogger(ExclusiveEconomicZoneResource.class);

    @EJB
    private CrudDao crudDao;

    @POST
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("getEezById")
    public ResponseDto getExclusiveEconomicZoneById(int eezId) {
        try {
            LOG.info("Getting user areas list");
            return new ResponseDto(crudDao.find(ExclusiveEconomicZone.class, eezId), ResponseCode.OK);
        } catch (Exception e) {
            LOG.error("[ Error when getting vessel list. ] ", e);// TODO veesel list?
            throw new RuntimeException("Please fix it");
        }
    }
}
