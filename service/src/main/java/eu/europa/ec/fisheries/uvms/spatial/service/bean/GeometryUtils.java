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
import org.geotools.measure.Latitude;
import org.geotools.measure.Longitude;
import org.geotools.referencing.CRS;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
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

import static java.lang.Math.toRadians;
import static org.geotools.referencing.crs.DefaultGeographicCRS.WGS84;

@Slf4j
public final class GeometryUtils {

    private static final String EPSG = "EPSG:";

    private static final int DEFAULT_SRID = 4326;

    private static GeometryFactory FACTORY = new GeometryFactory();

    public static final CoordinateReferenceSystem DEFAULT_CRS;

    static {
        DEFAULT_CRS = buildCrs(DEFAULT_SRID);
    }

    /**
     * private constructor to avoid class instantiation
     */
    private GeometryUtils() {
    }

    private static CoordinateReferenceSystem buildCrs(final Integer crs) throws IllegalArgumentException {
        CoordinateReferenceSystem referenceSystem;
        try {
            referenceSystem = CRS.decode(EPSG + crs);
        } catch (FactoryException e) {
            log.error(e.getMessage(), e);
            throw new IllegalArgumentException("COORDINATE REFERENCE SYSTEM NOT SUPPORTED");
        }
        return referenceSystem;
    }

    /**
     * This method returns a Point in WGS84 for a given latitude and longitude and Coordinate reference System.
     * It will check the parameters for inconsistencies and translate the point if necessary.
     *
     * @param y The latitude value in <strong>decimal degrees</strong>.
     * @param x The longitude value in <strong>decimal degrees</strong>.
     * @param sourceCode The longitude value in <strong>decimal degrees</strong>.
     * @return The Point in WGS84.
     */
    public static Point toWgs84Point(double y, double x, int sourceCode) {

        Point p;
        try {
            if (!isDefaultCrs(sourceCode)){
                p = FACTORY.createPoint(new Coordinate(x, y));
                if (!isDefaultCrs(sourceCode)) {
                    final CoordinateReferenceSystem sourceCRS = CRS.decode(EPSG + sourceCode);
                    MathTransform mathTransform = CRS.findMathTransform(sourceCRS, WGS84, false);
                    p = (Point) JTS.transform(p, mathTransform);
                }
                checkLatitude(p.getY());
                checkLongitude(p.getX());
            }
            else {
                checkLatitude(y);
                checkLongitude(x);
                p = FACTORY.createPoint(new Coordinate(x, y));
            }
            p.setSRID(DEFAULT_SRID);
        } catch (TransformException e) {
            log.error(e.getMessage(), e);
            throw new IllegalArgumentException("TRANSFORMATION FAILED", e);
        } catch (NoSuchAuthorityCodeException e) {
            log.error(e.getMessage(), e);
            throw new IllegalArgumentException("CRS CODE NOT UNDERSTOOD");
        } catch (FactoryException e) {
            log.error(e.getMessage(), e);
            throw new IllegalArgumentException("MATH TRANSFORM COULD BE CREATED");
        }
        return p;
    }

    public static Point toWgs84Point(double y, double x, int sourceCode, int targetCode) {
        Point p;
        try {
            p = FACTORY.createPoint(new Coordinate(x, y));
            final CoordinateReferenceSystem sourceCRS = CRS.decode(EPSG + sourceCode);
            final CoordinateReferenceSystem targetCRS = CRS.decode(EPSG + targetCode);
            MathTransform mathTransform = CRS.findMathTransform(sourceCRS, targetCRS, false);
            p = (Point) JTS.transform(p, mathTransform);
            checkLatitude(p.getY());
            checkLongitude(p.getX());
        } catch (FactoryException e) {
            log.error(e.getMessage(), e);
            throw new IllegalArgumentException("MATH TRANSFORM COULD BE CREATED");
        } catch (TransformException e) {
            log.error(e.getMessage(), e);
            throw new IllegalArgumentException("TRANSFORMATION FAILED", e);
        }
        return p;
    }
    /**
     * Checks the longitude validity. The argument {@code longitude} should be
     * greater or equal than -180 degrees and lower or equals than +180 degrees. As
     * a convenience, this method returns the longitude in radians.
     *
     * @param  longitude The longitude value in <strong>decimal degrees</strong>.
     * @return The longitude value in <strong>radians</strong>.
     * @throws IllegalArgumentException if {@code longitude} is not between -180 and +180 degrees.
     */
    private static double checkLongitude(final double longitude) throws IllegalArgumentException {
        if (longitude >= Longitude.MIN_VALUE && longitude <= Longitude.MAX_VALUE) {
            return toRadians(longitude);
        }
        throw new IllegalArgumentException(Errors.format(ErrorKeys.LONGITUDE_OUT_OF_RANGE_$1, new Longitude(longitude)));
    }

    /**
     * Checks the latitude validity. The argument {@code latitude} should be
     * greater or equal than -90 degrees and lower or equals than +90 degrees. As
     * a convenience, this method returns the latitude in radians.
     *
     * @param  latitude The latitude value in <strong>decimal degrees</strong>.
     * @return The latitude value in <strong>radians</strong>.
     * @throws IllegalArgumentException if {@code latitude} is not between -90 and +90 degrees.
     */
    private static double checkLatitude(final double latitude) throws IllegalArgumentException {
        if (latitude >= Latitude.MIN_VALUE && latitude <= Latitude.MAX_VALUE) {
            return toRadians(latitude);
        }
        throw new IllegalArgumentException(Errors.format(
                ErrorKeys.LATITUDE_OUT_OF_RANGE_$1, new Latitude(latitude)));
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

        } catch (FactoryException | TransformException e) {
            log.error(e.getMessage(), e);
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

    public static Geometry transform(Double tx, Double ty, Geometry geometry) {

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

}