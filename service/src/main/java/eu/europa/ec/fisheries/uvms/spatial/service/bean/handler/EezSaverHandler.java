package eu.europa.ec.fisheries.uvms.spatial.service.bean.handler;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaDisableService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.EezService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.EezDto;
import lombok.extern.slf4j.Slf4j;
import org.opengis.feature.Property;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Stateless
@LocalBean
@Transactional
@Slf4j
public class EezSaverHandler extends AbstractSaverHandler implements SaverHandler {

    @EJB
    private EezService eezService;

    @Override
    protected void saveNewAreas(Map<String, List<Property>> features, Date enabledOn) throws ServiceException {
        for (List<Property> properties : features.values()) {
            Map<String, Object> values = createAttributesMap(properties);

            EezDto eezDto = new EezDto();
            eezDto.setName((String) values.get("name"));
            eezDto.setCountry((String) values.get("country"));
            eezDto.setSovereign((String) values.get("sovereign"));
            eezDto.setRemarks((String) values.get("remarks"));
            eezDto.setSovId((Long) values.get("sov_id"));
            eezDto.setEezId((Long) values.get("eez_id"));
            eezDto.setCode((String) values.get("code"));
            eezDto.setMrgid(BigInteger.valueOf(((Double) values.get("mrgid")).longValue()));
            eezDto.setDateChang((String) values.get("date_chang"));
            eezDto.setAreaM2((Double) values.get("area_m2"));
            eezDto.setLongitude((Double) values.get("longitude"));
            eezDto.setLatitude((Double) values.get("latitude"));
            eezDto.setMrgidEez((Long) values.get("mrgid_eez"));
            eezDto.setGeometry((Geometry) values.get("the_geom"));
            eezDto.setEnabledOn(enabledOn);
            eezDto.setEnabled(true);

            eezService.createEzz(eezDto);
        }
    }

    @Override
    protected AreaDisableService getAreaDisableService() {
        return eezService;
    }

}
