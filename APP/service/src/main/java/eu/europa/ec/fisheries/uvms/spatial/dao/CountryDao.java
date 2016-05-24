package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.spatial.entity.CountryEntity;
import javax.persistence.EntityManager;

public class CountryDao extends AbstractDAO<CountryEntity> {

    private EntityManager em;

    public CountryDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
