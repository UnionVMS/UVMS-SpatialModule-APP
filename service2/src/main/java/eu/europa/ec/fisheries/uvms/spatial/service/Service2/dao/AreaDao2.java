/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.Service2.dao;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto.PortDistanceInfoDto;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.*;
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

    public <T> T find(Class<T> entity, long id)  {
        return em.find(entity, id);
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
        if(codes == null || codes.isEmpty()){
            return new ArrayList<>();
        }

        TypedQuery<PortEntity2> query = em.createNamedQuery(PortEntity2.SEARCH_PORT_BY_AREA_CODES, PortEntity2.class);
        query.setParameter("code", codes);
        return query.getResultList();
    }


    public PortDistanceInfoDto getClosestPort(Point point) {

        TypedQuery<PortDistanceInfoDto> query = em.createNamedQuery(PortEntity2.CLOSEST_PORT, PortDistanceInfoDto.class);
        query.setParameter("point", point);
        query.setMaxResults(1);
        List<PortDistanceInfoDto> rs =  query.getResultList();
        return rs.isEmpty() ? null : rs.get(0);
    }


    public List<UserAreasEntity2> intersects(final Geometry shape, final String userName) {

        TypedQuery<UserAreasEntity2> query = em.createNamedQuery(UserAreasEntity2.USER_AREA_DETAILS_BY_LOCATION, UserAreasEntity2.class);
        query.setParameter("shape",shape);
        query.setParameter(USER_NAME,userName);
        return   query.getResultList();
    }


    public List<UserAreasEntity2> getAllUserAreas(String userName, String scopeName) {

        TypedQuery<UserAreasEntity2> query = em.createNamedQuery(UserAreasEntity2.FIND_ALL_USER_AREAS, UserAreasEntity2.class);
        query.setParameter("userName",userName);
        query.setParameter("scopeName",scopeName);
        return query.getResultList();
    }


    public List<UserAreasEntity2> findByUserNameScopeNameAndPowerUser(String userName, String scopeName, boolean isPowerUser){
        TypedQuery<UserAreasEntity2> query =  em.createNamedQuery(UserAreasEntity2.FIND_USER_AREA_BY_USERNAME_SCOPE_AND_POWERUSER,UserAreasEntity2.class);
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

    /*  ---------------------- Get Areas By Codes ------------------------- */

    public List<EezEntity2> getEEZByAreaCodes(List<String> codes) {
        if(codes == null || codes.isEmpty()){
            return new ArrayList<>();
        }

        TypedQuery<EezEntity2> query = em.createNamedQuery(EezEntity2.AREA_BY_AREA_CODES, EezEntity2.class);
        query.setParameter("code", codes);
        return query.getResultList();
    }

    public List<FaoEntity2> getFAOByAreaCodes(List<String> codes) {
        if(codes == null || codes.isEmpty()){
            return new ArrayList<>();
        }

        TypedQuery<FaoEntity2> query = em.createNamedQuery(FaoEntity2.AREA_BY_AREA_CODES, FaoEntity2.class);
        query.setParameter("code", codes);
        return query.getResultList();
    }

    public List<GfcmEntity2> getGFCMByAreaCodes(List<String> codes) {
        if(codes == null || codes.isEmpty()){
            return new ArrayList<>();
        }

        TypedQuery<GfcmEntity2> query = em.createNamedQuery(GfcmEntity2.AREA_BY_AREA_CODES, GfcmEntity2.class);
        query.setParameter("code", codes);
        return query.getResultList();
    }

    public List<PortAreaEntity2> getPortAreaByAreaCodes(List<String> codes) {
        if(codes == null || codes.isEmpty()){
            return new ArrayList<>();
        }

        TypedQuery<PortAreaEntity2> query = em.createNamedQuery(PortAreaEntity2.AREA_BY_AREA_CODES, PortAreaEntity2.class);
        query.setParameter("code", codes);
        return query.getResultList();
    }

    public List<RfmoEntity2> getRFMOByAreaCodes(List<String> codes) {
        if(codes == null || codes.isEmpty()){
            return new ArrayList<>();
        }

        TypedQuery<RfmoEntity2> query = em.createNamedQuery(RfmoEntity2.AREA_BY_AREA_CODES, RfmoEntity2.class);
        query.setParameter("code", codes);
        return query.getResultList();
    }

    public List<StatRectEntity2> getStatRectByAreaCodes(List<String> codes) {
        if(codes == null || codes.isEmpty()){
            return new ArrayList<>();
        }

        TypedQuery<StatRectEntity2> query = em.createNamedQuery(StatRectEntity2.AREA_BY_AREA_CODES, StatRectEntity2.class);
        query.setParameter("code", codes);
        return query.getResultList();
    }

    public List<UserAreasEntity2> getUserAreasByAreaCodes(List<String> codes) {
        if(codes == null || codes.isEmpty()){
            return new ArrayList<>();
        }

        TypedQuery<UserAreasEntity2> query = em.createNamedQuery(UserAreasEntity2.AREA_BY_AREA_CODES, UserAreasEntity2.class);
        query.setParameter("code", codes);
        return query.getResultList();
    }


}