package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.schema.spatial.source.GetEezSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.dao.CommonGenericDAO;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.EezMapper;

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
    private EezMapper eezMapper;

    @Override
    @SuppressWarnings("unchecked")
    public GetEezSpatialRS getExclusiveEconomicZoneById(int id) {
        EezEntity eez = (EezEntity) commonGenericDAO.findEntityById(EezEntity.class, id);
        return createResponse(eez);
    }

    private GetEezSpatialRS createResponse(EezEntity eez) {
        return new GetEezSpatialRS(eezMapper.eezEntityToSchema(eez));
    }
}
