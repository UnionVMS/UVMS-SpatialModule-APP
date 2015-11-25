package eu.europa.ec.fisheries.uvms.spatial.repository;

import com.google.common.collect.ImmutableMap;
import eu.europa.ec.fisheries.uvms.spatial.dao.AreaDao;
import eu.europa.ec.fisheries.uvms.spatial.dao.CountryDao;
import eu.europa.ec.fisheries.uvms.spatial.dao.MapConfigDao;
import eu.europa.ec.fisheries.uvms.spatial.dao.ReportConnectSpatialDao;
import eu.europa.ec.fisheries.uvms.spatial.dao.UserAreaDao;
import eu.europa.ec.fisheries.uvms.spatial.entity.ReportConnectSpatialEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.mapper.ReportConnectSpatialMapper;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.MapConfigurationType;
import eu.europa.ec.fisheries.uvms.spatial.util.SqlPropertyHolder;
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
    private SqlPropertyHolder sql;

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
    public void shouldSaveMapConfiguration(){

        MapConfigurationType config = new MapConfigurationType();
        config.setCoordinatesFormat(null);

        ReportConnectSpatialEntity reportConnectSpatialEntity
                = ReportConnectSpatialMapper.INSTANCE.mapConfigurationTypeToReportConnectSpatialEntity(config);
        spatialRepositoryBean.saveMapConfiguration(config);

        Mockito.verify(reportConnectSpatialDao, Mockito.times(1)).createEntity(reportConnectSpatialEntity);

    }

    @Test(expected = IllegalArgumentException.class)
    @SneakyThrows
    public void shouldThrowExceptionWhenSavingMapConfigurationIsNull(){

        spatialRepositoryBean.saveMapConfiguration(null);

    }

    private void mockCountries() {
        Mockito.when(countryDao.findAllCountriesDesc()).thenReturn(getMockCountries());
    }

    private List<Map<String, String>> getMockCountries() {
        List<Map<String, String>> countries = new ArrayList<Map<String, String>>();
        countries.add(ImmutableMap.<String, String>builder().put("VEN", "Venezuela").build());
        countries.add(ImmutableMap.<String, String>builder().put( "VIR","U.S. Virgin Is.").build());
        countries.add(ImmutableMap.<String, String>builder().put("ZAF", "South Africa").build());
        countries.add(ImmutableMap.<String, String>builder().put("VNM", "Vietnam").build());

        return countries;
    }
}
