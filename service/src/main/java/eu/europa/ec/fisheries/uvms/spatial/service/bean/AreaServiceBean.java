package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.schema.spatial.source.GetAreaTypesSpatialRS;
import eu.europa.ec.fisheries.schema.spatial.source.GetAreasByLocationRS;
import eu.europa.ec.fisheries.schema.spatial.types.AreaType;
import eu.europa.ec.fisheries.uvms.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.exception.SpatialServiceException;
import eu.europa.ec.fisheries.uvms.spatial.dao.CommonGenericDAO;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaTypeEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import java.util.List;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

@Stateless
@Local(AreaService.class)
public class AreaServiceBean implements AreaService {

    private final static Logger LOG = LoggerFactory.getLogger(AreaServiceBean.class);

    @EJB
    private CommonGenericDAO commonGenericDAO;

    @Override
    @SuppressWarnings("unchecked")
    public GetAreaTypesSpatialRS getAreaTypes() {
        List<String> areaTypes = null;
        try {
            //areaTypes = commonGenericDAO.findEntityByQuery(String.class, "SELECT a.typeName FROM AreaTypeEntity a");
            areaTypes = commonGenericDAO.findEntityByNamedQuery(String.class, AreaTypeEntity.FIND_ALL);
        } catch (Exception e) {
            throw new SpatialServiceException(SpatialServiceErrors.DAO_FIX_IT_ERROR, e.getCause());
        }
        return createGetAreaTypesResponse(areaTypes);
    }

    @Override
    public GetAreasByLocationRS getAreasByLocation(double lat, double lon, int crs) {
        //commonGenericDAO.
        return createGetAreasByLocationResponse();
    }

    private GetAreasByLocationRS createGetAreasByLocationResponse() {
        GetAreasByLocationRS response = new GetAreasByLocationRS();

//        if (areaTypesWithId)
//        ArrayList<GetAreasByLocationRS.AreaTypes> objects = Lists.newArrayList();
//        response.setAreaTypes(objects);

        return response;
    }

    private GetAreaTypesSpatialRS createGetAreaTypesResponse(List<String> areaTypeNames) {
        GetAreaTypesSpatialRS response = new GetAreaTypesSpatialRS();
        if (isNotEmpty(areaTypeNames)) {
            List<AreaType> areaTypes = Lists.transform(areaTypeNames, new Function<String, AreaType>() {
                @Override
                public AreaType apply(String areaName) {
                    AreaType areaType = new AreaType();
                    areaType.setTypeName(areaName);
                    return areaType;
                }
            });
            response.setAreaTypes(areaTypes);
        }
        return response;
    }

}
