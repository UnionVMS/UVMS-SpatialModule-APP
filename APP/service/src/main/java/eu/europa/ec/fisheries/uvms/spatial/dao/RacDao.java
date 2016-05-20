package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.RacEntity;
import java.util.Map;
import javax.persistence.EntityManager;

import static eu.europa.ec.fisheries.uvms.spatial.entity.RacEntity.*;

public class RacDao extends AbstractSpatialDao<RacEntity> {

    private EntityManager em;

    public RacDao(EntityManager em) {
        this.em = em;
    }

    @Override
    protected String getIntersectNamedQuery() {
        return RAC_BY_INTERSECT;
    }

    @Override
    protected Class<RacEntity> getClazz() {
        return RacEntity.class;
    }

    @Override
    protected RacEntity createEntity(Map<String, Object> values) throws ServiceException {
        return new RacEntity(values);
    }

    @Override
    protected String getDisableAreaNamedQuery() {
        return DISABLE_RAC;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
