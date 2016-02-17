package eu.europa.ec.fisheries.uvms.spatial.service.bean.handler;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.RfmoService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.RfmoDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import lombok.extern.slf4j.Slf4j;
import org.opengis.feature.Property;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Stateless
@LocalBean
@Transactional
@Slf4j
public class RfmoSaverHandler extends AbstractSaverHandler implements SaverHandler {

    @EJB
    private RfmoService rfmoService;

    @Override
    public void replaceAreas(Map<String, List<Property>> features) throws ServiceException {
        rfmoService.disableAllRfmoAreas();
        saveNewAreas(features);
    }

    private void saveNewAreas(Map<String, List<Property>> features) throws ServiceException {
        try {
            Date enabledOn = new Date();
            for (List<Property> properties : features.values()) {
                Map<String, Object> values = createAttributesMap(properties);

                RfmoDto rfmoDto = new RfmoDto();
                rfmoDto.setGeometry((Geometry) values.get("the_geom"));
                rfmoDto.setCode((String) values.get("code"));
                rfmoDto.setName((String) values.get("name"));
                rfmoDto.setTuna((String) values.get("tuna"));
                rfmoDto.setEnabled(true);
                rfmoDto.setEnabledOn(enabledOn);

                rfmoService.createRfmo(rfmoDto);
            }
        } catch (Exception e) {
            throw new SpatialServiceException(SpatialServiceErrors.INVALID_UPLOAD_AREA_DATA, e);
        }
    }

}
