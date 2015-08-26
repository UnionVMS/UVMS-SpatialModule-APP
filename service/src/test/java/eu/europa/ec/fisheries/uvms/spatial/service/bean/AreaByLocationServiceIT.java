package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaByLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaByLocationSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.PointType;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import static junit.framework.TestCase.assertNotNull;

/**
 * Created by kopyczmi on 19-Aug-15.
 */
@RunWith(Arquillian.class)
public class AreaByLocationServiceIT extends AbstractArquillianIT {

    private static final double LATITUDE = 32.85615;
    private static final double LONGITUDE = -10.74118;
    private static final int DEFAULT_CRS = 4326;

    @EJB
    private AreaByLocationService areaByLocationService;

    @Test
    public void shouldGetAreasByLocation() throws Exception {
        // given
        AreaByLocationSpatialRQ request = new AreaByLocationSpatialRQ(new PointType(LONGITUDE, LATITUDE, DEFAULT_CRS));

        // when
        AreaByLocationSpatialRS areasByLocation = areaByLocationService.getAreasByLocation(request);

        //then
        assertNotNull(areasByLocation);
        assertNotNull(areasByLocation.getAreasByLocation());
    }
}