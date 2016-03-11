package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.service.QueryParameter;
import eu.europa.ec.fisheries.uvms.spatial.dao.AreaDao;
import eu.europa.ec.fisheries.uvms.spatial.dao.BookmarkDao;
import eu.europa.ec.fisheries.uvms.spatial.dao.CountryDao;
import eu.europa.ec.fisheries.uvms.spatial.dao.EezDao;
import eu.europa.ec.fisheries.uvms.spatial.dao.MapConfigDao;
import eu.europa.ec.fisheries.uvms.spatial.dao.PortAreaDao;
import eu.europa.ec.fisheries.uvms.spatial.dao.ProjectionDao;
import eu.europa.ec.fisheries.uvms.spatial.dao.ReportConnectSpatialDao;
import eu.europa.ec.fisheries.uvms.spatial.dao.RfmoDao;
import eu.europa.ec.fisheries.uvms.spatial.dao.SysConfigDao;
import eu.europa.ec.fisheries.uvms.spatial.dao.UserAreaJpaDao;
import eu.europa.ec.fisheries.uvms.spatial.entity.BookmarkEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.PortAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ProjectionEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ReportConnectServiceAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ReportConnectSpatialEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ServiceLayerEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.UserAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.config.SysConfigEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.bookmark.Bookmark;
import eu.europa.ec.fisheries.uvms.spatial.model.constants.USMSpatial;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.ClosestAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.ClosestLocationDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.FilterAreasDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.UserAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.ProjectionDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.AreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.ServiceLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.util.MeasurementUnit;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.BookmarkMapper;
import eu.europa.ec.fisheries.uvms.spatial.util.SqlPropertyHolder;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static eu.europa.ec.fisheries.uvms.service.QueryParameter.with;
import static org.apache.commons.collections.CollectionUtils.isEmpty;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

@Stateless
@Local(value = SpatialRepository.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED) // TODO why class level?
public class SpatialRepositoryBean extends AbstractDAO implements SpatialRepository { //FIXME extends AbstractDao

    private @PersistenceContext(unitName = "spatialPU") EntityManager em;
    private @EJB SqlPropertyHolder sql;

    private AreaDao areaDao;
    private UserAreaJpaDao userAreaDao;
    private CountryDao countryDao;
    private MapConfigDao mapConfigDao;
    private EezDao eezDao;
    private SysConfigDao sysConfigDao;
    private ReportConnectSpatialDao reportConnectSpatialDao;
    private BookmarkDao bookmarkDao;
    private ProjectionDao projectionDao;
    private PortAreaDao portAreaDao;
    private RfmoDao rfmoDao;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    @PostConstruct
    public void init() {
        areaDao = new AreaDao(em, sql);
        userAreaDao = new UserAreaJpaDao(em);
        countryDao = new CountryDao(em);
        mapConfigDao = new MapConfigDao(em);
        eezDao = new EezDao(em);
        sysConfigDao = new SysConfigDao(em);
        reportConnectSpatialDao = new ReportConnectSpatialDao(em);
        bookmarkDao = new BookmarkDao(em);
        projectionDao = new ProjectionDao(em);
        portAreaDao = new PortAreaDao(em);
        rfmoDao = new RfmoDao(em);

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
    public List<AreaLayerDto> findSystemAreaAndLocationLayerMapping() {
        return areaDao.findSystemAreaAndLocationLayerMapping();
    }

    @Override
    public List<UserAreaLayerDto> findUserAreaLayerMapping() {
        return userAreaDao.findUserAreaLayerMapping();
    }

    @Override
    public List<Map<String, String>> findAreaByFilter(String areaType, String filter) {
        return areaDao.findAreaByFilter(areaType, filter);
    }

    @Override
    public List<UserAreasEntity> findUserAreaDetailsByLocation(String userName, Point point) {
        return userAreaDao.findUserAreaDetailsWithGeom(userName, point);
    }

    @Override
    public List<UserAreaDto> findUserAreaDetailsBySearchCriteria(String userName, String scopeName, String searchCriteria, boolean isPowerUser) {
        return userAreaDao.findUserAreaDetailsBySearchCriteria(userName, scopeName, searchCriteria, isPowerUser);
    }

    @Override
    public List<Map<String, String>> findSelectedAreaColumns(String namedQueryString, Number gid) {
        return areaDao.findSelectedAreaColumns(namedQueryString, gid);
    }

    @Override
    public FilterAreasDto filterAreas(List<String> userAreaTables, List<String> userAreaIds, List<String> scopeAreaTables, List<String> scopeAreaIds) {
        return areaDao.filterAreas(userAreaTables, userAreaIds, scopeAreaTables, scopeAreaIds);
    }

    @Override
    public List<Map<String, String>> findAllCountriesDesc() {
        return countryDao.findAllCountriesDesc();
    }

    @Override
    public List<ProjectionDto> findProjectionByMap(long reportId) {
        return mapConfigDao.findProjectionByMap(reportId);
    }

    @Override
    public List<ProjectionDto> findProjectionById(Long id) {
        return mapConfigDao.findProjectionById(id);
    }

    @Override
    public List<ReportConnectServiceAreasEntity> findReportConnectServiceAreas(long reportId) {
        return mapConfigDao.findReportConnectServiceAreas(reportId);
    }

    @Override
    public List<ServiceLayerEntity> findServiceLayerEntityByIds(List<Long> ids) {
        return mapConfigDao.findServiceLayerEntityByIds(ids);
    }

    @Override
    public ReportConnectSpatialEntity findReportConnectSpatialBy(final Long reportId) throws ServiceException {
        List<ReportConnectSpatialEntity> list = reportConnectSpatialDao.findReportConnectSpatialBy(reportId);

        ReportConnectSpatialEntity result = null;

        if (isNotEmpty(list)) {
            if (list.size() > 1) {
                throw new IllegalStateException("More than one map configuration has been found for report with id = " + reportId);
            } else {
                result = list.get(0);
            }
        }

        return result;
    }

    @Override
    @Transactional
    public boolean saveOrUpdateMapConfiguration(final ReportConnectSpatialEntity mapConfiguration) throws ServiceException {
        validateMapConfiguration(mapConfiguration);
        return reportConnectSpatialDao.saveOrUpdateEntity(mapConfiguration) != null;
    }

    private void validateMapConfiguration(ReportConnectSpatialEntity mapConfiguration) {
        if (mapConfiguration == null) {
            throw new IllegalArgumentException("MAP CONFIGURATION CAN NOT BE NULL");
        }
    }

    @Override
    public EezEntity getEezById(final Long id) throws ServiceException {
        return eezDao.getEezById(id);
    }

    @Override
    public void updateSystemConfigs(List<SysConfigEntity> sysConfigs) {
        sysConfigDao.updateSystemConfigs(sysConfigs);
    }

    @Override
    public void updateSystemConfig(Map<String, String> parameters, String value) throws ServiceException {
        List<SysConfigEntity> configs = findEntityByNamedQuery(SysConfigEntity.class, QueryNameConstants.FIND_CONFIG, parameters);
        if (configs != null && !configs.isEmpty()) {
            SysConfigEntity sysConfigEntity = configs.get(0);
            sysConfigEntity.setValue(value);
        } else {
            SysConfigEntity sysConfigEntity = new SysConfigEntity();
            String name = new ArrayList<>(parameters.keySet()).get(0);
            sysConfigEntity.setName(name);
            sysConfigEntity.setValue(value);
            saveOrUpdateEntity(sysConfigEntity);
        }
    }

    @Override
    public List<SysConfigEntity> findSystemConfigs() {
        return sysConfigDao.findSystemConfigs();
    }

    @Override
    public String findSystemConfigByName(Map<String, String> parameters) throws ServiceException {
        List<String> geoServerUrl = findEntityByNamedQuery(String.class, QueryNameConstants.FIND_CONFIG_BY_NAME, parameters, 1);
        return (geoServerUrl != null && !geoServerUrl.isEmpty()) ? geoServerUrl.get(0) : null;
    }

    @Override
    @Transactional
    public void deleteBy(final List<Long> spatialConnectIds) throws ServiceException {
        reportConnectSpatialDao.deleteById(spatialConnectIds);
    }

    public List<ServiceLayerDto> findServiceLayerBySubType(List<String> subAreaTypes, boolean isWithBing) {
        return areaDao.findServiceLayerBySubType(subAreaTypes, isWithBing);
    }

    @Override
    public List<AreaDto> getAllUserAreas(String userName) {
        return userAreaDao.getAllUserAreas(userName);
    }

    @Override
    public List<UserAreasEntity> findUserAreaById(Long userAreaId, String userName, Boolean isPowerUser, String scopeName) throws ServiceException {
        QueryParameter param = with("userAreaId", userAreaId).and("userName", userName).and("isPowerUser", isPowerUser?1:0).and("scopeName", scopeName);

        return findEntityByNamedQuery(UserAreasEntity.class, QueryNameConstants.FIND_USER_AREA_BY_ID, param.parameters(), 1);
    }

    @Override
    public int disableAllEezAreas() throws ServiceException {
        return updateEntityByNamedQuery(QueryNameConstants.DISABLE_EEZ_AREAS);
    }

    @Override
    public int disableAllRfmoAreas() throws ServiceException {
        return updateEntityByNamedQuery(QueryNameConstants.DISABLE_RFMO_AREAS);
    }

    @Override
    public int disableAllPortLocations() throws ServiceException {
        return updateEntityByNamedQuery(QueryNameConstants.DISABLE_PORT_LOCATIONS);
    }

    @Override
    public int disableAllPortAreas() throws ServiceException {
        return updateEntityByNamedQuery(QueryNameConstants.DISABLE_PORT_AREAS);
    }

    @Override
    public List<String> getUserAreaTypes(String userName, String scopeName, boolean isPowerUser) throws ServiceException {
        QueryParameter params = with(USMSpatial.USER_NAME, userName).and(USMSpatial.SCOPE_NAME, scopeName).and("isPowerUser", isPowerUser?1:0);
        List<String> userAreaTypes = findEntityByNamedQuery(String.class, QueryNameConstants.FIND_USER_AREA_TYPES, params.parameters());
        if (isEmpty(userAreaTypes) || userAreaTypes.get(0) == null) {
            return Collections.emptyList();
        }
        return userAreaTypes;
    }

    @Override
    public List<PortAreasEntity> findPortAreaById(Long portAreaId) throws ServiceException {
        return findEntityByNamedQuery(PortAreasEntity.class, QueryNameConstants.FIND_PORT_AREA_BY_ID, with("portAreaId", portAreaId).parameters(), 1);
    }

    @Override
    public List<AreaDto> findAllUserAreasByGids(List<Long> gids) {
        return userAreaDao.findAllUserAreasByGids(gids);
    }


    @Override
    public BookmarkEntity create(BookmarkEntity bookmark) throws ServiceException {
        return bookmarkDao.createEntity(bookmark);
    }

    @Override
    public Set<Bookmark> listBookmarksBy(String userName) throws ServiceException {
        return BookmarkMapper.INSTANCE.bookmarkEntityListToBookmarkSet(bookmarkDao.findAllByUser(userName));
    }

    @Override
    public void delete(Long id) throws ServiceException {

        BookmarkEntity entityById = bookmarkDao.findEntityById(BookmarkEntity.class, id);
        if (entityById != null) {
            bookmarkDao.deleteEntity(entityById);
        }
    }

    @Override
    public void update(Bookmark bookmark) throws ServiceException {
        BookmarkEntity entityById = bookmarkDao.findEntityById(BookmarkEntity.class, bookmark.getId());
        BookmarkMapper.INSTANCE.merge(bookmark, entityById);

    }

    @Override
    public ProjectionEntity findProjection(Integer srsCode) throws ServiceException {

        return projectionDao.findBySrsCode(srsCode);
    }

    @Override
    public List<String> getAreaGroups(String userName, String scopeName, boolean isPowerUser) {
        return areaDao.listAreaGroups(userName, scopeName, isPowerUser);
    }

    @Override
    public List<UserAreasEntity> findUserAreasByType(String userName, String scopeName, String type, boolean isPowerUser) throws ServiceException {
        List<UserAreasEntity> userAreasDTOs;
        QueryParameter params = with(USMSpatial.USER_NAME, userName).and(USMSpatial.SCOPE_NAME, scopeName).and("isPowerUser", isPowerUser?1:0).and("type", type);
        List<UserAreasEntity>  userAreas = findEntityByNamedQuery(UserAreasEntity.class, QueryNameConstants.FIND_USER_AREA_BY_TYPE, params.parameters());
        if (isEmpty(userAreas)) {
            userAreasDTOs = Collections.emptyList();
        } else {
            userAreasDTOs = userAreas;
        }

        return userAreasDTOs;
    }

    // AreaRepository
    @Override
    public List<EezEntity> findEezByIntersect(final Point point) throws ServiceException {
        return eezDao.intersects(point);
    }

    // AreaRepository
    @Override
    public List findPortAreaByIntersect(final Point point) throws ServiceException {
        return portAreaDao.intersects(point);
    }

    // AreaRepository
    @Override
    public List findRfmoByIntersect(final Point point) throws ServiceException {
        return rfmoDao.intersects(point);
    }

    // AreaRepository
    @Override
    public List findUserAreaByIntersect(final Point point) throws ServiceException {
        return userAreaDao.intersects(point);
    }

    @Override
    public void deleteReportConnectServiceAreas(Long id) {
        mapConfigDao.deleteReportConnectServiceAreas(id);
    }

    @Override
    public void deleteReportConnectServiceAreas(Set<ReportConnectServiceAreasEntity> reportConnectServiceAreases) {
        if (!reportConnectServiceAreases.isEmpty()) {
            for (ReportConnectServiceAreasEntity entity : reportConnectServiceAreases) {
                deleteEntity(entity);
            }
        }
    }

    @Override
    public ReportConnectSpatialEntity findReportConnectSpatialById(final Long reportId, final Long id) throws ServiceException {
        List<ReportConnectSpatialEntity> list = reportConnectSpatialDao.findReportConnectSpatialById(reportId, id);
        if (isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }
}
