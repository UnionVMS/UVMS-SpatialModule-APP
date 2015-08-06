package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.dao.CrudDao;
import eu.europa.ec.fisheries.uvms.spatial.entity.CountryEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialService;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.mock.Mock;

import static org.junit.Assert.assertEquals;

public class SpatialServiceBeanTest extends UnitilsJUnit4 {

    @TestedObject
    private SpatialService spatialService;

    @InjectIntoByType
    private Mock<CrudDao> crudServiceMock;

    private CountryEntity country;

    @Before
    public void beforeTest() {
        spatialService = new SpatialServiceBean();
        country = createCountry();
    }

    @Test
    public void shouldGetCountryById() {
        crudServiceMock.returns(country).find(CountryEntity.class, 1);
        CountryEntity countryById = spatialService.getCountryById(1);
        assertEquals(countryById, countryById);
    }

    private CountryEntity createCountry() {
        CountryEntity country = new CountryEntity();
        country.setId(1);
        country.setSovereign("Sovereign");
        return country;
    }
}
