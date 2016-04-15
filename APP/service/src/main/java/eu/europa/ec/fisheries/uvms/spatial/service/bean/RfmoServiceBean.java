package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.RfmoEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.RfmoDto;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.RfmoMapper;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Local(RfmoService.class)
@Transactional
public class RfmoServiceBean implements RfmoService {

    private @EJB SpatialRepository repository;

    @Override
    public int disableAllAreas() throws ServiceException {
        return repository.disableAllRfmoAreas();
    }

    @Override
    public long createRfmo(RfmoDto rfmoDto) throws ServiceException {
        RfmoEntity rfmoEntity = RfmoMapper.INSTANCE.rfmoDtoToRfmoEntity(rfmoDto);
        rfmoEntity = repository.create(rfmoEntity);
        return rfmoEntity.getGid();
    }
}
