package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezType;
import eu.europa.ec.fisheries.uvms.spatial.repository.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.EezDto;
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
        EezEntity eezById = repository.getEezById(Integer.parseInt(getEezSpatialRQ.getEezId()));
        return mapper.eezEntityToEezType(eezById);
    }

    @Override
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public EezDto getEezById(int id) {
        EezEntity eez = (EezEntity) repository.findEntityById(EezEntity.class, id);
        return mapper.eezEntityToEezDto(eez);
    }
}
