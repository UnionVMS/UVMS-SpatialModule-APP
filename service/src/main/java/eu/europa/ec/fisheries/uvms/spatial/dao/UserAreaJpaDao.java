package eu.europa.ec.fisheries.uvms.spatial.dao;

import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import com.google.common.collect.ImmutableMap;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.service.QueryParameter;
import eu.europa.ec.fisheries.uvms.spatial.entity.UserAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.AreaDto;
import org.geotools.geometry.jts.WKTWriter2;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

public class UserAreaJpaDao extends AbstractDAO<UserAreasEntity> {

    private EntityManager em;

    private static final String USER_NAME = "userName";
    private static final String SCOPE_NAME = "scopeName";
	private static final String CRS = "crs";
	private static final String WKT = "wktPoint";
	private static final String GID_LIST = "gids";

    public UserAreaJpaDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<UserAreasEntity> intersects(final Geometry shape, final String userName) throws ServiceException {
        return findEntityByNamedQuery(UserAreasEntity.class, UserAreasEntity.USER_AREA_DETAILS_BY_LOCATION, QueryParameter.with("shape", shape).and("userName", userName).parameters());
    }

    public List<UserAreasEntity> intersects(final Geometry shape) throws ServiceException {
        return findEntityByNamedQuery(UserAreasEntity.class, UserAreasEntity.USER_AREA_BY_COORDINATE, QueryParameter.with("shape", shape).parameters());
    }

    public List findUserAreaLayerMapping() {

        Query query = getSession().getNamedQuery(QueryNameConstants.FIND_USER_AREA_LAYER);
        query.setResultTransformer(Transformers.aliasToBean(UserAreaLayerDto.class));
        return query.list();
    }

	public List<UserAreasEntity> findUserAreaDetailsWithGeom(String userName, Point point) {
        String wkt = new WKTWriter2().write(point);
        int crs = point.getSRID();
		Map<String, Object> parameters = ImmutableMap.<String, Object>builder().
                put(USER_NAME, userName).
				put("shape", "SRID=" + crs + ";" + wkt).
				build();

        Query query = getSession().getNamedQuery(UserAreasEntity.USER_AREA_DETAILS_BY_LOCATION);
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.list();
	}

    public List<AreaDto> getAllUserAreas(String userName, String scopeName) {
		Map<String, Object> parameters = ImmutableMap.<String, Object>builder().put(USER_NAME, userName).put(SCOPE_NAME, scopeName).build();
        Query query = createNamedNativeQuery(QueryNameConstants.FIND_ALL_USER_AREAS, parameters);
        query.setResultTransformer(Transformers.aliasToBean(AreaDto.class));
        return query.list();
	}

    public List<AreaDto> getAllUserAreaGroupName(String userName, String scopeName) {
        Map<String, Object> parameters = ImmutableMap.<String, Object>builder().put(USER_NAME, userName).put(SCOPE_NAME, scopeName).build();
        Query query = createNamedNativeQuery(QueryNameConstants.FIND_ALL_USER_AREAS_GROUP, parameters);
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