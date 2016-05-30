package eu.europa.ec.fisheries.uvms.spatial.service;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.BaseUnitilsTest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Area;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.PointType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.UnitType;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialServiceBean;
import java.util.Arrays;
import java.util.List;
import lombok.SneakyThrows;
import org.geotools.geometry.jts.WKTReader2;
import org.junit.Before;
import org.junit.Test;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.mock.Mock;

import static junit.framework.TestCase.assertEquals;

public class SpatialServiceBeanTest extends BaseUnitilsTest {

    @TestedObject
    private SpatialService service = new SpatialServiceBean();

    private ClosestAreaSpatialRQ closestAreaRequest;

    @InjectIntoByType
    private Mock<SpatialRepository> repo;

    @Before
    public void before(){
        closestAreaRequest = new ClosestAreaSpatialRQ();
        closestAreaRequest.setUnit(UnitType.KILOMETERS);
        ClosestAreaSpatialRQ.AreaTypes areaTypes = new ClosestAreaSpatialRQ.AreaTypes();
        areaTypes.setAreaTypes(Arrays.asList(AreaType.EEZ));
        closestAreaRequest.setAreaTypes(areaTypes);

        PointType point = new PointType();
        point.setCrs(4326);
        point.setLatitude(12);
        point.setLongitude(12);
        closestAreaRequest.setPoint(point);
    }
    @Test //TODO needs more testing
    @SneakyThrows
    public void testGetClosestArea(){

        Object[][] areas  = new Object[1][5];
        areas[0][0] = "EEZ";
        areas[0][1] = 231;
        areas[0][2] = "MAR";
        areas[0][3] = "Moroccan Exclusive Zone";
        Geometry geometry = new WKTReader2().read("MULTIPOLYGON(((106.867924148 -9.16467987999994,108.036593601 -12.9679006599999," +
                "103.079231596 -12.82837266, 102.56917584 -8.87249927999994," +
                "106.867924148 -9.16467987999994)))");
        areas[0][4] = geometry;

        repo.returns(Arrays.asList(areas)).closestArea(null, null, null);

        List<Area> closestArea = service.getClosestArea(closestAreaRequest);
        assertEquals(10281.53025864021, closestArea.get(0).getDistance());
    }
}
