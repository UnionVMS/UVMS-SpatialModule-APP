package eu.europa.ec.fisheries.uvms.spatial.service.bean.handler;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaDisableService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.RfmoService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.RfmoDto;
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
    protected void saveNewAreas(Map<String, List<Property>> features, Date enabledOn) throws ServiceException {
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
    }

    @Override
    protected AreaDisableService getAreaDisableService() {
        return rfmoService;
    }

}
