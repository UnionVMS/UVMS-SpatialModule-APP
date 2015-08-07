package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.schema.spatial.types.AreaResultType;
import eu.europa.ec.fisheries.schema.spatial.types.GeometryType;
import eu.europa.ec.fisheries.schema.spatial.types.PropertyType;
import eu.europa.ec.fisheries.schema.spatial.types.PropertyType.Entry;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;

import javax.ejb.Stateless;
import java.util.ArrayList;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * Created by kopyczmi on 06-Aug-15.
 */
@Stateless
public class EezMapper {

    private static final String FEATURE = "Feature";

    public AreaResultType eezEntityToSchema(EezEntity eezEntity) {
        AreaResultType areaResultType = new AreaResultType();

        areaResultType.setType(FEATURE);
        areaResultType.setGeometry(createGeometry(eezEntity.getGeom()));
        areaResultType.setProperties(createProperties(eezEntity));

        return areaResultType;
    }

    private PropertyType createProperties(EezEntity eezEntity) {
        PropertyType propertyType = new PropertyType();
        ArrayList<Entry> properties = Lists.newArrayList();
        addLongitude(eezEntity, properties);
        addLatitude(eezEntity, properties);
        addCountry(eezEntity, properties);
        addSovereign(eezEntity, properties);
        addRemarks(eezEntity, properties);
        addIso3Digit(eezEntity, properties);
        addDateChang(eezEntity, properties);
        addAreaM2(eezEntity, properties);
        propertyType.setEntry(properties);
        return propertyType;
    }

    private void addAreaM2(EezEntity eezEntity, ArrayList<Entry> properties) {
        if (eezEntity.getAreaM2() != null) {
            Entry entry = new Entry();
            entry.setKey("areaM2");
            entry.setValue(Double.toString(eezEntity.getAreaM2()));
            properties.add(entry);
        }
    }

    private void addDateChang(EezEntity eezEntity, ArrayList<Entry> properties) {
        if (isNotEmpty(eezEntity.getDateChang())) {
            Entry entry = new Entry();
            entry.setKey("dateChang");
            entry.setValue(eezEntity.getDateChang());
            properties.add(entry);
        }
    }

    private void addIso3Digit(EezEntity eezEntity, ArrayList<Entry> properties) {
        if (isNotEmpty(eezEntity.getIso3Digit())) {
            Entry entry = new Entry();
            entry.setKey("iso3Digit");
            entry.setValue(eezEntity.getIso3Digit());
            properties.add(entry);
        }
    }

    private void addRemarks(EezEntity eezEntity, ArrayList<Entry> properties) {
        if (isNotEmpty(eezEntity.getRemarks())) {
            Entry entry = new Entry();
            entry.setKey("remarks");
            entry.setValue(eezEntity.getRemarks());
            properties.add(entry);
        }
    }

    private void addSovereign(EezEntity eezEntity, ArrayList<Entry> properties) {
        if (isNotEmpty(eezEntity.getSovereign())) {
            Entry entry = new Entry();
            entry.setKey("sovereign");
            entry.setValue(eezEntity.getSovereign());
            properties.add(entry);
        }
    }

    private void addCountry(EezEntity eezEntity, ArrayList<Entry> properties) {
        if (isNotEmpty(eezEntity.getCountry())) {
            Entry entry = new Entry();
            entry.setKey("country");
            entry.setValue(eezEntity.getCountry());
            properties.add(entry);
        }
    }

    private void addLatitude(EezEntity eezEntity, ArrayList<Entry> properties) {
        if (eezEntity.getLatitude() != null) {
            Entry entry = new Entry();
            entry.setKey("latitude");
            entry.setValue(Double.toString(eezEntity.getLatitude()));
            properties.add(entry);
        }
    }

    private void addLongitude(EezEntity eezEntity, ArrayList<Entry> properties) {
        if (eezEntity.getLongitude() != null) {
            Entry entry = new Entry();
            entry.setKey("longitude");
            entry.setValue(Double.toString(eezEntity.getLongitude()));
            properties.add(entry);
        }
    }

    private GeometryType createGeometry(Geometry geom) {
        GeometryType geometryType = new GeometryType();
        geometryType.setType(geom.getGeometryType());
        geometryType.setCoordinates(geom.toText());
        return geometryType;
    }

}