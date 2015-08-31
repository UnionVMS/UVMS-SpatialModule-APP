package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.service.AbstractCrudService;
import eu.europa.ec.fisheries.uvms.service.CrudService;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;

@Stateless
@Local(value = CrudService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CommonGenericDAOBean extends AbstractCrudService {

    @PersistenceContext(unitName = "UVMS")
    EntityManager em;

    @EJB
    private PostgreSqlEncoder encoder;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    protected String replaceAndEscapeParameters(String sqlString, HashMap<String, String> parameters) {
        for (String key : parameters.keySet()) {
            sqlString = sqlString.replace(key, encoder.encode(parameters.get(key)));
        }
        return sqlString;
    }
}