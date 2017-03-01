/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.bean.impl;

import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.service.QueryParameter;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.AbstractAreaDao;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.AreaLocationTypesDao;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.BookmarkDao;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.CountryDao;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.EezDao;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.PortDao;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.ProjectionDao;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.ReportConnectServiceAreaDao;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.ReportConnectSpatialDao;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.ServiceLayerDao;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.SysConfigDao;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.UserAreaDao;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.util.DatabaseDialect;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.area.AreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.layer.ServiceLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.BookmarkEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.CountryEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.PortEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.ProjectionEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.ReportConnectServiceAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.ReportConnectSpatialEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.ServiceLayerEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.UserAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.SysConfigEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.bookmark.Bookmark;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaSimpleType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.area.AreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.layer.UserAreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.config.ProjectionDto;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.BookmarkMapper;
import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

@Stateless
@Local(value = SpatialRepository.class)
public class SpatialRepositoryBean implements SpatialRepository {

    private EntityManager em;

    @PersistenceContext(unitName = "spatialPUpostgres")
    private EntityManager postgres;

    @PersistenceContext(unitName = "spatialPUoracle")
    private EntityManager oracle;	
	
    private UserAreaDao userAreaDao;
    private AbstractAreaDao abstractAreaDao;
    private SysConfigDao sysConfigDao;
    private ReportConnectSpatialDao reportConnectSpatialDao;
    private BookmarkDao bookmarkDao;
    private ProjectionDao projectionDao;
    private AreaLocationTypesDao areaLocationTypeDao;
    private ServiceLayerDao serviceLayerDao;
    private ReportConnectServiceAreaDao connectServiceAreaDao;
    private PortDao portDao;
    private CountryDao countryDao;

    public void initEntityManager() {
        String dbDialect = System.getProperty("db.dialect");
        if ("oracle".equalsIgnoreCase(dbDialect)) {
            em = oracle;
        } else {
            em = postgres;
        }
    }	
	
    @PostConstruct
    public void init() {
		initEntityManager();	
        abstractAreaDao = new EezDao(em);
        userAreaDao = new UserAreaDao(em);
        sysConfigDao = new SysConfigDao(em);
        reportConnectSpatialDao = new ReportConnectSpatialDao(em);
        bookmarkDao = new BookmarkDao(em);
        projectionDao = new ProjectionDao(em);
        areaLocationTypeDao = new AreaLocationTypesDao(em);
        serviceLayerDao = new ServiceLayerDao(em);
        connectServiceAreaDao = new ReportConnectServiceAreaDao(em);
        portDao = new PortDao(em);
        countryDao = new CountryDao(em);
    }

    @Override
    public List<AreaLayerDto> findSystemAreaLayerMapping() {
        return areaLocationTypeDao.findSystemAreaLayerMapping();
    }

    @Override
    public List<AreaLayerDto> findSystemAreaAndLocationLayerMapping() {
        return areaLocationTypeDao.findSystemAreaAndLocationLayerMapping();
    }

    @Override
    public List<UserAreaLayerDto> findUserAreaLayerMapping() {
        return areaLocationTypeDao.findUserAreaLayerMapping();
    }

    @Override
    public List<UserAreasEntity> findUserAreaDetailsByLocation(final String userName, final Point point) throws ServiceException {
        return userAreaDao.findByUserNameAndGeometry(userName, point);
    }

    @Override
    public List<Map<String, String>> findSelectedAreaColumns(String namedQueryString, List<Long> gids) {
        return abstractAreaDao.findSelectedAreaColumns(namedQueryString, gids);
    }

    @Override
    public List<ProjectionDto> findProjectionByMap(long reportId) {
        return reportConnectSpatialDao.findProjectionByMap(reportId);
    }

    @Override
    public List<ProjectionDto> findProjectionById(Long id) {
        return projectionDao.findProjectionById(id);
    }

    @Override
    public ProjectionEntity findProjectionEntityById(Long id) throws ServiceException {
        return projectionDao.findEntityById(ProjectionEntity.class, id);
    }

    @Override
    public List<ReportConnectServiceAreasEntity> findReportConnectServiceAreas(long reportId) {
        return connectServiceAreaDao.findReportConnectServiceAreas(reportId);
    }

    @Override
    public List<ServiceLayerEntity> findServiceLayerEntityByIds(List<Long> ids) {
        return serviceLayerDao.findServiceLayerEntityByIds(ids);
    }

    @Override
    public ReportConnectSpatialEntity findReportConnectSpatialByReportId(final Long reportId) throws ServiceException {
        return reportConnectSpatialDao.findByReportId(reportId);
    }

    @Override
    public List<ReportConnectSpatialEntity> findReportConnectSpatialByConnectId(final Long id) throws ServiceException {
        return reportConnectSpatialDao.findByConnectId(id);
    }

    @Override
    public boolean saveOrUpdateMapConfiguration(final ReportConnectSpatialEntity mapConfiguration) throws ServiceException {
        return reportConnectSpatialDao.saveOrUpdateEntity(mapConfiguration) != null;
    }

    @Override
    public void updateSystemConfigs(List<SysConfigEntity> sysConfigs) {
        sysConfigDao.updateSystemConfigs(sysConfigs);
    }

    @Override
    public void updateSystemConfig(String name, String value) throws ServiceException { // TODO move logic to resource or dao please
        List<SysConfigEntity> configs = sysConfigDao.findSystemConfigByName(name);
        if (configs != null && !configs.isEmpty()) {
            SysConfigEntity sysConfigEntity = configs.get(0);
            sysConfigEntity.setValue(value);
        } else {
            SysConfigEntity sysConfigEntity = new SysConfigEntity();
            sysConfigEntity.setName(name);
            sysConfigEntity.setValue(value);
            sysConfigDao.saveOrUpdateEntity(sysConfigEntity);
        }
    }

    @Override
    public List<SysConfigEntity> findSystemConfigs() {
        return sysConfigDao.findSystemConfigs();
    }

    @Override
    public String findSystemConfigByName(String name) throws ServiceException {
        List<SysConfigEntity> entityByNamedQuery = sysConfigDao.findSystemConfigByName(name);
        return (entityByNamedQuery != null && !entityByNamedQuery.isEmpty()) ? entityByNamedQuery.get(0).getValue() : null;
    }

    public List<ServiceLayerDto> findServiceLayerBySubType(List<String> subAreaTypes, boolean isWithBing) {
        return serviceLayerDao.findServiceLayerBySubType(subAreaTypes, isWithBing);
    }

    @Override
    public List<AreaDto> getAllUserAreas(String userName, String scopeName) {
        return userAreaDao.getAllUserAreas(userName, scopeName);
    }

    @Override
    public List<AreaDto> getAllUserAreaGroupNames(String userName, String scopeName) {
        return userAreaDao.getAllUserAreaGroupName(userName, scopeName);
    }

    @Override
    public List<Long> getAllSharedGids(String userName, String scopeName, String type) {
        return userAreaDao.getAllSharedGids(userName, scopeName, type);
    }

    @Override
    public UserAreasEntity findUserAreaById(Long userAreaId, String userName, Boolean isPowerUser, String scopeName) throws ServiceException {
        return userAreaDao.findOne(userAreaId, userName, isPowerUser, scopeName);
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
    public void deleteBookmark(Long id) throws ServiceException {

        BookmarkEntity entityById = bookmarkDao.findEntityById(BookmarkEntity.class, id);
        if (entityById != null) {
            bookmarkDao.deleteEntity(BookmarkEntity.class, entityById.getId());
        }
    }

    @Override
    public BookmarkEntity getBookmarkBy(Long id) throws ServiceException {
        return bookmarkDao.findEntityById(BookmarkEntity.class, id);
    }

    @Override
    public List<ProjectionEntity> findProjection(Integer srsCode) throws ServiceException {
        return projectionDao.findBySrsCode(srsCode);
    }

    @Override
    public List<ProjectionEntity> findProjection() throws ServiceException {
        return projectionDao.findAllEntity(ProjectionEntity.class);
    }

    @Override
    public List<UserAreasEntity> findUserAreasByType(String userName, String scopeName, String type, boolean isPowerUser) throws ServiceException {
       return userAreaDao.findByUserNameAndScopeNameAndTypeAndPowerUser(userName, scopeName, type, isPowerUser);
    }

    @Override
    public AreaLocationTypesEntity findAreaLocationTypeByTypeName(final String typeName) throws ServiceException {
        return areaLocationTypeDao.findOneByTypeName(typeName);
    }

    @Override
    public List<AreaLocationTypesEntity> findAllIsPointIsSystemWide(Boolean isLocation, Boolean isSystemWide) throws ServiceException {
        return areaLocationTypeDao.findByIsLocationAndIsSystemWide(isLocation, isSystemWide);
    }

    @Override
    public List<AreaLocationTypesEntity> findAllIsLocation(Boolean isLocation) throws ServiceException {
        return areaLocationTypeDao.findByIsLocation(isLocation);
    }

    @Override
    public void deleteReportConnectServiceAreas(Long id) {
        connectServiceAreaDao.deleteReportConnectServiceAreas(id);
    }

    @Override
    public void deleteReportConnectServiceAreas(Set<ReportConnectServiceAreasEntity> reportConnectServiceAreases) {
        if (!reportConnectServiceAreases.isEmpty()) {
            for (ReportConnectServiceAreasEntity entity : reportConnectServiceAreases) {
                connectServiceAreaDao.delete(entity);
            }
        }
    }

    @Override
    public ReportConnectSpatialEntity findReportConnectSpatialByReportIdAndConnectId(final Long reportId, final Long id) throws ServiceException {
        List<ReportConnectSpatialEntity> list = reportConnectSpatialDao.findByReportIdAndConnectId(reportId, id);
        if (isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public ServiceLayerEntity getServiceLayerBy(AreaType areaType) throws ServiceException {
        return serviceLayerDao.getBy(areaType);
    }

    @Override
    public ServiceLayerEntity getServiceLayerBy(Long id) throws ServiceException {
        return serviceLayerDao.findEntityById(ServiceLayerEntity.class, id);
    }

    @Override
    public List<UserAreasEntity> listUserAreaByCriteria(String userName, String scopeName, String searchCriteria, boolean isPowerUser) throws ServiceException {
        return userAreaDao.listByCriteria(userName, scopeName, searchCriteria, isPowerUser);
    }

    @Override
    public UserAreasEntity getUserAreaByUserNameAndName(String userName, String name) throws ServiceException {
        return userAreaDao.getByUserNameAndName(userName, name);
    }

    @Override
    public  List<UserAreasEntity> findUserArea(String userName, String scopeName, boolean isPowerUser) throws ServiceException {
        return userAreaDao.findUserAreasTypes(userName, scopeName, isPowerUser);
    }

    @Override
    public UserAreasEntity save(UserAreasEntity userAreasEntity) throws ServiceException {
        return userAreaDao.save(userAreasEntity);
    }

    @Override
    public UserAreasEntity update(UserAreasEntity entity) throws ServiceException {
        return userAreaDao.update(entity);
    }

    @Override
    public void deleteEntity(ReportConnectSpatialEntity entity) {
        reportConnectSpatialDao.deleteEntity(ReportConnectSpatialEntity.class, entity.getId());
    }

    @Override
    public List closestArea(List<AreaLocationTypesEntity> entities, DatabaseDialect spatialFunction, Point point) {
        return abstractAreaDao.closestArea(entities, spatialFunction, point);
    }

    @Override
    public List closestPoint(List<AreaLocationTypesEntity> entities, DatabaseDialect spatialFunction, Point incomingPoint) {
        return abstractAreaDao.closestPoint(entities, spatialFunction, incomingPoint);
    }

    @Override
    public List intersectingArea(List<AreaLocationTypesEntity> entities, DatabaseDialect spatialFunction, Point point) {
        return abstractAreaDao.intersectingArea(entities, spatialFunction, point);
    }

    @Override
    public List<AreaLocationTypesEntity> listAllArea() throws ServiceException {
        return areaLocationTypeDao.findByIsLocation(false);
    }

    @Override
    public void deleteUserArea(UserAreasEntity userAreaById) {
        userAreaDao.deleteEntity(UserAreasEntity.class, userAreaById.getId());
    }

    @Override
    public List<UserAreasEntity> findUserAreaByUserNameAndScopeName(String userName, String scopeName) throws ServiceException {
        return userAreaDao.findByUserNameAndScopeName(userName, scopeName);
    }

    @Override
    public List<PortEntity> listClosestPorts(final Point point, final Integer limit) throws ServiceException {
        final Map parameters = QueryParameter.with("shape", point).parameters();
        return portDao.findEntityByNamedQuery(PortEntity.class, PortEntity.LIST_ORDERED_BY_DISTANCE, parameters, limit);
    }

    @Override
    public List<CountryEntity> findAllCountries() throws ServiceException {
        return countryDao.findEntitiesByNamedQuery(CountryEntity.FIND_ALL);
    }

    @Override
    public List listBaseAreaList(final String query) throws ServiceException {
        return abstractAreaDao.listBaseAreas(query);
    }

    @Override
    public void makeGeomValid(String areaDbTable, DatabaseDialect dialect) {
        abstractAreaDao.makeGeomValid(areaDbTable, dialect);
    }

    /**
     * {@inheritDoc}
     */
    public void updateUserAreaForUserAndScope(Date startDate, Date endDate, String type) throws ServiceException {
        userAreaDao.updateUserAreasForUserAndScope(startDate, endDate, type);
    }

    /**
     * {@inheritDoc}
     */
    public void updateUserAreaForUser(String remoteUser, Date startDate, Date endDate, String type) throws ServiceException {
        userAreaDao.updateUserAreasForUser(remoteUser, startDate, endDate, type);
    }

    @Override
    public List areaByCode(List<AreaSimpleType> areaSimpleTypeList) throws ServiceException {
        return  ((AbstractAreaDao) portDao).byCode(areaSimpleTypeList);
    }

}