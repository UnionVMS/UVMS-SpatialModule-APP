package eu.europa.ec.fisheries.uvms.spatial.service.bean.handler;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaDisableService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.EezService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.EezDto;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;

@Stateless
@LocalBean
@Transactional
@Slf4j
public class EezSaverHandler extends AbstractSaverHandler implements SaverHandler {

    @EJB
    private EezService eezService;

    @Override
    protected void saveNewAreas(Map<String, Object> values, Date enabledOn) throws ServiceException, UnsupportedEncodingException {
        EezDto eezDto = new EezDto();
        eezDto.setName(readStringProperty(values, "name"));
        eezDto.setCountry(readStringProperty(values, "country"));
        eezDto.setSovereign(readStringProperty(values, "sovereign"));
        eezDto.setRemarks(readStringProperty(values, "remarks"));
        eezDto.setSovId((Long) values.get("sov_id"));
        eezDto.setEezId((Long) values.get("eez_id"));
        eezDto.setCode(readStringProperty(values, "code"));
        eezDto.setMrgid(BigInteger.valueOf(((Double) values.get("mrgid")).longValue()));
        eezDto.setDateChang(readStringProperty(values, "date_chang"));
        eezDto.setAreaM2((Double) values.get("area_m2"));
        eezDto.setLongitude((Double) values.get("longitude"));
        eezDto.setLatitude((Double) values.get("latitude"));
        eezDto.setMrgidEez((Long) values.get("mrgid_eez"));
        eezDto.setGeometry((Geometry) values.get("the_geom"));
        eezDto.setEnabledOn(enabledOn);
        eezDto.setEnabled(true);

        eezService.createEzz(eezDto);
    }

    @Override
    protected AreaDisableService getAreaDisableService() {
        return eezService;
    }

}
