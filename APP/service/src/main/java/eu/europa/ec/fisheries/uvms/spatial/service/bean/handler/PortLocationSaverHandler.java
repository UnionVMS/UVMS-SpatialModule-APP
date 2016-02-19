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
    protected void saveNewAreas(Map<String, Object> values, Date enabledOn) throws ServiceException {
        try {
            PortLocationDto portLocationDto = new PortLocationDto();
            portLocationDto.setGeometry((Geometry) values.get("the_geom"));
            portLocationDto.setCode((String) values.get("code"));
            portLocationDto.setName((String) values.get("name"));
            portLocationDto.setCountryCode((String) values.get("country_co"));
            portLocationDto.setFishingPort((String) values.get("fishing_po"));
            portLocationDto.setLandingPlace((String) values.get("landing_pl"));
            portLocationDto.setCommercialPort((String) values.get("commercial"));
            portLocationDto.setEnabled(true);
            portLocationDto.setEnabledOn(enabledOn);

            portLocationService.createPortLocation(portLocationDto);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected AreaDisableService getAreaDisableService() {
        return portLocationService;
    }
}
