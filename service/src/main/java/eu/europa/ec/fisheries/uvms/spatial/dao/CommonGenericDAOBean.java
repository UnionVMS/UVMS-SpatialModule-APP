package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.service.CommonGenericDAO;
import eu.europa.ec.fisheries.uvms.service.JPACommonGenericDAO;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;

@Stateless
@Local(value = CommonGenericDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CommonGenericDAOBean extends JPACommonGenericDAO {

    @PersistenceContext(unitName = "UVMS")
    EntityManager em;

    @EJB
    private PostgreSqlEncoder encoder;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    protected String replaceAndEscapeParameters(String sqlString, HashMap<String, String> parameters) {
        for (String key : parameters.keySet()) {
            sqlString = sqlString.replace(key, encoder.encode(parameters.get(key)));
        }
        return sqlString;
    }
}