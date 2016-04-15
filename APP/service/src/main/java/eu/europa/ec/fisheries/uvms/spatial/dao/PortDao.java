package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.spatial.entity.PortEntity;

import javax.persistence.EntityManager;

public class PortDao extends AbstractDAO<PortEntity> {

    private EntityManager em;

    public PortDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Integer disable() throws ServiceException {
        return updateEntityByNamedQuery(PortEntity.DISABLE);
    }
}
