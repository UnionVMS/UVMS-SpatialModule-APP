package eu.europa.ec.fisheries.uvms.spatial.util;

import com.google.common.base.Function;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;

public class TransformUtils {

    public static Function<AreaIdentifierType, String> EXTRACT_AREA_TYPE = new Function<AreaIdentifierType, String>() {
        @Override
        public String apply(AreaIdentifierType area) {
            return area.getAreaType().toUpperCase();
        }
    };

    public static Function<AreaIdentifierType, String> EXTRACT_AREA_ID = new Function<AreaIdentifierType, String>() {
        @Override
        public String apply(AreaIdentifierType area) {
            return area.getId();
        }
    };

}
