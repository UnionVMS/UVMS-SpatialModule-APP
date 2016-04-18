package eu.europa.ec.fisheries.uvms.spatial.entity.util;

/**
 * All constants for Spatial modules
 *
 * @author padhyad
 */
public final class QueryNameConstants {

    private QueryNameConstants() {}

    /**
     * Find System
     */
    public static final String FIND_USER_AREA_LAYER = "AreaLocationType.findUserAreaLayerMappings";
    
    /**
     * Find User Area
     */
    public static final String FIND_ALL_USER_AREAS = "UserArea.findAllUserAreas";
    public static final String FIND_ALL_USER_AREAS_GROUP = "UserArea.findAllUserAreaGroup";
    public static final String FIND_ALL_USER_AREAS_BY_GIDS = "UserAreas.findAllUserAreasByGid";

    /**
     * Find Area By Name
     */
    public static final String FIND_TYPE_BY_NAMES = "AreaLocationType.findAreaByNames";

    /**
     * Named Native queries
     */
    public static final String FAO_BY_COORDINATE = "faoEntity.ByCoordinate";
    public static final String GFCM_BY_COORDINATE = "gfcmEntity.ByCoordinate";
    public static final String RAC_BY_COORDINATE = "racEntity.ByCoordinate";
    public static final String STAT_RECT_BY_COORDINATE = "statRectEntity.ByCoordinate";

    /**
     * Native Queries
     */
    public static final String EEZ_COLUMNS = "eezEntity.findSelectedColumns";
    public static final String RFMO_COLUMNS = "rfmoEntity.findSelectedColumns";
    public static final String USERAREA_COLUMNS = "userAreasEntity.findSelectedColumns";

    /**
     * MapDto Config queries
     */
    public static final String FIND_BY_REPORT_ID = "ReportLayerConfig.findByReportId";
    public static final String FIND_BY_ID = "ReportLayerConfig.findById";
    public static final String FIND_SERVICE_LAYER_BY_SUBTYPE = "ServiceLayer.findServiceLayerBySubType";
    public static final String FIND_SERVICE_LAYER_BY_SUBTYPE_WITHOUT_BING = "ServiceLayer.findServiceLayerBySubTypeWithoutBing";

}
