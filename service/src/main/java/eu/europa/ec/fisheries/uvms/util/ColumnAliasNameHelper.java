package eu.europa.ec.fisheries.uvms.util;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTWriter;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.annotation.ColumnAliasName;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Slf4j
public class ColumnAliasNameHelper {

    public static Map<String, String> getFieldMap(Object object) {
        Map<String, String> map = newHashMap();
        Class objClass = object.getClass();

        try {
            for (Field field : objClass.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(ColumnAliasName.class)) {
                    String aliasName = field.getAnnotation(ColumnAliasName.class).aliasName();
                    log.info("Alias Name : " + aliasName);

                    String value = retrieveValue(object, field);
                    log.info("Value is : " + value);
                    if (isNotEmpty(value)) {
                        map.put(aliasName, value);
                    } else {
                        throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            log.error("Illegal acess exception : ", e);
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
        }

        return map;
    }

    private static String retrieveValue(Object object, Field field) throws IllegalAccessException {
        if (field.get(object) instanceof Number) {
            Number doubleVal = (Number) field.get(object);
            return String.valueOf(doubleVal);
        } else if (field.get(object) instanceof Geometry) {
            Geometry geometry = ((Geometry) field.get(object));
            return new WKTWriter().write(geometry);
        } else {
            return (String) field.get(object);
        }
    }

}
