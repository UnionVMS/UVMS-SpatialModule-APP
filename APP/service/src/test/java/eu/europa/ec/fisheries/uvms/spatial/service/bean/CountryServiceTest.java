package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.ImmutableMap;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Created by padhyad on 11/18/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class CountryServiceTest {

    @Mock
    private SpatialRepository repository;

    @InjectMocks
    private AreaServiceBean countryServiceBean;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCountriesTest() {
        mockCountries();
        Map<String, String> countries = countryServiceBean.getAllCountriesDesc();
        assertNotNull(countries);
        assertFalse(countries.isEmpty());
        Mockito.verify(repository, Mockito.times(1)).findAllCountriesDesc();
    }

    private void mockCountries() {
        Mockito.when(repository.findAllCountriesDesc()).thenReturn(getMockCountries());
    }

    private List<Map<String, String>> getMockCountries() {
        List<Map<String, String>> countries = new ArrayList<Map<String, String>>();
        countries.add(ImmutableMap.<String, String>builder().put("VEN", "Venezuela").build());
        countries.add(ImmutableMap.<String, String>builder().put( "VIR","U.S. Virgin Is.").build());
        countries.add(ImmutableMap.<String, String>builder().put("ZAF", "South Africa").build());
        countries.add(ImmutableMap.<String, String>builder().put("VNM","Vietnam").build());

        return countries;
    }
}
