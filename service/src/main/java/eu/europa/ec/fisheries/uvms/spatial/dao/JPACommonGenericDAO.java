package eu.europa.ec.fisheries.uvms.spatial.dao;

import org.jboss.logging.Logger;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * This class is responsible for all application level database interaction.
 * It provides unified apis for all basic CRUD operations like Create, Read, Update, Delete.
 */
@Stateless
@Local(CommonGenericDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class JPACommonGenericDAO<T> implements CommonGenericDAO<T> {

    private static final Logger LOG = Logger.getLogger(JPACommonGenericDAO.class);

    @PersistenceContext(unitName = "UVMS")
    EntityManager em;

    @Override
    public T createEntity(T entity) {

        LOG.debug("Persisting entity : " + entity.getClass().getSimpleName());
        em.persist(entity);

        return entity;
    }

    @Override
    public T updateEntity(T entity) {

        LOG.debug("Updating entity : " + entity.getClass().getSimpleName());
        em.merge(entity);
        return entity;
    }

    @Override
    public T findEntityById(Class<T> entityClass, Object id) {

        T obj;

        LOG.debug("Finding entity : " + entityClass.getSimpleName() + " with ID : " + id.toString());
        obj = em.find(entityClass, id);

        return obj;
    }

    @Override
    public List<T> findEntityByQuery(Class<T> entityClass, String hqlQuery) {

        List<T> objectList;

        LOG.debug("Finding entity for query : " + hqlQuery);
        objectList = em.createQuery(hqlQuery, entityClass).getResultList();

        return objectList;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findEntityByQuery(Class<T> entityClass, String hqlQuery, Map<Integer, String> parameters) {

        List<T> objectList;

        LOG.debug("Finding entity for query : " + hqlQuery);
        Set<Entry<Integer, String>> rawParameters = parameters.entrySet();
        Query query = em.createQuery(hqlQuery, entityClass);
        for (Entry<Integer, String> entry : rawParameters) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        objectList = query.getResultList();

        return objectList;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findEntityByQuery(Class<T> entityClass, String hqlQuery, Map<Integer, String> parameters, int maxResultLimit) {

        List<T> objectList;

        LOG.debug("Finding entity for query : " + hqlQuery);
        Set<Entry<Integer, String>> rawParameters = parameters.entrySet();
        Query query = em.createQuery(hqlQuery, entityClass);
        for (Entry<Integer, String> entry : rawParameters) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        if (maxResultLimit > 0) {
            query.setMaxResults(maxResultLimit);
        }
        objectList = query.getResultList();

        return objectList;
    }

    @Override
    public List<T> findAllEntity(Class<T> entityClass) {

        List<T> objectList;

        LOG.debug("Finding all entity list for : " + entityClass.getSimpleName());
        objectList = em.createQuery("from " + entityClass.getSimpleName(), entityClass).getResultList();

        return objectList;
    }

    @Override
    public void deleteEntity(T entity, Object id) {

        LOG.debug("Deleting entity : " + entity.getClass().getSimpleName());
        em.remove(em.contains(entity) ? entity : em.merge(entity));

    }

}