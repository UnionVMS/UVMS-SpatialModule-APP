package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.schema.spatial.types.AreaResultType;
import eu.europa.ec.fisheries.schema.spatial.types.PropertyType;
import eu.europa.ec.fisheries.schema.spatial.types.PropertyType.Entry;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import org.mapstruct.*;

import java.util.ArrayList;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Mapper(componentModel = "cdi", uses = GeometryTypeMapper.class)
public abstract class AreaResultTypeMapper {

    public static final String DATE_CHANG = "dateChang";
    private final static String FEATURE = "Feature";
    private final static String AREA_M2 = "areaM2";
    private final static String ISO_3_DIGIT = "iso3Digit";
    private final static String REMARKS = "remarks";
    private final static String SOVEREIGN = "sovereign";
    private final static String COUNTRY = "country";
    private final static String LATITUDE = "latitude";
    private final static String LONGITUDE = "longitude";

    @Mappings({
            @Mapping(target = "type", constant = FEATURE),
            @Mapping(source = "geom", target = "geometry"),
    })
    public abstract AreaResultType eezEntityToAreaResultType(EezEntity eezEntity);

    @AfterMapping
    protected void fillProperties(EezEntity eezEntity, @MappingTarget AreaResultType result) {
        ArrayList<Entry> properties = Lists.newArrayList();

        addLongitude(eezEntity, properties);
        addLatitude(eezEntity, properties);
        addCountry(eezEntity, properties);
        addSovereign(eezEntity, properties);
        addRemarks(eezEntity, properties);
        addIso3Digit(eezEntity, properties);
        addDateChang(eezEntity, properties);
        addAreaM2(eezEntity, properties);

        result.setProperties(new PropertyType(properties));
    }

    private void addAreaM2(EezEntity eezEntity, ArrayList<Entry> properties) {
        if (eezEntity.getAreaM2() != null) {
            properties.add(new Entry(AREA_M2, Double.toString(eezEntity.getAreaM2()), null));
        }
    }

    private void addDateChang(EezEntity eezEntity, ArrayList<Entry> properties) {
        if (isNotEmpty(eezEntity.getDateChang())) {
            properties.add(new Entry(DATE_CHANG, eezEntity.getDateChang(), null));
        }
    }

    private void addIso3Digit(EezEntity eezEntity, ArrayList<Entry> properties) {
        if (isNotEmpty(eezEntity.getIso3Digit())) {
            properties.add(new Entry(ISO_3_DIGIT, eezEntity.getIso3Digit(), null));
        }
    }

    private void addRemarks(EezEntity eezEntity, ArrayList<Entry> properties) {
        if (isNotEmpty(eezEntity.getRemarks())) {
            properties.add(new Entry(REMARKS, eezEntity.getRemarks(), null));
        }
    }

    private void addSovereign(EezEntity eezEntity, ArrayList<Entry> properties) {
        if (isNotEmpty(eezEntity.getSovereign())) {
            properties.add(new Entry(SOVEREIGN, eezEntity.getSovereign(), null));
        }
    }

    private void addCountry(EezEntity eezEntity, ArrayList<Entry> properties) {
        if (isNotEmpty(eezEntity.getCountry())) {
            properties.add(new Entry(COUNTRY, eezEntity.getCountry(), null));
        }
    }

    private void addLatitude(EezEntity eezEntity, ArrayList<Entry> properties) {
        if (eezEntity.getLatitude() != null) {
            properties.add(new Entry(LATITUDE, Double.toString(eezEntity.getLatitude()), null));
        }
    }

    private void addLongitude(EezEntity eezEntity, ArrayList<Entry> properties) {
        if (eezEntity.getLongitude() != null) {
            properties.add(new Entry(LONGITUDE, Double.toString(eezEntity.getLongitude()), null));
        }
    }
}
