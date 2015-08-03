package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.entity.Country;
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
    private Mock<CrudService> crudServiceMock;

    private Country country;

    @Before
    public void beforeTest() {
        spatialService = new SpatialServiceBean();
        country = createCountry();
    }

    private Country createCountry() {
        Country country = new Country();
        country.setId(1);
        country.setSovereign("Sovereign");
        return country;
    }

    @Test
    public void shouldGetCountryById() {
        // given
        crudServiceMock.returns(country).find(Country.class, 1);

        // when
        Country countryById = spatialService.getCountryById(1);

        //then
        assertEquals(countryById, countryById);
    }
}
