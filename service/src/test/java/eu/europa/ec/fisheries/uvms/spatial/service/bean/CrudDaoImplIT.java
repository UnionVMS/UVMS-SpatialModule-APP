package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.dao.CrudDao;
import eu.europa.ec.fisheries.uvms.spatial.entity.Country;
import eu.europa.ec.fisheries.uvms.spatial.entity.ExclusiveEconomicZone;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import static junit.framework.TestCase.assertNotNull;

@RunWith(Arquillian.class)
public class CrudDaoImplIT extends AbstractArquillianTest { // TODO make maven not read this test by renaming to CrudServiceBeanIT

    @EJB
    CrudDao crudDao;

    @Before
    public void beforeTest() {
        assertNotNull("CrudService not injected", crudDao);
    }

    @Test
    public void shouldFindCountry() {
        // when
        Country country = (Country) crudDao.find(Country.class, 1);

        //then
        assertNotNull(country);
    }

    @Test
    public void shouldFindExclusiveEconomicZone() {
        // when
        ExclusiveEconomicZone eez = (ExclusiveEconomicZone) crudDao.find(ExclusiveEconomicZone.class, 1);

        // then
        assertNotNull(eez);
        assertNotNull(eez.getGeometry());
    }
}