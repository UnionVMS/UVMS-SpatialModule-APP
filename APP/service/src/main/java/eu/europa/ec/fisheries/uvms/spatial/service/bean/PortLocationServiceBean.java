package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.PortsEntity;
import eu.europa.ec.fisheries.uvms.spatial.repository.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.PortLocationDto;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.PortLocationMapper;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

@Stateless
@Local(PortLocationService.class)
@Transactional
@Slf4j
public class PortLocationServiceBean implements PortLocationService {

    @EJB
    private SpatialRepository repository;

    @Inject
    private PortLocationMapper mapper;

    @Override
    public long createPortLocation(PortLocationDto portLocationDto) throws ServiceException {
        PortsEntity portsEntity = mapper.portLocationDtoToPortsEntity(portLocationDto);
        portsEntity = (PortsEntity) repository.saveOrUpdateEntity(portsEntity);
        return portsEntity.getGid();
    }

    @Override
    public int disableAllAreas() throws ServiceException {
        return repository.disableAllPortLocations();
    }

}
