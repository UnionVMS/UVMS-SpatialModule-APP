package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.PortEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.PortLocationDto;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.PortLocationMapper;
import lombok.extern.slf4j.Slf4j;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Local(PortLocationService.class)
@Transactional
@Slf4j
public class PortLocationServiceBean implements PortLocationService {

    private @EJB SpatialRepository repository;

    @Override
    public long createPortLocation(PortLocationDto portLocationDto) throws ServiceException {
        PortEntity portsEntity = PortLocationMapper.INSTANCE.portLocationDtoToPortsEntity(portLocationDto);
        portsEntity = (PortEntity) repository.createEntity(portsEntity);
        return portsEntity.getGid();
    }

}
