package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaByLocationSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.service.queue.AreaByLocationQueueService;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import static junit.framework.TestCase.assertNotNull;

/**
 * Created by kopyczmi on 19-Aug-15.
 */
@RunWith(Arquillian.class)
public class AreaByLocationServiceBeanIT extends AbstractArquillianIT {

    @EJB
    private AreaByLocationQueueService areaByLocationQueueService;

    @Test
    public void shouldGetAreasByLocation() throws Exception {
        // given

        // when
        AreaByLocationSpatialRS areasByLocation = areaByLocationQueueService.getAreasByLocation(1, 1, 213);

        //then
        assertNotNull(areasByLocation);
        //assertNotNull(areasByLocation.getAreasType());
    }
}