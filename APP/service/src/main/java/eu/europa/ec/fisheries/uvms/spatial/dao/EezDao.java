package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.service.QueryParameter;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import javax.persistence.EntityManager;
import java.util.List;

public class EezDao extends AbstractDAO<EezEntity> {

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
}
