package eu.europa.ec.fisheries.uvms.spatial.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import eu.europa.ec.fisheries.uvms.service.QueryParameter;
import eu.europa.ec.fisheries.uvms.spatial.entity.UserAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.GeometryType;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.ServiceLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.GeometryMapper;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaLayerDto;
import org.hibernate.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

public class AreaDao extends CommonDao {

    private static final String SUB_TYPE = "subTypes";

    public AreaDao(EntityManager em) {
    	super(em);
    }

    public List findAreaOrLocationByCoordinates(Point point, String nativeQueryString) {
        GeometryType geometryType = GeometryMapper.INSTANCE.geometryToWKT(point);
        int crs = point.getSRID();
        return createNamedNativeQuery(nativeQueryString, geometryType.getGeometry(), crs).list();
    }

    @SuppressWarnings("unchecked")
    public List<AreaLayerDto> findSystemAreaLayerMapping() {
        return createNamedQuery(QueryNameConstants.FIND_SYSTEM_AREA_LAYER, AreaLayerDto.class).list();
    }

    @SuppressWarnings("unchecked")
    public List<AreaLayerDto> findSystemAreaAndLocationLayerMapping() {
        return createNamedQuery(QueryNameConstants.FIND_SYSTEM_AREA_AND_LOCATION_LAYER, AreaLayerDto.class).list();
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, String>> findSelectedAreaColumns(String namedQueryString, Number gid) {
        Query query = getSession().getNamedQuery(namedQueryString);
        query.setParameter("gid", gid);
        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        return query.list();
    }

    public List<ServiceLayerDto> findServiceLayerBySubType(List<String> subAreaTypes, boolean isWithBing) {
        if (isWithBing) {
            return createNamedQueryWithParameterList(QueryNameConstants.FIND_SERVICE_LAYER_BY_SUBTYPE, SUB_TYPE, subAreaTypes, ServiceLayerDto.class).list();
        } else {
            return createNamedQueryWithParameterList(QueryNameConstants.FIND_SERVICE_LAYER_BY_SUBTYPE_WITHOUT_BING, SUB_TYPE, subAreaTypes, ServiceLayerDto.class).list();
        }

    }
}
