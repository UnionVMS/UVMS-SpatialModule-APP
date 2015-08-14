package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.dao.CommonGenericDAO;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.EezMapper;
import eu.schemas.GetEezSpatialRS;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 * //TODO create test
 */
@Stateless
@Local(ExclusiveEconomicZoneService.class)
@Transactional(Transactional.TxType.REQUIRED)
public class ExclusiveEconomicZoneServiceBean implements ExclusiveEconomicZoneService {

    private final static Logger LOG = LoggerFactory.getLogger(ExclusiveEconomicZoneServiceBean.class);

    @EJB
    private CommonGenericDAO commonGenericDAO;

    @Inject
    private EezMapper eezMapper;

    @Override
    @SuppressWarnings("unchecked")
    public GetEezSpatialRS getExclusiveEconomicZoneById(int id) {
        EezEntity eez = null;
        try {
            eez = (EezEntity) commonGenericDAO.findEntityById(EezEntity.class, id);
        } catch (HibernateException hex) {
            LOG.debug("HibernateException: ", hex);
            LOG.debug("HibernateException cause: ", hex.getCause());

            return createErrorResponse();
        }
        return createSuccessResponse(eez);
    }

    // TODO Please finish
    private GetEezSpatialRS createErrorResponse() {
        return null;
    }

    private GetEezSpatialRS createSuccessResponse(EezEntity eez) {
        return new GetEezSpatialRS(null, eezMapper.eezEntityToSchema(eez)); //TODO change null
    }
}
