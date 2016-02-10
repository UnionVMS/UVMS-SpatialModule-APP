package eu.europa.ec.fisheries.uvms.spatial.util;

import com.google.common.collect.Maps;
import com.vividsolutions.jts.geom.Geometry;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

public class ShapeFileReader {

    public Map<String, List<Property>> readShapeFile(String absolutePath, String fileName) throws IOException {
        Map<String, List<Property>> geometries = Maps.newHashMap();

        //CoordinateReferenceSystem crs = CRS.parseWKT(wkt);

        File file = new File(absolutePath + fileName);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("url", file.toURI().toURL());

        DataStore dataStore = DataStoreFinder.getDataStore(map);
        String typeName = dataStore.getTypeNames()[0];

        FeatureSource<SimpleFeatureType, SimpleFeature> source = dataStore
                .getFeatureSource(typeName);
        Filter filter = Filter.INCLUDE; // ECQL.toFilter("BBOX(THE_GEOM, 10,20,30,40)")

        FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures(filter);
        try (FeatureIterator<SimpleFeature> features = collection.features()) {
            while (features.hasNext()) {
                SimpleFeature feature = features.next();
                geometries.put(feature.getID(), newArrayList(feature.getProperties()));

                setCRSIfNotPresent(feature);
            }
        }
        return geometries;
    }

    private void setCRSIfNotPresent(SimpleFeature feature) {
        Geometry geom = (Geometry) feature.getDefaultGeometryProperty().getValue();
        if (geom.getSRID() == 0) {
            geom.setSRID(4326);
        }
    }

}
