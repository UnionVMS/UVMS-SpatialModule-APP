/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.AreaTypeNamesService;
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
import eu.europa.ec.fisheries.uvms.spatial.service.bean.helper.MapConfigHelper;
import lombok.SneakyThrows;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
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
    public List<AreaLayerDto> listSystemAreaLayerMapping(Collection<String> permittedLayersNames) {
        List<AreaLayerDto> systemAreaLayerMapping = repository.findSystemAreaLayerMapping();
        filterSystemAreaLayers(systemAreaLayerMapping, permittedLayersNames);
        addServiceUrlForInternalWMSLayers(systemAreaLayerMapping);
        return systemAreaLayerMapping;
    }

    @Override
    public List<AreaLayerDto> listSystemAreaAndLocationLayerMapping(Collection<String> permittedLayersNames) {
        List<AreaLayerDto> systemAreaLayerMapping = repository.findSystemAreaAndLocationLayerMapping();
        filterSystemAreaLayers(systemAreaLayerMapping, permittedLayersNames);
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

    @Override
    public List<AreaServiceLayerDto> getAllAreasLayerDescription(LayerSubTypeEnum layerTypeEnum, String userName, String scopeName) throws ServiceException {
        List<AreaServiceLayerDto> areaServiceLayerDtos = new ArrayList<AreaServiceLayerDto>();
        switch (layerTypeEnum) {
            case USERAREA:
                List<ServiceLayerDto> userserviceLayerDtos = getAreaLayerDescription(layerTypeEnum);
                List<AreaDto> allUserAreas = repository.getAllUserAreas(userName, scopeName);
                for (ServiceLayerDto serviceLayerDto : userserviceLayerDtos) {
                    AreaServiceLayerDto areaServiceLayerDto = new AreaServiceLayerDto(serviceLayerDto, allUserAreas);
                    areaServiceLayerDto.setAreaLocationTypeName(serviceLayerDto.getAreaLocationTypeName());
                    areaServiceLayerDtos.add(areaServiceLayerDto);
                }
                break;
            case AREAGROUP:
                List<ServiceLayerDto> areGroupServiceLayerDtos = getAreaLayerDescription(layerTypeEnum);
                List<AreaDto> allUserAreaGroupNames = repository.getAllUserAreaGroupNames(userName, scopeName);
                for (ServiceLayerDto serviceLayerDto : areGroupServiceLayerDtos) {
                    AreaServiceLayerDto areaServiceLayerDto = new AreaServiceLayerDto(serviceLayerDto, allUserAreaGroupNames);
                    areaServiceLayerDto.setAreaLocationTypeName(serviceLayerDto.getAreaLocationTypeName());
                    areaServiceLayerDtos.add(areaServiceLayerDto);
                }
        }
        return areaServiceLayerDtos;
    }


    private void filterSystemAreaLayers(List<AreaLayerDto> systemAreaLayerMapping, Collection<String> permittedLayersNames) {
        Iterator<AreaLayerDto> iterator = systemAreaLayerMapping.iterator();
        while(iterator.hasNext()) {
            AreaLayerDto areaLayerDto = iterator.next();
            if(!MapConfigHelper.isServiceLayerPermitted(areaLayerDto.getTypeName(), permittedLayersNames)) {
                iterator.remove();
            }
        }
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