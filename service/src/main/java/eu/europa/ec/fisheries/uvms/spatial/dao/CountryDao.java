package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.spatial.entity.CountryEntity;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class CountryDao extends AbstractDAO<CountryEntity> {

    private EntityManager em;

    public CountryDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<CountryEntity> findEntitiesByNamedQuery(final String queryName) {
        Query query = getEntityManager().createNamedQuery(queryName);
        List<Object[]> resultList = query.getResultList();
        List<CountryEntity> countryEntities = new ArrayList<>();
        for (Object[] obj : resultList) {
            CountryEntity country = new CountryEntity();
            country.setName((String)obj[0]);
            country.setCode((String)obj[1]);
            countryEntities.add(country);
        }
        return countryEntities;
    }
}
