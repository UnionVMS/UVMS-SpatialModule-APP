package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.ImmutableMap;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.repository.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import lombok.SneakyThrows;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Stateless
@Local(AreaTypeNamesService.class)
@Transactional
public class AreaTypeNamesServiceBean implements AreaTypeNamesService {

    private static final String WMS_SERVICE_TYPE = "wms";
    private static final String NAME = "name";
    private static final String GEO_SERVER = "geo_server_url";

    @EJB
    private SpatialRepository repository;

    @Override
    @SneakyThrows
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public List<String> listAllAreaTypeNames() {
        return repository.findEntityByNamedQuery(String.class, QueryNameConstants.FIND_ALL_AREA_TYPE_NAMES);
    }
    
    @Override
    public List<AreaLayerDto> listSystemAreaLayerMapping() {
        List<AreaLayerDto> systemAreaLayerMapping = repository.findSystemAreaLayerMapping();
        addServiceUrlForInternalWMSLayers(systemAreaLayerMapping);
        return systemAreaLayerMapping;
    }

    private void addServiceUrlForInternalWMSLayers(List<AreaLayerDto> systemAreaLayerMapping) {
        String geoServerUrl = getGeoServerUrl();

        for (AreaLayerDto areaLayerDto : systemAreaLayerMapping) {
            if (WMS_SERVICE_TYPE.equalsIgnoreCase(areaLayerDto.getServiceType()) && areaLayerDto.getIsInternal()) {
                areaLayerDto.setServiceUrl(geoServerUrl + WMS_SERVICE_TYPE);
            }
        }
    }

    private String getGeoServerUrl() {
        try {
            Map<String, String> parameters = ImmutableMap.<String, String>builder().put(NAME, GEO_SERVER).build();
            String geoServerUrl = repository.findSystemConfigByName(parameters);
            if (geoServerUrl == null) {
                throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
            }
            return geoServerUrl;
        } catch (ServiceException e) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
        }
    }
}
