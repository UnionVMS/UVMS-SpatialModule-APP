package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.schema.spatial.types.AreaResultType;
import eu.europa.ec.fisheries.schema.spatial.types.GeometryType;
import eu.europa.ec.fisheries.schema.spatial.types.PropertyType;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import org.mapstruct.*;

import java.util.ArrayList;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Mapper(componentModel="cdi", uses = GeometryMapper.class )
public abstract class AreaResultTypeMapper {

    @Mappings( {
        @Mapping(target = "type", constant = "Feature"),
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
            entry.setKey("areaM2");
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
            entry.setKey("iso3Digit");
            entry.setValue(eezEntity.getIso3Digit());
            properties.add(entry);
        }
    }

    private void addRemarks(EezEntity eezEntity, ArrayList<PropertyType.Entry> properties) {
        if (isNotEmpty(eezEntity.getRemarks())) {
            PropertyType.Entry entry = new PropertyType.Entry();
            entry.setKey("remarks");
            entry.setValue(eezEntity.getRemarks());
            properties.add(entry);
        }
    }

    private void addSovereign(EezEntity eezEntity, ArrayList<PropertyType.Entry> properties) {
        if (isNotEmpty(eezEntity.getSovereign())) {
            PropertyType.Entry entry = new PropertyType.Entry();
            entry.setKey("sovereign");
            entry.setValue(eezEntity.getSovereign());
            properties.add(entry);
        }
    }

    private void addCountry(EezEntity eezEntity, ArrayList<PropertyType.Entry> properties) {
        if (isNotEmpty(eezEntity.getCountry())) {
            PropertyType.Entry entry = new PropertyType.Entry();
            entry.setKey("country");
            entry.setValue(eezEntity.getCountry());
            properties.add(entry);
        }
    }

    private void addLatitude(EezEntity eezEntity, ArrayList<PropertyType.Entry> properties) {
        if (eezEntity.getLatitude() != null) {
            PropertyType.Entry entry = new PropertyType.Entry();
            entry.setKey("latitude");
            entry.setValue(Double.toString(eezEntity.getLatitude()));
            properties.add(entry);
        }
    }

    private void addLongitude(EezEntity eezEntity, ArrayList<PropertyType.Entry> properties) {
        if (eezEntity.getLongitude() != null) {
            PropertyType.Entry entry = new PropertyType.Entry();
            entry.setKey("longitude");
            entry.setValue(Double.toString(eezEntity.getLongitude()));
            properties.add(entry);
        }
    }
}
