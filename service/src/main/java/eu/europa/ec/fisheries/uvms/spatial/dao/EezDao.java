package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import lombok.extern.slf4j.Slf4j;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

import static eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity.*;

@Slf4j
public class EezDao extends AbstractSpatialDao<EezEntity> {

    private EntityManager em;

    public EezDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<EezEntity> listEmptyGeometries() throws ServiceException {
        return findEntityByNamedQuery(EezEntity.class, LIST_EMPTY_GEOMETRIES);
    }

    @Override
    protected String getIntersectNamedQuery() {
        return EEZ_BY_COORDINATE;
    }

    @Override
    protected Class<EezEntity> getClazz() {
        return EezEntity.class;
    }

    @Override
    protected EezEntity createEntity(Map<String, Object> values) throws ServiceException {
        return new EezEntity(values);
    }

    @Override
    protected String getDisableAreaNamedQuery() {
        return DISABLE_EEZ_AREAS;
    }

}
