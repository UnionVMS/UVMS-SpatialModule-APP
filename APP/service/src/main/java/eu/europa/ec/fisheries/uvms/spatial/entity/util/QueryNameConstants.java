package eu.europa.ec.fisheries.uvms.spatial.entity.util;

/**
 * All constants for Spatial modules
 *
 * @author padhyad
 */
public final class QueryNameConstants {

    private QueryNameConstants() {}

    /**
     * Find all
     */
    public static final String FIND_ALL_AREA_TYPE_NAMES = "AreaLocationType.findAllAreaTypeNames";
    public static final String FIND_ALL_AREA_AND_LOCATION_TYPE_NAMES = "AreaLocationType.findAllAreaAndLocationTypeNames";
    public static final String FIND_ALL_COUNTRY_DESC = "Countries.findAllCountriesDesc";

    /**
     * Find System
     */
    public static final String FIND_SYSTEM_AREA_LAYER = "AreaLocationType.findSystemAreaLayerMappings";
    public static final String FIND_SYSTEM_AREA_AND_LOCATION_LAYER = "AreaLocationType.findSystemAreaAndLocationLayerMappings";
    public static final String FIND_USER_AREA_LAYER = "AreaLocationType.findUserAreaLayerMappings";
    
    /**
     * Find User Area
     */
    public static final String FIND_GID_BY_USER = "UserArea.findGidByUserNameOrScope";
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

    public static final String DISABLE_EEZ_AREAS = "eezEntity.disableEezAreas";
    public static final String DISABLE_RFMO_AREAS = "rfmoEntity.disableRfmoAreas";
    public static final String DISABLE_PORT_AREAS = "portAreasEntity.disablePortAreas";
    public static final String DISABLE_PORT_LOCATIONS = "portsEntity.disablePortLocations";

    /**
     * MapDto Config queries
     */
    public static final String FIND_MAP_PROJ_BY_ID = "ReportLayerConfig.findMapProjectionById";
    public static final String FIND_REPORT_SERVICE_AREAS = "ReportLayerConfig.findReportConnectServiceAreas";
    public static final String DELETE_BY_REPORT_CONNECT_SPATIAL_ID = "ReportLayerConfig.deleteByReportConnectSpatialId";
    public static final String FIND_PROJECTION_BY_ID = "ReportLayerConfig.findProjectionById";
    public static final String FIND_SERVICE_LAYERS_BY_ID ="ReportLayerConfig.findServiceLayerById";
    public static final String FIND_BY_REPORT_ID = "ReportLayerConfig.findByReportId";
    public static final String FIND_BY_ID = "ReportLayerConfig.findById";
    public static final String FIND_CONFIG = "SysConfig.findConfig";
    public static final String FIND_SERVICE_LAYER_BY_SUBTYPE = "ServiceLayer.findServiceLayerBySubType";
    public static final String FIND_SERVICE_LAYER_BY_SUBTYPE_WITHOUT_BING = "ServiceLayer.findServiceLayerBySubTypeWithoutBing";

}
