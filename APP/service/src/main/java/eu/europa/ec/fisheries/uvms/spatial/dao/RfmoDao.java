package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.service.QueryParameter;
import eu.europa.ec.fisheries.uvms.spatial.entity.RfmoEntity;

import javax.persistence.EntityManager;
import java.util.List;

public class RfmoDao extends AbstractDAO<RfmoEntity> {

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
}
