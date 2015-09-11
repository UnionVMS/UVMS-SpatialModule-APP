package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.service.AbstractCrudService;
import eu.europa.ec.fisheries.uvms.service.CrudService;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@Local(value = CrudService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CrudServiceBean extends AbstractCrudService {

    @PersistenceContext(unitName = "spatialPU")
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

}