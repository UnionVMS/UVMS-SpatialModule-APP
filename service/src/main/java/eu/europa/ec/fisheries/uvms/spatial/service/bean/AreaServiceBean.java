package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
import java.util.HashMap;
import java.util.List;

import static java.lang.String.valueOf;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

@Stateless
@Local(AreaService.class)
public class AreaServiceBean implements AreaService {

    public static final String LAT = "lat";
    public static final String LON = "lon";
    public static final String CRS = "crs";
    private final static Logger LOG = LoggerFactory.getLogger(AreaServiceBean.class);
    @EJB
    private CommonGenericDAO commonDao;

    @Override
    @SuppressWarnings("unchecked")
    public GetAreaTypesSpatialRS getAreaTypes() {
        List<String> areaTypes = null;
        try {
            areaTypes = commonDao.findEntityByNamedQuery(String.class, AreaTypeEntity.FIND_ALL);
        } catch (Exception e) {
            LOG.error("Error during getting areas types", e);
            throw new SpatialServiceException(SpatialServiceErrors.DAO_FIX_IT_ERROR, e.getCause());
        }
        return createGetAreaTypesResponse(areaTypes);
    }

    @Override
    public GetAreasByLocationRS getAreasByLocation(double lat, double lon, int crs) {
        List<AreaTypeEntity> systemAreaTypes = commonDao.findEntityByNamedQuery(AreaTypeEntity.class, AreaTypeEntity.FIND_SYSTEM);
        for (AreaTypeEntity areaType : systemAreaTypes) {
            String areaDbTable = areaType.getAreaDbTable();

            HashMap<String, String> paramaters = createParamaters(lat, lon, crs);
            List resultList = commonDao.findEntityByNativeQuery("SELECT * FROM " + areaDbTable, paramaters);
        }

        return createGetAreasByLocationResponse();
    }

    private HashMap<String, String> createParamaters(double lat, double lon, int crs) {
        HashMap<String, String> result = Maps.newHashMap();
        result.put(LAT, valueOf(lat));
        result.put(LON, valueOf(lon));
        result.put(CRS, valueOf(crs));
        return result;
    }

    private GetAreasByLocationRS createGetAreasByLocationResponse() {
        GetAreasByLocationRS response = new GetAreasByLocationRS();

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
