package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.service.QueryParameter;
import eu.europa.ec.fisheries.uvms.spatial.entity.RfmoEntity;
import lombok.extern.slf4j.Slf4j;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

import static eu.europa.ec.fisheries.uvms.spatial.entity.RfmoEntity.*;

@Slf4j
public class RfmoDao extends AbstractSystemAreaDao<RfmoEntity> {

    private EntityManager em;

    public RfmoDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<RfmoEntity> intersects(final Geometry shape) throws ServiceException {
        return findEntityByNamedQuery(RfmoEntity.class, RFMO_BY_COORDINATE, QueryParameter.with("shape", shape).parameters());
    }

    @Override
    protected String getIntersectNamedQuery() {
        return RFMO_BY_COORDINATE;
    }

    @Override
    protected Class<RfmoEntity> getEntity() {
        return RfmoEntity.class;
    }

    @Override
    protected RfmoEntity createEntity(Map<String, Object> values) throws ServiceException {
        return new RfmoEntity(values);
    }

    @Override
    protected String getDisableAreaNamedQuery() {
        return DISABLE_RFMO_AREAS;
    }
}
