package eu.europa.ec.fisheries.uvms.spatial.service.rest;

import eu.europa.ec.fisheries.uvms.service.CommonGenericDAO;
import eu.europa.ec.fisheries.uvms.service.exception.CommonGenericDAOException;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import lombok.SneakyThrows;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Michal Kopyczok on 24-Aug-15.
 */
@Stateless
@Local(EezRestService.class)
@Transactional
public class AreaTypeRestServiceBean implements AreaTypeRestService {

    @EJB
    private CommonGenericDAO commonDao;

    @Override
    @SuppressWarnings("unchecked")
    @SneakyThrows(CommonGenericDAOException.class)
    public List<String> getAreaTypes() {
        return commonDao.findEntityByNamedQuery(String.class, QueryNameConstants.FIND_ALL_AREAS);
    }
}
