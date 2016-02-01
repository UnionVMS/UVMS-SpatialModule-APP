package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaByLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaExtendedIdentifierType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.PointType;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;

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
        PointType pointType = new PointType();
        pointType.setCrs(DEFAULT_CRS);
        pointType.setLatitude(LATITUDE);
        pointType.setLongitude(LONGITUDE);
        AreaByLocationSpatialRQ byLocationSpatialRQ = new AreaByLocationSpatialRQ();
        byLocationSpatialRQ.setPoint(pointType);
        AreaByLocationSpatialRQ request = byLocationSpatialRQ;

        // when
        List<AreaExtendedIdentifierType> areaTypesByLocation = areaByLocationService.getAreaTypesByLocation(request);

        //then
        assertNotNull(areaTypesByLocation);
    }
}