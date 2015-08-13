package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.exception.SpatialServiceException;
import org.apache.commons.lang3.NotImplementedException;
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
    public T createEntity(final T entity) throws SpatialServiceException {
        try {
            LOG.debug("Persisting entity : " + entity.getClass().getSimpleName());
            em.persist(entity);
        }
        catch (Exception e){
            e.printStackTrace();
            LOG.debug("Error occurred during Persisting entity : " + entity.getClass().getSimpleName());
            throw new SpatialServiceException();
        }
        return entity;
    }

    @Override
    public T updateEntity(final T entity) throws SpatialServiceException {
        try {
            LOG.debug("Updating entity : " + entity.getClass().getSimpleName());
            em.merge(entity);
        }
        catch (Exception e){
            e.printStackTrace();
            LOG.debug("Error occurred during updating entity : " + entity.getClass().getSimpleName());
            throw new SpatialServiceException();
        }
        return entity;
    }

    @Override
    public T findEntityById(final Class<T> entityClass, final Object id) throws SpatialServiceException {
        T obj;

        try {
            LOG.debug("Finding entity : " + entityClass.getSimpleName() + " with ID : " + id.toString());
            obj = em.find(entityClass, id);
        }
        catch (Exception e) {
            e.printStackTrace();
            LOG.debug("Error occurred during finding entity : " + entityClass.getSimpleName() + " with ID : " + id.toString());
            throw new SpatialServiceException();
        }
        return obj;
    }

    @Override
    public List<T> findEntityByQuery(final Class<T> entityClass, final String hqlQuery) throws SpatialServiceException {

        List<T> objectList;

        try {
            LOG.debug("Finding entity for query : " + hqlQuery);
            objectList = em.createQuery(hqlQuery, entityClass).getResultList();

        }
        catch (Exception e) {
            e.printStackTrace();
            LOG.debug("Error occurred during finding entity for query : " + hqlQuery);
            throw new SpatialServiceException();
        }

        return objectList;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findEntityByQuery(final Class<T> entityClass, final String hqlQuery, final Map<Integer, String> parameters) throws SpatialServiceException {
        List<T> objectList;

        try {
            LOG.debug("Finding entity for query : " + hqlQuery);
            Set<Entry<Integer, String>> rawParameters = parameters.entrySet();
            Query query = em.createQuery(hqlQuery, entityClass);
            for (Entry<Integer, String> entry : rawParameters) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
            objectList = query.getResultList();
        }
        catch (Exception e) {
            e.printStackTrace();
            LOG.debug("Error occurred during finding entity for query : " + hqlQuery);
            throw new SpatialServiceException();
        }

        return objectList;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findEntityByQuery(final Class<T> entityClass, final String hqlQuery, final Map<Integer, String> parameters, final int maxResultLimit) throws SpatialServiceException {
        List<T> objectList;

        try {
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
        }
        catch (Exception e) {
            e.printStackTrace();
            LOG.debug("Error occurred during finding entity for query : " + hqlQuery);
            throw new SpatialServiceException();
        }

        return objectList;
    }

    @Override
    public List<T> findEntityByNamedQuery(final Class<T> entityClass, final String queryName) throws SpatialServiceException {
        throw new NotImplementedException("Not implemented, yet");
    }

    @Override
    public List<T> findEntityByNamedQuery(Class<T> entityClass, String queryName, Map<Integer, String> parameters) throws SpatialServiceException {
        throw new NotImplementedException("Not implemented, yet");
    }

    @Override
    public List<T> findEntityByNamedQuery(Class<T> entityClass, String queryName, Map<Integer, String> parameters, int maxResultLimit) throws SpatialServiceException {
        throw new NotImplementedException("Not implemented, yet");
    }

    @Override
    public List<T> findAllEntity(final Class<T> entityClass) throws SpatialServiceException {

        List<T> objectList;

        try {
            LOG.debug("Finding all entity list for : " + entityClass.getSimpleName());
            objectList = em.createQuery("from " + entityClass.getSimpleName(), entityClass).getResultList();
        }

        catch (Exception e) {
            e.printStackTrace();
            LOG.debug("Error occurred while finding all entity list for : " + entityClass.getSimpleName());
            throw new SpatialServiceException();
        }
        return objectList;
    }

    @Override
    public void deleteEntity(final T entity, final Object id) throws SpatialServiceException {

        try {
            LOG.debug("Deleting entity : " + entity.getClass().getSimpleName());
            em.remove(em.contains(entity) ? entity : em.merge(entity));
        }
        catch (Exception e) {
            e.printStackTrace();
            LOG.debug("Error occurred during deleting entity : " + entity.getClass().getSimpleName());
            throw new SpatialServiceException();
        }

    }

}