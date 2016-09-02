/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;
import eu.europa.ec.fisheries.uvms.BaseUnitilsTest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Area;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.PointType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.UnitType;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialServiceBean;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.mock.Mock;
import java.util.List;

import static java.util.Arrays.asList;
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
        areaTypes.setAreaTypes(asList(AreaType.EEZ));
        closestAreaRequest.setAreaTypes(areaTypes);

        PointType point = new PointType();
        point.setCrs(3216);
        point.setLatitude((double) 50);
        point.setLongitude((double) -21);
        closestAreaRequest.setPoint(point);
    }
    @Test
    @SneakyThrows
    public void testGetClosestArea(){

        Object[][] areas  = new Object[1][5];
        areas[0][0] = "EEZ";
        areas[0][1] = 231;
        areas[0][2] = "MAR";
        areas[0][3] = "Moroccan Exclusive Zone";

        Geometry geometry = new WKTReader().read("POINT (-75.347781567 -106.9794456)");
        geometry.setSRID(4326);
        areas[0][4] = geometry;
        repo.returns(asList(areas)).closestArea(null, null, null);

        List<Area> closestArea = service.getClosestArea(closestAreaRequest);
        assertEquals(18267.45280663312, closestArea.get(0).getDistance());
    }

}