package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.schema.spatial.source.GetAreaTypesSpatialRS;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.Arrays;

/**
 * Created by kopyczmi on 03-Aug-15.
 */
@Stateless
@Local(AreaService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class AreaServiceBean implements AreaService {

    @Override
    public GetAreaTypesSpatialRS getAreaTypes() {
        GetAreaTypesSpatialRS response = new GetAreaTypesSpatialRS();
        response.setAreaType(Arrays.asList("Portugal", "Belgium", "Poland", "Bulgaria"));
        return response;
    }

}
