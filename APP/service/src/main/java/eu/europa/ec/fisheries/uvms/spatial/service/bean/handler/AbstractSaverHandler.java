package eu.europa.ec.fisheries.uvms.spatial.service.bean.handler;

import com.google.common.collect.Maps;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaDisableService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import org.opengis.feature.Property;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class AbstractSaverHandler {

    protected abstract void saveNewAreas(Map<String, Object> values, Date enabledOn) throws ServiceException, UnsupportedEncodingException;

    protected abstract AreaDisableService getAreaDisableService();

    public void replaceAreas(Map<String, List<Property>> features) throws ServiceException {
        getAreaDisableService().disableAllAreas(); // FIXME this is illegal use of EJB
        try {
            Date enabledOn = new Date();
            for (List<Property> properties : features.values()) {
                Map<String, Object> values = createAttributesMap(properties);
                saveNewAreas(values, enabledOn);
            }
        } catch (Exception e) {
            throw new SpatialServiceException(SpatialServiceErrors.INVALID_UPLOAD_AREA_DATA, e);
        }
    }

    protected Map<String, Object> createAttributesMap(List<Property> properties) {
        Map<String, Object> resultMap = Maps.newHashMap();
        for (Property property : properties) {
            String name = property.getName().toString();
            Object value = property.getValue();
            resultMap.put(name, value);
        }
        return resultMap;
    }

    protected String readStringProperty(Map<String, Object> values, String propertyName) throws UnsupportedEncodingException {
        return new String(((String) values.get(propertyName)).getBytes("ISO-8859-1"), "UTF-8");
    }

}
