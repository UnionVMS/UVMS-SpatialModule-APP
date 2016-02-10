package eu.europa.ec.fisheries.uvms.spatial.service.bean.handler;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.opengis.feature.Property;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Stateless
@LocalBean
@Transactional
@Slf4j
public class RfmoSaverHandler implements SaverHandler {

    @Override
    public void save(Map<String, List<Property>> features) throws ServiceException {
        throw new NotImplementedException("Not yet implemented");
    }

}
