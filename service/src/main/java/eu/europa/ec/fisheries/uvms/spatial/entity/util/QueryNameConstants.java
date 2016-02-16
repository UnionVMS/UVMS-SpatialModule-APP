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
    public static final String FIND_ALL_AREAS = "AreaLocationType.findAllAreas";
    public static final String FIND_ALL_LOCATIONS = "AreaLocationType.findAllLocations";
    public static final String FIND_ALL_COUNTRY_DESC = "Countries.findAllCountriesDesc";

    /**
     * Find System
     */
    public static final String FIND_SYSTEM_AREAS = "AreaLocationType.findSystemAreas";
    public static final String FIND_SYSTEM_LOCATIONS = "AreaLocationType.findSystemLocations";
    public static final String FIND_SYSTEM_AREA_LAYER = "AreaLocationType.findSystemAreaLayerMappings";
    public static final String FIND_USER_AREA_LAYER = "AreaLocationType.findUserAreaLayerMappings";
    
    /**
     * Find User Area
     */
    public static final String FIND_GID_BY_USER = "UserArea.findGidByUserNameOrScope";
    public static final String FIND_ALL_USER_AREAS = "UserArea.findAllUserAreas";
    public static final String FIND_ALL_USER_AREAS_BY_GIDS = "UserAreas.findAllUserAreasByGid";
    public static final String USER_AREA_DETAILS = "UserArea.findUserAreaDetails";

    public static final String USER_AREA_DETAILS_WITH_EXTENT_BY_LOCATION = "UserArea.findUserAreaDetailsWithExtentByLocation";
    public static final String USER_AREA_DETAILS_BY_LOCATION = "UserArea.findUserAreaDetailsByLocation";

    public static final String SEARCH_USER_AREA = "UserArea.searchUserAreaByNameAndDesc";
    public static final String FIND_USER_AREA_BY_ID = "UserArea.findUserAreaById";
    public static final String FIND_USER_AREA_TYPES = "UserArea.findUserAreaTypes";

    /**
     * Find Ports Area
     */
    public static final String FIND_PORT_AREA_BY_ID = "PortArea.findPortAreaById";

    /**
     * Find Area By Name
     */
    public static final String FIND_TYPE_BY_NAME = "AreaLocationType.findAreaByName";
    public static final String FIND_TYPE_BY_NAMES = "AreaLocationType.findAreaByNames";

    /**
     * Named Native queries
     */
    public static final String EEZ_BY_COORDINATE = "eezEntity.ByCoordinate";
    public static final String RFMO_BY_COORDINATE = "rfmoEntity.ByCoordinate";
    public static final String COUNTRY_BY_COORDINATE = "countryEntity.ByCoordinate";
    public static final String FAO_BY_COORDINATE = "faoEntity.ByCoordinate";
    public static final String GFCM_BY_COORDINATE = "gfcmEntity.ByCoordinate";
    public static final String RAC_BY_COORDINATE = "racEntity.ByCoordinate";
    public static final String STAT_RECT_BY_COORDINATE = "statRectEntity.ByCoordinate";
    public static final String PORT_BY_COORDINATE = "portEntity.ByCoordinate";
    public static final String PORTAREA_BY_COORDINATE = "portEntity.PortAreaByCoordinate";
    public static final String USERAREA_BY_COORDINATE = "userAreasEntity.ByCoordinate";

    /**
     * Native Queries
     */
    public static final String EEZ_COLUMNS = "eezEntity.findSelectedColumns";
    public static final String RFMO_COLUMNS = "rfmoEntity.findSelectedColumns";
    public static final String COUNTRY_COLUMNS = "countryEntity.findSelectedColumns";
    public static final String FAO_COLUMNS = "faoEntity.findSelectedColumns";
    public static final String GFCM_COLUMNS = "gfcmEntity.findSelectedColumns";
    public static final String RAC_COLUMNS = "racEntity.findSelectedColumns";
    public static final String STAT_RECT_COLUMNS = "statRectEntity.findSelectedColumns";
    public static final String PORT_COLUMNS = "portEntity.findSelectedColumns";
    public static final String USERAREA_COLUMNS = "userAreasEntity.findSelectedColumns";

    public static final String DISABLE_EEZ_AREAS = "eezEntity.disableEezAreas";
    public static final String DISABLE_RFMO_AREAS = "rfmoEntity.disableRfmoAreas";

    /**
     * MapDto Config queries
     */
    public static final String FIND_MAP_PROJ_BY_ID = "ReportLayerConfig.findMapProjectionById";
    public static final String FIND_REPORT_SERVICE_AREAS = "ReportLayerConfig.findReportConnectServiceAreas";
    public static final String FIND_PROJECTION_BY_ID = "ReportLayerConfig.findProjectionById";
    public static final String FIND_SERVICE_LAYERS_BY_ID ="ReportLayerConfig.findServiceLayerById";
    public static final String FIND_BY_REPORT_ID = "ReportLayerConfig.findByReportId";
    public static final String FIND_CONFIG_BY_NAME = "SysConfig.findConfigById";
    public static final String FIND_CONFIG = "SysConfig.findConfig";

    public static final String FIND_SERVICE_LAYER_BY_SUBTYPE = "ServiceLayer.findServiceLayerBySubType";
    public static final String FIND_SERVICE_LAYER_BY_SUBTYPE_WITHOUT_BING = "ServiceLayer.findServiceLayerBySubTypeWithoutBing";

    /**
     * Area Group
     */
    public static final String FIND_ALL_AREA_GROUP_BY_NAME = "AreaGroup.findAllAreaGroupByName";
    public static final String FIND_AREA_GROUP_BY_ID = "AreaGroup.findAreaGroupById";
}
