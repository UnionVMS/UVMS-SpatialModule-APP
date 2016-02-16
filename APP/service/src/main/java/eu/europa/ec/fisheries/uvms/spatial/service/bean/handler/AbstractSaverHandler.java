package eu.europa.ec.fisheries.uvms.spatial.service.bean.handler;

import com.google.common.collect.Maps;
import org.opengis.feature.Property;

import java.util.List;
import java.util.Map;

public class AbstractSaverHandler {

    protected Map<String, Object> createAttributesMap(List<Property> properties) {
        Map<String, Object> resultMap = Maps.newHashMap();
        for (Property property : properties) {
            String name = property.getName().toString();
            Object value = property.getValue();
            resultMap.put(name, value);
        }
        return resultMap;
    }

}
