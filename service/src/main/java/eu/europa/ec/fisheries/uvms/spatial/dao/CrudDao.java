package eu.europa.ec.fisheries.uvms.spatial.dao;

import java.util.List;
import java.util.Map;

public interface CrudDao {
    Object create(Object t);

    Object find(Class type, Object id);

    Object update(Object t);

    void delete(Class type, Object id);

    List findWithNamedQuery(String queryName);

    List findWithNamedQuery(String queryName, int resultLimit);

    List findWithNamedQuery(String namedQueryName, Map parameters);

    List findWithNamedQuery(String namedQueryName, Map parameters, int resultLimit);

    List findByNativeQuery(String sql, Class type);
}
