/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package service.dao;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Session;
import service.entity.UserAreasEntity2;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class UserAreaDao2  {

    @PersistenceContext
    private EntityManager em;

    private static final String USER_NAME = "userName";
    private static final String SCOPE_NAME = "scopeName";
    private static final String TYPE = "type";
    private static final String GID_LIST = "gids";
    private static final String IS_POWER_USER = "isPowerUser";



    public List<UserAreasEntity2> intersects(final Geometry shape, final String userName) {

        TypedQuery<UserAreasEntity2> query = em.createNamedQuery(UserAreasEntity2.USER_AREA_DETAILS_BY_LOCATION, UserAreasEntity2.class);
        query.setParameter("shape",shape);
        query.setParameter(USER_NAME,userName);
        return   query.getResultList();
    }

    public List<UserAreasEntity2> intersects(final Geometry shape) {

        TypedQuery<UserAreasEntity2> query = em.createNamedQuery(UserAreasEntity2.USER_AREA_BY_COORDINATE, UserAreasEntity2.class);
        query.setParameter("shape",shape);
        return   query.getResultList();
    }

    protected UserAreasEntity2 createEntity(UserAreasEntity2 entity) {

        em.persist(entity);
        return entity;
    }


    public List<UserAreasEntity2> findByUserNameAndGeometry(String userName, Geometry shape){

        // TODO userName not used in query ?????

        if (shape != null ){
            TypedQuery<UserAreasEntity2> query = em.createNamedQuery(UserAreasEntity2.USER_AREA_DETAILS_BY_LOCATION, UserAreasEntity2.class);
            query.setParameter("shape",shape);
            return query.getResultList();
        }
        return new ArrayList<>();
    }

    public List<UserAreasEntity2> getAllUserAreas(String userName, String scopeName) {

        TypedQuery<UserAreasEntity2> query = em.createNamedQuery(UserAreasEntity2.FIND_ALL_USER_AREAS, UserAreasEntity2.class);
        query.setParameter("userName",userName);
        query.setParameter("scopeName",scopeName);
        return query.getResultList();
    }

    /*
    public List<AreaDto2> getAllUserAreaGroupName(String userName, String scopeName) {
        Map<String, Object> parameters = ImmutableMap.<String, Object>builder().put(USER_NAME, userName).put(SCOPE_NAME, scopeName).build();
        Query query = createNamedNativeQuery(UserAreasEntity2.FIND_ALL_USER_AREAS_GROUP, parameters);
        query.setResultTransformer(Transformers.aliasToBean(AreaDto2.class));
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

    */

    private Query createNamedNativeQuery(String nativeQueryString, Map<String, Object> parameters) {
        Query query = em.unwrap(Session.class).getNamedQuery(nativeQueryString);
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query;
    }







    public List<UserAreasEntity2> listByCriteria(String userName, String scopeName, String searchCriteria, Boolean isPowerUser) {

        TypedQuery<UserAreasEntity2> query =  em.createNamedQuery(UserAreasEntity2.SEARCH_BY_CRITERIA,UserAreasEntity2.class);
        query.setParameter(IS_POWER_USER, isPowerUser);
        query.setParameter("searchCriteria",  "%" + searchCriteria + "%");
        query.setParameter("SCOPE_NAME", scopeName);
        query.setParameter(USER_NAME, userName);
        return query.getResultList();
    }




    public UserAreasEntity2 getByUserNameAndName(String userName, String name) {

        TypedQuery<UserAreasEntity2> query =  em.createNamedQuery(UserAreasEntity2.FIND_BY_USERNAME_AND_NAME,UserAreasEntity2.class);
        query.setParameter("name", name);
        query.setParameter(USER_NAME, userName);
        List<UserAreasEntity2> rs = query.getResultList();
        if (!CollectionUtils.isEmpty(rs)){
            return rs.get(0);
        }
        return null;
    }



    public UserAreasEntity2 findOne(final Long userAreaId, final String userName, final Boolean isPowerUser, final String scopeName) {

        TypedQuery<UserAreasEntity2> query =  em.createNamedQuery(UserAreasEntity2.FIND_USER_AREA_BY_ID,UserAreasEntity2.class);
        query.setParameter("userAreaId", userAreaId);
        query.setParameter(USER_NAME, userName);
        query.setParameter(IS_POWER_USER, isPowerUser);
        query.setParameter(SCOPE_NAME, scopeName);
        List<UserAreasEntity2> rs  = query.getResultList();

        if (!CollectionUtils.isEmpty(rs)){
            return rs.get(0);
        }
        return null;
    }

    public UserAreasEntity2 save(UserAreasEntity2 entity) {
        return createEntity(entity);
    }

    public UserAreasEntity2 update(UserAreasEntity2 entity)  {
        return em.merge(entity);
    }

    public List<UserAreasEntity2> findByUserNameAndScopeNameAndTypeAndPowerUser(String userName, String scopeName, String type, boolean isPowerUser) {

        TypedQuery<UserAreasEntity2> query =  em.createNamedQuery(UserAreasEntity2.FIND_USER_AREA_BY_TYPE,UserAreasEntity2.class);
        query.setParameter("type", type);
        query.setParameter(USER_NAME, userName);
        query.setParameter(IS_POWER_USER, isPowerUser);
        query.setParameter(SCOPE_NAME, scopeName);
        return query.getResultList();
    }

    public List<UserAreasEntity2> findUserAreasTypes(final String userName, final String scopeName, final Boolean isPowerUser)  {

        TypedQuery<UserAreasEntity2> query =  em.createNamedQuery(UserAreasEntity2.FIND_USER_AREA_BY_USER,UserAreasEntity2.class);
        query.setParameter(USER_NAME, userName);
        query.setParameter(IS_POWER_USER, isPowerUser);
        query.setParameter(SCOPE_NAME, scopeName);
        return query.getResultList();

    }

    public List<UserAreasEntity2> findByUserNameAndScopeName(String userName, String scopeName){

        TypedQuery<UserAreasEntity2> query =  em.createNamedQuery(UserAreasEntity2.FIND_BY_USER_NAME_AND_SCOPE_NAME,UserAreasEntity2.class);
        query.setParameter(USER_NAME, userName);
        query.setParameter(SCOPE_NAME, scopeName);
        return query.getResultList();
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
     * @see UserAreaDao2#updateUserAreasForUser(String, Date, Date, String)
     */
    public void updateUserAreasForUserAndScope(Date startDate, Date endDate, String type) {

        TypedQuery<UserAreasEntity2> query =  em.createNamedQuery(UserAreasEntity2.UPDATE_USERAREA_FORUSER_AND_SCOPE,UserAreasEntity2.class);
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
     * @see UserAreaDao2#updateUserAreasForUserAndScope(Date, Date, String)
     */
    public void updateUserAreasForUser(String remoteUser, Date startDate, Date endDate, String type) {

        TypedQuery<UserAreasEntity2> query =  em.createNamedQuery(UserAreasEntity2.UPDATE_USERAREA_FORUSER,UserAreasEntity2.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("userName", remoteUser);
        query.setParameter("type", type);
        query.executeUpdate();
    }

}