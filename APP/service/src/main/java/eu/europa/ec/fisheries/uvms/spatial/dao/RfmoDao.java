package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.service.QueryParameter;
import eu.europa.ec.fisheries.uvms.spatial.entity.RfmoEntity;
import lombok.extern.slf4j.Slf4j;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

@Slf4j
public class RfmoDao extends AbstractAreaDao<RfmoEntity> {

    private EntityManager em;

    public RfmoDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<RfmoEntity> intersects(final Geometry shape) throws ServiceException {
        return findEntityByNamedQuery(RfmoEntity.class, RfmoEntity.RFMO_BY_COORDINATE, QueryParameter.with("shape", shape).parameters());
    }

    @Override
    protected RfmoEntity createEntity(Map<String, Object> values) throws ServiceException {
        return new RfmoEntity(values);
    }

    @Override
    protected String getDisableAreaNamedQuery() {
        return RfmoEntity.DISABLE_RFMO_AREAS;
    }
}
