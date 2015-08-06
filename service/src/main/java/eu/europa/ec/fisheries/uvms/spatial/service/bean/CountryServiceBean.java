package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.dao.CrudDao;
import eu.europa.ec.fisheries.uvms.spatial.entity.CountryEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.CountryDto;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.CountryMapper;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;

/**
 * //TODO create test
 * This class is ment to serve business logic regarding countries
 * in the context of uvms
 *
 */
@Stateless
@Local(CountryService.class)
public class CountryServiceBean implements CountryService {

    @EJB
    private CrudDao crudDao;

    @Override
    public CountryDto getCountryById(int id) {
        CountryEntity country = (CountryEntity) crudDao.find(CountryEntity.class, id);
        return CountryMapper.INSTANCE.countryToCountryDto(country);
    }
}
