package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.schema.spatial.source.GetEezSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.dao.CrudDao;
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

    private GetEezSpatialRS createResponse(EezEntity eez) {
        GetEezSpatialRS response = new GetEezSpatialRS();
        if (eez != null) {
            response.setEez(EezMapper.INSTANCE.eezEntityToSchema(eez));
        }
        return response;
    }
}
