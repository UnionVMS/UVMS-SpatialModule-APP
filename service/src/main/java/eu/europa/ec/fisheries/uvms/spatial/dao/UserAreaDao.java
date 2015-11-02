package eu.europa.ec.fisheries.uvms.spatial.dao;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import com.google.common.collect.ImmutableMap;

import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaLayerDto;

public class UserAreaDao extends AbstractDao {
	
	private String USER_NAME = "userName";
	private String SCOPE_NAME = "scopeName";
	
	public UserAreaDao(EntityManager em) {
		super(em);
	}
	
    @SuppressWarnings("unchecked")
    public List<UserAreaLayerDto> findUserAreaLayerMapping() {
    	return createQuery(QueryNameConstants.FIND_USER_AREA_LAYER, UserAreaLayerDto.class).list();
    }
    
    @SuppressWarnings("unchecked")
	public List<UserAreaDto> findUserAreaDetails(String userName, String scopeName) {
    	Map<String, String> parameters = ImmutableMap.<String, String>builder().put(USER_NAME, userName).put(SCOPE_NAME, scopeName).build();
    	return createNamedNativeQuery(QueryNameConstants.USER_AREA_DETAILS, parameters, UserAreaDto.class).list();
    }

}
