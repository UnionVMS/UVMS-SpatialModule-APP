package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.PortEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.upload.UploadMappingProperty;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import javax.persistence.EntityManager;
import java.util.Map;

import static eu.europa.ec.fisheries.uvms.spatial.entity.PortEntity.*;

@Slf4j
public class PortDao extends AbstractSpatialDao<PortEntity> {

    private EntityManager em;

    public PortDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    protected String getIntersectNamedQuery() {
        return PORT_BY_COORDINATE;
    }

    @Override
    protected String getSearchNamedQuery() {
        return SEARCH_PORT;
    }

    @Override
    protected Class<PortEntity> getClazz() {
        return PortEntity.class;
    }

    @Override
    protected PortEntity createEntity(Map<String, Object> values, List<UploadMappingProperty> mapping) throws ServiceException {
        return new PortEntity(values, mapping);
    }

    @Override
    protected String getDisableAreaNamedQuery() {
        return DISABLE;
    }
}
