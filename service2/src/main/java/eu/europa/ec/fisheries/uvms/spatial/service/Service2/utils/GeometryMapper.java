package eu.europa.ec.fisheries.uvms.spatial.service.Service2.utils;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;


public class GeometryMapper  {

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

       return  new WKTReader().read(wkt) ;

    }

    public String calculateBuffer(final Double latitude, final Double longitude, final Double buffer) {

        GeometryFactory gf = new GeometryFactory();
        Point point = gf.createPoint(new com.vividsolutions.jts.geom.Coordinate(longitude, latitude));
        Geometry geometry = point.buffer(buffer);
        return geometryToWkt(geometry);

    }



}
