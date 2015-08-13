package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.exception.SpatialServiceException;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static eu.europa.ec.fisheries.uvms.exception.SpatialServiceErrors.DAO_FIX_IT_ERROR;

/**
 * This class is responsible for all application level database interaction.
 * It provides unified apis for all basic CRUD operations like Create, Read, Update, Delete.
 */
@Stateless
@Local(CommonGenericDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class JPACommonGenericDAO<T> implements CommonGenericDAO<T> {

    private static final Logger LOG = LoggerFactory.getLogger(JPACommonGenericDAO.class);

    @PersistenceContext(unitName = "UVMS")
    EntityManager em;

    @Override
    public T createEntity(final T entity) {
        try {
            LOG.debug("Persisting entity : " + entity.getClass().getSimpleName());
            em.persist(entity);
        } catch (Exception e) {
            LOG.error("Error occurred during Persisting entity : " + entity.getClass().getSimpleName());
            throw new SpatialServiceException(DAO_FIX_IT_ERROR, e);
        }
        return entity;
    }

    @Override
    public T updateEntity(final T entity) {
        try {
            LOG.debug("Updating entity : " + entity.getClass().getSimpleName());
            em.merge(entity);
        } catch (Exception e) {
            LOG.error("Error occurred during updating entity : " + entity.getClass().getSimpleName());
            throw new SpatialServiceException(DAO_FIX_IT_ERROR, e);
        }
        return entity;
    }

    @Override
    public T findEntityById(final Class<T> entityClass, final Object id) {
        T obj;

        try {
            LOG.debug("Finding entity : " + entityClass.getSimpleName() + " with ID : " + id.toString());
            obj = em.find(entityClass, id);
        } catch (Exception e) {
            LOG.error("Error occurred during finding entity : " + entityClass.getSimpleName() + " with ID : " + id.toString());
            throw new SpatialServiceException(DAO_FIX_IT_ERROR, e);
        }
        return obj;
    }

    @Override
    public List<T> findEntityByQuery(final Class<T> entityClass, final String hqlQuery) {
        List<T> objectList;

        try {
            LOG.debug("Finding entity for query : " + hqlQuery);
            objectList = em.createQuery(hqlQuery, entityClass).getResultList();
        } catch (Exception e) {
            LOG.error("Error occurred during finding entity for query : " + hqlQuery);
            throw new SpatialServiceException(DAO_FIX_IT_ERROR, e);
        }

        return objectList;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findEntityByQuery(final Class<T> entityClass, final String hqlQuery, final Map<Integer, String> parameters) {
        List<T> objectList;

        try {
            LOG.debug("Finding entity for query : " + hqlQuery);
            Set<Entry<Integer, String>> rawParameters = parameters.entrySet();
            Query query = em.createQuery(hqlQuery, entityClass);
            for (Entry<Integer, String> entry : rawParameters) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
            objectList = query.getResultList();
        } catch (Exception e) {
            LOG.error("Error occurred during finding entity for query : " + hqlQuery);
            throw new SpatialServiceException(DAO_FIX_IT_ERROR, e);
        }

        return objectList;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findEntityByQuery(final Class<T> entityClass, final String hqlQuery, final Map<Integer, String> parameters, final int maxResultLimit) {
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
        } catch (Exception e) {
            LOG.error("Error occurred during finding entity for query : " + hqlQuery);
            throw new SpatialServiceException(DAO_FIX_IT_ERROR, e);
        }

        return objectList;
    }

    @Override
    public List<T> findEntityByNamedQuery(final Class<T> entityClass, final String queryName) {
        try {
            TypedQuery<T> query = em.createNamedQuery(queryName, entityClass);
            return query.getResultList();
        } catch (Exception e) {
            LOG.error("Error occurred during finding entity for query : {}", e.getMessage());
            throw new SpatialServiceException(DAO_FIX_IT_ERROR, e, queryName);
        }
    }

    @Override
    public List<T> findEntityByNamedQuery(Class<T> entityClass, String queryName, Map<Integer, String> parameters) {
        throw new NotImplementedException("Not implemented, yet");
    }

    @Override
    public List<T> findEntityByNamedQuery(Class<T> entityClass, String queryName, Map<Integer, String> parameters, int maxResultLimit) {
        throw new NotImplementedException("Not implemented, yet");
    }

    @Override
    public List<T> findAllEntity(final Class<T> entityClass) {
        List<T> objectList;

        try {
            LOG.debug("Finding all entity list for : " + entityClass.getSimpleName());
            objectList = em.createQuery("from " + entityClass.getSimpleName(), entityClass).getResultList();
        } catch (Exception e) {
            LOG.error("Error occurred while finding all entity list for : " + entityClass.getSimpleName());
            throw new SpatialServiceException(DAO_FIX_IT_ERROR, e);
        }
        return objectList;
    }

    @Override
    public void deleteEntity(final T entity, final Object id) {
        try {
            LOG.debug("Deleting entity : " + entity.getClass().getSimpleName());
            em.remove(em.contains(entity) ? entity : em.merge(entity));
        } catch (Exception e) {
            LOG.error("Error occurred during deleting entity : " + entity.getClass().getSimpleName());
            throw new SpatialServiceException(DAO_FIX_IT_ERROR, e);
        }
    }

}