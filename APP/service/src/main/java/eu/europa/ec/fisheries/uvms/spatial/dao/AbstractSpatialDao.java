package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.service.QueryParameter;
import eu.europa.ec.fisheries.uvms.spatial.entity.BaseSpatialEntity;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.opengis.feature.Property;

import java.util.List;
import java.util.Map;

@Slf4j
public abstract class AbstractSpatialDao<E extends BaseSpatialEntity> extends AbstractDAO<E> {

    protected static final String SHAPE = "shape";

    public void bulkInsert(Map<String, List<Property>> features) throws ServiceException {
        StatelessSession session = (getEntityManager().unwrap(Session.class)).getSessionFactory().openStatelessSession();
        Transaction tx = session.beginTransaction();
        try {
            Query query = session.getNamedQuery(getDisableAreaNamedQuery());
            query.executeUpdate();
            for (List<Property> properties : features.values()) {
                Map<String, Object> values = BaseSpatialEntity.createAttributesMap(properties);
                session.insert(createEntity(values));
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
    }

    protected abstract String getIntersectNamedQuery();

    protected abstract Class<E> getClazz();

    protected abstract BaseSpatialEntity createEntity(Map<String, Object> values) throws ServiceException;

    protected abstract String getDisableAreaNamedQuery();

    public BaseSpatialEntity findOne(final Long id) throws ServiceException {
        return findEntityById(getClazz(), id);
    }

    public List findByIntersect(Point point) throws ServiceException {
        return findEntityByNamedQuery(getClazz(), getIntersectNamedQuery(), QueryParameter.with(SHAPE, point).parameters());
    }
}
