package eu.europa.ec.fisheries.uvms.spatial.dao;

import org.hibernate.cfg.NotYetImplementedException;
import org.jboss.logging.Logger;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for all application level database interaction.
 * It provides unified apis for all basic CRUD operations like Create, Read, Update, Delete.
 *
 */
@Stateless
@Local(CommonGenericDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class JPACommonGenericDAO<T> implements CommonGenericDAO<T> {

    private static final Logger LOG = Logger.getLogger(JPACommonGenericDAO.class);

    @PersistenceContext(unitName = "UVMS")
    EntityManager em;

    @Override
    public T createEntity(T entity) throws Exception {
        try {
            LOG.debug("Persisting entity : " + entity.getClass().getSimpleName());
            em.persist(entity);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.debug("Error occured during Persisting entity : " + entity.getClass().getSimpleName());
            throw new Exception();
        }
        return entity;
    }

    @Override
    public T updateEntity(T entity) {
        throw new NotYetImplementedException();
    }

    @Override
    public T findEntityById(Class<T> entityClass, Object id) throws Exception {
        T obj;
        try {
            LOG.debug("Finding entity : " + entityClass.getSimpleName() + " with ID : " + id.toString());
            obj = (T) em.find(entityClass, id);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.debug("Error occurred during finding entity : " + entityClass.getSimpleName() + " with ID : " + id.toString());
            throw new Exception();
        }
        return obj;
    }

    @Override
    public List findWithNamedQuery(String namedQueryName) {
        return this.em.createNamedQuery(namedQueryName).getResultList();
    }

    @Override
    public List findWithNamedQuery(String queryName, int resultLimit) {
        return this.em.createNamedQuery(queryName).
                setMaxResults(resultLimit).
                getResultList();
    }

    @Override
    public List findWithNamedQuery(String namedQueryName, Map parameters) {
        return findWithNamedQuery(namedQueryName, parameters, 0);
    }

    @Override
    public List findWithNamedQuery(String namedQueryName, Map parameters, int resultLimit) {
        throw new NotYetImplementedException();
    }

    @Override
    public List<T> findEntityByQuery(Class<T> entityClass, String hqlQuery) throws Exception {
        List<T> objectList;

        try {
            LOG.debug("Finding entity for query : " + hqlQuery);
            objectList = em.createQuery(hqlQuery, entityClass).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            LOG.debug("Error occurred during finding entity for query : " + hqlQuery);
            throw new Exception();
        }
        return objectList;
    }

    @Override
    public boolean deleteEntity(T entity, Object id) {
        throw new NotYetImplementedException();
    }

}