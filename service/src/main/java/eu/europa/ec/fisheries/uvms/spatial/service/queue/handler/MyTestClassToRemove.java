package eu.europa.ec.fisheries.uvms.spatial.service.queue.handler;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTWriter;

/**
 * Created by Michal Kopyczok on 21-Aug-15.
 */
public class MyTestClassToRemove {
    public static void main(String[] args) {
        double lat = 32.85615;
        double lon = -10.74118;
        GeometryFactory gf = new GeometryFactory();
        Coordinate coord = new Coordinate(lon, lat);
        Point point = gf.createPoint(coord);
        point.setSRID(3245);
        String wktPoint = new WKTWriter(2).writeFormatted(point);
        System.out.println(wktPoint);
    }
}
