package eu.europa.ec.fisheries.uvms.spatial.repository;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.spatial.dao.AreaDao;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.*;
import eu.europa.ec.fisheries.uvms.spatial.util.SqlPropertyHolder;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

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
    public void init() {
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

    @Override
    public List<AreaLayerDto> findSystemAreaLayerMapping() {
        return areaDao.findSystemAreaLayerMapping();
    }

    @Override
    public List<Map<String, String>> findAreaByFilter(String areaType, String filter) {
        return areaDao.findAreaByFilter(areaType, filter);
    }

    @Override
    public List<Map<String, String>> findSelectedAreaColumns(String namedQueryString, Number gid) {
        return areaDao.findSelectedAreaColumns(namedQueryString, gid);
    }

    @Override
    public String filterAreas(List<AreaIdentifierDto> userAreas, List<AreaIdentifierDto> scopeAreas) {
        return areaDao.filterAreas(userAreas, scopeAreas);
    }
}
