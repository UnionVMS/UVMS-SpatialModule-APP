package eu.europa.ec.fisheries.uvms.spatial.dao;

import java.util.ArrayList;
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
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.AreaDto;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.geotools.geometry.jts.WKTWriter2;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import static eu.europa.ec.fisheries.uvms.service.QueryParameter.with;
import static eu.europa.ec.fisheries.uvms.spatial.entity.UserAreasEntity.*;

public class UserAreaDao extends AbstractDAO<UserAreasEntity> {

    private EntityManager em;

    private static final String USER_NAME = "userName";
    private static final String SCOPE_NAME = "scopeName";
    private static final String TYPE = "type";
    private static final String GID_LIST = "gids";

    public UserAreaDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<UserAreasEntity> intersects(final Geometry shape, final String userName) throws ServiceException {
        return findEntityByNamedQuery(UserAreasEntity.class, USER_AREA_DETAILS_BY_LOCATION, with("shape", shape).and("userName", userName).parameters());
    }

    public List<UserAreasEntity> intersects(final Geometry shape) throws ServiceException {
        return findEntityByNamedQuery(UserAreasEntity.class, USER_AREA_BY_COORDINATE, with("shape", shape).parameters());
    }

    public List<UserAreasEntity> findByUserNameAndGeometry(String userName, Point point) {

        List<UserAreasEntity> entityList = new ArrayList<>();

        if(!StringUtils.isBlank(userName) && point != null ){
            String wkt = new WKTWriter2().write(point);
            int crs = point.getSRID();
            Map<String, Object> parameters = ImmutableMap.<String, Object>builder().
                    put(USER_NAME, userName).
                    put("shape", "SRID=" + crs + ";" + wkt). // TODO Check on oracle
                    build();

            Query query = getSession().getNamedQuery(USER_AREA_DETAILS_BY_LOCATION);
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
            entityList = query.list();
        }
        return entityList;
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

    public List<Long> getAllSharedGids(String userName, String scopeName, String type) {
        Map<String, Object> parameters = ImmutableMap.<String, Object>builder().put(USER_NAME, userName).put(SCOPE_NAME, scopeName).put(TYPE, type).build();
        Query query = createNamedNativeQuery(FIND_GID_FOR_SHARED_AREA, parameters);
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

    public List<UserAreasEntity> listByCriteria(String userName, String scopeName, String searchCriteria, Boolean isPowerUser) throws ServiceException {

        Map parameters = with("isPowerUser", isPowerUser ? 1 : 0)
                .and("searchCriteria", "%" + searchCriteria + "%")
                .and("scopeName", scopeName)
                .and("userName", userName).parameters();

        return findEntityByNamedQuery(UserAreasEntity.class, SEARCH_BY_CRITERIA, parameters);

    }

    public UserAreasEntity getByUserNameAndName(String userName, String name) throws ServiceException {

        Map parameters = with("userName", userName)
                .and("name", name).parameters();

        UserAreasEntity result = null;

        List<UserAreasEntity> entityByNamedQuery =
                findEntityByNamedQuery(UserAreasEntity.class, FIND_BY_USERNAME_AND_NAME, parameters);

        if (!CollectionUtils.isEmpty(entityByNamedQuery)){
            result = entityByNamedQuery.get(0);
        }

        return result;

    }

    public UserAreasEntity findOne(final Long userAreaId, final String userName, final Boolean isPowerUser, final String scopeName) throws ServiceException {

        UserAreasEntity result = null;

        QueryParameter param = with("userAreaId", userAreaId)
                .and("userName", userName)
                .and("isPowerUser", isPowerUser ? 1 : 0)
                .and("scopeName", scopeName);

        List<UserAreasEntity> entityByNamedQuery = findEntityByNamedQuery(UserAreasEntity.class, FIND_USER_AREA_BY_ID, param.parameters(), 1);

        if (!CollectionUtils.isEmpty(entityByNamedQuery)){
            result = entityByNamedQuery.get(0);
        }

        return result;

    }

    public UserAreasEntity save(UserAreasEntity userAreasEntity) throws ServiceException {
        return createEntity(userAreasEntity);
    }

    public UserAreasEntity update(UserAreasEntity entity) throws ServiceException {
        return updateEntity(entity);
    }

    public List<UserAreasEntity> findByUserNameAndScopeNameAndTypeAndPowerUser(String userName, String scopeName, String type, boolean isPowerUser) throws ServiceException {

        QueryParameter param = with("type", type)
                .and("userName", userName)
                .and("isPowerUser", isPowerUser ? 1 : 0)
                .and("scopeName", scopeName);

        return findEntityByNamedQuery(UserAreasEntity.class, UserAreasEntity.FIND_USER_AREA_BY_TYPE, param.parameters());
    }

    public List<UserAreasEntity> findUserAreasTypes(final String userName, final String scopeName, final Boolean isPowerUser) throws ServiceException {

        QueryParameter param = with("userName", userName)
                .and("isPowerUser", isPowerUser ? 1 : 0)
                .and("scopeName", scopeName);

        return findEntityByNamedQuery(UserAreasEntity.class, UserAreasEntity.FIND_USER_AREA_BY_USER, param.parameters());
    }
}
