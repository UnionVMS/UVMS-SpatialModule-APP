/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.dao;

import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.commons.service.dao.AbstractDAO;
import eu.europa.ec.fisheries.uvms.commons.service.dao.QueryParameter;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaSimpleType;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.util.DatabaseDialect;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.util.Oracle;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.upload.UploadMappingProperty;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.BaseAreaEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.spatial.GeometryType;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.StringType;
import org.opengis.feature.Property;

@Slf4j
public abstract class AbstractAreaDao<E extends BaseAreaEntity> extends AbstractDAO<E> {

    protected static final String SHAPE = "shape";
    private static final String GID = "gid";
    private static final String NAME = "name";
    private static final String CODE = "code";
    private static final String TYPE = "type";
    private static final String CLOSEST = "closest";
    private static final String GEOM = "geom";
    private static final String DISTANCE = "dist";
    private static final String UNION_ALL = " UNION ALL ";
    private static final String QUERY = "{} QUERY => {}";


    public List<Serializable> bulkInsert(Map<String, List<Property>> features, List<UploadMappingProperty> mapping) throws ServiceException {
        List<Serializable> invalidGeometryList = new ArrayList<>();
        StatelessSession session = (getEntityManager().unwrap(Session.class)).getSessionFactory().openStatelessSession();
        Transaction tx = session.beginTransaction();
        try {
            Query query = session.getNamedQuery(getDisableAreaNamedQuery());
            query.executeUpdate();
            for (List<Property> properties : features.values()) {
                Map<String, Object> values = BaseAreaEntity.createAttributesMap(properties);
                BaseAreaEntity entity = createEntity(values, mapping);
                if (entity.getName() == null || entity.getCode() == null){
                    throw new ServiceException("NAME AND CODE FIELD ARE MANDATORY");
                }
                Serializable identifier = session.insert(entity);
                if(!entity.getGeom().isValid()){
                    invalidGeometryList.add(identifier);
                }
            }
            log.debug("Commit transaction");
            tx.commit();
        }
        catch (Exception e){
            tx.rollback();
            throw new ServiceException("Rollback transaction", e);
        }
        finally {
            log.debug("Closing session");
            session.close();
        }
        return invalidGeometryList;
    }

    protected abstract String getIntersectNamedQuery();

    protected abstract String getSearchNamedQuery();

    protected abstract String getSearchNameByCodeQuery();

    @Deprecated
    protected abstract Class<E> getClazz();

    protected abstract BaseAreaEntity createEntity(Map<String, Object> values, List<UploadMappingProperty> mapping) throws ServiceException;

    protected abstract String getDisableAreaNamedQuery();

    @Deprecated
    public BaseAreaEntity findOne(final Long id) throws ServiceException {
        return findEntityById(getClazz(), id);
    }

    public BaseAreaEntity findOne(Class clazz, final Long id) throws ServiceException {
        return findEntityById(clazz, id);
    }

    @Deprecated
    public List findByIntersect(Point point) throws ServiceException {
        return findEntityByNamedQuery(getClazz(), getIntersectNamedQuery(), QueryParameter.with(SHAPE, point).parameters());
    }

    public List searchEntity(String filter) throws ServiceException {
        String name = "%" +filter.toUpperCase()+"%";
        String code = "%" +filter.toUpperCase()+"%";
        return findEntityByNamedQuery(getClazz(), getSearchNamedQuery(), QueryParameter.with(NAME, name).and(CODE, code).parameters());
    }

    public List searchNameByCode(String filter) throws ServiceException {
        String name = "%" +filter.toUpperCase()+"%";
        String code = "%" +filter.toUpperCase()+"%";
        return findEntityByNamedQuery(getClazz(), getSearchNameByCodeQuery(), QueryParameter.with(NAME, name).and(CODE, code).parameters());
    }

    public List listBaseAreas(final String query) throws ServiceException {
        javax.persistence.Query emNativeQuery = getEntityManager().createNativeQuery(query);
        emNativeQuery.unwrap(SQLQuery.class)
                .addScalar(TYPE, StringType.INSTANCE)
                .addScalar(GEOM, GeometryType.INSTANCE);
        return emNativeQuery.getResultList();
    }

    public List listBaseAreaGeometries(final String query) {
        javax.persistence.Query emNativeQuery = getEntityManager().createNativeQuery(query);
        emNativeQuery.unwrap(SQLQuery.class)
                .addScalar(GEOM, GeometryType.INSTANCE);
        return emNativeQuery.getResultList();
    }

    public List<E> byCode(List<AreaSimpleType> areaSimpleTypeList) throws ServiceException {
        if (areaSimpleTypeList == null) {
            throw new IllegalArgumentException("LIST CAN NOT BE EMPTY OR NULL");
        }
        if (areaSimpleTypeList.isEmpty()) {
            return new ArrayList<>();
        }
        final StringBuilder sb = new StringBuilder();
        List resultList;
        Iterator<AreaSimpleType> iterator = areaSimpleTypeList.iterator();
        while (iterator.hasNext()) {
            AreaSimpleType next = iterator.next();
            sb.append("SELECT '")
                    .append(next.getAreaType())
                    .append("' as type,'")
                    .append(next.getAreaCode()).append("' as code,")
                    .append(" GEOM FROM ").append(next.getAreaType())
                    .append(" WHERE code = '").append(next.getAreaCode())
                    .append("' AND enabled='Y'");
            if (iterator.hasNext()) {
                sb.append(UNION_ALL);
            }
        }
        log.debug(QUERY, sb.toString());
        final javax.persistence.Query emNativeQuery = getEntityManager().createNativeQuery(sb.toString());
        emNativeQuery.unwrap(SQLQuery.class)
                .addScalar("type", StringType.INSTANCE)
                .addScalar("code", StringType.INSTANCE)
                .addScalar(GEOM, org.hibernate.spatial.GeometryType.INSTANCE);

        resultList = emNativeQuery.getResultList();
        return resultList;
    }

    public List<Map<String, Object>> findSelectedAreaColumns(String namedQueryString, List<Long> gids) {
        Query query = getEntityManager().unwrap(Session.class).getNamedQuery(namedQueryString);
        query.setParameterList("ids", gids);
        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        return query.list();
    }

    // FIXME duplicated functionality with AreaServeBean getClosestPointByPoint
    public List<BaseAreaEntity> closestPoint(final List<AreaLocationTypesEntity> entities, final DatabaseDialect spatialFunction, final Point point){
        List resultList = new ArrayList();
        if (spatialFunction != null && CollectionUtils.isNotEmpty(entities) && (point != null && !point.isEmpty())) {
            final StringBuilder sb = new StringBuilder();
            final Double longitude = point.getX();
            final Double latitude = point.getY();
            Iterator<AreaLocationTypesEntity> it = entities.iterator();
            while (it.hasNext()) {
                AreaLocationTypesEntity next = it.next();
                String typeName = next.getTypeName();
                sb.append(spatialFunction.closestPointToPoint(typeName, next.getAreaDbTable(), latitude, longitude, 10));
                it.remove(); // avoids a ConcurrentModificationException
                if (it.hasNext()) {
                    sb.append(UNION_ALL);
                }
            }
            log.debug(QUERY, spatialFunction.getClass().getSimpleName().toUpperCase(), sb.toString());
            final javax.persistence.Query emNativeQuery = getEntityManager().createNativeQuery(sb.toString());
            emNativeQuery.unwrap(SQLQuery.class).addScalar("type", StringType.INSTANCE).addScalar(GID, IntegerType.INSTANCE)
                    .addScalar(CODE, StringType.INSTANCE).addScalar(NAME, StringType.INSTANCE).addScalar(GEOM, GeometryType.INSTANCE)
                    .addScalar("distance", DoubleType.INSTANCE);
            resultList = emNativeQuery.getResultList();
        }
        return resultList;
    }

    public List<BaseAreaEntity> closestArea(final List<AreaLocationTypesEntity> entities, final DatabaseDialect dialect, final Point point){
        List resultList = new ArrayList();
        if (dialect != null && CollectionUtils.isNotEmpty(entities) && (point != null && !point.isEmpty())) {
            final StringBuilder sb = new StringBuilder();
            final Double longitude = point.getX();
            final Double latitude = point.getY();
            Iterator<AreaLocationTypesEntity> it = entities.iterator();
            sb.append(dialect.closestAreaToPointPrefix());
            int index = 0;
            while (it.hasNext()) {
                AreaLocationTypesEntity next = it.next();
                final String areaDbTable = next.getAreaDbTable();
                final String typeName = next.getTypeName();
                sb.append(dialect.closestAreaToPoint(index,typeName, areaDbTable, latitude, longitude, dialect instanceof Oracle ? 10 : 1));
                index++;
                it.remove(); // avoids a ConcurrentModificationException
                if (it.hasNext()) {
                    sb.append(UNION_ALL);
                }
            }
            sb.append(dialect.closestAreaToPointSuffix());
            log.debug(QUERY, dialect.getClass().getSimpleName().toUpperCase(), sb.toString());
            javax.persistence.Query emNativeQuery = getEntityManager().createNativeQuery(sb.toString());
            emNativeQuery.unwrap(SQLQuery.class)
                    .addScalar(TYPE, StandardBasicTypes.STRING)
                    .addScalar(GID, StandardBasicTypes.INTEGER)
                    .addScalar(CODE, StandardBasicTypes.STRING)
                    .addScalar(NAME, StandardBasicTypes.STRING)
                    .addScalar(CLOSEST, org.hibernate.spatial.GeometryType.INSTANCE)
                    .addScalar(DISTANCE, StandardBasicTypes.DOUBLE);

            resultList = emNativeQuery.getResultList();
        }
        return resultList;
    }

    public List<BaseAreaEntity> intersectingArea(final List<AreaLocationTypesEntity> entities, final DatabaseDialect dialect, final Point point){
        List resultList = new ArrayList();
        int index = 1;
        if (dialect != null && CollectionUtils.isNotEmpty(entities) && (point != null && !point.isEmpty())) {
            final StringBuilder sb = new StringBuilder();
            final Double longitude = point.getX();
            final Double latitude = point.getY();
            Iterator<AreaLocationTypesEntity> it = entities.iterator();
            sb.append("select * from  (");
            while (it.hasNext()) {
                AreaLocationTypesEntity next = it.next();
                final String areaDbTable = next.getAreaDbTable();
                final String typeName = next.getTypeName();
                sb.append("(SELECT ").append(index).append(" as indexRS,").append("'").append(typeName).append("' as type, gid, code, name FROM spatial.").
                        append(areaDbTable).append(" WHERE ").
                        append(dialect.stIntersects(latitude, longitude)).append(" AND enabled = 'Y')");
                it.remove(); // avoids a ConcurrentModificationException
                index++;
                if (it.hasNext()) {
                    sb.append(UNION_ALL);
                }
            }
            sb.append(") a ORDER BY indexRS, gid ASC ");
            log.debug(QUERY, dialect.getClass().getSimpleName().toUpperCase(), sb.toString());
            javax.persistence.Query emNativeQuery = getEntityManager().createNativeQuery(sb.toString());
            emNativeQuery.unwrap(SQLQuery.class)
                    .addScalar(TYPE, StandardBasicTypes.STRING)
                    .addScalar(GID, StandardBasicTypes.INTEGER)
                    .addScalar(CODE, StandardBasicTypes.STRING)
                    .addScalar(NAME, StandardBasicTypes.STRING);
            resultList = emNativeQuery.getResultList();
        }

        return resultList;

    }


    public void makeGeomValid(final String areaDbTable, final DatabaseDialect dialect) {
        String query = dialect.makeGeomValid(areaDbTable);
        log.debug(QUERY, dialect.getClass().getSimpleName().toUpperCase(), query);
        javax.persistence.Query nativeQuery = getEntityManager().createNativeQuery(query);
        nativeQuery.executeUpdate();
    }
}