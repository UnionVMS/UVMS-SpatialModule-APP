package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.service.SpatialRepository;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by padhyad on 11/18/2015.
 */
@Stateless
@Local(CountryService.class)
@Transactional
public class CountryServiceBean implements CountryService {

    private static final String CODE = "code";

    private static final String NAME = "name";

    @EJB
    private SpatialRepository repository;

    @Override
    public Map<String, String> getAllCountriesDesc() {
        Map<String, String> countries = new HashMap<String, String>();
        List<Map<String, String>> countryList = repository.findAllCountriesDesc();
        for (Map<String, String> country : countryList) {
            countries.put(country.get(CODE), country.get(NAME));
        }
        return countries;
    }
}
