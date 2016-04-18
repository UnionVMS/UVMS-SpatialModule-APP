package eu.europa.ec.fisheries.uvms.spatial.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.spatial.dao.util.SpatialFunction;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.BaseAreaEntity;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.spatial.util.SpatialTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.hibernate.type.StandardBasicTypes;

@Slf4j
public class AreaDao extends AbstractDAO<BaseAreaEntity> {

    private static final String GID = "gid";
    private static final String NAME = "name";
    private static final String CODE = "code";
    private static final String TYPE = "type";
    private static final String CLOSEST = "closest";

    private EntityManager em;

    public AreaDao(EntityManager em) {
        this.em = em;
    }

    public List<Map<String, String>> findSelectedAreaColumns(String namedQueryString, Number gid) {
        Query query = em.unwrap(Session.class).getNamedQuery(namedQueryString);
        query.setParameter("gid", gid);
        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        return query.list();
    }

    public Object findOneByTypeNameAndId(final String typeName, final Long id) throws ServiceException { // FIXME this needs improvement
        final Class entityClassByType = getEntityClassByType(typeName);
        return findEntityById(entityClassByType, id);
    }

    private Class getEntityClassByType(String value) {
        for (SpatialTypeEnum areaType : SpatialTypeEnum.values()) {
            if(areaType.getType().equalsIgnoreCase(value)) {
                return areaType.getEntityClass();
            }
        }
        throw new IllegalArgumentException(value);
    }

    public List closestArea(final List<AreaLocationTypesEntity> entities, final SpatialFunction spatialFunction, final Point point){

        List resultList = new ArrayList();

        if (spatialFunction != null && CollectionUtils.isNotEmpty(entities) && (point != null && !point.isEmpty())) {

            final StringBuilder sb = new StringBuilder();
            final Double longitude = point.getX();
            final Double latitude = point.getY();

            Iterator<AreaLocationTypesEntity> it = entities.iterator();
            while (it.hasNext()) {
                AreaLocationTypesEntity next = it.next();
                final String areaDbTable = next.getAreaDbTable();
                final String typeName = next.getTypeName();
                sb.append("(SELECT '").append(typeName).append("' AS type, gid, code, name, ")
                        .append(spatialFunction.stClosestPoint(latitude, longitude))
                        .append(" AS closest ").append("FROM spatial.").append(areaDbTable).append(" ")
                        .append("WHERE NOT ST_IsEmpty(geom) AND enabled = 'Y' ").append("ORDER BY ")
                        .append(spatialFunction.stDistance(latitude, longitude)).append(" ")
                        .append(spatialFunction.limit(10)).append(")");
                it.remove(); // avoids a ConcurrentModificationException
                if (it.hasNext()) {
                    sb.append(" UNION ALL ");
                }
            }

            log.debug("{} QUERY => {}", spatialFunction.getClass().getSimpleName().toUpperCase(), sb.toString());

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

    public List intersectingArea(final List<AreaLocationTypesEntity> entities, final SpatialFunction spatialFunction, final Point point){

        List resultList = new ArrayList();

        if (spatialFunction != null && CollectionUtils.isNotEmpty(entities) && (point != null && !point.isEmpty())) {

            final StringBuilder sb = new StringBuilder();
            final Double longitude = point.getX();
            final Double latitude = point.getY();

            Iterator<AreaLocationTypesEntity> it = entities.iterator();
            while (it.hasNext()) {
                AreaLocationTypesEntity next = it.next();
                final String areaDbTable = next.getAreaDbTable();
                final String typeName = next.getTypeName();
                sb.append("SELECT '").append(typeName).append("' as type, gid, name, code FROM spatial.").
                        append(areaDbTable).append(" WHERE ").
                        append(spatialFunction.stIntersects(latitude, longitude)).append(" AND enabled = 'Y'");
                it.remove(); // avoids a ConcurrentModificationException
                if (it.hasNext()) {
                    sb.append(" UNION ALL ");
                }
            }

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
