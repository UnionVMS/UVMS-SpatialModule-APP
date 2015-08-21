package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.service.CommonGenericDAO;
import eu.europa.ec.fisheries.uvms.service.JPACommonGenericDAO;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@Local(value = CommonGenericDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CommonGenericDAOBean extends JPACommonGenericDAO {

    @PersistenceContext(unitName = "UVMS")
    EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}