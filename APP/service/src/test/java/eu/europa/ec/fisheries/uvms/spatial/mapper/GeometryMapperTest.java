/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.mapper;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.GeometryType;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.GeometryMapper;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.GeometryMapperImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GeometryMapperTest {

    private GeometryMapper mapper;
    private Geometry geometry;

    @Before
    public void beforeTest() {
        mapper = new GeometryMapperImpl();
    }

    @Test
    public void shouldReturnGeometryTypeWithTypePoint() {
        geometry = new GeometryFactory().createPoint(new Coordinate(1, 11));
        GeometryType geometryType = mapper.geometryToWKT(geometry);
        assertEquals("POINT (1 11)", geometryType.getGeometry());
    }

    @Test
    public void shouldReturnGeometryTypeWithTypePolygon() {
        List<Coordinate> coordinates = Arrays.asList(new Coordinate(1, 11), new Coordinate(1, 11), new Coordinate(1, 11), new Coordinate(1, 11));
        Polygon polygon = new GeometryFactory().createPolygon((Coordinate[]) coordinates.toArray());
        GeometryType geometryType = mapper.geometryToWKT(polygon);
        assertEquals("POLYGON ((1 11, 1 11, 1 11, 1 11))", geometryType.getGeometry());
    }
}