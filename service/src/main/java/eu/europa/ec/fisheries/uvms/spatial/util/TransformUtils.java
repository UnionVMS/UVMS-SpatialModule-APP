package eu.europa.ec.fisheries.uvms.spatial.util;

import com.google.common.base.Function;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaIdentifierDto;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.AreaIdentifierDtoMapper;

public class TransformUtils {

    public static Function<AreaIdentifierDto, String> AREA_DTO_TO_TYPE_STRING = new Function<AreaIdentifierDto, String>() {
        @Override
        public String apply(AreaIdentifierDto area) {
            return area.getAreaType();
        }
    };

    public static Function<AreaIdentifierDto, String> AREA_DTO_TO_ID_STRING = new Function<AreaIdentifierDto, String>() {
        @Override
        public String apply(AreaIdentifierDto area) {
            return area.getId();
        }
    };

    public static Function<AreaIdentifierType, AreaIdentifierDto> AREA_IDENTIFIER_TO_DTO = new Function<AreaIdentifierType, AreaIdentifierDto>() {
        @Override
        public AreaIdentifierDto apply(AreaIdentifierType areaIdentifier) {
            return AreaIdentifierDtoMapper.INSTANCE.areaIdentifierTypeToAreaDto(areaIdentifier);
        }
    };

}
