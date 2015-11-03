package eu.europa.ec.fisheries.uvms.spatial.entity.util;

/**
 * All constants for Spatial modules
 *
 * @author padhyad
 */
public class QueryNameConstants {

    /**
     * Find all
     */
    public static final String FIND_ALL_AREA_TYPE_NAMES = "AreaLocationType.findAllAreaTypeNames";
    public static final String FIND_ALL_AREAS = "AreaLocationType.findAllAreas";
    public static final String FIND_ALL_LOCATIONS = "AreaLocationType.findAllLocations";

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
    public static final String USER_AREA_DETAILS = "UserArea.findUserAreaDetails";
    public static final String SEARCH_USER_AREA = "UserArea.searchUserAreaByNameAndDesc";

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
}
