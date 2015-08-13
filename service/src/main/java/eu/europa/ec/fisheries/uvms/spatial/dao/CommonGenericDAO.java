package eu.europa.ec.fisheries.uvms.spatial.dao;

import java.util.List;
import java.util.Map;

public interface CommonGenericDAO<T> {

    T createEntity(T entity);

    T updateEntity(T entity);

    T findEntityById(Class<T> entityClass, Object id);

    List<T> findEntityByQuery(Class<T> entityClass, String hqlQuery);

    List<T> findEntityByQuery(Class<T> entityClass, String hqlQuery, Map<Integer, String> parameters);

    List<T> findEntityByQuery(Class<T> entityClass, String hqlQuery, Map<Integer, String> parameters, int maxResultLimit);

    List<T> findEntityByNamedQuery(Class<T> entityClass, String queryName);

    List<T> findEntityByNamedQuery(Class<T> entityClass, String queryName, Map<Integer, String> parameters);

    List<T> findEntityByNamedQuery(Class<T> entityClass, String queryName, Map<Integer, String> parameters, int maxResultLimit);

    List<T> findAllEntity(Class<T> entityClass);

    void deleteEntity(T entity, Object id);

}
