package eu.europa.ec.fisheries.uvms.spatial.dao;

import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import com.google.common.collect.ImmutableMap;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.spatial.entity.UserAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.constants.USMSpatial;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.UserAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.AreaDto;
import org.geotools.geometry.jts.WKTWriter2;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

public class UserAreaJpaDao extends AbstractDAO<UserAreasEntity> {

    private EntityManager em;

    private static final String USER_NAME = "userName";
	private static final String NAME = "name";
	private static final String DESC = "desc";
	private static final String CRS = "crs";
	private static final String WKT = "wktPoint";
	private static final String GID_LIST = "gids";

    private NativeQueries nativeQueries;

    public UserAreaJpaDao(EntityManager em) {
        this.em = em;
    }


    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List findUserAreaLayerMapping() {

        Query query = getSession().getNamedQuery(QueryNameConstants.FIND_USER_AREA_LAYER);
        query.setResultTransformer(Transformers.aliasToBean(UserAreaLayerDto.class));
        return query.list();
    }

    public void initializeNatieveQueries(){
        nativeQueries = new PostgreNativeQueries(em);
    }

	public List<UserAreaDto> findUserAreaDetailsWithExtent(String userName, Point point) {
        String wkt = new WKTWriter2().write(point);
        int crs = point.getSRID();
    	Map<String, Object> parameters = ImmutableMap.<String, Object>builder().
    			put(USER_NAME, userName).
    			put(WKT, wkt).
    			put(CRS, crs).
    			build();

        Query query = createNamedNativeQuery(QueryNameConstants.USER_AREA_DETAILS_WITH_EXTENT_BY_LOCATION, parameters);
        query.setResultTransformer(Transformers.aliasToBean(UserAreaDto.class));
        return query.list();
    }

	public List<UserAreasEntity> findUserAreaDetailsWithGeom(String userName, Point point) {
        String wkt = new WKTWriter2().write(point);
        int crs = point.getSRID();
		Map<String, Object> parameters = ImmutableMap.<String, Object>builder().
                put(USER_NAME, userName).
				put(WKT, wkt).
				put(CRS, crs).
				build();

        Query query = getSession().getNamedQuery(QueryNameConstants.USER_AREA_DETAILS_BY_LOCATION);
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.list();
	}

    public List<UserAreaDto> findUserAreaDetailsBySearchCriteria(String userName, String scopeName, String searchCriteria, boolean isPowerUser) {
    	Map<String, Object> parameters = ImmutableMap.<String, Object>builder().
    			put(USER_NAME, userName).
    			put(USMSpatial.SCOPE_NAME, scopeName).
    			put(NAME, "%" + searchCriteria + "%").
    			put(DESC, "%" + searchCriteria + "%").
				put("isPowerUser", isPowerUser ? 1 : 0).
    			build();

        Query query = createNamedNativeQuery(QueryNameConstants.SEARCH_USER_AREA, parameters);
        query.setResultTransformer(Transformers.aliasToBean(UserAreaDto.class));
        return query.list();
    }

    public List<AreaDto> getAllUserAreas(String userName) {
		Map<String, Object> parameters = ImmutableMap.<String, Object>builder().put(USER_NAME, userName).build();
        Query query = createNamedNativeQuery(QueryNameConstants.FIND_ALL_USER_AREAS, parameters);
        query.setResultTransformer(Transformers.aliasToBean(AreaDto.class));
        return query.list();
	}

    public List<AreaDto> findAllUserAreasByGids(List<Long> gids) {
        Query query = getSession().getNamedQuery(QueryNameConstants.FIND_ALL_USER_AREAS_BY_GIDS);
        query.setParameterList(GID_LIST, gids);
        query.setResultTransformer(Transformers.aliasToBean( AreaDto.class));
        return query.list();
	}

    private Query createNamedNativeQuery(String nativeQueryString, Map<String, Object> parameters) {
        Query query = getSession().getNamedQuery(nativeQueryString);
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query;
    }

    private Session getSession() {
        return em.unwrap(Session.class);
    }

}
