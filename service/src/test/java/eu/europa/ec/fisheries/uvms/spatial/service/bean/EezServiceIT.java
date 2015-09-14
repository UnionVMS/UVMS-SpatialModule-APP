package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezType;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import static junit.framework.TestCase.assertNotNull;

@RunWith(Arquillian.class)
public class EezServiceIT extends AbstractArquillianIT {

    @EJB
    private EezService service;

    @Test
    public void testGetEezById() throws Exception {
        EezSpatialRQ eezSpatialRQ = new EezSpatialRQ();
        eezSpatialRQ.setEezId("123");
        EezType eez = service.getEezById(eezSpatialRQ);
        assertNotNull(eez);
        assertNotNull(eez.getEez());
    }

}
