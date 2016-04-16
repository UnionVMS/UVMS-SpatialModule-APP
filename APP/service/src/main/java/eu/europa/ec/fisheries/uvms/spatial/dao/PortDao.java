package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.PortEntity;
import lombok.extern.slf4j.Slf4j;
import javax.persistence.EntityManager;
import java.util.Map;

@Slf4j
public class PortDao extends AbstractAreaDao<PortEntity> {

    private EntityManager em;

    public PortDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    protected PortEntity createEntity(Map<String, Object> values) throws ServiceException {
        return new PortEntity(values);
    }

    @Override
    protected String getDisableAreaNamedQuery() {
        return PortEntity.DISABLE;
    }
}
