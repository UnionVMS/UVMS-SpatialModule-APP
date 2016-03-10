package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.google.common.collect.ImmutableMap;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.ReportConnectServiceAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ReportConnectSpatialEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ServiceLayerEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.DisplayProjectionDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.ProjectionDto;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static eu.europa.ec.fisheries.uvms.service.QueryParameter.with;

/**
 * Created by padhyad on 11/20/2015.
 */
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
        return createNamedNativeQuery(QueryNameConstants.FIND_MAP_PROJ_BY_ID, parameters, ProjectionDto.class).list();
    }

    @SuppressWarnings("unchecked")
    public List<ProjectionDto> findProjectionById(Long id) {
        Map<String, Object> parameters = ImmutableMap.<String, Object>builder().put(ID, id).build();
        return createNamedNativeQuery(QueryNameConstants.FIND_PROJECTION_BY_ID, parameters, ProjectionDto.class).list();
    }

    @SuppressWarnings("unchecked")
    public List<ReportConnectServiceAreasEntity> findReportConnectServiceAreas(long reportId) {
        Map<String, Object> parameters = ImmutableMap.<String, Object>builder().put(REPORT_ID, reportId).build();
        return createNamedQuery(QueryNameConstants.FIND_REPORT_SERVICE_AREAS, parameters).list();
    }

    public List<ServiceLayerEntity> findServiceLayerEntityByIds(List<Long> ids) {
        Map<String, List<Long>> parameters = ImmutableMap.<String, List<Long>>builder().put(ID_LIST, ids).build();
        return createNamedQueryWithParameterList(QueryNameConstants.FIND_SERVICE_LAYERS_BY_ID, parameters).list();
    }

    public void deleteReportConnectServiceAreas(Long id) {
        Map<String, Object> parameters = ImmutableMap.<String, Object>builder().put(ID, id).build();
        createNamedQuery(QueryNameConstants.DELETE_BY_REPORT_CONNECT_SPATIAL_ID, parameters).executeUpdate();
    }
}
