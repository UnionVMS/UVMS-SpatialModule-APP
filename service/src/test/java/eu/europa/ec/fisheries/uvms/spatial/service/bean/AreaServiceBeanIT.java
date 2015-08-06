package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.schema.spatial.source.GetAreaTypesSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.service.AreaService;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertFalse;

/**
 * Created by kopyczmi on 04-Aug-15.
 */
@RunWith(Arquillian.class)
public class AreaServiceBeanIT extends AbstractArquillianIT {

    @EJB
    private AreaService areaService;

    @Test
    public void shouldReturnAreaTypes() throws Exception {
        // when
        GetAreaTypesSpatialRS areaTypes = areaService.getAreaTypes();

        //then
        assertNotNull(areaTypes);
        assertFalse(areaTypes.getAreaTypes().isEmpty());
    }

}