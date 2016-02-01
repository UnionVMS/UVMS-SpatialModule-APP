package eu.europa.ec.fisheries.uvms.spatial.validator;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.MapConfigurationType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialDeleteMapConfigurationRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialSaveOrUpdateMapConfigurationRQ;

public class SpatialValidator {


    private SpatialValidator(){

    }

    private static void validateNull(Object object){

        if (object == null) {

            throw new IllegalArgumentException("ARGUMENT CAN NOT BE NULL");

        }
    }

    public static void validate(SpatialSaveOrUpdateMapConfigurationRQ request) {

        validateNull(request);

        MapConfigurationType mapConfiguration = request.getMapConfiguration();


        if (mapConfiguration == null) {

            throw new IllegalArgumentException("MAP CONFIGURATION CAN NOT BE NULL");

        }

        if (mapConfiguration.getDisplayProjectionId() != null && mapConfiguration.getMapProjectionId() == null) {

            throw new IllegalArgumentException("MAP PROJECTION IS MANDATORY");

        }

    }

    public static void validate(SpatialDeleteMapConfigurationRQ request) {

        validateNull(request);

    }

    public static void validate(Long reportId) {

        validateNull(reportId);

    }
}
