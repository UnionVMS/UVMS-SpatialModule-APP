package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.entity.AreaTypeEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreasNameType;
import org.apache.commons.lang3.NotImplementedException;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
@Local(AreaTypeService.class)
@Transactional(Transactional.TxType.REQUIRED)
public class AreaTypeServiceBean extends AbstractServiceBean implements AreaTypeService {

    @Override
    @SuppressWarnings("unchecked")
    public AreaTypeSpatialRS getAreaTypes() {
        List<String> areaTypes = execute();

        return createSuccessGetAreaTypesResponse(areaTypes);
    }

    private List<String> execute() {
        return commonDao.findEntityByNamedQuery(String.class, AreaTypeEntity.FIND_ALL);
    }

    private AreaTypeSpatialRS createSuccessGetAreaTypesResponse(List<String> areaTypeNames) {
        return new AreaTypeSpatialRS(createSuccessResponseMessage(), new AreasNameType(areaTypeNames));
    }

    @Override
    public Object execute(Object o) {
        throw new NotImplementedException("Not implemented yet");
    }
}
