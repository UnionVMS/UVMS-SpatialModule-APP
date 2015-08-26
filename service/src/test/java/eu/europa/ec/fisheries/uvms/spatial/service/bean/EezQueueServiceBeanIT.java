package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezSpatialRS;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import static junit.framework.TestCase.assertNotNull;

/**
 * Created by kopyczmi on 06-Aug-15.
 */
@RunWith(Arquillian.class)
public class EezQueueServiceBeanIT extends AbstractArquillianIT {

    @EJB
    private EezService service;

    @Test
    public void shouldReturnEez() throws Exception {
        // given
        EezSpatialRQ getEezSpatialRQ = new EezSpatialRQ("123");

        // when
        EezSpatialRS eez = service.getEezByIdQueue(getEezSpatialRQ);

        //then
        assertNotNull(eez);
        assertNotNull(eez.getEez());
    }

}
