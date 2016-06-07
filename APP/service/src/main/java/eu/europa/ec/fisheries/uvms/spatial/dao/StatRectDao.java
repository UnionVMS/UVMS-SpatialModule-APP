package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.StatRectEntity;

import eu.europa.ec.fisheries.uvms.spatial.model.upload.UploadMappingProperty;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import java.util.Map;

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
    protected String getSearchNameByCodeQuery() {
        return SEARCH_STATRECT_NAMES_BY_CODE;
    }

    @Override
    protected Class<StatRectEntity> getClazz() {
        return StatRectEntity.class;
    }

    @Override
    protected StatRectEntity createEntity(Map<String, Object> values, List<UploadMappingProperty> mapping) throws ServiceException {
        return new StatRectEntity(values, mapping);
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
