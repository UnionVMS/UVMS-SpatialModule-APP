/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaSimpleType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.util.DatabaseDialect;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.area.AreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.area.AreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.bookmark.Bookmark;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.config.ProjectionDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.layer.ServiceLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.layer.UserAreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.BookmarkEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.CountryEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.PortEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.ProjectionEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.ReportConnectServiceAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.ReportConnectSpatialEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.ServiceLayerEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.SysConfigEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.UserAreasEntity;

public interface SpatialRepository {

    List<AreaLayerDto> findSystemAreaLayerMapping();

    List<AreaLayerDto> findSystemAreaAndLocationLayerMapping();

    List<UserAreaLayerDto> findUserAreaLayerMapping();

    List<Map<String, String>> findSelectedAreaColumns(String namedQueryString, List<Long> gids);

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

    ReportConnectSpatialEntity findReportConnectSpatialByReportIdAndConnectId(Long reportId, Long id) throws ServiceException;

    AreaLocationTypesEntity findAreaLocationTypeByTypeName(String typeName) throws ServiceException;

    List<AreaLocationTypesEntity> findAllIsPointIsSystemWide(Boolean isLocation, Boolean isSystemWide) throws ServiceException;

    List<AreaLocationTypesEntity> findAllIsLocation(Boolean isLocation) throws ServiceException;

    ServiceLayerEntity getServiceLayerBy(AreaType areaType) throws ServiceException;

    ServiceLayerEntity getServiceLayerBy(Long id) throws ServiceException;

    List<UserAreasEntity> findUserAreasByType(String userName, String scopeName, String type, boolean isPowerUser) throws ServiceException;

    List<UserAreasEntity> findUserArea(String userName, String scopeName, boolean isPowerUser) throws ServiceException;

    List<UserAreasEntity> listUserAreaByCriteria(String userName, String scopeName, String searchCriteria, boolean isPowerUser) throws ServiceException;

    List<UserAreasEntity> findUserAreaDetailsByLocation(String userName, Point point) throws ServiceException;

    UserAreasEntity getUserAreaByUserNameAndName(String userName, String name) throws ServiceException;

    UserAreasEntity save(UserAreasEntity userAreasEntity) throws ServiceException;

    UserAreasEntity update(UserAreasEntity userAreasEntity) throws ServiceException;

    List closestArea(List<AreaLocationTypesEntity> entities, DatabaseDialect spatialFunction, Point point);

    List closestPoint(List<AreaLocationTypesEntity> typeEntities, DatabaseDialect spatialFunction, Point incomingPoint);

    List intersectingArea( List<AreaLocationTypesEntity> entities, DatabaseDialect spatialFunction, Point point);

    List<AreaLocationTypesEntity> listAllArea() throws ServiceException;

    void deleteUserArea(UserAreasEntity userAreaById);

    List<UserAreasEntity> findUserAreaByUserNameAndScopeName(String userName, String scopeName) throws ServiceException;

    List<PortEntity> listClosestPorts(Point point, Integer limit) throws ServiceException;

    List<CountryEntity> findAllCountries() throws ServiceException;

    List listBaseAreaList(String query) throws ServiceException;

    void makeGeomValid(String areaDbTable, DatabaseDialect dialect);

    /**
     * <p>Update Start date and End date for user areas if the user is having scope <code><B>MANAGE_ANY_USER_AREA</B></code>
     * <p><code>StartDate</code> and <code>EndDate</code> can be NULL or Empty or a Valid Date</p>
     *
     * @param startDate Start Date
     * @param endDate End Date
     * @param type Area Type
     * @exception ServiceException Exception is Date cannot be updated
     *
     * @see SpatialRepository#updateUserAreaForUser(String, Date, Date, String)
     */
    void updateUserAreaForUserAndScope(Date startDate, Date endDate, String type) throws ServiceException;

    /**
     * <p>Update Start date and End date for user areas those are created by the user</p>
     * <p><code>StartDate</code> and <code>EndDate</code> can be NULL or Empty or a Valid Date</p>
     *
     * @param remoteUser User Name
     * @param startDate Start Date
     * @param endDate End Date
     * @param type Area Type
     * @throws ServiceException Exception is Date cannot be updated
     *
     * @see SpatialRepository#updateUserAreaForUserAndScope(Date, Date, String)
     */
    void updateUserAreaForUser(String remoteUser, Date startDate, Date endDate, String type) throws ServiceException;

    List areaByCode(List<AreaSimpleType> areaSimpleTypeList) throws ServiceException;

    Integer mapToEpsgSRID(Integer srid) throws ServiceException;

    Boolean isOracle();

    Integer mapEpsgToSRID(Integer epsg);

    void deleteReportConnectServiceAreas(List<Long> spatialConnectIds) throws ServiceException;
}