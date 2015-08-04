package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.schema.spatial.source.GetAreaTypesSpatialRS;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by kopyczmi on 03-Aug-15.
 */
@Stateless
@Local(AreaService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class AreaServiceBean implements AreaService { // TODO this class does same as CrudService class use that instead

    @PersistenceContext(unitName = "UVMS")
    private EntityManager em;

    @Override // TODO don't use jaxb generated classes directly into business logic create mapper instead
    public GetAreaTypesSpatialRS getAreaTypes() {
        GetAreaTypesSpatialRS response = new GetAreaTypesSpatialRS();
        //response.setAreaTypes(Arrays.asList("Portugal", "Belgium", "Poland", "Bulgaria"));

        List<String> areaTypes = em.createNamedQuery("getAreaTypes", String.class).getResultList();
        response.setAreaTypes(areaTypes);

        return response;
    }

}
