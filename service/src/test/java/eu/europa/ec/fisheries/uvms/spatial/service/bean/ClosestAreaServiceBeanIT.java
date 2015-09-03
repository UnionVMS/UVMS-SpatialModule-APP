//package eu.europa.ec.fisheries.uvms.spatial.service.bean;
//
//import eu.europa.ec.fisheries.uvms.common.SpatialUtils;
//import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
//import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRS;
//import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRQ;
//import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SuccessType;
//import org.jboss.arquillian.junit.Arquillian;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import javax.ejb.EJB;
//
//import static org.junit.Assert.assertEquals;
//
//@RunWith(Arquillian.class)
//public class ClosestAreaServiceBeanIT extends AbstractArquillianIT {
//
//    @EJB
//    private ClosestAreaService service;
//
//    @Test
//    public void testGetClosestArea() throws Exception {
//        // given
//        ClosestAreaSpatialRQ request = new ClosestAreaSpatialRQ();
//        request.setUnit(eu.europa.ec.fisheries.uvms.spatial.model.schemas.MeasurementUnit.KILOMETER);
//        request.setWkt("POINT(-7.14925 45.11557)");
//        request.getArea().add(AreaType.COUNTRY);
//        request.setCrs(SpatialUtils.DEFAULT_CRS);
//
//        // when
//        ClosestAreaSpatialRS response = service.getClosestAreas(request);
//
//        //then
//        assertEquals(new SuccessType(), response.getMessageType().getSuccess());
//        assertEquals(597.1283174583547, response.getClosestAreas().get(0).getDistance(), 0.01);
//        assertEquals("145", response.getClosestAreas().get(0).getId());
//
//    }
//
//    @Test
//    public void testGetClosestAreaWithTransform() throws Exception {
//        // given
//        ClosestAreaSpatialRQ request = new ClosestAreaSpatialRQ();
//        request.setUnit(eu.europa.ec.fisheries.uvms.spatial.model.schemas.MeasurementUnit.KILOMETER);
//        request.setWkt("POINT(-7.14925 45.11557)");
//        request.getArea().add(AreaType.COUNTRY);
//        request.setCrs(3857);
//
//        // when
//        ClosestAreaSpatialRS response = service.getClosestAreas(request);
//
//        //then
//        assertEquals(new SuccessType(), response.getMessageType().getSuccess());
//        assertEquals(3460.563176305411, response.getClosestAreas().get(0).getDistance(), 0.01);
//        assertEquals("81", response.getClosestAreas().get(0).getId());
//
//    }
//
//}
