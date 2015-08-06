package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.dao.CrudDao;
import eu.europa.ec.fisheries.uvms.spatial.entity.CountryEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.CountryDto;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.mock.Mock;

import static org.junit.Assert.assertEquals;

public class CountryServiceBeanTest extends UnitilsJUnit4 {

    @TestedObject
    private CountryService countryService;

    @InjectIntoByType
    private Mock<CrudDao> crudServiceMock;

    private CountryEntity country;

    @Before
    public void beforeTest() {
        countryService = new CountryServiceBean();
        country = createCountry();
    }

    @Test
    public void shouldGetCountryById() {
        crudServiceMock.returns(country).find(CountryEntity.class, 1);
        CountryDto countryById = countryService.getCountryById(1);
        assertEquals(countryById, countryById);
    }

    private CountryEntity createCountry() {
        CountryEntity country = new CountryEntity();
        country.setId(1);
        return country;
    }
}
