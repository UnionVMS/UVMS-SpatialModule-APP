/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.mapper.GeometryMapper;
import eu.europa.ec.fisheries.uvms.rest.security.bean.USMService;
import eu.europa.ec.fisheries.uvms.spatial.entity.UserAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.UserScopeEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.constants.USMSpatial;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaProperty;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.service.AreaTypeNamesService;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.UserAreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.UserAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.geojson.UserAreaGeoJsonDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import eu.europa.ec.fisheries.uvms.spatial.mapper.UserAreaMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Local(UserAreaService.class)
@Slf4j
public class UserAreaServiceBean implements UserAreaService {

    private @EJB SpatialRepository repository;
    private @EJB AreaTypeNamesService areaTypeNamesService;
    private @EJB USMService usmService;

    @Override
    @Transactional
    public Long storeUserArea(UserAreaGeoJsonDto userAreaDto, String userName) throws ServiceException {

        UserAreasEntity userAreaByUserNameAndName = repository.getUserAreaByUserNameAndName(userName, userAreaDto.getName());

        if (userAreaByUserNameAndName != null){
            throw new SpatialServiceException(SpatialServiceErrors.USER_AREA_ALREADY_EXISTING);
        }

        else {

            UserAreasEntity userAreasEntity = UserAreaMapper.mapper().fromDtoToEntity(userAreaDto);
            userAreasEntity.setUserName(userName);
            userAreasEntity.setCreatedOn(new Date());

            UserAreasEntity persistedEntity = repository.save(userAreasEntity);

            if (StringUtils.isNotBlank(persistedEntity.getDatasetName())) {
                usmService.createDataset(USMSpatial.APPLICATION_NAME, persistedEntity.getDatasetName(), createDescriminator(persistedEntity), USMSpatial.USM_DATASET_CATEGORY, USMSpatial.USM_DATASET_DESCRIPTION);
            }
            return persistedEntity.getId();
        }

    }


    @Override
    @Transactional
    public Long updateUserArea(UserAreaGeoJsonDto userAreaDto, String userName, boolean isPowerUser, String scopeName) throws ServiceException {
        Long id = userAreaDto.getId();

        if (userName == null) {
            throw new IllegalArgumentException("USER CANNOT BE NULL");
        }

        if (id == null) {
            throw new SpatialServiceException(SpatialServiceErrors.MISSING_USER_AREA_ID);
        }

        UserAreasEntity userAreaById = repository.findUserAreaById(id, userName, isPowerUser, scopeName);

        if (userAreaById == null) {
            throw new SpatialServiceException(SpatialServiceErrors.USER_AREA_DOES_NOT_EXIST, userAreaById);
        }

        if (!(userName).equals(userAreaById.getUserName()) && !isPowerUser) {
            throw new ServiceException("user_not_authorised");
        }

        if (StringUtils.isNotBlank(userAreaDto.getDatasetName()) && !userAreaDto.getDatasetName().equals(userAreaById.getDatasetName())) {
            updateUSMDataset(userAreaById, userAreaDto.getDatasetName());
        }
        UserAreaMapper.mapper().updateUserAreaEntity(userAreaDto, userAreaById);
        createScopeSelection(userAreaDto, userAreaById);

        UserAreasEntity persistedUpdatedEntity = repository.update(userAreaById);
        return persistedUpdatedEntity.getId();
    }

    @Override
    @Transactional
    public void deleteUserArea(Long userAreaId, String userName, boolean isPowerUser, String scopeName) throws ServiceException {

        UserAreasEntity userAreaById = repository.findUserAreaById(userAreaId, userName, isPowerUser, scopeName);
        if (userAreaById == null) {
            throw new SpatialServiceException(SpatialServiceErrors.USER_AREA_DOES_NOT_EXIST, userAreaId);
        }
        if (!userAreaById.getUserName().equals(userName) && !isPowerUser) {
            throw new ServiceException("user_not_authorised");
        }
        repository.deleteUserArea(userAreaById);
    }

    @Override
    @Transactional
    public List<String> getUserAreaTypes(String userName, String scopeName, boolean isPowerUser) throws ServiceException {

        Set<String> stringSet = new LinkedHashSet<>();
        List<UserAreasEntity> userArea = repository.findUserArea(userName, scopeName, isPowerUser);

        for (UserAreasEntity entity : userArea){
            stringSet.add(entity.getType());
        }

        return new ArrayList<>(stringSet);
    }

    @Override
    @Transactional
    public UserAreaLayerDto getUserAreaLayerDefinition(String userName, String scopeName) {

        List<UserAreaLayerDto> userAreaLayerDtoList = areaTypeNamesService.listUserAreaLayerMapping();
        UserAreaLayerDto userAreaLayerDto = userAreaLayerDtoList.get(0);
        userAreaLayerDto.setIdList(getUserAreaGuid(userName, scopeName));
        return userAreaLayerDto;
    }

    @Override
    @Transactional
    public List<AreaDetails> getUserAreaDetailsWithExtentById(AreaTypeEntry areaTypeEntry, String userName, boolean isPowerUser, String scopeName) throws ServiceException {

        UserAreasEntity userAreaById = repository.findUserAreaById(Long.parseLong(areaTypeEntry.getId()), userName, isPowerUser, scopeName);
        List<AreaDetails> areaDetailsList;

        try {
            if (userAreaById != null) {
                areaDetailsList = new ArrayList<>();
                Map<String, Object> properties = userAreaById.getFieldMap();
                addCentroidToProperties(properties);
                areaDetailsList.add(createAreaDetailsSpatialResponse(properties, areaTypeEntry));

            } else {
                AreaDetails areaDetails = new AreaDetails();
                areaDetails.setAreaType(areaTypeEntry);
                areaDetailsList = Arrays.asList(areaDetails);
            }
            return areaDetailsList;

        } catch (ParseException e) {
            String error = "Error while trying to parse geometry";
            log.error(error, e);
            throw new ServiceException(error);
        }
    }

    @Override
    @Transactional
    public List<AreaDetails> getUserAreaDetailsById(AreaTypeEntry areaTypeEntry, String userName, boolean isPowerUser, String scopeName) throws ServiceException {

        UserAreasEntity userAreaById = repository.findUserAreaById(Long.parseLong(areaTypeEntry.getId()), userName, isPowerUser, scopeName);
        try {
            List<AreaDetails> areaDetailsList = new ArrayList<>();

            if (userAreaById != null){
                Map<String, Object> properties = userAreaById.getFieldMap();
                addCentroidToProperties(properties);
                areaDetailsList.add(createAreaDetailsSpatialResponse(properties, areaTypeEntry));
            }

            return areaDetailsList;

        } catch (ParseException e) {
            String error = "Error while trying to parse geometry";
            log.error(error, e);
            throw new ServiceException(error);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateUserAreaDates(String remoteUser, Date startDate, Date endDate, String type, boolean isPowerUser) throws ServiceException {
        if (isPowerUser) {
            repository.updateUserAreaForUserAndScope(startDate, endDate, type);
        } else {
            repository.updateUserAreaForUser(remoteUser, startDate, endDate, type);
        }
    }


    private String createDescriminator(UserAreasEntity persistedEntity) {
        return AreaType.USERAREA.value() + USMSpatial.DELIMITER + persistedEntity.getId();
    }

    //BUG FIX: Caused by: org.hibernate.HibernateException: A collection with cascade="all-delete-orphan" was no longer referenced by the owning entity instance: eu.europa.ec.fisheries.uvms.spatial.entity.UserAreasEntity.scopeSelection
    private void createScopeSelection(UserAreaGeoJsonDto userAreaDto, UserAreasEntity persistentUserArea) {
        Set<UserScopeEntity> scopeSelection = persistentUserArea.getScopeSelection();
        scopeSelection.clear();
        scopeSelection.addAll(UserAreaMapper.fromScopeArrayToEntity(userAreaDto.getScopeSelection(), persistentUserArea));
    }

    private void updateUSMDataset(UserAreasEntity oldDatasetName, String newDatasetName) throws ServiceException {
        //first remove the old dataset
        if (StringUtils.isNotBlank(oldDatasetName.getDatasetName())) {
            usmService.deleteDataset(USMSpatial.APPLICATION_NAME, oldDatasetName.getDatasetName());
        }
        //and if it is a renaming action
        if (StringUtils.isNotBlank(newDatasetName)) {
            //create the new dataset
            usmService.createDataset(USMSpatial.APPLICATION_NAME, newDatasetName, createDescriminator(oldDatasetName), USMSpatial.USM_DATASET_CATEGORY, USMSpatial.USM_DATASET_DESCRIPTION);
        }
    }

    private void addCentroidToProperties(Map<String, Object> properties) throws ParseException {

        Object geometry = properties.get("geometry");

        if (geometry != null){
            Geometry centroid =
                    GeometryMapper.INSTANCE.wktToGeometry(String.valueOf(geometry)).getValue().getCentroid();
            properties.put("centroid", GeometryMapper.INSTANCE.geometryToWkt(centroid).getValue());
        }
    }

    private AreaDetails createAreaDetailsSpatialResponse(Map<String, Object> properties, AreaTypeEntry areaTypeEntry) {
        List<AreaProperty> areaProperties = new ArrayList<>();
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            AreaProperty areaProperty = new AreaProperty();
            areaProperty.setPropertyName(entry.getKey());
            areaProperty.setPropertyValue(entry.getValue());
            areaProperties.add(areaProperty);
        }
        AreaDetails areaDetails = new AreaDetails();
        areaDetails.setAreaType(areaTypeEntry);
        areaDetails.getAreaProperties().addAll(areaProperties);
        return areaDetails;
    }

    @SuppressWarnings("unchecked")
    public List<Long> getUserAreaGuid(String userName, String scopeName) {
        try {
            List<Long> longList = new ArrayList<>();
            List<UserAreasEntity> userAreaByUserNameAndScopeName = repository.findUserAreaByUserNameAndScopeName(userName, scopeName);
            for (UserAreasEntity entity : userAreaByUserNameAndScopeName){
                longList.add(entity.getId());
            }
            return longList;
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
        }
    }

    @Override
    @Transactional
    public List<UserAreaDto> searchUserAreasByCriteria(String userName, String scopeName, String searchCriteria, boolean isPowerUser) throws ServiceException {

        List<UserAreasEntity> userAreas = repository.listUserAreaByCriteria(userName, scopeName, searchCriteria, isPowerUser);
        final List<UserAreaDto> userAreaDtos = new ArrayList<>();
        Iterator it = userAreas.iterator();

        while (it.hasNext( )) {
            UserAreasEntity next = (UserAreasEntity) it.next();
            it.remove(); // avoids a ConcurrentModificationException
            Geometry geom = next.getGeom();
            if (geom != null){
                Geometry envelope = next.getGeom().getEnvelope();
                userAreaDtos.add(new UserAreaDto(next.getId(), next.getName(),
                        StringUtils.isNotBlank(next.getAreaDesc()) ? next.getAreaDesc() : StringUtils.EMPTY,
                        GeometryMapper.INSTANCE.geometryToWkt(envelope).getValue(), next.getUserName()));
            }
        }
        return userAreaDtos;
    }

    @Override
    @Transactional
    public List<UserAreaGeoJsonDto> searchUserAreasByType(String userName, String scopeName, String type, boolean isPowerUser) throws ServiceException {
        List<UserAreasEntity> userAreas = repository.findUserAreasByType(userName, scopeName, type, isPowerUser);
        return  UserAreaMapper.mapper().fromEntityListToDtoList(userAreas, false);
    }
}