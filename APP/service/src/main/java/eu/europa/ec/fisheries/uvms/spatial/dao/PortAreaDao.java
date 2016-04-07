package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.spatial.entity.PortAreasEntity;
import javax.persistence.EntityManager;
import java.util.List;

import static eu.europa.ec.fisheries.uvms.service.QueryParameter.with;
import static eu.europa.ec.fisheries.uvms.spatial.entity.PortAreasEntity.*;

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
        return findEntityByNamedQuery(PortAreasEntity.class, PORT_AREA_BY_COORDINATE, with("shape", shape).parameters());
    }

    /**
     * Retrieves an entity by its id.
     * @throws ServiceException
     */
    public List<PortAreasEntity> findOne(final Long id) throws ServiceException {
        return findEntityByNamedQuery(PortAreasEntity.class, PORT_AREA_BY_ID, with("gid", id).parameters(), 1);
    }
}
