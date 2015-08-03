package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

/**
 * //TODO create test
 */
@Stateless
@Local(CrudService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CrudServiceBean implements CrudService {

    @PersistenceContext(unitName = "UVMS")
    EntityManager em;

    @Override
    public Object create(Object t) {
        return null;
    }

    @Override
    public Object find(Class type, Object id) {
        return this.em.find(type, id);
    }

    @Override
    public Object update(Object t) {
        return null;
    }

    @Override
    public void delete(Class type, Object id) {

    }

    @Override
    public List findWithNamedQuery(String queryName) {
        return null;
    }

    @Override
    public List findWithNamedQuery(String queryName, int resultLimit) {
        return null;
    }

    @Override
    public List findWithNamedQuery(String namedQueryName, Map parameters) {
        return null;
    }

    @Override
    public List findWithNamedQuery(String namedQueryName, Map parameters, int resultLimit) {
        return null;
    }

}
