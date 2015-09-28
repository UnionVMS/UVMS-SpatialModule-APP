package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezType;
import eu.europa.ec.fisheries.uvms.spatial.repository.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.EezDto;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.EezDtoMapper;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.EezTypeMapper;

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
    private EezTypeMapper eezMapper;

    @Override
    @SuppressWarnings("unchecked")
    public EezType getEezById(EezSpatialRQ getEezSpatialRQ) {
        EezEntity eez = (EezEntity) repository.findEntityById(EezEntity.class, Integer.parseInt(getEezSpatialRQ.getEezId()));
        return eezMapper.eezEntityToEezType(eez);
    }

    @Override
    @SuppressWarnings("unchecked")
    public EezDto getEezById(int id) {
        EezEntity eez = (EezEntity) repository.findEntityById(EezEntity.class, id);
        return EezDtoMapper.INSTANCE.eezEntityToEezDto(eez);
    }
}
