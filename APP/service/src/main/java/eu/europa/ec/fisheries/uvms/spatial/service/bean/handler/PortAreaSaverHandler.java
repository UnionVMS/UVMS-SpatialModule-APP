package eu.europa.ec.fisheries.uvms.spatial.service.bean.handler;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaDisableService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.PortAreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.PortAreaDto;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.Map;

@Stateless
@LocalBean
@Transactional
@Slf4j
public class PortAreaSaverHandler extends AbstractSaverHandler implements SaverHandler {

    @EJB
    private PortAreaService portAreaService;

    @Override
    protected void saveNewAreas(Map<String, Object> values, Date enabledOn) throws ServiceException {
        PortAreaDto portAreaDto = new PortAreaDto();
        portAreaDto.setGeometry((Geometry) values.get("the_geom"));
        portAreaDto.setCode((String) values.get("code"));
        portAreaDto.setName((String) values.get("name"));
        portAreaDto.setEnabled(true);
        portAreaDto.setEnabledOn(enabledOn);

        portAreaService.createPortArea(portAreaDto);
    }

    @Override
    protected AreaDisableService getAreaDisableService() {
        return portAreaService;
    }
}
