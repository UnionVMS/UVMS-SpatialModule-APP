package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.schema.spatial.source.GetEezSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.dao.CrudDao;
import eu.europa.ec.fisheries.uvms.spatial.dao.CrudDaoImpl;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.EezMapper;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;

/**
 * //TODO create test
 */
@Stateless
@Local(ExclusiveEconomicZoneService.class)
public class ExclusiveEconomicZoneServiceBean implements ExclusiveEconomicZoneService {

    @EJB
    private CrudDao crudDao;

    @Override
    public GetEezSpatialRS getExclusiveEconomicZoneById(long id) {
        EezEntity eez = (EezEntity) crudDao.find(EezEntity.class, id);
        return createResponse(eez);
    }

    // TODO It is better to let the container (EJB) to manage the lifecycle of objects than manually create objects
    // TODO It is very difficult to write test independent of the Mapper when you can not mock it. As far as we are using EJB Injection in other places we should try to Inject as many components to our class as possible.
    // TODO http://mapstruct.org/documentation/#section-retr-mapper => 4.2 Using dependency injection
    private GetEezSpatialRS createResponse(EezEntity eez) {
        GetEezSpatialRS response = new GetEezSpatialRS();
        if (eez != null) {
            response.setEez(EezMapper.INSTANCE.eezEntityToSchema(eez));
        }
        return response;
    }
}
