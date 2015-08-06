package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.schema.spatial.source.GetAreaTypesSpatialRS;
import eu.europa.ec.fisheries.schema.spatial.types.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.dao.CrudDao;
import org.apache.commons.lang3.NotImplementedException;

import javax.ejb.*;
import java.util.List;

@Stateless
@Local(AreaService.class)
public class AreaServiceBean implements AreaService {

    @EJB
    private CrudDao crudDao;

    @Override
    public GetAreaTypesSpatialRS getAreaTypes() {
        List<String> areaTypes = crudDao.findByNativeQuery("SELECT a.typeName FROM AreaTypeEntity a", String.class);
        return createResponse(areaTypes);
    }

    // TODO change return type
    @Override
    public Object getAreasByLocation(double lat, double lon, int crs) {
        throw new NotImplementedException("Not implemented, yet");

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
