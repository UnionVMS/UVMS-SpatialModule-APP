package eu.europa.ec.fisheries.uvms.spatial.repository;

import eu.europa.ec.fisheries.uvms.util.SqlPropertyHolder;
import lombok.experimental.Delegate;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@Local(value = SpatialRepository.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SpatialRepositoryBean implements SpatialRepository {

    @PersistenceContext(unitName = "spatialPU")
    private EntityManager em;

    @EJB
    private SqlPropertyHolder sql;

    @Delegate
    private AreaDao areaDao;

    @PostConstruct
    public void init(){
        areaDao = new AreaDao(em, sql);
    }

}
