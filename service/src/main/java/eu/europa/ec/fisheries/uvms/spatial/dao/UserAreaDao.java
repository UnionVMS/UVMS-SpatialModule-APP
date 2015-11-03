package eu.europa.ec.fisheries.uvms.spatial.dao;

import static eu.europa.ec.fisheries.uvms.spatial.util.SpatialUtils.convertToWkt;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import com.google.common.collect.ImmutableMap;
import com.vividsolutions.jts.geom.Point;

import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaLayerDto;

public class UserAreaDao extends AbstractDao {
	
	private static final String USER_NAME = "userName";
	private static final String SCOPE_NAME = "scopeName";
	private static final String NAME = "name";
	private static final String DESC = "desc";
	private static final String CRS = "crs";
	private static final String WKT = "wktPoint";
	
	public UserAreaDao(EntityManager em) {
		super(em);
	}
	
    @SuppressWarnings("unchecked")
    public List<UserAreaLayerDto> findUserAreaLayerMapping() {
    	return createQuery(QueryNameConstants.FIND_USER_AREA_LAYER, UserAreaLayerDto.class).list();
    }
    
    @SuppressWarnings("unchecked")
	public List<UserAreaDto> findUserAreaDetails(String userName, String scopeName, Point point) {
        String wktPoint = convertToWkt(point);
        int crs = point.getSRID();
    	Map<String, Object> parameters = ImmutableMap.<String, Object>builder().
    			put(USER_NAME, userName).
    			put(SCOPE_NAME, scopeName).
    			put(WKT, wktPoint).
    			put(CRS, crs).
    			build();
    	return createNamedNativeQuery(QueryNameConstants.USER_AREA_DETAILS, parameters, UserAreaDto.class).list();
    }
    
    @SuppressWarnings("unchecked")
    public List<UserAreaDto> findUserAreaDetailsBySearchCriteria(String userName, String scopeName, String searchCriteria) {
    	Map<String, Object> parameters = ImmutableMap.<String, Object>builder().
    			put(USER_NAME, userName).
    			put(SCOPE_NAME, scopeName).
    			put(NAME, "%"+searchCriteria+"%").
    			put(DESC, "%"+searchCriteria+"%").
    			build();
    	return createNamedNativeQuery(QueryNameConstants.SEARCH_USER_AREA, parameters, UserAreaDto.class).list();
    }

}
