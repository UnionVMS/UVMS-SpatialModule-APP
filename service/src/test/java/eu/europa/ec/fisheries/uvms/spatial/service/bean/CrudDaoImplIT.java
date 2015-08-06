package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.dao.CrudDao;
import eu.europa.ec.fisheries.uvms.spatial.entity.CountryEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import static junit.framework.TestCase.assertNotNull;

@RunWith(Arquillian.class)
public class CrudDaoImplIT extends AbstractArquillianIT {

    @EJB
    CrudDao crudDao;

    @Before
    public void beforeTest() {
        assertNotNull("CrudService not injected", crudDao);
    }

    @Test
    public void shouldFindCountry() {
        // when
        CountryEntity country = (CountryEntity) crudDao.find(CountryEntity.class, 1);

        //then
        assertNotNull(country);
    }

    @Test
    public void shouldFindExclusiveEconomicZone() {
        // when
        EezEntity eez = (EezEntity) crudDao.find(EezEntity.class, 1);

        // then
        assertNotNull(eez);
        assertNotNull(eez.getGeom());
    }
}