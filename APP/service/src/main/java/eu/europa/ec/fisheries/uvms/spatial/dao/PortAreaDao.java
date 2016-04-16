package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.PortAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.PortEntity;
import lombok.extern.slf4j.Slf4j;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

import static eu.europa.ec.fisheries.uvms.service.QueryParameter.with;
import static eu.europa.ec.fisheries.uvms.spatial.entity.PortAreasEntity.*;

@Slf4j
public class PortAreaDao extends AbstractAreaDao<PortAreasEntity> {

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

    @Override
    protected PortAreasEntity createEntity(Map<String, Object> values) throws ServiceException {
        return new PortAreasEntity(values);
    }

    @Override
    protected String getDisableAreaNamedQuery() {
        return PortEntity.DISABLE;
    }

    public List<PortAreasEntity> findOne(final Long id) throws ServiceException {
        return findEntityByNamedQuery(PortAreasEntity.class, PORT_AREA_BY_ID, with("gid", id).parameters(), 1);
    }

}
