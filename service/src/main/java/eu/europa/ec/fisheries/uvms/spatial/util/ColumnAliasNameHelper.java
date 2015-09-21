package eu.europa.ec.fisheries.uvms.spatial.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTWriter;

import eu.europa.ec.fisheries.uvms.spatial.service.bean.annotation.ColumnAliasName;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;

public class ColumnAliasNameHelper {
	
	private static Logger LOG = LoggerFactory.getLogger(ColumnAliasNameHelper.class.getName());
	
	public static  Map<String, String> getFieldMap(Object object) {
		Map<String, String> map = new HashMap<String, String>();
		Class objClass = object.getClass();
		try {
			for (Field field : objClass.getDeclaredFields()) {
				field.setAccessible(true);
				if (field.isAnnotationPresent(ColumnAliasName.class)) {
					String aliasName = field.getAnnotation(ColumnAliasName.class).aliasName();
					LOG.info("Alias Name : " + aliasName);
					String value = getFieldValue(field, object);
					map.put(aliasName, value);
					LOG.info("Value is : " + value);					
				}				
			}		
		} catch (IllegalAccessException e) {
			LOG.error("Illegal acess exception : ", e);
			throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
		}		
		return map;
	}
	
	private static String getFieldValue(Field field, Object object) throws IllegalAccessException {
		if ((field.get(object) instanceof Number)) {
			Number numberVal = (Number)field.get(object);
			return String.valueOf(numberVal);
		} else if ((field.get(object) instanceof Geometry)) {
			Geometry geometry = ((Geometry)field.get(object));
			return new WKTWriter().write(geometry);
		} else {
			return (String)field.get(object);
		}
	}

}
