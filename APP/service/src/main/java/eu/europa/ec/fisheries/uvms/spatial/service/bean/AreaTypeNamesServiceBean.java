package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.AreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.AreaServiceLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.AreaSubTypeEnum;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.LayerSubTypeEnum;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.ServiceLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import lombok.SneakyThrows;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Stateless
@Local(AreaTypeNamesService.class)
@Transactional
public class AreaTypeNamesServiceBean implements AreaTypeNamesService {

    private static final String WMS_SERVICE_TYPE = "wms";
    private static final String GEO_SERVER = "geo_server_url";
    private static final String BING_API_KEY = "bing_api_key";

    private @EJB SpatialRepository repository;

    @Override
    @SneakyThrows
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public List<String> listAllAreaTypeNames() {
        List<String> stringList = new ArrayList<>();
        List<AreaLocationTypesEntity> typesEntities = repository.listAllArea();
        for (AreaLocationTypesEntity entity : typesEntities){
            stringList.add(entity.getTypeName());
        }
        return stringList;
    }

    @Override
    @SneakyThrows
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public List<String> listAllAreaAndLocationTypeNames() {
        List<String> stringList = new ArrayList<>();
        List<AreaLocationTypesEntity> typesEntities = repository.listAllAreaAndLocation();
        for(AreaLocationTypesEntity entity : typesEntities){
            stringList.add(entity.getTypeName());
        }
        return stringList;
    }

    @Override
    public List<AreaLayerDto> listSystemAreaLayerMapping() {
        List<AreaLayerDto> systemAreaLayerMapping = repository.findSystemAreaLayerMapping();
        addServiceUrlForInternalWMSLayers(systemAreaLayerMapping);
        return systemAreaLayerMapping;
    }

    @Override
    public List<AreaLayerDto> listSystemAreaAndLocationLayerMapping() {
        List<AreaLayerDto> systemAreaLayerMapping = repository.findSystemAreaAndLocationLayerMapping();
        addServiceUrlForInternalWMSLayers(systemAreaLayerMapping);
        return systemAreaLayerMapping;
    }

    @Override
    public List<ServiceLayerDto> getAreaLayerDescription(LayerSubTypeEnum layerTypeEnum) throws ServiceException {
        String apiKey = getBingApiKey();
        if (apiKey != null) {
            return repository.findServiceLayerBySubType(constructInParameters(layerTypeEnum), true);
        } else {
            return repository.findServiceLayerBySubType(constructInParameters(layerTypeEnum), false);
        }
    }

    public List<AreaServiceLayerDto> getAllAreasLayerDescription(LayerSubTypeEnum layerTypeEnum, String userName, String scopeName) throws ServiceException {
        List<AreaServiceLayerDto> areaServiceLayerDtos = new ArrayList<AreaServiceLayerDto>();
        switch (layerTypeEnum) {
            case USERAREA:
                List<ServiceLayerDto> userserviceLayerDtos = getAreaLayerDescription(layerTypeEnum);
                List<AreaDto> allUserAreas = repository.getAllUserAreas(userName, scopeName);
                for (ServiceLayerDto serviceLayerDto : userserviceLayerDtos) {
                    AreaServiceLayerDto areaServiceLayerDto = new AreaServiceLayerDto(serviceLayerDto, allUserAreas);
                    areaServiceLayerDtos.add(areaServiceLayerDto);
                }
                break;
            case AREAGROUP:
                List<ServiceLayerDto> areGroupServiceLayerDtos = getAreaLayerDescription(layerTypeEnum);
                List<AreaDto> allUserAreaGroupNames = repository.getAllUserAreaGroupNames(userName, scopeName);
                for (ServiceLayerDto serviceLayerDto : areGroupServiceLayerDtos) {
                    AreaServiceLayerDto areaServiceLayerDto = new AreaServiceLayerDto(serviceLayerDto, allUserAreaGroupNames);
                    areaServiceLayerDtos.add(areaServiceLayerDto);
                }
        }
        return areaServiceLayerDtos;
    }

    private String getBingApiKey() throws ServiceException {
        return repository.findSystemConfigByName(BING_API_KEY);
    }

    private List<String> constructInParameters(LayerSubTypeEnum layerTypeEnum) {
        List<String> inClause = new ArrayList<String>();
        switch (layerTypeEnum) {
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
            case AREAGROUP:
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
            String geoServerUrl = repository.findSystemConfigByName(GEO_SERVER);
            if (geoServerUrl == null) {
                throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
            }
            return geoServerUrl;
        } catch (ServiceException e) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
        }
    }
}
