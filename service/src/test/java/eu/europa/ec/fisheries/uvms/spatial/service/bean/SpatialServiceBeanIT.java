package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.schema.spatial.source.GetEezSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialService;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import static junit.framework.TestCase.assertNotNull;

/**
 * Created by kopyczmi on 06-Aug-15.
 */
@RunWith(Arquillian.class)
public class SpatialServiceBeanIT extends AbstractArquillianIT {

    @EJB
    private SpatialService spatialService;

    @Test
    public void shouldReturnEez() throws Exception {
        // when
        GetEezSpatialRS eez = spatialService.getExclusiveEconomicZoneById(23);

        //then
        assertNotNull(eez);
        assertNotNull(eez.getEez());
    }

}
