package eu.europa.ec.fisheries.uvms.spatial.dao;

import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import com.google.common.collect.ImmutableMap;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.spatial.entity.UserAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.GeometryType;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.UserAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.AreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.GeometryMapper;

public class UserAreaDao extends CommonDao {
	
	private static final String USER_NAME = "userName";
	private static final String SCOPE_NAME = "scopeName";
	private static final String NAME = "name";
	private static final String DESC = "desc";
	private static final String CRS = "crs";
	private static final String WKT = "wktPoint";
	private static final String GID_LIST = "gids";
	
	public UserAreaDao(EntityManager em) {
		super(em);
	}
	
    @SuppressWarnings("unchecked")
    public List<UserAreaLayerDto> findUserAreaLayerMapping() {
    	return createNamedQuery(QueryNameConstants.FIND_USER_AREA_LAYER, UserAreaLayerDto.class).list();
    }
    
    @SuppressWarnings("unchecked")
	public List<UserAreaDto> findUserAreaDetailsWithExtent(String userName, Point point) {
        GeometryType geometryType = GeometryMapper.INSTANCE.geometryToWKT(point);
        int crs = point.getSRID();
    	Map<String, Object> parameters = ImmutableMap.<String, Object>builder().
    			put(USER_NAME, userName).
    			put(WKT, geometryType.getGeometry()).
    			put(CRS, crs).
    			build();
    	return createNamedNativeQuery(QueryNameConstants.USER_AREA_DETAILS_WITH_EXTENT_BY_LOCATION, parameters, UserAreaDto.class).list();
    }

	@SuppressWarnings("unchecked")
	public List<UserAreasEntity> findUserAreaDetailsWithGeom(String userName, Point point) {
		GeometryType geometryType = GeometryMapper.INSTANCE.geometryToWKT(point);
		int crs = point.getSRID();
		Map<String, Object> parameters = ImmutableMap.<String, Object>builder().
				put(USER_NAME, userName).
				put(WKT, geometryType.getGeometry()).
				put(CRS, crs).
				build();
		return createNamedNativeQuery(QueryNameConstants.USER_AREA_DETAILS_BY_LOCATION, parameters).list();
	}

    @SuppressWarnings("unchecked")
    public List<UserAreaDto> findUserAreaDetailsBySearchCriteria(String userName, String scopeName, String searchCriteria) {
    	Map<String, Object> parameters = ImmutableMap.<String, Object>builder().
    			put(USER_NAME, userName).
    			put(SCOPE_NAME, scopeName).
    			put(NAME, "%" + searchCriteria + "%").
    			put(DESC, "%" + searchCriteria + "%").
    			build();
    	return createNamedNativeQuery(QueryNameConstants.SEARCH_USER_AREA, parameters, UserAreaDto.class).list();
    }

	public List<AreaDto> getAllUserAreas(String userName) {
		Map<String, Object> parameters = ImmutableMap.<String, Object>builder().put(USER_NAME, userName).build();
		return createNamedNativeQuery(QueryNameConstants.FIND_ALL_USER_AREAS, parameters, AreaDto.class).list();
	}

	public List<AreaDto> findAllUserAreasByGids(List<Long> gids) {
		return createNamedQueryWithParameterList(QueryNameConstants.FIND_ALL_USER_AREAS_BY_GIDS, GID_LIST, gids, AreaDto.class).list();
	}

}
