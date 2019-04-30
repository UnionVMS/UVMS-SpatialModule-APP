/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.dao;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.EezEntity2;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.PortAreaEntity2;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.PortEntity2;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.UserAreasEntity2;
import org.apache.commons.collections.CollectionUtils;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
public class AreaDao2 {

    private static final String USER_NAME = "userName";
    private static final String SCOPE_NAME = "scopeName";
    private static final String IS_POWER_USER = "isPowerUser";

    @PersistenceContext
    private EntityManager em;

    public <T> T create(T entity)  {
        em.persist(entity);
        return entity;
    }


    public <T> T update(T entity)  {
        return em.merge(entity);
    }

    public List<PortAreaEntity2> getPortAreasByPoint(Point point) {

        TypedQuery<PortAreaEntity2> query = em.createNamedQuery(PortAreaEntity2.PORT_AREA_BY_POINT, PortAreaEntity2.class);
        query.setParameter("point", point);
        return query.getResultList();
    }


    public List<PortEntity2> getPortsByAreaCodes(List<String> codes) {

        TypedQuery<PortEntity2> query = em.createNamedQuery(PortEntity2.SEARCH_PORT_BY_AREA_CODES, PortEntity2.class);
        query.setParameter("code", codes);
        return query.getResultList();
    }


    public PortEntity2 getClosestPort(Point point) {

        TypedQuery<PortEntity2> query = em.createNamedQuery(PortEntity2.CLOSEST_PORT, PortEntity2.class);
        query.setParameter("point", point);
        query.setMaxResults(1);
        List<PortEntity2> rs =  query.getResultList();
        return rs.isEmpty() ? null : rs.get(0);
    }


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


    public List<UserAreasEntity2> findByUserNameAndGeometry(Geometry shape){

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



    public UserAreasEntity2 getByUserNameAndName(String userName, String areaName) {

        TypedQuery<UserAreasEntity2> query =  em.createNamedQuery(UserAreasEntity2.FIND_BY_USERNAME_AND_NAME,UserAreasEntity2.class);
        query.setParameter("name", areaName);
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