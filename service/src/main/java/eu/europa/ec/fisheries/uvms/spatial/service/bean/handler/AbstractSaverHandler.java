package eu.europa.ec.fisheries.uvms.spatial.service.bean.handler;

import com.google.common.collect.Maps;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaDisableService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import org.opengis.feature.Property;

import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class AbstractSaverHandler {

    protected abstract void saveNewAreas(Map<String, Object> values, Date enabledOn) throws ServiceException;

    protected abstract AreaDisableService getAreaDisableService();

    public void replaceAreas(Map<String, List<Property>> features) throws ServiceException {
        getAreaDisableService().disableAllAreas();
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

}
