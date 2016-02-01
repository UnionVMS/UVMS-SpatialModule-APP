package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

/**
 * Created by padhyad on 11/18/2015.
 */
public class CountryDao extends CommonDao {

    public CountryDao(EntityManager em) {
        super(em);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, String>> findAllCountriesDesc() {
        return createNamedQuery(QueryNameConstants.FIND_ALL_COUNTRY_DESC).list();
    }
}
