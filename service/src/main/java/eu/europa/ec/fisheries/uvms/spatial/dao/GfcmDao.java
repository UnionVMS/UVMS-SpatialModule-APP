package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.GfcmEntity;
import java.util.Map;
import javax.persistence.EntityManager;

import static eu.europa.ec.fisheries.uvms.spatial.entity.GfcmEntity.*;

public class GfcmDao extends AbstractSpatialDao<GfcmEntity> {

    private EntityManager em;

    public GfcmDao(EntityManager em) {
        this.em = em;
    }

    @Override
    protected String getIntersectNamedQuery() {
        return GFMC_BY_INTERSECT;
    }

    @Override
    protected String getSearchNamedQuery() {
        return SEARCH_GFCM;
    }

    @Override
    protected Class<GfcmEntity> getClazz() {
        return GfcmEntity.class;
    }

    @Override
    protected GfcmEntity createEntity(Map<String, Object> values) throws ServiceException {
        return new GfcmEntity(values);
    }

    @Override
    protected String getDisableAreaNamedQuery() {
        return DISABLE_GFMC_AREAS;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
