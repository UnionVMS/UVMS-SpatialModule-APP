package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.PortsEntity;
import eu.europa.ec.fisheries.uvms.spatial.repository.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.PortAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
@Local(PortAreaService.class)
@Transactional
@Slf4j
public class PortAreaServiceBean implements PortAreaService {

    @EJB
    private SpatialRepository repository;

    @Override
    public long updatePortArea(PortAreaDto portAreaDto) throws ServiceException {
        Long id = portAreaDto.getId();
        validateGid(id);

        return update(id, portAreaDto.getGeometry());
    }

    private long update(Long id, Geometry geometry) throws ServiceException {
        List<PortsEntity> persistentPortAreas = repository.findPortAreaById(id);
        validateNotNull(id, persistentPortAreas);

        PortsEntity persistentPortArea = persistentPortAreas.get(0);
        persistentPortArea.setGeom(geometry);

        PortsEntity persistedUpdatedEntity = (PortsEntity) repository.updateEntity(persistentPortArea);
        return persistedUpdatedEntity.getGid();
    }

    @Override
    public void deletePortArea(Long portAreaId) throws ServiceException {
        update(portAreaId, null);
    }

    private void validateGid(Long gid) {
        if (gid == null) {
            throw new SpatialServiceException(SpatialServiceErrors.MISSING_PORT_AREA_ID);
        }
    }

    private void validateNotNull(Long portAreaId, List<PortsEntity> persistentPortAreas) {
        if (CollectionUtils.isEmpty(persistentPortAreas)) {
            throw new SpatialServiceException(SpatialServiceErrors.PORT_AREA_DOES_NOT_EXIST, portAreaId);
        }
    }

}
