package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.spatial.entity.CountriesEntity;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

public class CountryDao extends AbstractDAO<CountriesEntity> {

    private EntityManager em;

    public CountryDao(EntityManager em) {
        this.em = em;
    }

    public List<Map<String, String>> findAllCountriesDesc() {
        Query query = em.unwrap(Session.class).getNamedQuery(CountriesEntity.FIND_ALL_COUNTRY_DESC);
        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        return query.list();
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
