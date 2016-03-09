package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.service.QueryParameter;
import eu.europa.ec.fisheries.uvms.spatial.entity.PortAreasEntity;
import javax.persistence.EntityManager;
import java.util.List;

public class PortAreaDao extends AbstractDAO<PortAreasEntity> {

    private EntityManager em;

    public PortAreaDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<PortAreasEntity> intersects(final Geometry shape) throws ServiceException {
        return findEntityByNamedQuery(PortAreasEntity.class, PortAreasEntity.PORTAREA_BY_COORDINATE, QueryParameter.with("shape", shape).parameters());
    }
}
