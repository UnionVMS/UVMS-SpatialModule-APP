package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.spatial.entity.BaseAreaEntity;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.opengis.feature.Property;

import java.util.List;
import java.util.Map;

@Slf4j
public abstract class AbstractAreaDao<E extends BaseAreaEntity> extends AbstractDAO<E> {

    public void bulkInsert(Map<String, List<Property>> features) {
        StatelessSession session = (getEntityManager().unwrap(Session.class)).getSessionFactory().openStatelessSession();
        Transaction tx = session.beginTransaction();
        try {
            Query query = session.getNamedQuery(getDisableAreaNamedQuery());
            query.executeUpdate();
            for (List<Property> properties : features.values()) {
                Map<String, Object> values = BaseAreaEntity.createAttributesMap(properties);
                session.insert(createEntity(values));
            }
            log.debug("Commit session");
            tx.commit();
        }
        catch (Exception e){
            log.debug("Rollback session");
            tx.rollback();
        }
        finally {
            log.debug("Closing session");
            session.close();
        }
    }

    protected abstract E createEntity(Map<String, Object> values) throws ServiceException;

    protected abstract String getDisableAreaNamedQuery();

}
