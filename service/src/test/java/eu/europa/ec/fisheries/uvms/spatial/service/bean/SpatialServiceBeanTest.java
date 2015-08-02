package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.entity.Country;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.mock.Mock;

import static org.junit.Assert.assertEquals;

/**
 * //TODO create test
 */
public class SpatialServiceBeanTest extends UnitilsJUnit4 {

    @TestedObject
    private SpatialService spatialService;

    @InjectIntoByType
    private Mock<CrudService> crudServiceMock;

    private Country country;

    @Before
    public void beforeTest(){

        spatialService = new SpatialServiceBean();

        country = new Country();
        country.setId(1);
        country.setSovereign("Sovereign");
    }

    @Test
    public void testGetCountryById(){

        crudServiceMock.returns(country).find(Country.class, 1);
        Country countryById = spatialService.getCountryById(1);
        assertEquals(countryById, countryById);
    }
}
