package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.spatial.dao.util.DatabaseDialect;
import eu.europa.ec.fisheries.uvms.spatial.entity.BaseSpatialEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import com.vividsolutions.jts.geom.Point;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.spatial.GeometryType;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.StringType;

@Slf4j
public class AreasDao extends AbstractDAO<BaseSpatialEntity> {

    private static final String GID = "gid";
    private static final String NAME = "name";
    private static final String CODE = "code";
    private static final String TYPE = "type";
    private static final String CLOSEST = "closest";
    private static final String GEOM = "geom";

    private EntityManager em;

    public AreasDao(EntityManager em) {
        this.em = em;
    }

    public List getNameAndCode(final List<AreaLocationTypesEntity> entities, List<AreaTypeEntry> areaTypes) {

        List resultList;
        final StringBuilder sb = new StringBuilder();
        Iterator<AreaTypeEntry> it = areaTypes.iterator();

        Map<String, AreaLocationTypesEntity> typesEntityMap = new HashMap<>();
        for(AreaLocationTypesEntity entity : entities){
            typesEntityMap.put(entity.getTypeName(), entity);
        }

        while (it.hasNext()) {
            AreaTypeEntry next = it.next();
            AreaType areaType = next.getAreaType();
            Long id = Long.valueOf(next.getId());
            sb.append("(SELECT '").append(areaType.value()).append("' as type, area.name AS name, area.code AS code FROM spatial."
                    + typesEntityMap.get(areaType.value()).getAreaDbTable() + " AS area WHERE area.gid = " + id + ")");
            it.remove(); // avoids a ConcurrentModificationException
            if (it.hasNext()) {
                sb.append(" UNION ALL ");
            }
        }

        log.debug("{} QUERY => {}", sb.toString());

        final javax.persistence.Query emNativeQuery = em.createNativeQuery(sb.toString());
        emNativeQuery.unwrap(SQLQuery.class).addScalar("type", StringType.INSTANCE).addScalar("name", StringType.INSTANCE).addScalar("code", StringType.INSTANCE);
        resultList = emNativeQuery.getResultList();

        return resultList;
      }

    public List<Map<String, String>> findSelectedAreaColumns(String namedQueryString, Number gid) {
        Query query = em.unwrap(Session.class).getNamedQuery(namedQueryString);
        query.setParameter("gid", gid);
        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        return query.list();
    }

    public List<BaseSpatialEntity> closestPoint(final List<AreaLocationTypesEntity> entities, final DatabaseDialect spatialFunction, final Point point){

        List resultList = new ArrayList();

        if (spatialFunction != null && CollectionUtils.isNotEmpty(entities) && (point != null && !point.isEmpty())) {

            final StringBuilder sb = new StringBuilder();
            final Double longitude = point.getX();
            final Double latitude = point.getY();

            Iterator<AreaLocationTypesEntity> it = entities.iterator();

        while (it.hasNext()) {
            AreaLocationTypesEntity next = it.next();
            String typeName = next.getTypeName();
            sb.append(spatialFunction.closestPointToPoint(typeName, next.getAreaDbTable(), longitude, latitude, 10));
            it.remove(); // avoids a ConcurrentModificationException
            if (it.hasNext()) {
                sb.append(" UNION ALL ");
            }
        }

        log.debug("{} QUERY => {}", spatialFunction.getClass().getSimpleName().toUpperCase(), sb.toString());

        final javax.persistence.Query emNativeQuery = em.createNativeQuery(sb.toString());
        emNativeQuery.unwrap(SQLQuery.class).addScalar("type", StringType.INSTANCE).addScalar(GID, IntegerType.INSTANCE)
                .addScalar(CODE, StringType.INSTANCE).addScalar(NAME, StringType.INSTANCE).addScalar(GEOM, GeometryType.INSTANCE)
                .addScalar("distance", DoubleType.INSTANCE);

            resultList = emNativeQuery.getResultList();
        }
        return resultList;
    }

    public List<BaseSpatialEntity> closestArea(final List<AreaLocationTypesEntity> entities, final DatabaseDialect dialect, final Point point){

        List resultList = new ArrayList();

        if (dialect != null && CollectionUtils.isNotEmpty(entities) && (point != null && !point.isEmpty())) {

            final StringBuilder sb = new StringBuilder();
            final Double longitude = point.getX();
            final Double latitude = point.getY();

            Iterator<AreaLocationTypesEntity> it = entities.iterator();
            while (it.hasNext()) {
                AreaLocationTypesEntity next = it.next();
                final String areaDbTable = next.getAreaDbTable();
                final String typeName = next.getTypeName();
                sb.append(dialect.closestAreaToPoint(typeName, areaDbTable, latitude, longitude, 10));
                it.remove(); // avoids a ConcurrentModificationException
                if (it.hasNext()) {
                    sb.append(" UNION ALL ");
                }
            }

            log.debug("{} QUERY => {}", dialect.getClass().getSimpleName().toUpperCase(), sb.toString());

            javax.persistence.Query emNativeQuery = em.createNativeQuery(sb.toString());

            emNativeQuery.unwrap(SQLQuery.class)
                    .addScalar(TYPE, StandardBasicTypes.STRING)
                    .addScalar(GID, StandardBasicTypes.INTEGER)
                    .addScalar(CODE, StandardBasicTypes.STRING)
                    .addScalar(NAME, StandardBasicTypes.STRING)
                    .addScalar(CLOSEST, org.hibernate.spatial.GeometryType.INSTANCE);

            resultList = emNativeQuery.getResultList();
        }

        return resultList;

    }

    public List<BaseSpatialEntity> intersectingArea(final List<AreaLocationTypesEntity> entities, final DatabaseDialect dialect, final Point point){

        List resultList = new ArrayList();

        if (dialect != null && CollectionUtils.isNotEmpty(entities) && (point != null && !point.isEmpty())) {

            final StringBuilder sb = new StringBuilder();
            final Double longitude = point.getX();
            final Double latitude = point.getY();

            Iterator<AreaLocationTypesEntity> it = entities.iterator();
            while (it.hasNext()) {
                AreaLocationTypesEntity next = it.next();
                final String areaDbTable = next.getAreaDbTable();
                final String typeName = next.getTypeName();
                sb.append("(SELECT '").append(typeName).append("' as type, gid, code, name FROM spatial.").
                        append(areaDbTable).append(" WHERE ").
                        append(dialect.stIntersects(latitude, longitude)).append(" AND enabled = 'Y')");
                it.remove(); // avoids a ConcurrentModificationException
                if (it.hasNext()) {
                    sb.append(" UNION ALL ");
                }
            }

            log.debug("{} QUERY => {}", dialect.getClass().getSimpleName().toUpperCase(), sb.toString());

            javax.persistence.Query emNativeQuery = em.createNativeQuery(sb.toString());

            emNativeQuery.unwrap(SQLQuery.class)
                    .addScalar(TYPE, StandardBasicTypes.STRING)
                    .addScalar(GID, StandardBasicTypes.INTEGER)
                    .addScalar(CODE, StandardBasicTypes.STRING)
                    .addScalar(NAME, StandardBasicTypes.STRING);

            resultList = emNativeQuery.getResultList();
        }

        return resultList;

    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

}
