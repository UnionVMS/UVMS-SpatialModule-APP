package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.google.common.collect.ImmutableMap;
import eu.europa.ec.fisheries.uvms.spatial.entity.ReportConnectServiceAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.ProjectionDto;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

/**
 * Created by padhyad on 11/20/2015.
 */
public class MapConfigDao extends CommonDao {

    private static final String REPORT_ID = "reportId";

    private static final String SRS_CODE = "srsCode";

    public MapConfigDao(EntityManager em) {
        super(em);
    }

    @SuppressWarnings("unchecked")
    public List<ProjectionDto> findProjectionByMap(long reportId) {
        Map<String, Object> parameters = ImmutableMap.<String, Object>builder().put(REPORT_ID, reportId).build();
        return createNamedNativeQuery(QueryNameConstants.FIND_MAP_PROJ_BY_ID, parameters, ProjectionDto.class).list();
    }

    @SuppressWarnings("unchecked")
    public List<ProjectionDto> findProjectionBySrsCode(int srsCode) {
        Map<String, Object> parameters = ImmutableMap.<String, Object>builder().put(SRS_CODE, srsCode).build();
        return createNamedNativeQuery(QueryNameConstants.FIND_PROJECTION_BY_SRS_CODE, parameters, ProjectionDto.class).list();
    }

    @SuppressWarnings("unchecked")
    public List<ProjectionDto> findProjectionByDisplay(long reportId) {
        Map<String, Object> parameters = ImmutableMap.<String, Object>builder().put(REPORT_ID, reportId).build();
        return createNamedNativeQuery(QueryNameConstants.FIND_DISPLAY_PROJ_BY_ID, parameters, ProjectionDto.class).list();
    }

    @SuppressWarnings("unchecked")
    public List<ReportConnectServiceAreasEntity> findReportConnectServiceAreas(long reportId) {
        Map<String, Object> parameters = ImmutableMap.<String, Object>builder().put(REPORT_ID, reportId).build();
        return createNamedQuery(QueryNameConstants.FIND_REPORT_SERVICE_AREAS, parameters).list();
    }
}
