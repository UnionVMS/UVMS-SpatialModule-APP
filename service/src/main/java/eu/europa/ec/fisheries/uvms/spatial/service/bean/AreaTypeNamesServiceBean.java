package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.repository.SpatialRepository;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
@Local(AreaTypeNamesService.class)
@Transactional
public class AreaTypeNamesServiceBean implements AreaTypeNamesService {

    @EJB
    private SpatialRepository repository;

    @Override
    @SuppressWarnings("unchecked")
    public List<String> listAllAreaTypeNames() {
        return repository.findEntityByNamedQuery(String.class, QueryNameConstants.FIND_ALL_AREA_TYPE_NAMES);
    }
}
