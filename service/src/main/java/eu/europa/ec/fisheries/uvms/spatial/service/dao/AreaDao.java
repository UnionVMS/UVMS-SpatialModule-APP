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
import eu.europa.ec.fisheries.uvms.spatial.service.dto.PortDistanceInfoDto;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.*;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class AreaDao {

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

    public void delete(BaseAreaEntity entity)  {
        em.remove(entity);
    }

    public <T extends BaseAreaEntity> void disableAllAreasOfType(T entity){


    }

    public List<PortAreaEntity> getPortAreasByPoint(Point point) {

        TypedQuery<PortAreaEntity> query = em.createNamedQuery(PortAreaEntity.PORT_AREA_BY_POINT, PortAreaEntity.class);
        query.setParameter("point", point);
        return query.getResultList();
    }


    public List<PortEntity> getPortsByAreaCodes(List<String> codes) {
        if(codes == null || codes.isEmpty()){
            return new ArrayList<>();
        }

        TypedQuery<PortEntity> query = em.createNamedQuery(PortEntity.SEARCH_PORT_BY_AREA_CODES, PortEntity.class);
        query.setParameter("code", codes);
        return query.getResultList();
    }


    public PortDistanceInfoDto getClosestPort(Point point) {

        TypedQuery<PortDistanceInfoDto> query = em.createNamedQuery(PortEntity.CLOSEST_PORT, PortDistanceInfoDto.class);
        query.setParameter("point", point);
        query.setMaxResults(1);
        List<PortDistanceInfoDto> rs =  query.getResultList();
        return rs.isEmpty() ? null : rs.get(0);
    }


    public List<UserAreasEntity> intersects(final Geometry shape, final String userName) {

        TypedQuery<UserAreasEntity> query = em.createNamedQuery(UserAreasEntity.USER_AREA_DETAILS_BY_LOCATION, UserAreasEntity.class);
        query.setParameter("shape",shape);
        query.setParameter(USER_NAME,userName);
        return   query.getResultList();
    }


    public List<UserAreasEntity> getAllUserAreas(String userName, String scopeName) {

        TypedQuery<UserAreasEntity> query = em.createNamedQuery(UserAreasEntity.FIND_ALL_USER_AREAS, UserAreasEntity.class);
        query.setParameter("userName",userName);
        query.setParameter("scopeName",scopeName);
        return query.getResultList();
    }


    public List<UserAreasEntity> findByUserNameScopeNameAndPowerUser(String userName, String scopeName, boolean isPowerUser){
        TypedQuery<UserAreasEntity> query =  em.createNamedQuery(UserAreasEntity.FIND_USER_AREA_BY_USERNAME_SCOPE_AND_POWERUSER, UserAreasEntity.class);
        query.setParameter(USER_NAME, userName);
        query.setParameter(IS_POWER_USER, isPowerUser);
        query.setParameter(SCOPE_NAME, scopeName);
        return query.getResultList();
    }

    public List<UserAreasEntity> findByUserNameAndScopeName(String userName, String scopeName){

        TypedQuery<UserAreasEntity> query =  em.createNamedQuery(UserAreasEntity.FIND_BY_USER_NAME_AND_SCOPE_NAME, UserAreasEntity.class);
        query.setParameter(USER_NAME, userName);
        query.setParameter(SCOPE_NAME, scopeName);
        return query.getResultList();
    }

    /*  ---------------------- Get Areas By Codes ------------------------- */

    public List<EezEntity> getEEZByAreaCodes(List<String> codes) {
        if(codes == null || codes.isEmpty()){
            return new ArrayList<>();
        }

        TypedQuery<EezEntity> query = em.createNamedQuery(EezEntity.AREA_BY_AREA_CODES, EezEntity.class);
        query.setParameter("code", codes);
        return query.getResultList();
    }

    public List<FaoEntity> getFAOByAreaCodes(List<String> codes) {
        if(codes == null || codes.isEmpty()){
            return new ArrayList<>();
        }

        TypedQuery<FaoEntity> query = em.createNamedQuery(FaoEntity.AREA_BY_AREA_CODES, FaoEntity.class);
        query.setParameter("code", codes);
        return query.getResultList();
    }

    public List<GfcmEntity> getGFCMByAreaCodes(List<String> codes) {
        if(codes == null || codes.isEmpty()){
            return new ArrayList<>();
        }

        TypedQuery<GfcmEntity> query = em.createNamedQuery(GfcmEntity.AREA_BY_AREA_CODES, GfcmEntity.class);
        query.setParameter("code", codes);
        return query.getResultList();
    }

    public List<PortAreaEntity> getPortAreaByAreaCodes(List<String> codes) {
        if(codes == null || codes.isEmpty()){
            return new ArrayList<>();
        }

        TypedQuery<PortAreaEntity> query = em.createNamedQuery(PortAreaEntity.AREA_BY_AREA_CODES, PortAreaEntity.class);
        query.setParameter("code", codes);
        return query.getResultList();
    }

    public List<RfmoEntity> getRFMOByAreaCodes(List<String> codes) {
        if(codes == null || codes.isEmpty()){
            return new ArrayList<>();
        }

        TypedQuery<RfmoEntity> query = em.createNamedQuery(RfmoEntity.AREA_BY_AREA_CODES, RfmoEntity.class);
        query.setParameter("code", codes);
        return query.getResultList();
    }

    public List<StatRectEntity> getStatRectByAreaCodes(List<String> codes) {
        if(codes == null || codes.isEmpty()){
            return new ArrayList<>();
        }

        TypedQuery<StatRectEntity> query = em.createNamedQuery(StatRectEntity.AREA_BY_AREA_CODES, StatRectEntity.class);
        query.setParameter("code", codes);
        return query.getResultList();
    }

    public List<UserAreasEntity> getUserAreasByAreaCodes(List<String> codes) {
        if(codes == null || codes.isEmpty()){
            return new ArrayList<>();
        }

        TypedQuery<UserAreasEntity> query = em.createNamedQuery(UserAreasEntity.AREA_BY_AREA_CODES, UserAreasEntity.class);
        query.setParameter("code", codes);
        return query.getResultList();
    }

    /*  ---------------------- Get All Areas ------------------------- */

    public List<EezEntity> getAllEezAreas() {
        TypedQuery<EezEntity> query = em.createNamedQuery(EezEntity.ALL_AREAS, EezEntity.class);
        return query.getResultList();
    }

    public List<FaoEntity> getAllFaoAreas() {
        TypedQuery<FaoEntity> query = em.createNamedQuery(FaoEntity.ALL_AREAS, FaoEntity.class);
        return query.getResultList();
    }

    public List<GfcmEntity> getAllGfcmAreas() {
        TypedQuery<GfcmEntity> query = em.createNamedQuery(GfcmEntity.ALL_AREAS, GfcmEntity.class);
        return query.getResultList();
    }

    public List<PortAreaEntity> getAllPortAreaAreas() {
        TypedQuery<PortAreaEntity> query = em.createNamedQuery(PortAreaEntity.ALL_AREAS, PortAreaEntity.class);
        return query.getResultList();
    }

    public List<PortEntity> getAllPorts() {
        TypedQuery<PortEntity> query = em.createNamedQuery(PortEntity.ALL_AREAS, PortEntity.class);
        return query.getResultList();
    }

    public List<RfmoEntity> getAllRfmoAreas() {
        TypedQuery<RfmoEntity> query = em.createNamedQuery(RfmoEntity.ALL_AREAS, RfmoEntity.class);
        return query.getResultList();
    }

    public List<StatRectEntity> getAllStatRectAreas() {
        TypedQuery<StatRectEntity> query = em.createNamedQuery(StatRectEntity.ALL_AREAS, StatRectEntity.class);
        return query.getResultList();
    }


}