package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.exception.SpatialServiceException;

import java.util.List;
import java.util.Map;

public interface CommonGenericDAO<T> {

    T createEntity(T entity) throws SpatialServiceException;

    T updateEntity(T entity) throws SpatialServiceException;

    T findEntityById(Class<T> entityClass, Object id) throws SpatialServiceException;

    List<T> findEntityByQuery(Class<T> entityClass, String hqlQuery) throws SpatialServiceException;

    List<T> findEntityByQuery(Class<T> entityClass, String hqlQuery, Map<Integer, String> parameters) throws SpatialServiceException;

    List<T> findEntityByQuery(Class<T> entityClass, String hqlQuery, Map<Integer, String> parameters, int maxResultLimit) throws SpatialServiceException;

    List<T> findEntityByNamedQuery(Class<T> entityClass, String queryName) throws SpatialServiceException;

    List<T> findEntityByNamedQuery(Class<T> entityClass, String queryName, Map<Integer, String> parameters) throws SpatialServiceException;

    List<T> findEntityByNamedQuery(Class<T> entityClass, String queryName, Map<Integer, String> parameters, int maxResultLimit) throws SpatialServiceException;

    List<T> findAllEntity(Class<T> entityClass) throws SpatialServiceException;

    void deleteEntity(T entity, Object id) throws SpatialServiceException;

}
