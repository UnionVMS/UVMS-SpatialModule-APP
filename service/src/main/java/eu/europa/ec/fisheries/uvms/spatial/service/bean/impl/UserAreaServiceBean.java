/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.spatial.service.bean.impl;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import eu.europa.ec.fisheries.uvms.common.utils.GeometryUtils;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.mapper.GeometryMapper;
import eu.europa.ec.fisheries.uvms.message.MessageException;
import eu.europa.ec.fisheries.uvms.rest.security.bean.USMService;
import eu.europa.ec.fisheries.uvms.spatial.message.service.SpatialConsumerBean;
import eu.europa.ec.fisheries.uvms.spatial.message.service.UserProducerBean;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMapperException;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMarshallException;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelValidationException;
import eu.europa.ec.fisheries.uvms.spatial.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaProperty;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialFault;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaTypeNamesService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.UserAreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.util.DatabaseDialect;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.util.DatabaseDialectFactory;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.area.UserAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.geojson.UserAreaGeoJsonDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.layer.UserAreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.usm.USMSpatial;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.UserAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.UserScopeEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.exception.SpatialServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.UserAreaMapper;
import eu.europa.ec.fisheries.uvms.user.model.exception.ModelMarshallException;
import eu.europa.ec.fisheries.uvms.user.model.mapper.UserModuleRequestMapper;
import eu.europa.ec.fisheries.wsdl.user.module.CreateDatasetResponse;
import eu.europa.ec.fisheries.wsdl.user.module.DeleteDatasetResponse;
import eu.europa.ec.fisheries.wsdl.user.module.FilterDatasetResponse;
import eu.europa.ec.fisheries.wsdl.user.types.DatasetExtension;
import eu.europa.ec.fisheries.wsdl.user.types.DatasetFilter;
import eu.europa.ec.fisheries.wsdl.user.types.DatasetList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

@Stateless
@Local(UserAreaService.class)
@Slf4j
public class UserAreaServiceBean implements UserAreaService {

    public static final String NOK = "NOK";
    private static final String ERROR_WHEN_MARSHALLING_DATA = "[ Error when marshalling data. ] {}";
    private static final String ERROR_WHEN_MARSHALLING_OBJECT_TO_STRING = "Error when marshalling object to String";
    @EJB
    private SpatialRepository repository;

    @EJB
    private AreaTypeNamesService areaTypeNamesService;

    @EJB
    private USMService usmService;

    @EJB
    private PropertiesBean properties;

    @EJB
    private UserProducerBean userProducer;

    @EJB
    private SpatialConsumerBean consumer;

    private DatabaseDialect dialect;

    private static void validateResponse(TextMessage response, String correlationId) throws SpatialModelValidationException {

        try {
            if (response == null) {
                throw new SpatialModelValidationException("Error when validating response in ResponseMapper: Response is Null");
            }

            if (response.getJMSCorrelationID() == null) {
                throw new SpatialModelValidationException("No correlationId in response (Null) . Expected was: " + correlationId);
            }

            if (!correlationId.equalsIgnoreCase(response.getJMSCorrelationID())) {
                throw new SpatialModelValidationException("Wrong correlationId in response. Expected was: " + correlationId + " But actual was: " + response.getJMSCorrelationID());
            }

            try {
                SpatialFault fault = JAXBMarshaller.unmarshall(response, SpatialFault.class);
                throw new SpatialModelValidationException(fault.getCode() + " : " + fault.getFault());
            } catch (SpatialModelMarshallException e) {
                log.info("Expected Exception"); // Exception received in case if the validation is success
            }

        } catch (JMSException e) {
            log.error("JMS exception during validation ", e);
            throw new SpatialModelValidationException("JMS exception during validation " + e.getMessage());
        }
    }

    @PostConstruct
    public void init(){
        dialect = new DatabaseDialectFactory(properties).getInstance();
    }

    @Override
    @Transactional
    public Long storeUserArea(UserAreaGeoJsonDto userAreaDto, String userName) throws ServiceException {

        UserAreasEntity persistedEntity;

        try {

            checkIfContainsDataSetName(userAreaDto, fetchSpatialDatasetListFromUSM());

            persistedEntity = repository.getUserAreaByUserNameAndName(userName, userAreaDto.getName());

            if (persistedEntity != null) {
                throw new SpatialServiceException(SpatialServiceErrors.USER_AREA_ALREADY_EXISTING);
            } else {

                userAreaDto.getGeometry().setSRID(dialect.defaultSRID());
                UserAreasEntity userAreasEntity = UserAreaMapper.mapper().fromDtoToEntity(userAreaDto);
                userAreasEntity.setUserName(userName);
                userAreasEntity.setCreatedOn(new Date());

                persistedEntity = repository.save(userAreasEntity);

                if (StringUtils.isNotBlank(persistedEntity.getDatasetName())) {
                    persistDatasetInUSM(persistedEntity);
                }
            }

        } catch (MessageException | ModelMarshallException | SpatialModelMapperException e) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
        }

        return persistedEntity.getId();

    }

    private void checkIfContainsDataSetName(UserAreaGeoJsonDto userAreaDto, DatasetList datasetList) {
        for (DatasetExtension extension : datasetList.getList()) {
            if (extension != null && extension.getName().equals(userAreaDto.getDatasetName())) {
                throw new SpatialServiceException(SpatialServiceErrors.DATA_SET_NAME_ALREADY_IN_USE);
            }
        }
    }

    private DatasetList fetchSpatialDatasetListFromUSM() throws ModelMarshallException, MessageException, SpatialModelMapperException {
        DatasetFilter filter = new DatasetFilter();
        filter.setApplicationName(USMSpatial.APPLICATION_NAME);
        filter.setCategory(USMSpatial.USM_DATASET_CATEGORY);
        String request = UserModuleRequestMapper.mapToFindDatasetRequest(filter);
        String correlationId = userProducer.sendModuleMessage(request, consumer.getDestination());
        TextMessage message = consumer.getMessage(correlationId, TextMessage.class);
        return mapToFindDatasetResponse(message, correlationId);
    }

    private void persistDatasetInUSM(UserAreasEntity persistedEntity) throws ModelMarshallException, MessageException, SpatialModelMapperException {
        DatasetExtension dataset = new DatasetExtension();
        dataset.setApplicationName(USMSpatial.APPLICATION_NAME);
        dataset.setDiscriminator(createDescriminator(persistedEntity));
        dataset.setName(persistedEntity.getDatasetName());
        dataset.setCategory(USMSpatial.USM_DATASET_CATEGORY);
        dataset.setDescription(USMSpatial.USM_DATASET_DESCRIPTION);
        String payload = UserModuleRequestMapper.mapToCreateDatasetRequest(dataset);
        String correlationId = userProducer.sendModuleMessage(payload, consumer.getDestination());
        TextMessage message = consumer.getMessage(correlationId, TextMessage.class);
        mapToCreateDatasetResponse(message, correlationId);
    }

    private String deleteDataSetNameFromUSM(String previousDataSetName, String applicationName) throws ModelMarshallException, MessageException, SpatialModelMapperException {
        String result = NOK;
        if (StringUtils.isNotBlank(previousDataSetName)) {
            DatasetExtension extension = new DatasetExtension();
            extension.setName(previousDataSetName);
            extension.setApplicationName(applicationName);
            String request = UserModuleRequestMapper.mapToDeleteDatasetRequest(extension);
            String correlationId = userProducer.sendModuleMessage(request, consumer.getDestination());
            TextMessage message = consumer.getMessage(correlationId, TextMessage.class);
            result = mapToDeleteDataSetResponse(message, correlationId);
        }

        if (result.equals(NOK)) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
        }
        return result;
    }

    private String mapToDeleteDataSetResponse(TextMessage response, String correlationId) throws SpatialModelMapperException {
        try {
            validateResponse(response, correlationId);
            DeleteDatasetResponse deleteDatasetResponse = JAXBMarshaller.unmarshall(response, DeleteDatasetResponse.class);
            return deleteDatasetResponse.getResponse();
        } catch (SpatialModelMarshallException e) {
            log.error(ERROR_WHEN_MARSHALLING_DATA, e);
            throw new SpatialModelMarshallException(ERROR_WHEN_MARSHALLING_OBJECT_TO_STRING, e);
        }
    }

    private DatasetList mapToFindDatasetResponse(TextMessage response, String correlationId) throws SpatialModelMapperException {
        try {
            validateResponse(response, correlationId);
            FilterDatasetResponse createDataSetResponse = JAXBMarshaller.unmarshall(response, FilterDatasetResponse.class);
            return createDataSetResponse.getDatasetList();
        } catch (SpatialModelMarshallException e) {
            log.error(ERROR_WHEN_MARSHALLING_DATA, e);
            throw new SpatialModelMarshallException(ERROR_WHEN_MARSHALLING_OBJECT_TO_STRING, e);
        }
    }

    private String mapToCreateDatasetResponse(TextMessage response, String correlationId) throws SpatialModelMapperException {
        try {
            validateResponse(response, correlationId);
            CreateDatasetResponse createDatasetResponse = JAXBMarshaller.unmarshall(response, CreateDatasetResponse.class);
            return createDatasetResponse.getResponse();
        } catch (SpatialModelMarshallException e) {
            log.error(ERROR_WHEN_MARSHALLING_DATA, e);
            throw new SpatialModelMarshallException(ERROR_WHEN_MARSHALLING_OBJECT_TO_STRING, e);
        }
    }

    @Override
    @Transactional
    public Long updateUserArea(UserAreaGeoJsonDto userAreaDto, String userName, boolean isPowerUser, String scopeName) throws ServiceException {

        UserAreasEntity persistedUpdatedEntity;

        try {

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

            String newDataSetName = userAreaDto.getDatasetName();
            String previousDataSetName = userAreaById.getDatasetName();

            if (newDataSetName != null && !newDataSetName.equals(previousDataSetName)) {
                checkIfContainsDataSetName(userAreaDto, fetchSpatialDatasetListFromUSM());
            } else {
                deleteDataSetNameFromUSM(previousDataSetName, USMSpatial.APPLICATION_NAME);
            }

            if (StringUtils.isNotBlank(userAreaDto.getDatasetName()) && !userAreaDto.getDatasetName().equals(userAreaById.getDatasetName())) {
                updateUSMDataset(userAreaById, userAreaDto.getDatasetName());
            }

            UserAreaMapper.mapper().updateUserAreaEntity(userAreaDto, userAreaById);
            userAreaById.getGeom().setSRID(dialect.defaultSRID());
            createScopeSelection(userAreaDto, userAreaById);
            persistedUpdatedEntity = repository.update(userAreaById);

        } catch (ModelMarshallException | MessageException | SpatialModelMapperException e) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
        }

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

        try {
            deleteDataSetNameFromUSM(userAreaById.getDatasetName(), USMSpatial.APPLICATION_NAME);
        } catch (ModelMarshallException | SpatialModelMapperException | MessageException e) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
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

    //BUG FIX: Caused by: org.hibernate.HibernateException: A collection with cascade="all-delete-orphan" was no longer referenced by the owning entity instance: eu.europa.ec.fisheries.uvms.spatial.service.entity.UserAreasEntity.scopeSelection
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
            properties.put("centroid", GeometryUtils.wktToCentroidWkt(String.valueOf(geometry)));
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

    // UT
    public void setDialect(DatabaseDialect dialect) {
        this.dialect = dialect;
    }

}