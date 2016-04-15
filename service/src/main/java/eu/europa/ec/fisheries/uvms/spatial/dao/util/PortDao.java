package eu.europa.ec.fisheries.uvms.spatial.dao.util;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.service.QueryParameter;
import eu.europa.ec.fisheries.uvms.spatial.entity.PortEntity;

import javax.persistence.EntityManager;
import java.util.List;

public class PortDao extends AbstractDAO<PortEntity> {

    private EntityManager em;

    public PortDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<PortEntity> intersects(final Geometry shape) throws ServiceException {

        return findEntityByNamedQuery(PortEntity.class, PortEntity.PORT_BY_COORDINATE, QueryParameter.with("shape", shape).parameters());
    }
}
