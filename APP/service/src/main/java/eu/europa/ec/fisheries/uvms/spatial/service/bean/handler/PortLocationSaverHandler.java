package eu.europa.ec.fisheries.uvms.spatial.service.bean.handler;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaDisableService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.PortLocationService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.PortLocationDto;
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
public class PortLocationSaverHandler extends AbstractSaverHandler implements SaverHandler {

    @EJB
    private PortLocationService portLocationService;

    @Override
    protected void saveNewAreas(Map<String, Object> values, Date enabledOn) throws ServiceException, UnsupportedEncodingException {
        PortLocationDto portLocationDto = new PortLocationDto();
        portLocationDto.setGeometry((Geometry) values.get("the_geom"));
        portLocationDto.setCode(readStringProperty(values, "code"));
        portLocationDto.setName(readStringProperty(values, "name"));
        portLocationDto.setCountryCode(readStringProperty(values, "country_co"));
        portLocationDto.setFishingPort(readStringProperty(values, "fishing_po"));
        portLocationDto.setLandingPlace(readStringProperty(values, "landing_pl"));
        portLocationDto.setCommercialPort(readStringProperty(values, "commercial"));
        portLocationDto.setEnabled(true);
        portLocationDto.setEnabledOn(enabledOn);

        portLocationService.createPortLocation(portLocationDto);
    }

    @Override
    protected AreaDisableService getAreaDisableService() {
        return portLocationService;
    }
}
