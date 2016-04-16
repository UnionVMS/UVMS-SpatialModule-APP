package eu.europa.ec.fisheries.uvms.spatial.service.bean.handler;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.RfmoService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.RfmoDto;
import lombok.extern.slf4j.Slf4j;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

@Stateless
@LocalBean
@Transactional
@Slf4j
public class RfmoSaverHandler extends AbstractSaverHandler implements SaverHandler {

    @EJB
    private RfmoService rfmoService;

    @Override
    protected void saveNewAreas(Map<String, Object> values, Date enabledOn) throws ServiceException, UnsupportedEncodingException {
        RfmoDto rfmoDto = new RfmoDto();
        rfmoDto.setGeometry((Geometry) values.get("the_geom"));
        rfmoDto.setCode(readStringProperty(values, "code"));
        rfmoDto.setName(readStringProperty(values, "name"));
        rfmoDto.setTuna(readStringProperty(values, "tuna"));
        rfmoDto.setEnabled(true);
        rfmoDto.setEnabledOn(enabledOn);

        rfmoService.createRfmo(rfmoDto);
    }

}
