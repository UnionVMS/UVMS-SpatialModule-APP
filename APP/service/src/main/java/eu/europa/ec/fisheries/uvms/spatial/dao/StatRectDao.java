package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.StatRectEntity;
import java.util.Map;
import javax.persistence.EntityManager;

import static eu.europa.ec.fisheries.uvms.spatial.entity.StatRectEntity.*;

public class StatRectDao extends AbstractSpatialDao<StatRectEntity> {

    private EntityManager em;

    public StatRectDao(EntityManager em) {
        this.em = em;
    }

    @Override
    protected String getIntersectNamedQuery() {
        return BY_INTERSECT;
    }

    @Override
    protected String getSearchNamedQuery() {
        return SEARCH_STATRECT;
    }

    @Override
    protected Class<StatRectEntity> getClazz() {
        return StatRectEntity.class;
    }

    @Override
    protected StatRectEntity createEntity(Map<String, Object> values) throws ServiceException {
        return new StatRectEntity(values);
    }

    @Override
    protected String getDisableAreaNamedQuery() {
        return DISABLE;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
