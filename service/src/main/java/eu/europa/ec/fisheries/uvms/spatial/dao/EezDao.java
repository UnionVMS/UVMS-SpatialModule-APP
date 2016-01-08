package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;

import javax.persistence.EntityManager;

/**
 * //TODO create test
 */
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
}
