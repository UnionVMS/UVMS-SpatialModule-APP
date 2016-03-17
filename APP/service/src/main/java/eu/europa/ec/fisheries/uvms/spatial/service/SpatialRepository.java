package eu.europa.ec.fisheries.uvms.spatial.service;

import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.service.DAO;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.BookmarkEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.PortAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ProjectionEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ReportConnectServiceAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ReportConnectSpatialEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ServiceLayerEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.UserAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.config.SysConfigEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.bookmark.Bookmark;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.ClosestAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.ProjectionDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.AreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.ServiceLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.util.MeasurementUnit;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SpatialRepository extends DAO {

    List<ClosestAreaDto> findClosestArea(Point point, MeasurementUnit unit, String areaDbTable);

    List findAreaOrLocationByCoordinates(Point point, String nativeQueryString);

    List<AreaLayerDto> findSystemAreaLayerMapping();

    List<AreaLayerDto> findSystemAreaAndLocationLayerMapping();

    List<UserAreaLayerDto> findUserAreaLayerMapping();

    List<Map<String, String>> findSelectedAreaColumns(String namedQueryString, Number gid);

    List<UserAreasEntity> findUserAreaDetailsByLocation(String userName, Point point);

    List<Map<String, String>> findAllCountriesDesc();

    EezEntity getEezById(Long id) throws ServiceException;

    List<ProjectionDto> findProjectionByMap(long reportId);

    List<ProjectionDto> findProjectionById(Long id);

    List<ReportConnectServiceAreasEntity> findReportConnectServiceAreas(long reportId);

    List<ServiceLayerEntity> findServiceLayerEntityByIds(List<Long> ids);

    ReportConnectSpatialEntity findReportConnectSpatialBy(Long reportId) throws ServiceException;

    ReportConnectSpatialEntity findReportConnectSpatialByConnectId(final Long id) throws ServiceException;

    boolean saveOrUpdateMapConfiguration(ReportConnectSpatialEntity mapConfiguration) throws ServiceException;

    void updateSystemConfigs(List<SysConfigEntity> sysConfigs);

    void updateSystemConfig(Map<String, String> parameter, String value) throws ServiceException;

    String findSystemConfigByName(Map<String, String> parameters) throws ServiceException;

    List<SysConfigEntity> findSystemConfigs();

    void deleteBy(List<Long> spatialConnectIds) throws ServiceException;

    List<ServiceLayerDto> findServiceLayerBySubType(List<String> subAreaTypes, boolean isWithBing);

    List<AreaDto> getAllUserAreas(String userName, String scopeName);

    List<AreaDto> getAllUserAreaGroupNames(String userName, String scopeName);

    List<Long> getAllSharedGids(String userName, String scopeName, String type);

    List<AreaDto> findAllUserAreasByGids(List<Long> gids);

    List<UserAreasEntity> findUserAreaById(Long userAreaId, String userName, Boolean isPowerUser, String scopeName) throws ServiceException;

    List<PortAreasEntity> findPortAreaById(Long id) throws ServiceException;

    int disableAllEezAreas() throws ServiceException;

    int disableAllRfmoAreas() throws ServiceException;

    int disableAllPortLocations() throws ServiceException;

    int disableAllPortAreas() throws ServiceException;

    List<String> getUserAreaTypes(String userName, String scopeName, boolean isPowerUser) throws ServiceException;

    BookmarkEntity create(BookmarkEntity bookmark) throws ServiceException;

    Set<Bookmark> listBookmarksBy(String userName) throws ServiceException;

    void delete(Long id) throws ServiceException;

    void update(Bookmark bookmark) throws ServiceException;

    ProjectionEntity findProjection(Integer srsCode) throws ServiceException;

    List<String> getAreaGroups(String userName, String scopeName, boolean isPowerUser);

    List<UserAreasEntity> findUserAreasByType(String userName, String scopeName, String type, boolean isPowerUser) throws ServiceException;

    void deleteReportConnectServiceAreas(Long id);

    void deleteReportConnectServiceAreas(Set<ReportConnectServiceAreasEntity> reportConnectServiceAreases);

    ReportConnectSpatialEntity findReportConnectSpatialById(Long reportId, Long id) throws ServiceException;

    // AreaRepository
    List<EezEntity> findEezByIntersect(Point point) throws ServiceException;
    // AreaRepository
    List findPortAreaByIntersect(Point point) throws ServiceException;
    // AreaRepository
    List findRfmoByIntersect(Point point) throws ServiceException;
    // AreaRepository
    List findUserAreaByIntersect(Point point) throws ServiceException;

    AreaLocationTypesEntity findAreaLocationTypeByTypeName(String typeName) throws ServiceException;

}
