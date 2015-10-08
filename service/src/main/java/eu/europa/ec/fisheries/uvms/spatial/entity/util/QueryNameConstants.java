package eu.europa.ec.fisheries.uvms.spatial.entity.util;

/**
 * All constants for Spatial modules
 * @author padhyad 
 *
 */
public class QueryNameConstants {
	
	/** Find all */
    public static final String FIND_ALL_AREA_TYPE_NAMES = "AreaType.findAllAreaTypeNames";
    public static final String FIND_ALL_AREAS = "AreaType.findAllAreas";
    public static final String FIND_ALL_LOCATIONS = "AreaType.findAllLocations";

    /** Find System */
    public static final String FIND_SYSTEM_AREAS = "AreaType.findSystemAreas";
    public static final String FIND_SYSTEM_LOCATIONS = "AreaType.findSystemLocations";
    public static final String FIND_SYSTEM_AREA_LAYER = "AreaType.findSystemAreaLayerMappings";

    /** Find Area By Id */
    public static final String FIND_TYPE_BY_ID = "AreaType.findAreaById";
    
    /** Named Native queries */
    public static final String EEZ_BY_COORDINATE = "eezEntity.ByCoordinate";
    public static final String RFMO_BY_COORDINATE = "rfmoEntity.ByCoordinate";
    public static final String COUNTRY_BY_COORDINATE = "countryEntity.ByCoordinate";
    public static final String FAO_BY_COORDINATE = "faoEntity.ByCoordinate";
    public static final String GFCM_BY_COORDINATE = "gfcmEntity.ByCoordinate";
    public static final String RAC_BY_COORDINATE = "racEntity.ByCoordinate";
    public static final String STAT_RECT_BY_COORDINATE = "statRectEntity.ByCoordinate";
    public static final String PORT_BY_COORDINATE = "portEntity.ByCoordinate";
    
    /** Native Queries */
    public static final String EEZ_COLUMNS = "eezEntity.findSelectedColumns";
    public static final String RFMO_COLUMNS = "rfmoEntity.findSelectedColumns";
    public static final String COUNTRY_COLUMNS = "countryEntity.findSelectedColumns";
    public static final String FAO_COLUMNS = "faoEntity.findSelectedColumns";
    public static final String GFCM_COLUMNS = "gfcmEntity.findSelectedColumns";
    public static final String RAC_COLUMNS = "racEntity.findSelectedColumns";
    public static final String STAT_RECT_COLUMNS = "statRectEntity.findSelectedColumns";
    public static final String PORT_COLUMNS = "portEntity.findSelectedColumns";
}
