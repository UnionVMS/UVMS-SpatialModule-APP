package eu.europa.ec.fisheries.uvms.spatial.repository;

import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.spatial.dao.AreaDao;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.ClosestAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.ClosestLocationDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.MeasurementUnit;
import eu.europa.ec.fisheries.uvms.spatial.util.SqlPropertyHolder;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
@Local(value = SpatialRepository.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SpatialRepositoryBean extends AbstractDAO implements SpatialRepository {

    @PersistenceContext(unitName = "spatialPU")
    private EntityManager em;

    @EJB
    private SqlPropertyHolder sql;

    private AreaDao areaDao;

    @PostConstruct
    public void init(){
        areaDao = new AreaDao(em, sql);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    public List<Integer> findAreasIdByLocation(Point point, String areaDbTable) {
        return areaDao.findAreasIdByLocation(point, areaDbTable);
    }

    @Override
    public List<ClosestAreaDto> findClosestArea(Point point, MeasurementUnit unit, String areaDbTable) {
        return areaDao.findClosestArea(point, unit, areaDbTable);
    }

    @Override
    public List<ClosestLocationDto> findClosestlocation(Point point, MeasurementUnit unit, String areaDbTable) {
        return areaDao.findClosestlocation(point, unit, areaDbTable);
    }

    @Override
    public List findAreaOrLocationByCoordinates(Point point, String nativeQueryString) {
        return areaDao.findAreaOrLocationByCoordinates(point, nativeQueryString);
    }
}
