package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.schema.spatial.types.AreaResultType;
import eu.europa.ec.fisheries.schema.spatial.types.PropertyType;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import org.mapstruct.*;

import java.util.ArrayList;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Mapper(componentModel="cdi", uses = GeometryTypeMapper.class )
public abstract class AreaResultTypeMapper {

    private final static String FEATURE = "Feature";
    private final static String AREAM2 = "areaM2";
    private final static String ISO3DIGIT = "iso3Digit";
    private final static String REMARKS = "remarks";
    private final static String SOVEREIGN = "sovereign";
    private final static String COUNTRY = "country";
    private final static String LATITUDE = "latitude";
    private final static String LONGITUDE = "longitude";

    @Mappings( {
        @Mapping(target = "type", constant = FEATURE),
        @Mapping(source = "geom", target = "geometry"),
    })
    public abstract AreaResultType eezEntityToAreaResultType(EezEntity eezEntity);

    @AfterMapping
    protected void fillProperties(EezEntity eezEntity, @MappingTarget AreaResultType result) {

        PropertyType propertyType = new PropertyType();
        ArrayList<PropertyType.Entry> properties = Lists.newArrayList();
        addLongitude(eezEntity, properties);
        addLatitude(eezEntity, properties);
        addCountry(eezEntity, properties);
        addSovereign(eezEntity, properties);
        addRemarks(eezEntity, properties);
        addIso3Digit(eezEntity, properties);
        addDateChang(eezEntity, properties);
        addAreaM2(eezEntity, properties);
        propertyType.setEntry(properties);
        result.setProperties(propertyType);
    }

    private void addAreaM2(EezEntity eezEntity, ArrayList<PropertyType.Entry> properties) {
        if (eezEntity.getAreaM2() != null) {
            PropertyType.Entry entry = new PropertyType.Entry();
            entry.setKey(AREAM2);
            entry.setValue(Double.toString(eezEntity.getAreaM2()));
            properties.add(entry);
        }
    }

    private void addDateChang(EezEntity eezEntity, ArrayList<PropertyType.Entry> properties) {
        if (isNotEmpty(eezEntity.getDateChang())) {
            PropertyType.Entry entry = new PropertyType.Entry();
            entry.setKey("dateChang");
            entry.setValue(eezEntity.getDateChang());
            properties.add(entry);
        }
    }

    private void addIso3Digit(EezEntity eezEntity, ArrayList<PropertyType.Entry> properties) {
        if (isNotEmpty(eezEntity.getIso3Digit())) {
            PropertyType.Entry entry = new PropertyType.Entry();
            entry.setKey(ISO3DIGIT);
            entry.setValue(eezEntity.getIso3Digit());
            properties.add(entry);
        }
    }

    private void addRemarks(EezEntity eezEntity, ArrayList<PropertyType.Entry> properties) {
        if (isNotEmpty(eezEntity.getRemarks())) {
            PropertyType.Entry entry = new PropertyType.Entry();
            entry.setKey(REMARKS);
            entry.setValue(eezEntity.getRemarks());
            properties.add(entry);
        }
    }

    private void addSovereign(EezEntity eezEntity, ArrayList<PropertyType.Entry> properties) {
        if (isNotEmpty(eezEntity.getSovereign())) {
            PropertyType.Entry entry = new PropertyType.Entry();
            entry.setKey(SOVEREIGN);
            entry.setValue(eezEntity.getSovereign());
            properties.add(entry);
        }
    }

    private void addCountry(EezEntity eezEntity, ArrayList<PropertyType.Entry> properties) {
        if (isNotEmpty(eezEntity.getCountry())) {
            PropertyType.Entry entry = new PropertyType.Entry();
            entry.setKey(COUNTRY);
            entry.setValue(eezEntity.getCountry());
            properties.add(entry);
        }
    }

    private void addLatitude(EezEntity eezEntity, ArrayList<PropertyType.Entry> properties) {
        if (eezEntity.getLatitude() != null) {
            PropertyType.Entry entry = new PropertyType.Entry();
            entry.setKey(LATITUDE);
            entry.setValue(Double.toString(eezEntity.getLatitude()));
            properties.add(entry);
        }
    }

    private void addLongitude(EezEntity eezEntity, ArrayList<PropertyType.Entry> properties) {
        if (eezEntity.getLongitude() != null) {
            PropertyType.Entry entry = new PropertyType.Entry();
            entry.setKey(LONGITUDE);
            entry.setValue(Double.toString(eezEntity.getLongitude()));
            properties.add(entry);
        }
    }
}
