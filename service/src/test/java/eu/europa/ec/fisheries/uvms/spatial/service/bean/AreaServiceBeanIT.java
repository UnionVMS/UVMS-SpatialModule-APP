package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.common.SpatialUtils;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialResponse;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class AreaServiceBeanIT extends AbstractArquillianIT {

    @EJB
    private AreaService service;

    @Test
    public void testGetClosestArea() throws Exception {
        // given
        ClosestAreaSpatialRequest request = new ClosestAreaSpatialRequest();
        request.setUnit(eu.europa.ec.fisheries.uvms.spatial.model.schemas.UnitType.KILOMETERS);
        request.setWkt("POINT(-7.14925 45.11557)");
        request.setCrs(SpatialUtils.DEFAULT_CRS);

        // when
        ClosestAreaSpatialResponse response = service.getClosestArea(request);

        //then
        assertEquals(152.99006034643287, response.getClosestArea().get(0).getDistance(), 0.01);
        assertEquals("81", response.getClosestArea().get(0).getId());

    }

    @Test
    public void testGetClosestAreaWithTransform() throws Exception {
        // given
        ClosestAreaSpatialRequest request = new ClosestAreaSpatialRequest();
        request.setUnit(eu.europa.ec.fisheries.uvms.spatial.model.schemas.UnitType.KILOMETERS);
        request.setWkt("POINT(-7.14925 45.11557)");
        ///request.setArea("countries");
        request.setCrs(3857);

        // when
        ClosestAreaSpatialResponse response = service.getClosestArea(request);

        //then
        assertEquals(3460.563176305411, response.getClosestArea().get(0).getDistance(), 0.01);
        assertEquals("81", response.getClosestArea().get(0).getId());

    }

    @Test(expected = Exception.class)
    public void shouldThrowException() {

        ClosestAreaSpatialRequest request = new ClosestAreaSpatialRequest();
        request.setWkt("POLYGON(-7.14925 45.11557)");
        service.getClosestArea(request);
    }
}
