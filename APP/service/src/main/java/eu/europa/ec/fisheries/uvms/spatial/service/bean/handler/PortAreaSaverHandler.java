package eu.europa.ec.fisheries.uvms.spatial.service.bean.handler;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaDisableService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.EezService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.PortAreaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
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
public class PortAreaSaverHandler extends AbstractSaverHandler implements SaverHandler {

    @EJB
    private PortAreaService portAreaService;

    @Override
    protected void saveNewAreas(Map<String, List<Property>> features, Date enabledOn) throws ServiceException {
        throw new NotImplementedException("Not yet implemented");
    }

    @Override
    protected AreaDisableService getAreaDisableService() {
        throw new NotImplementedException("Not yet implemented");
    }
}
