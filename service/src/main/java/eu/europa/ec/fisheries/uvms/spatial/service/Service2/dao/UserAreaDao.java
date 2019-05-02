/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.spatial.service.Service2.dao;

import static eu.europa.ec.fisheries.uvms.commons.service.dao.QueryParameter.with;
import static eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.UserAreasEntity.BY_INTERSECT;
import static eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.UserAreasEntity.DISABLE;
import static eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.UserAreasEntity.FIND_BY_USERNAME_AND_NAME;
import static eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.UserAreasEntity.FIND_BY_USER_NAME_AND_SCOPE_NAME;
import static eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.UserAreasEntity.FIND_GID_FOR_SHARED_AREA;
import static eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.UserAreasEntity.FIND_USER_AREA_BY_ID;
import static eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.UserAreasEntity.FIND_USER_AREA_BY_TYPE;
import static eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.UserAreasEntity.FIND_USER_AREA_BY_USER;
import static eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.UserAreasEntity.SEARCH_BY_CRITERIA;
import static eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.UserAreasEntity.SEARCH_USERAREA;
import static eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.UserAreasEntity.SEARCH_USERAREA_NAMES_BY_CODE;
import static eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.UserAreasEntity.USER_AREA_BY_COORDINATE;
import static eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.UserAreasEntity.USER_AREA_DETAILS_BY_LOCATION;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.commons.service.dao.QueryParameter;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto.area.AreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto.upload.UploadMappingProperty;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.UserAreasEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

@Slf4j
public class UserAreaDao extends AbstractAreaDao<UserAreasEntity> {

    private EntityManager em;

    private static final String USER_NAME = "userName";
    private static final String SCOPE_NAME = "scopeName";
    private static final String TYPE = "type";
    private static final String GID_LIST = "gids";
    private static final String IS_POWER_USER = "isPowerUser";

    public UserAreaDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<UserAreasEntity> intersects(final Geometry shape, final String userName) throws ServiceException {
        return findEntityByNamedQuery(UserAreasEntity.class, USER_AREA_DETAILS_BY_LOCATION,
                with("shape", shape).and(USER_NAME, userName).parameters());
    }

    public List<UserAreasEntity> intersects(final Geometry shape) throws ServiceException {
        return findEntityByNamedQuery(UserAreasEntity.class, USER_AREA_BY_COORDINATE, with("shape", shape).parameters());
    }

    @Override
    protected String getIntersectNamedQuery() {
        return BY_INTERSECT;
    }

    @Override
    protected String getSearchNamedQuery() {
        return SEARCH_USERAREA;
    }

    @Override
    protected String getSearchNameByCodeQuery() {
        return SEARCH_USERAREA_NAMES_BY_CODE;
    }

    @Override
    protected Class<UserAreasEntity> getClazz() {
        return UserAreasEntity.class;
    }

    @Override
    protected UserAreasEntity createEntity(Map<String, Object> values, List<UploadMappingProperty> mapping) throws ServiceException {
        return new UserAreasEntity(values);
    }

    @Override
    protected String getDisableAreaNamedQuery() {
        return DISABLE;
    }

    public List<UserAreasEntity> findByUserNameAndGeometry(String userName, Geometry shape) throws ServiceException {

        List<UserAreasEntity> entityList = new ArrayList<>();

        if (shape != null ){
            entityList = findEntityByNamedQuery(UserAreasEntity.class, USER_AREA_DETAILS_BY_LOCATION,
                    with("shape", shape).parameters());
        }
        return entityList;
    }

    public List<AreaDto> getAllUserAreas(String userName, String scopeName) {
        Map<String, Object> parameters = ImmutableMap.<String, Object>builder().put(USER_NAME, userName).put(SCOPE_NAME, scopeName).build();
        Query query = createNamedNativeQuery(UserAreasEntity.FIND_ALL_USER_AREAS, parameters);
        query.setResultTransformer(Transformers.aliasToBean(AreaDto.class));
        return query.list();
    }

    public List<AreaDto> getAllUserAreaGroupName(String userName, String scopeName) {
        Map<String, Object> parameters = ImmutableMap.<String, Object>builder().put(USER_NAME, userName).put(SCOPE_NAME, scopeName).build();
        Query query = createNamedNativeQuery(UserAreasEntity.FIND_ALL_USER_AREAS_GROUP, parameters);
        query.setResultTransformer(Transformers.aliasToBean(AreaDto.class));
        return query.list();
    }

    public List<Long> getAllSharedGids(String userName, String scopeName, String type) {
        Map<String, Object> parameters =
                ImmutableMap.<String, Object>builder().put(USER_NAME, userName).put(SCOPE_NAME, scopeName).put(TYPE, type).build();
        Query query = createNamedNativeQuery(FIND_GID_FOR_SHARED_AREA, parameters);
        return query.list();
    }

    public List<AreaDto> findAllUserAreasByGids(List<Long> gids) {
        Query query = getSession().getNamedQuery(UserAreasEntity.FIND_ALL_USER_AREAS_BY_GIDS);
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

        Map parameters = with(IS_POWER_USER, isPowerUser ? 1 : 0)
                .and("searchCriteria", "%" + searchCriteria + "%")
                .and(SCOPE_NAME, scopeName)
                .and(USER_NAME, userName).parameters();

        return findEntityByNamedQuery(UserAreasEntity.class, SEARCH_BY_CRITERIA, parameters);

    }

    public UserAreasEntity getByUserNameAndName(String userName, String name) throws ServiceException {

        Map parameters = with(USER_NAME, userName)
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
                .and(USER_NAME, userName)
                .and(IS_POWER_USER, isPowerUser ? 1 : 0)
                .and(SCOPE_NAME, scopeName);

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
                .and(USER_NAME, userName)
                .and(IS_POWER_USER, isPowerUser ? 1 : 0)
                .and(SCOPE_NAME, scopeName);

        return findEntityByNamedQuery(UserAreasEntity.class, FIND_USER_AREA_BY_TYPE, param.parameters());
    }

    public List<UserAreasEntity> findUserAreasTypes(final String userName, final String scopeName, final Boolean isPowerUser) throws ServiceException {

        QueryParameter param = with(USER_NAME, userName)
                .and(IS_POWER_USER, isPowerUser ? 1 : 0)
                .and(SCOPE_NAME, scopeName);

        return findEntityByNamedQuery(UserAreasEntity.class, FIND_USER_AREA_BY_USER, param.parameters());
    }

    public List<UserAreasEntity> findByUserNameAndScopeName(String userName, String scopeName) throws ServiceException {
        return findEntityByNamedQuery(UserAreasEntity.class, FIND_BY_USER_NAME_AND_SCOPE_NAME,
                with(USER_NAME, userName).and(SCOPE_NAME, scopeName).parameters());
    }

    /**
     * <p>Update Start date and End date for user areas if the user is having scope <code><B>MANAGE_ANY_USER_AREA</B></code>
     * <p><code>StartDate</code> and <code>EndDate</code> can be NULL or Empty or a Valid Date</p>
     *
     * @param startDate Start Date
     * @param endDate End Date
     * @param type Area Type
     * @exception ServiceException Exception is Date cannot be updated
     *
     * @see UserAreaDao#updateUserAreasForUser(String, Date, Date, String)
     */
    public void updateUserAreasForUserAndScope(Date startDate, Date endDate, String type) throws ServiceException {
        TypedQuery query = (TypedQuery) getEntityManager().createNamedQuery(UserAreasEntity.UPDATE_USERAREA_FORUSER_AND_SCOPE);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("type", type);
        query.executeUpdate();
    }

    /**
     * <p>Update Start date and End date for user areas those are created by the user</p>
     * <p><code>StartDate</code> and <code>EndDate</code> can be NULL or Empty or a Valid Date</p>
     *
     * @param remoteUser User Name
     * @param startDate Start Date
     * @param endDate End Date
     * @param type Area Type
     * @throws ServiceException Exception is Date cannot be updated
     *
     * @see UserAreaDao#updateUserAreasForUserAndScope(Date, Date, String)
     */
    public void updateUserAreasForUser(String remoteUser, Date startDate, Date endDate, String type) throws ServiceException {
        TypedQuery query = (TypedQuery) getEntityManager().createNamedQuery(UserAreasEntity.UPDATE_USERAREA_FORUSER);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("userName", remoteUser);
        query.setParameter("type", type);
        query.executeUpdate();
    }

}