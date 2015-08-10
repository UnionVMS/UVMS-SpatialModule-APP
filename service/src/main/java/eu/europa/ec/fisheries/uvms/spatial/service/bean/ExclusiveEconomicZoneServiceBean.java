package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.schema.spatial.source.GetEezSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.dao.CommonGenericDAO;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.AreaResultTypeMapper;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * //TODO create test
 */
@Stateless
@Local(ExclusiveEconomicZoneService.class)
public class ExclusiveEconomicZoneServiceBean implements ExclusiveEconomicZoneService {

    @EJB
    private CommonGenericDAO commonGenericDAO;

    @Inject
    private AreaResultTypeMapper eezMapper;

    @Override
    @SuppressWarnings("unchecked")
    public GetEezSpatialRS getExclusiveEconomicZoneById(long id) {
        EezEntity eez = null;
        try {
            eez = (EezEntity) commonGenericDAO.findEntityById(EezEntity.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return createResponse(eez);
    }

    private GetEezSpatialRS createResponse(EezEntity eez) {
        GetEezSpatialRS response = new GetEezSpatialRS();
        if (eez != null) {
            response.setEez(eezMapper.eezEntityToAreaResultType(eez));
        }
        return response;
    }
}
