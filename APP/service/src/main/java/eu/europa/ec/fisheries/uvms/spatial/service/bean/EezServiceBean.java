package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezType;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.EezDto;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.EezMapper;
import lombok.SneakyThrows;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

@Stateless
@Local(EezService.class)
@Transactional
public class EezServiceBean implements EezService {

    @EJB
    private SpatialRepository repository;

    @Inject
    private EezMapper mapper;

    @Override
    @SneakyThrows
    public EezType getEezById(EezSpatialRQ getEezSpatialRQ) {
        EezEntity eezById = repository.getEezById(Long.parseLong(getEezSpatialRQ.getEezId()));
        return mapper.eezEntityToEezType(eezById);
    }

    @Override
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public EezDto getEezById(int id) {
        EezEntity eez = (EezEntity) repository.findEntityById(EezEntity.class, id);
        return mapper.eezEntityToEezDto(eez);
    }

    @Override
    public long createEzz(EezDto eezDto) throws ServiceException {
        EezEntity eezEntity = mapper.eezDtoToEezEntity(eezDto);
        eezEntity = (EezEntity) repository.createEntity(eezEntity);
        return eezEntity.getGid();
    }

    @Override
    public int disableAllAreas() throws ServiceException {
        return repository.disableAllEezAreas();
    }
}
