package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.BaseSpatialEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.FmzEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.upload.UploadMappingProperty;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;

import static eu.europa.ec.fisheries.uvms.spatial.entity.FmzEntity.*;

public class FmzDao extends AbstractSpatialDao<FmzEntity> {

    private EntityManager em;

    public FmzDao(EntityManager em) {
        this.em = em;
    }

    @Override
    protected String getIntersectNamedQuery() {
        return BY_INTERSECT;
    }

    @Override
    protected String getSearchNamedQuery() {
        return SEARCH_FMZ;
    }

    @Override
    protected Class<FmzEntity> getClazz() {
        return FmzEntity.class;
    }

    @Override
    protected BaseSpatialEntity createEntity(Map<String, Object> values, List<UploadMappingProperty> mapping) throws ServiceException {
        return new FmzEntity(values, mapping);
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
