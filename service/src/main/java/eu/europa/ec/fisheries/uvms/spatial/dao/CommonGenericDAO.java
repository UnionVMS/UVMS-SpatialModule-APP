package eu.europa.ec.fisheries.uvms.spatial.dao;

import java.util.List;
import java.util.Map;

public interface CommonGenericDAO<T> {

    T createEntity(T entity) throws Exception;

    T updateEntity(T entity);

    T findEntityById(Class<T> entityClass, Object id) throws Exception;

    List findWithNamedQuery(String queryName);

    List findWithNamedQuery(String queryName, int resultLimit);

    List findWithNamedQuery(String namedQueryName, Map parameters);

    List findWithNamedQuery(String namedQueryName, Map parameters, int resultLimit);

    List<T> findEntityByQuery(Class<T> entityClass, String hqlQuery) throws Exception;

    boolean deleteEntity(T entity, Object id);

}
