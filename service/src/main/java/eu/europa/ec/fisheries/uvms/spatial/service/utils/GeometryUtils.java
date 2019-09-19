package eu.europa.ec.fisheries.uvms.spatial.service.utils;

/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;
import org.apache.commons.collections.CollectionUtils;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GeometryUtils {

    private static final Logger LOG = LoggerFactory.getLogger(GeometryUtils.class);
    private static final String EPSG = "EPSG:";

    public static final int DEFAULT_EPSG_SRID = 4326;
    public static GeometryFactory geometryFactory = new GeometryFactory();

    public static String geometryToWkt(Geometry geometry) {
        if ( geometry == null ) {
            return null;
        }


        return new WKTWriter().write(geometry) ;
    }

    public static  Geometry wktToGeometry(String wkt) throws ParseException {
        if ( wkt == null ) {
            return null;
        }

        Geometry geom = new WKTReader().read(wkt);
        geom.setSRID(4326);
        return  geom;

    }

    public static String calculateBuffer(final Double latitude, final Double longitude, final Double buffer) {

        GeometryFactory gf = new GeometryFactory();
        Point point = gf.createPoint(new com.vividsolutions.jts.geom.Coordinate(longitude, latitude));
        Geometry geometry = point.buffer(buffer);
        return geometryToWkt(geometry);

    }

    /**
     * Returns the centroid of a given geometry as WKT
     *
     * @param wkt a WKT
     * @return wkt the centroid as WKT
     */
    public static String wktToCentroidWkt(final String wkt){

        String theWktString = null;

        try {
            Point centroid = wktToGeometry(wkt).getCentroid();
            theWktString = geometryToWkt(centroid);

        } catch (ParseException e) {
            LOG.error(e.getMessage(), e);
        }

        return theWktString;
    }


    public static boolean isDefaultEpsgSRID(int crs) {
        return crs == DEFAULT_EPSG_SRID;
    }



    public static Geometry createPoint(Double latitude, Double longitude) {
        if(null == longitude || null == latitude){
            return null;
        }
        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coordinate = new Coordinate(longitude, latitude);
        Geometry point = geometryFactory.createPoint(coordinate);
        point.setSRID(DEFAULT_EPSG_SRID);
        return point;
    }

    public static Geometry createPoint(Coordinate coordinate) {
        GeometryFactory geometryFactory = new GeometryFactory();
        Geometry point = geometryFactory.createPoint(coordinate);
        point.setSRID(DEFAULT_EPSG_SRID);
        return point;
    }

    public static Geometry createMultipoint(List<Geometry> geometries) {
        if (CollectionUtils.isEmpty(geometries)) {
            return null;
        }
        GeometryFactory geometryFactory = new GeometryFactory();
        Set<Coordinate> coordinates = new HashSet<>();
        for (Geometry geom : geometries) {
            coordinates.add(geom.getCoordinate());
        }
        Geometry multiPoint = geometryFactory.createMultiPoint(coordinates.toArray(new Coordinate[coordinates.size()]));
        multiPoint.setSRID(DEFAULT_EPSG_SRID);
        return multiPoint;
    }

    public static Geometry createLineString(String wkt1, String wkt2) {
        LineString line;

        try {
            Geometry point1 = wktToGeometry(wkt1);
            Geometry point2 = wktToGeometry(wkt2);
            GeometryFactory geometryFactory = new GeometryFactory();
            List<Coordinate> coordinates = new ArrayList<>();
            coordinates.add(point1.getCoordinate());
            coordinates.add(point2.getCoordinate());
            line = geometryFactory.createLineString(coordinates.toArray(new Coordinate[coordinates.size()]));
            line.setSRID(DEFAULT_EPSG_SRID);

        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        return line;
    }

    public static Geometry convertGeometryTo4326(Geometry convertee, String sridFrom) throws FactoryException, TransformException {
        CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:4326");
        CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:" + sridFrom);

        MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
        Geometry convertedGeometry = JTS.transform( convertee, transform);
        return convertedGeometry;
    }
}
