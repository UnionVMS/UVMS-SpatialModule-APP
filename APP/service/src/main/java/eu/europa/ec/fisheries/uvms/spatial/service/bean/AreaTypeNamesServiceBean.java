package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.ImmutableMap;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.repository.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.*;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import lombok.SneakyThrows;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Stateless
@Local(AreaTypeNamesService.class)
@Transactional
public class AreaTypeNamesServiceBean implements AreaTypeNamesService {

    private static final String WMS_SERVICE_TYPE = "wms";
    private static final String NAME = "name";
    private static final String GEO_SERVER = "geo_server_url";
    private static final String BING_API_KEY = "bing_api_key";

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


    @Override
    public List<ServiceLayerDto> getAreaLayerDescription(LayerTypeEnum layerTypeEnum) throws ServiceException {
        String apiKey = getBingApiKey();
        if (apiKey != null) {
            return repository.findServiceLayerBySubType(constructInParameters(layerTypeEnum), true);
        } else {
            return repository.findServiceLayerBySubType(constructInParameters(layerTypeEnum), false);
        }
    }

    public List<AreaServiceLayerDto> getAllAreasLayerDescription(LayerTypeEnum layerTypeEnum, String userName) throws ServiceException {
        List<AreaServiceLayerDto> areaServiceLayerDtos = new ArrayList<AreaServiceLayerDto>();
        switch(layerTypeEnum) {
            case USERAREA:
                List<ServiceLayerDto> serviceLayerDtos = getAreaLayerDescription(layerTypeEnum);
                List<AreaDto> allAreas = repository.getAllUserAreas(userName);
                for (ServiceLayerDto serviceLayerDto : serviceLayerDtos) {
                    AreaServiceLayerDto areaServiceLayerDto = new AreaServiceLayerDto(serviceLayerDto, allAreas);
                    areaServiceLayerDtos.add(areaServiceLayerDto);
                }
                break;
        }
        return areaServiceLayerDtos;
    }

    private String getBingApiKey() throws ServiceException {
        Map<String, String> parameters = ImmutableMap.<String, String>builder().put(NAME, BING_API_KEY).build();
        return repository.findSystemConfigByName(parameters);
    }

    private List<String> constructInParameters(LayerTypeEnum layerTypeEnum) {
        List<String> inClause = new ArrayList<String>();
        switch(layerTypeEnum) {
            case BACKGROUND:
                inClause.add(AreaSubTypeEnum.BACKGROUND.getAreaSubType());
                inClause.add(AreaSubTypeEnum.OTHERS.getAreaSubType());
                break;
            case ADDITIONAL:
                inClause.add(AreaSubTypeEnum.ADDITIONAL.getAreaSubType());
                inClause.add(AreaSubTypeEnum.OTHERS.getAreaSubType());
                break;
            case PORT:
                inClause.add(AreaSubTypeEnum.PORT.getAreaSubType());
                inClause.add(AreaSubTypeEnum.PORTAREA.getAreaSubType());
                break;
            case SYSAREA:
                inClause.add(AreaSubTypeEnum.SYSAREA.getAreaSubType());
                break;
            case USERAREA:
                inClause.add(AreaSubTypeEnum.USERAREA.getAreaSubType());
                break;
        }
        return inClause;
    }

    @Override
    public List<UserAreaLayerDto> listUserAreaLayerMapping() {
        List<UserAreaLayerDto> systemAreaLayerMapping = repository.findUserAreaLayerMapping();
        addServiceUrlForInternalWMSLayers(systemAreaLayerMapping);
        return systemAreaLayerMapping;
    }

    private void addServiceUrlForInternalWMSLayers(List<? extends AreaLayerDto> systemAreaLayerMapping) {
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
