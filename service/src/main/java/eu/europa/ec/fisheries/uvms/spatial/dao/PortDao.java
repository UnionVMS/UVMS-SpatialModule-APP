package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.spatial.entity.PortsEntity;

import javax.persistence.EntityManager;

public class PortDao extends AbstractDAO<PortsEntity> {

    private EntityManager em;

    public PortDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Integer disable() throws ServiceException {
        return updateEntityByNamedQuery(PortsEntity.DISABLE_PORT_LOCATIONS);
    }
}
