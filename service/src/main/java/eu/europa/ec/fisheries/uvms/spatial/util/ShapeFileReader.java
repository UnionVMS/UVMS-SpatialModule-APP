package eu.europa.ec.fisheries.uvms.spatial.util;

import com.google.common.collect.Maps;
import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

public class ShapeFileReader {

    public static final String EPSG = "EPSG:";
    private static final int DEFAULT_CRS_NUMBER = 4326;
    private static final String DEFAULT_CRS = EPSG + DEFAULT_CRS_NUMBER;

    public Map<String, List<Property>> readShapeFile(Path shapeFilePath, CoordinateReferenceSystem sourceCRS) throws IOException {
        try {
            Map<String, List<Property>> geometries = Maps.newHashMap();

            CoordinateReferenceSystem targetCRS = CRS.decode(DEFAULT_CRS);
            MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);

            Map<String, Object> map = Maps.newHashMap();
            map.put("url", shapeFilePath.toUri().toURL());

            DataStore dataStore = DataStoreFinder.getDataStore(map);
            String typeName = dataStore.getTypeNames()[0];

            FeatureSource<SimpleFeatureType, SimpleFeature> source = dataStore
                    .getFeatureSource(typeName);
            Filter filter = Filter.INCLUDE;

            FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures(filter);
            try (FeatureIterator<SimpleFeature> features = collection.features()) {
                while (features.hasNext()) {
                    SimpleFeature feature = features.next();
                    geometries.put(feature.getID(), newArrayList(feature.getProperties()));

                    transformCRSToDefault(feature, sourceCRS, targetCRS, transform);
                }
            }
            return geometries;
        } catch (FactoryException | TransformException e) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
        }
    }

    private void transformCRSToDefault(SimpleFeature feature, CoordinateReferenceSystem sourceCRS, CoordinateReferenceSystem targetCRS, MathTransform transform) throws FactoryException, TransformException {
        Geometry sourceGeometry = (Geometry) feature.getDefaultGeometryProperty().getValue();
        if (sourceCRS != targetCRS) {
            Geometry targetGeometry = JTS.transform(sourceGeometry, transform);
            targetGeometry.setSRID(DEFAULT_CRS_NUMBER);
            feature.setDefaultGeometry(targetGeometry);
        } else {
            sourceGeometry.setSRID(DEFAULT_CRS_NUMBER);
        }
    }

}
