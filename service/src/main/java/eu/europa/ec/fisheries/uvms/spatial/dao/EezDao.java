package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.service.QueryParameter;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import lombok.extern.slf4j.Slf4j;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

@Slf4j
public class EezDao extends AbstractAreaDao<EezEntity> {

    private EntityManager em;

    public EezDao(EntityManager em) {
        this.em = em;
    }

    public EezEntity getEezById(final Long id) throws ServiceException {
        return findEntityById(EezEntity.class, id);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<EezEntity> intersects(final Geometry shape) throws ServiceException {
        return findEntityByNamedQuery(EezEntity.class, EezEntity.EEZ_BY_COORDINATE, QueryParameter.with("shape", shape).parameters());
    }

    public List<EezEntity> listEmptyGeometries() throws ServiceException {
        return findEntityByNamedQuery(EezEntity.class, EezEntity.LIST_EMPTY_GEOMETRIES);
    }

    @Override
    protected EezEntity createEntity(Map<String, Object> values) throws ServiceException {
        return new EezEntity(values);
    }

    @Override
    protected String getDisableAreaNamedQuery() {
        return EezEntity.DISABLE_EEZ_AREAS;
    }
}
