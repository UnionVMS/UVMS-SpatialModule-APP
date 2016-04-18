package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.google.common.collect.ImmutableMap;
import eu.europa.ec.fisheries.uvms.spatial.entity.ProjectionEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ReportConnectServiceAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ReportConnectSpatialEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ServiceLayerEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.ProjectionDto;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

public class MapConfigDao extends CommonDao {

    private static final String REPORT_ID = "reportId";
    private static final String ID = "id";
    private static final String ID_LIST = "ids";

    public MapConfigDao(EntityManager em) {
        super(em);
    }

    @SuppressWarnings("unchecked")
    public List<ProjectionDto> findProjectionByMap(long reportId) {
        Map<String, Object> parameters = ImmutableMap.<String, Object>builder().put(REPORT_ID, reportId).build();
        return createNamedNativeQuery(ReportConnectSpatialEntity.FIND_MAP_PROJ_BY_ID, parameters, ProjectionDto.class).list(); // TODO move to reportconnectDao
    }

    @SuppressWarnings("unchecked")
    public List<ProjectionDto> findProjectionById(Long id) {
        Map<String, Object> parameters = ImmutableMap.<String, Object>builder().put(ID, id).build();
        return createNamedNativeQuery(ProjectionEntity.FIND_PROJECTION_BY_ID, parameters, ProjectionDto.class).list();  // TODO move to projectionDao
    }

    @SuppressWarnings("unchecked")
    public List<ReportConnectServiceAreasEntity> findReportConnectServiceAreas(long reportId) {
        Map<String, Object> parameters = ImmutableMap.<String, Object>builder().put(REPORT_ID, reportId).build();
        return createNamedQuery(ReportConnectServiceAreasEntity.FIND_REPORT_SERVICE_AREAS, parameters).list();  // TODO move to reportconnectServiceAreaDao
    }

    public List<ServiceLayerEntity> findServiceLayerEntityByIds(List<Long> ids) {
        Map<String, List<Long>> parameters = ImmutableMap.<String, List<Long>>builder().put(ID_LIST, ids).build();
        return createNamedQueryWithParameterList(ServiceLayerEntity.FIND_SERVICE_LAYERS_BY_ID, parameters).list();  // TODO move to servicelayerDao
    }

    public void deleteReportConnectServiceAreas(Long id) {
        Map<String, Object> parameters = ImmutableMap.<String, Object>builder().put(ID, id).build();
        createNamedQuery(ReportConnectServiceAreasEntity.DELETE_BY_REPORT_CONNECT_SPATIAL_ID, parameters).executeUpdate(); // TODO move to reportconnectServiceAreaDao
    }
}
