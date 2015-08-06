package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.schema.spatial.source.GetAreaTypesSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.dao.SpatialDao;
import eu.europa.ec.fisheries.uvms.spatial.service.AreaService;

import javax.ejb.*;
import java.util.List;

import static java.util.Arrays.asList;

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
    public GetAreaTypesSpatialRS getAreaTypes() { // Todo return AreaDto please
        List<String> testData = asList("Portugal", "Belgium", "Poland", "Bulgaria"); //TODO remove it

        List<String> areaTypes = spatialDao.getAreaTypes();
        areaTypes.addAll(testData);

        return createResponse(areaTypes);
    }

    private GetAreaTypesSpatialRS createResponse(List<String> areaTypes) { //TODO move this code to rest layer
        GetAreaTypesSpatialRS response = new GetAreaTypesSpatialRS();
        response.setAreaTypes(areaTypes);
        return response;
    }

}
