package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Created by padhyad on 11/18/2015.
 */
@RunWith(Arquillian.class)
public class CountryServiceIT extends AbstractArquillianIT {

    @EJB
    private CountryService countryService;

    @Test
    public void TestGetAllCountries() {
        List<Map<String, String>> allCountries =  countryService.getAllCountriesDesc();
        assertNotNull(allCountries);
        assertFalse(allCountries.isEmpty());
    }
}
