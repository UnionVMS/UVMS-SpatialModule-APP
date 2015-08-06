package eu.europa.ec.fisheries.uvms.spatial.dao;

import org.hibernate.cfg.NotYetImplementedException;

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
@Local(CrudDao.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CrudDaoImpl implements CrudDao {

    @PersistenceContext(unitName = "UVMS")
    EntityManager em;

    @Override
    public Object create(Object t) {
        throw new NotYetImplementedException();
    }

    @Override
    public Object find(Class type, Object id) {
        return this.em.find(type, id);
    }

    @Override
    public Object update(Object t) {
       throw new NotYetImplementedException();
    }

    @Override
    public void delete(Class type, Object id) {
        throw new NotYetImplementedException();
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
    public List findByNativeQuery(String sql, Class type) {
        return this.em.createNativeQuery(sql, type).getResultList();
    }

}