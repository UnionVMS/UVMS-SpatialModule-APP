package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.GfcmEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.upload.UploadMappingProperty;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import java.util.Map;

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
    protected String getSearchNameByCodeQuery() {
        return SEARCH_GFCM_NAMES_BY_CODE;
    }

    @Override
    protected Class<GfcmEntity> getClazz() {
        return GfcmEntity.class;
    }

    @Override
    protected GfcmEntity createEntity(Map<String, Object> values, List<UploadMappingProperty> mapping) throws ServiceException {
        return new GfcmEntity(values, mapping);
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
