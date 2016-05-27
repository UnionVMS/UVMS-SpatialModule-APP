package eu.europa.ec.fisheries.uvms.spatial.service;

import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.dao.util.DatabaseDialect;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.BookmarkEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.CountryEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.PortEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ProjectionEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ReportConnectServiceAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ReportConnectSpatialEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ServiceLayerEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.UserAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.config.SysConfigEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.bookmark.Bookmark;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.ProjectionDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.AreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.ServiceLayerDto;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SpatialRepository {

    List<AreaLayerDto> findSystemAreaLayerMapping();

    List<AreaLayerDto> findSystemAreaAndLocationLayerMapping();

    List<UserAreaLayerDto> findUserAreaLayerMapping();

    List<Map<String, String>> findSelectedAreaColumns(String namedQueryString, Number gid);

    List<ProjectionDto> findProjectionByMap(long reportId);

    List<ProjectionDto> findProjectionById(Long id);

    ProjectionEntity findProjectionEntityById(Long id) throws ServiceException;

    List<ReportConnectServiceAreasEntity> findReportConnectServiceAreas(long reportId);

    List<ServiceLayerEntity> findServiceLayerEntityByIds(List<Long> ids);

    ReportConnectSpatialEntity findReportConnectSpatialByReportId(Long reportId) throws ServiceException;

    List<ReportConnectSpatialEntity> findReportConnectSpatialByConnectId(final Long id) throws ServiceException;

    boolean saveOrUpdateMapConfiguration(ReportConnectSpatialEntity mapConfiguration) throws ServiceException;

    void updateSystemConfigs(List<SysConfigEntity> sysConfigs);

    void updateSystemConfig(String name, String value) throws ServiceException;

    String findSystemConfigByName(String name) throws ServiceException;

    List<SysConfigEntity> findSystemConfigs();

    List<ServiceLayerDto> findServiceLayerBySubType(List<String> subAreaTypes, boolean isWithBing);

    List<AreaDto> getAllUserAreas(String userName, String scopeName);

    List<AreaDto> getAllUserAreaGroupNames(String userName, String scopeName);

    List<Long> getAllSharedGids(String userName, String scopeName, String type);

    List<AreaDto> findAllUserAreasByGids(List<Long> gids);

    UserAreasEntity findUserAreaById(Long userAreaId, String userName, Boolean isPowerUser, String scopeName) throws ServiceException;

    BookmarkEntity create(BookmarkEntity bookmark) throws ServiceException;

    Set<Bookmark> listBookmarksBy(String userName) throws ServiceException;

    void deleteBookmark(Long id) throws ServiceException;

    BookmarkEntity getBookmarkBy(Long id) throws ServiceException;

    List<ProjectionEntity> findProjection(Integer srsCode) throws ServiceException;

    List<ProjectionEntity> findProjection() throws ServiceException;

    void deleteReportConnectServiceAreas(Long id);

    void deleteReportConnectServiceAreas(Set<ReportConnectServiceAreasEntity> reportConnectServiceAreas);

    ReportConnectSpatialEntity findReportConnectSpatialByReportIdAndConnectId(Long reportId, Long id) throws ServiceException;

    AreaLocationTypesEntity findAreaLocationTypeByTypeName(String typeName) throws ServiceException;

    List<AreaLocationTypesEntity> findAllIsPointIsSystemWide(Boolean isLocation, Boolean isSystemWide) throws ServiceException;

    List<AreaLocationTypesEntity> findAllIsLocation(Boolean isLocation) throws ServiceException;

    ServiceLayerEntity getServiceLayerBy(String locationType) throws ServiceException;

    ServiceLayerEntity getServiceLayerBy(Long id) throws ServiceException;

    ServiceLayerEntity getByAreaLocationType(String areaLocationType) throws ServiceException;

    List<UserAreasEntity> findUserAreasByType(String userName, String scopeName, String type, boolean isPowerUser) throws ServiceException;

    List<UserAreasEntity> findUserArea(String userName, String scopeName, boolean isPowerUser) throws ServiceException;

    List<UserAreasEntity> listUserAreaByCriteria(String userName, String scopeName, String searchCriteria, boolean isPowerUser) throws ServiceException;

    List<UserAreasEntity> findUserAreaDetailsByLocation(String userName, Point point) throws ServiceException;

    UserAreasEntity getUserAreaByUserNameAndName(String userName, String name) throws ServiceException;

    UserAreasEntity save(UserAreasEntity userAreasEntity) throws ServiceException;

    UserAreasEntity update(UserAreasEntity userAreasEntity) throws ServiceException;

    void deleteEntity(ReportConnectSpatialEntity entity);

    List closestArea(List<AreaLocationTypesEntity> entities, DatabaseDialect spatialFunction, Point point);

    List closestPoint(List<AreaLocationTypesEntity> typeEntities, DatabaseDialect spatialFunction, Point incomingPoint);

    List intersectingArea( List<AreaLocationTypesEntity> entities, DatabaseDialect spatialFunction, Point point);

    List<AreaLocationTypesEntity> listAllArea() throws ServiceException;

    void deleteUserArea(UserAreasEntity userAreaById);

    List<UserAreasEntity> findUserAreaByUserNameAndScopeName(String userName, String scopeName) throws ServiceException;

    List<PortEntity> listClosestPorts(Double incomingLongitude, Double incomingLatitude, Integer limit) throws ServiceException;

    List<CountryEntity> findAllCountries() throws ServiceException;
}