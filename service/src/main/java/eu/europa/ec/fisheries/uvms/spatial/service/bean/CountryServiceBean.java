package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.repository.SpatialRepository;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * Created by padhyad on 11/18/2015.
 */
@Stateless
@Local(CountryService.class)
@Transactional
public class CountryServiceBean implements CountryService {

    @EJB
    private SpatialRepository repository;

    @Override
    public List<Map<String, String>> getAllCountriesDesc() {
        return repository.findAllCountriesDesc();
    }
}
