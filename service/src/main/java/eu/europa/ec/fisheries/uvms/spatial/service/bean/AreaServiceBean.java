package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.schema.spatial.source.GetAreaTypesSpatialRS;
import eu.europa.ec.fisheries.schema.spatial.types.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.dao.SpatialDao;
import eu.europa.ec.fisheries.uvms.spatial.service.AreaService;

import javax.ejb.*;
import java.util.List;

/**
 * Created by kopyczmi on 03-Aug-15.
 */
@Stateless
@Local(AreaService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class AreaServiceBean implements AreaService {

    @EJB
    private SpatialDao spatialDao;

    @Override
    public GetAreaTypesSpatialRS getAreaTypes() {
        List<String> areaTypes = spatialDao.getAreaTypes();
        return createResponse(areaTypes);
    }

    private GetAreaTypesSpatialRS createResponse(List<String> areaTypeNames) {
        GetAreaTypesSpatialRS response = new GetAreaTypesSpatialRS();
        List<AreaType> areaTypes = Lists.transform(areaTypeNames, new Function<String, AreaType>() {
            @Override
            public AreaType apply(String areaName) {
                AreaType areaType = new AreaType();
                areaType.setTypeName(areaName);
                return areaType;
            }
        });
        response.setAreaTypes(areaTypes);
        return response;
    }

}
