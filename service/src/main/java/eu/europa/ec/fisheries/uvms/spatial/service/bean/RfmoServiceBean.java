package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.RfmoEntity;
import eu.europa.ec.fisheries.uvms.spatial.repository.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.RfmoDto;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.RfmoMapper;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

@Stateless
@Local(RfmoService.class)
@Transactional
public class RfmoServiceBean implements RfmoService {

    @EJB
    private SpatialRepository repository;

    @Inject
    private RfmoMapper mapper;

    @Override
    public int disableAllAreas() throws ServiceException {
        return repository.disableAllRfmoAreas();
    }

    @Override
    public long createRfmo(RfmoDto rfmoDto) throws ServiceException {
        RfmoEntity rfmoEntity = mapper.rfmoDtoToRfmoEntity(rfmoDto);
        rfmoEntity = (RfmoEntity) repository.createEntity(rfmoEntity);
        return rfmoEntity.getGid();
    }
}
