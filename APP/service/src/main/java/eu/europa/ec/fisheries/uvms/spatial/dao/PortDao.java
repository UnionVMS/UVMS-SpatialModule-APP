package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.service.QueryParameter;
import eu.europa.ec.fisheries.uvms.spatial.entity.PortEntity;
import lombok.extern.slf4j.Slf4j;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

import static eu.europa.ec.fisheries.uvms.spatial.entity.PortEntity.*;

@Slf4j
public class PortDao extends AbstractSystemAreaDao<PortEntity> {

    private EntityManager em;

    public PortDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<PortEntity> intersects(final Geometry shape) throws ServiceException {
        return findEntityByNamedQuery(PortEntity.class, PORT_BY_COORDINATE, QueryParameter.with("shape", shape).parameters());
    }

    @Override
    protected String getIntersectNamedQuery() {
        return PORT_BY_COORDINATE;
    }

    @Override
    protected Class<PortEntity> getEntity() {
        return PortEntity.class;
    }

    @Override
    protected PortEntity createEntity(Map<String, Object> values) throws ServiceException {
        return new PortEntity(values);
    }

    @Override
    protected String getDisableAreaNamedQuery() {
        return DISABLE;
    }
}
