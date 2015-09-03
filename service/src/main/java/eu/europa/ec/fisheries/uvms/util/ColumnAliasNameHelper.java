package eu.europa.ec.fisheries.uvms.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
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
					String value = null;
					
					if ((field.get(object) instanceof Double)) {
						Double doubleVal = (Double)field.get(object);
						value = String.valueOf(doubleVal);
					} else if ((field.get(object) instanceof Integer)) {
						Integer integerVal = (Integer)field.get(object);
						value = String.valueOf(integerVal);
					} else if ((field.get(object) instanceof String)) {
						value = (String)field.get(object);
					} else if ((field.get(object) instanceof BigDecimal)) {
						BigDecimal fieldVal = (BigDecimal)field.get(object);
						value = fieldVal.toString();
					} else if ((field.get(object) instanceof Geometry)) {
						Geometry geometry = ((Geometry)field.get(object));
						value = new WKTWriter().write(geometry);
					} else {
						value = (String)field.get(object);
					}
					LOG.info("Value is : " + value);	
					if (value != null && value !="") {
						map.put(aliasName, value);
					}					
				}				
			}		
		} catch (IllegalAccessException e) {
			LOG.error("Illegal acess exception : ", e);
			throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
		}
		
		return map;
	}

}
