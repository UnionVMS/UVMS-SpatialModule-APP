package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.FaoEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.upload.UploadMappingProperty;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;

import static eu.europa.ec.fisheries.uvms.spatial.entity.FaoEntity.*;

public class FaoDao extends AbstractSpatialDao<FaoEntity> {

    private EntityManager em;

    public FaoDao(EntityManager em) {
        this.em = em;
    }

    @Override
    protected String getIntersectNamedQuery() {
        return FAO_BY_INTERSECT;
    }

    @Override
    protected String getSearchNamedQuery() {
        return SEARCH_FAO;
    }

    @Override
    protected Class<FaoEntity> getClazz() {
        return FaoEntity.class;
    }

    @Override
    protected FaoEntity createEntity(Map<String, Object> values, List<UploadMappingProperty> mapping) throws ServiceException {
        return new FaoEntity(values, mapping);
    }

    @Override
    protected String getDisableAreaNamedQuery() {
        return DISABLE_FAO_AREAS;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
