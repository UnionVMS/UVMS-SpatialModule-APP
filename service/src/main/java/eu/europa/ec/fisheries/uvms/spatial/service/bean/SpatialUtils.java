/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import eu.europa.ec.fisheries.uvms.spatial.model.upload.UploadProperty;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import lombok.extern.slf4j.Slf4j;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.CRS;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.geotools.referencing.crs.DefaultGeographicCRS.WGS84;

@Slf4j
public class SpatialUtils {

    private static final String EPSG = "EPSG:";
    private static final int DEFAULT_SRID = 4326;
    private static GeometryFactory gf = new GeometryFactory();
    public static final CoordinateReferenceSystem DEFAULT_CRS;

    static {
        DEFAULT_CRS = buildCrs(DEFAULT_SRID);
    }

    private SpatialUtils() {
    }

    private static CoordinateReferenceSystem buildCrs(final Integer srid){
        CoordinateReferenceSystem referenceSystem;
        try {
            referenceSystem = CRS.decode(EPSG + srid);
        } catch (FactoryException e) {
            throw new IllegalArgumentException("COORDINATE REFERENCE SYSTEM NOT SUPPORTED");
        }
        return referenceSystem;
    }

    static Geometry translate(Double tx, Double ty, Geometry geometry) {

        AffineTransform translate= AffineTransform.getTranslateInstance(tx, ty);
        Coordinate[] source = geometry.getCoordinates();
        Coordinate[] target = new Coordinate[source.length];

        for (int i= 0; i < source.length; i++){
            Coordinate sourceCoordinate = source[i];
            Point2D p = new Point2D.Double(sourceCoordinate.x,sourceCoordinate.y);
            Point2D transform = translate.transform(p, null);
            target[i] = new Coordinate(transform.getX(), transform.getY());
        }

        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        Geometry targetGeometry;

        if (geometry instanceof Point){
            targetGeometry = geometryFactory.createPoint(target[0]);
        }
        else if (geometry instanceof Polygon){
           targetGeometry = geometryFactory.createPolygon(target);
        }
        else {
            throw new UnsupportedOperationException("GEOMETRY TYPE NOT SUPPORTED");
        }

        return targetGeometry;
    }

    private static Point convertToPoint(Double lon, Double lat, Integer crs) {
        Point p = gf.createPoint(new Coordinate(lat, lon));
        p.setSRID(crs);
        return p;
    }

    static Point transform(Double lon, Double lat, Integer crs) {

        Point p;
        if (!isDefaultCrs(crs)){
            p = transformToDefault(lat, lon, crs);
        }
        else {
            p = convertToPoint(lat, lon, crs);
        }
        return p;
    }

    private static Point transformToDefault(double lon, double lat, int crs) {
        try {

            Point p = gf.createPoint(new Coordinate(lat, lon));
            p.setSRID(DEFAULT_SRID);

            if (!isDefaultCrs(crs)) {
                CoordinateReferenceSystem inputCrs = CRS.decode(EPSG + crs);
                MathTransform mathTransform = CRS.findMathTransform(inputCrs, WGS84, false);
                p = (Point) JTS.transform(p, mathTransform);
            }
            return p;

        } catch (FactoryException ex) {
            throw new SpatialServiceException(SpatialServiceErrors.NO_SUCH_CRS_CODE_ERROR, String.valueOf(crs), ex);
        } catch (MismatchedDimensionException | TransformException ex) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR, ex);
        }
    }

    public static boolean isDefaultCrs(int crs) {
        return crs == DEFAULT_SRID;
    }

    public static Map<String, List<Property>> readShapeFile(Path shapeFilePath, CoordinateReferenceSystem sourceCRS) throws IOException {

        Map<String, Object> map = new HashMap<>();
        map.put("url", shapeFilePath.toUri().toURL());
        DataStore dataStore = DataStoreFinder.getDataStore(map);
        Map<String, List<Property>> geometries = new HashMap<>();
        String typeName = dataStore.getTypeNames()[0];
        FeatureSource<SimpleFeatureType, SimpleFeature> source = dataStore.getFeatureSource(typeName);
        FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures(Filter.INCLUDE);
        FeatureIterator<SimpleFeature> iterator = collection.features();

        try {

            MathTransform transform = CRS.findMathTransform(sourceCRS, DEFAULT_CRS);
            while (iterator.hasNext()) {
                final SimpleFeature feature = iterator.next();
                geometries.put(feature.getID(), new ArrayList<>(feature.getProperties()));
                Geometry targetGeometry = (Geometry) feature.getDefaultGeometry();
                if (targetGeometry != null) {
                    if (DEFAULT_CRS.getName().equals(sourceCRS.getName())){
                        targetGeometry = JTS.transform(targetGeometry, transform);
                    }
                }
                else {
                    throw new InvalidParameterException("TARGET GEOMETRY CANNOT BE NULL");
                }
                targetGeometry.setSRID(DEFAULT_SRID);
                feature.setDefaultGeometry(targetGeometry);
            }
            return geometries;

        } catch (Exception e) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR, e);
        }
        finally {
            iterator.close();
            dataStore.dispose();
        }
    }

    public static List<UploadProperty> readAttribute(Path shapeFilePath) throws IOException {

        List<UploadProperty> properties = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("url", shapeFilePath.toUri().toURL());
        DataStore dataStore = DataStoreFinder.getDataStore(map);
        String typeName = dataStore.getTypeNames()[0];
        FeatureSource<SimpleFeatureType, SimpleFeature> source = dataStore.getFeatureSource(typeName);
        FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures(Filter.INCLUDE);
        FeatureIterator<SimpleFeature> iterator = collection.features();
        SimpleFeature next = iterator.next();

        try {

            List<AttributeDescriptor> attributeDescriptors = next.getFeatureType().getAttributeDescriptors();
            for (AttributeDescriptor attributeDescriptor : attributeDescriptors){
                String localPart = attributeDescriptor.getName().getLocalPart();
                switch (localPart){
                    case "the_geom":
                    case "geom":
                        break;
                    default:
                        properties.add(new UploadProperty().withName(localPart).withType(attributeDescriptor.getType().getBinding().getSimpleName())); // TODO nullpointer checks
                }
            }
            return properties;

        } finally {
            iterator.close();
            dataStore.dispose();
        }
    }

}