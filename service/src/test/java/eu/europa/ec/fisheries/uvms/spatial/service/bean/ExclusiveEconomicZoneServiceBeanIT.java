package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.GetEezSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.GetEezSpatialRS;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import static junit.framework.TestCase.assertNotNull;

/**
 * Created by kopyczmi on 06-Aug-15.
 */
@RunWith(Arquillian.class)
public class ExclusiveEconomicZoneServiceBeanIT extends AbstractArquillianIT {

    @EJB
    private ExclusiveEconomicZoneService exclusiveEconomicZoneService;

    @Test
    public void shouldReturnEez() throws Exception {
        // given
        GetEezSpatialRQ getEezSpatialRQ = new GetEezSpatialRQ("123");

        // when
        GetEezSpatialRS eez = exclusiveEconomicZoneService.getExclusiveEconomicZoneById(getEezSpatialRQ);

        //then
        assertNotNull(eez);
        assertNotNull(eez.getEez());
    }

}
