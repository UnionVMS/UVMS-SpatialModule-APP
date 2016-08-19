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
import eu.europa.ec.fisheries.uvms.BaseUnitilsTest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialServiceBean;
import lombok.SneakyThrows;
import org.geotools.geometry.jts.WKTReader2;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.mock.Mock;

import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

@Ignore(value = "FIX LATER")
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
        point.setCrs(3261);
        point.setLatitude(12);
        point.setLongitude(12);
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

        Geometry geometry = new WKTReader2().read("MULTIPOLYGON(((151.464692488022 -89.9998252076401,166.020867143701 -89.9998601005151," +
                "104.287122332492 -89.9998930298125,151.464692488022 -89.9998252076401)))");
        areas[0][4] = geometry;
        geometry.setSRID(3216);

        repo.returns(Arrays.asList(areas)).closestArea(null, null, null);

        List<Area> closestArea = service.getClosestArea(closestAreaRequest);
        assertEquals(0.024537057275323824, closestArea.get(0).getDistance());
    }
}