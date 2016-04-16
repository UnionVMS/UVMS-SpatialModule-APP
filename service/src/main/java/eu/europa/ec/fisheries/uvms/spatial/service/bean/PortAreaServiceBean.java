package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.PortAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.PortAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.geojson.PortAreaGeoJsonDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.PortAreaMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
@Local(PortAreaService.class)
@Transactional
@Slf4j
public class PortAreaServiceBean implements PortAreaService {

    @EJB
    private SpatialRepository repository;

    @Inject
    private PortAreaMapper mapper;

    @Override
    public long updatePortArea(PortAreaGeoJsonDto portAreaGeoJsonDto) throws ServiceException {
        Long id = portAreaGeoJsonDto.getId();
        validateGid(id);

        return update(id, portAreaGeoJsonDto.getGeometry());
    }

    private long update(Long id, Geometry geometry) throws ServiceException {
        List<PortAreasEntity> persistentPortAreas = repository.findPortAreaById(id);
        validateNotNull(id, persistentPortAreas);

        PortAreasEntity persistentPortArea = persistentPortAreas.get(0);
        persistentPortArea.setGeom(geometry);

        PortAreasEntity persistedUpdatedEntity = repository.update(persistentPortArea);
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

    private void validateNotNull(Long portAreaId, List<PortAreasEntity> persistentPortAreas) {
        if (CollectionUtils.isEmpty(persistentPortAreas)) {
            throw new SpatialServiceException(SpatialServiceErrors.PORT_AREA_DOES_NOT_EXIST, portAreaId);
        }
    }
}
