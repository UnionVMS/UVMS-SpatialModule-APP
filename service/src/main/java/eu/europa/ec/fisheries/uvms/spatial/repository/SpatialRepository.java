package eu.europa.ec.fisheries.uvms.spatial.repository;

import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.service.DAO;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ReportConnectServiceAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.config.SysConfigEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.*;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.ProjectionDto;

import java.util.List;
import java.util.Map;

public interface SpatialRepository extends DAO {

    List<AreaExtendedIdentifierDto> findAreasIdByLocation(Point point, String areaDbTable);

    List<ClosestAreaDto> findClosestArea(Point point, MeasurementUnit unit, String areaDbTable);

    List<ClosestLocationDto> findClosestlocation(Point point, MeasurementUnit unit, String areaDbTable);

    List findAreaOrLocationByCoordinates(Point point, String nativeQueryString);

    List<AreaLayerDto> findSystemAreaLayerMapping();
    
    List<UserAreaLayerDto> findUserAreaLayerMapping();

    List<Map<String, String>> findAreaByFilter(String areaType, String filter);

    List<Map<String, String>> findSelectedAreaColumns(String namedQueryString, Number gid);
    
    List<UserAreaDto> findUserAreaDetails(String userName, String scopeName, Point point);
    
    List<UserAreaDto> findUserAreaDetailsBySearchCriteria(String userName, String scopeName, String searchCriteria);

    FilterAreasDto filterAreas(List<String> userAreaTables, List<String> userAreaIds, List<String> scopeAreaTables, List<String> scopeAreaIds);

    List<Map<String, String>> findAllCountriesDesc();

    EezEntity getEezById(Integer id) throws ServiceException;

    List<ProjectionDto> findProjectionByMap(long reportId);

    List<ReportConnectServiceAreasEntity> findReportConnectServiceAreas(long reportId);

    void updateSystemConfigs(List<SysConfigEntity> sysConfigs);

    List<SysConfigEntity> findSystemConfigs();
}
