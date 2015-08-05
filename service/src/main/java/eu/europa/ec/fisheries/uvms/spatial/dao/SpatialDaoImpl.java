package eu.europa.ec.fisheries.uvms.spatial.dao;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by kopyczmi on 05-Aug-15.
 */
@Stateless
@Local(SpatialDao.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SpatialDaoImpl implements SpatialDao {

    @PersistenceContext(unitName = "UVMS")
    private EntityManager em;

    @Override
    public List<String> getAreaTypes() {
        String sql = "SELECT a.typeName FROM AreaTypes a";
        return em.createQuery(sql, String.class).getResultList();
    }
}
