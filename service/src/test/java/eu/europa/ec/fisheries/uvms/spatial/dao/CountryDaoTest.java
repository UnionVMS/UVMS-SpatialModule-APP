package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.spatial.util.CountryFactory;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CountryDaoTest extends BaseSpatialDaoTest {

    private CountryDao dao = new CountryDao(em);

    @Before
    @SneakyThrows
    public void prepare(){

        EntityTransaction t = em.getTransaction();
        t.begin();
        em.persist(CountryFactory.getCountry("Austria"));
        em.persist(CountryFactory.getCountry("Belgium"));

        em.flush();
        t.commit();

    }

    @Test
    @SneakyThrows
    public void shouldReturnAllCountries() {

        List<Map<String, String>> allCountriesDesc = dao.findAllCountriesDesc();

        assertEquals("Austria", allCountriesDesc.get(0).get("name"));
        assertEquals("AUT", allCountriesDesc.get(0).get("code"));

        assertEquals("Belgium", allCountriesDesc.get(1).get("name"));
        assertEquals("BEL", allCountriesDesc.get(1).get("code"));

    }
}
