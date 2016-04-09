package eu.europa.ec.fisheries.uvms.spatial.repository;

import com.google.common.collect.ImmutableMap;
import eu.europa.ec.fisheries.uvms.spatial.dao.*;
import eu.europa.ec.fisheries.uvms.spatial.entity.ReportConnectSpatialEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialRepositoryBean;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class SpatialRepositoryTest {

    @Mock
    private EntityManager em;

    @Mock
    private AreaDao areaDao;

    @Mock
    private UserAreaDao userAreaDao;

    @Mock
    private CountryDao countryDao;

    @Mock
    private MapConfigDao mapConfigDao;

    @Mock
    private ReportConnectSpatialDao reportConnectSpatialDao;

    @InjectMocks
    private SpatialRepositoryBean spatialRepositoryBean;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCountriesTest() {
        mockCountries();
        List<Map<String, String>> countries = spatialRepositoryBean.findAllCountriesDesc();
        assertNotNull(countries);
        assertFalse(countries.isEmpty());
        Mockito.verify(countryDao, Mockito.times(1)).findAllCountriesDesc();
    }

    @Test
    @SneakyThrows
    public void shouldSaveMapConfiguration() {
        ReportConnectSpatialEntity reportConnectSpatialEntity = new ReportConnectSpatialEntity();
        reportConnectSpatialEntity.setDisplayFormatType(null);

        spatialRepositoryBean.saveOrUpdateMapConfiguration(reportConnectSpatialEntity);

        Mockito.verify(reportConnectSpatialDao, Mockito.times(1)).saveOrUpdateEntity(reportConnectSpatialEntity);
    }

    @Test(expected = IllegalArgumentException.class)
    @SneakyThrows
    public void shouldThrowExceptionWhenSavingMapConfigurationIsNull() {
        spatialRepositoryBean.saveOrUpdateMapConfiguration(null);

    }

    private void mockCountries() {
        Mockito.when(countryDao.findAllCountriesDesc()).thenReturn(getMockCountries());
    }

    private List<Map<String, String>> getMockCountries() {
        List<Map<String, String>> countries = new ArrayList<Map<String, String>>();
        countries.add(ImmutableMap.<String, String>builder().put("VEN", "Venezuela").build());
        countries.add(ImmutableMap.<String, String>builder().put("VIR", "U.S. Virgin Is.").build());
        countries.add(ImmutableMap.<String, String>builder().put("ZAF", "South Africa").build());
        countries.add(ImmutableMap.<String, String>builder().put("VNM", "Vietnam").build());

        return countries;
    }
}
