package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import eu.europa.ec.fisheries.uvms.spatial.entity.CountryEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.CountryDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * //TODO create test
 */
@Mapper
public interface CountryMapper {

    CountryMapper INSTANCE = Mappers.getMapper(CountryMapper.class);

    CountryDto countryToCountryDto(CountryEntity countryEntity);

}
