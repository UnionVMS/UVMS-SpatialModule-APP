package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.uvms.service.CrudService;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;

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
    private CrudService crudService;

    @Override
    @SuppressWarnings("unchecked")
    public List<String> listAllAreaTypeNames() {
        List<AreaLocationTypesEntity> areas = crudService.findEntityByNamedQuery(AreaLocationTypesEntity.class, QueryNameConstants.FIND_ALL_AREAS);
        return convertToTypeNameList(areas);
    }

    private List<String> convertToTypeNameList(List<AreaLocationTypesEntity> areas) {
        return Lists.transform(Lists.newArrayList(areas), new Function<AreaLocationTypesEntity, String>() {
            @Override
            public String apply(AreaLocationTypesEntity area) {
                return area.getTypeName();
            }
        });
    }
}
