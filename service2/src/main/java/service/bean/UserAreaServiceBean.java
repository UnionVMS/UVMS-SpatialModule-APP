/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package service.bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.commons.geometry.mapper.GeometryMapper;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.message.service.UserProducerBean;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMapperException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaTypeNamesService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.UserAreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.util.DatabaseDialect;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.util.DatabaseDialectFactory;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.geojson.UserAreaGeoJsonDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.layer.UserAreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.UserAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.exception.DataSetNotFoundException;
import eu.europa.ec.fisheries.uvms.spatial.service.exception.SpatialServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.UserAreaMapper;
import eu.europa.ec.fisheries.uvms.user.model.exception.ModelMarshallException;
import eu.europa.ec.fisheries.wsdl.user.types.DatasetExtension;
import eu.europa.ec.fisheries.wsdl.user.types.DatasetList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.*;

import static eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType.USERAREA;
import static eu.europa.ec.fisheries.uvms.spatial.service.dto.usm.USMSpatial.APPLICATION_NAME;
import static eu.europa.ec.fisheries.uvms.spatial.service.dto.usm.USMSpatial.DELIMITER;
import static eu.europa.ec.fisheries.uvms.spatial.service.exception.SpatialServiceErrors.*;

@Stateless
@Local(UserAreaService.class)
@Slf4j
public class UserAreaServiceBean implements UserAreaService {

    public static final String NOK = "NOK";

    @EJB
    private SpatialRepository repository;

    @EJB
    private AreaTypeNamesService areaTypeNamesService;

    @EJB
    private SpatialUserServiceBean spatialUSMServiceBean;

    @EJB
    private PropertiesBean properties;

    @EJB
    private UserProducerBean userProducer;

    @Inject
    private UserAreaMapper mapper;

    private DatabaseDialect dialect;

    @PostConstruct
    public void init(){
        dialect = new DatabaseDialectFactory(properties).getInstance();
    }

    @Override
    @Transactional
    public Long updateUserArea(UserAreaGeoJsonDto dto, String userName, boolean isPowerUser, String scopeName) throws ServiceException {
        dto.getGeometry().setSRID(dialect.defaultSRID());
        UserAreasEntity userAreaToBeUpdated;
        try {
            Long id = dto.getId();
            userAreaToBeUpdated = repository.findUserAreaById(id, userName, isPowerUser, scopeName);
            String newDataSetName = dto.getDatasetName();
            String oldDataSetName = userAreaToBeUpdated.getDatasetName();
            if (newDataSetName == null) {
                tryToDeleteDatasetFromUSM(userAreaToBeUpdated, oldDataSetName);
            }
            else if (!newDataSetName.equals(oldDataSetName)) {
                if (dataSetNameUnique(newDataSetName)){
                    tryToDeleteDatasetFromUSM(userAreaToBeUpdated, oldDataSetName);
                    spatialUSMServiceBean.persistDataSetInUSM(newDataSetName, USERAREA.value() + DELIMITER + userAreaToBeUpdated.getId());
                }
                else {
                    throw new SpatialServiceException(DATA_SET_NAME_ALREADY_IN_USE);
                }
            }
            userAreaToBeUpdated.merge(dto);

        } catch (ModelMarshallException | MessageException | SpatialModelMapperException e) {
            throw new SpatialServiceException(INTERNAL_APPLICATION_ERROR);
        }
        return userAreaToBeUpdated.getId();
    }

    private void tryToDeleteDatasetFromUSM(UserAreasEntity userAreaToBeUpdated, String oldDataSetName) {
        try {
            spatialUSMServiceBean.deleteDataSetNameFromUSM(oldDataSetName, APPLICATION_NAME, USERAREA.value() + DELIMITER + userAreaToBeUpdated.getId());
        } catch (DataSetNotFoundException e) {
            log.warn(e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Long createUserArea(UserAreaGeoJsonDto dto, String userName) throws ServiceException {
        UserAreasEntity persisted;
        try {
            String dataSetName = dto.getDatasetName();
            if (StringUtils.isEmpty(dataSetName)){
                persisted = persistUserAreasEntity(dto, userName);
            }
            else {
                if (dataSetNameUnique(dataSetName)){
                    persisted = persistUserAreasEntity(dto, userName);
                    spatialUSMServiceBean.persistDataSetInUSM(dataSetName, USERAREA.value() + DELIMITER + persisted.getId());
                }
                else {
                    throw new SpatialServiceException(DATA_SET_NAME_ALREADY_IN_USE);
                }
            }
        } catch (MessageException | ModelMarshallException | SpatialModelMapperException e) {
            throw new SpatialServiceException(INTERNAL_APPLICATION_ERROR);
        }
        return persisted.getId();
    }

    private UserAreasEntity persistUserAreasEntity(UserAreaGeoJsonDto dto, String userName) throws ServiceException {
        UserAreasEntity persisted;
        dto.getGeometry().setSRID(dialect.defaultSRID());
        UserAreasEntity userAreasEntity = mapper.fromDtoToEntity(dto);
        userAreasEntity.setUserName(userName);
        userAreasEntity.setCreatedOn(new Date());
        String code = dto.getName();
        if(code != null && code.length() > 20){
            code = code.substring(0 , 20);
        }
        userAreasEntity.setCode(code);
        persisted = repository.save(userAreasEntity);
        return persisted;
    }

    private Boolean dataSetNameUnique(String dataSetName) {
        DatasetList dataSetListFromUSM;
        try {
            dataSetListFromUSM = spatialUSMServiceBean.listDatasets();
            List<DatasetExtension> extensions = new ArrayList<>();
            if (dataSetListFromUSM != null){
                extensions = dataSetListFromUSM.getList();
            }
            if (extensions != null){
                for (DatasetExtension extension : extensions) {
                    if (extension != null && extension.getName().equals(dataSetName)) {
                        return false;
                    }
                }
            }
        } catch (ModelMarshallException | MessageException | SpatialModelMapperException e) {
            throw new SpatialServiceException(INTERNAL_APPLICATION_ERROR);
        }
        return true;
    }

    @Override
    @Transactional
    public void deleteUserArea(Long userAreaId, String userName, boolean isPowerUser, String scopeName) throws ServiceException {
        UserAreasEntity userAreaById = repository.findUserAreaById(userAreaId, userName, isPowerUser, scopeName);
        if (userAreaById == null) {
            throw new SpatialServiceException(USER_AREA_DOES_NOT_EXIST, userAreaId);
        }
        try {
            if (userAreaById.getDatasetName() != null){
                spatialUSMServiceBean.deleteDataSetNameFromUSM(userAreaById.getDatasetName(),
                        APPLICATION_NAME, USERAREA.value() + DELIMITER + userAreaById.getId());
            }
        } catch (DataSetNotFoundException e) {
            log.warn("DATA SET NOT FOUND IN USM", e);
        }
        repository.deleteUserArea(userAreaById);
    }

    @Override
    public List<String> getUserAreaTypes(String userName, String scopeName, boolean isPowerUser) throws ServiceException {
        Set<String> stringSet = new LinkedHashSet<>();
        List<UserAreasEntity> userArea = repository.findUserArea(userName, scopeName, isPowerUser);
        for (UserAreasEntity entity : userArea){
            stringSet.add(entity.getType());
        }
        return new ArrayList<>(stringSet);
    }

    @Override
    public UserAreaLayerDto getUserAreaLayerDefinition(String userName, String scopeName) {
        List<UserAreaLayerDto> userAreaLayerDtoList = areaTypeNamesService.listUserAreaLayerMapping();
        UserAreaLayerDto userAreaLayerDto = userAreaLayerDtoList.get(0);
        userAreaLayerDto.setIdList(getUserAreaGuid(userName, scopeName));
        return userAreaLayerDto;
    }

    @Override
    public Map<String, Object> getUserAreaDetailsWithExtentById(AreaTypeEntry areaTypeEntry, String userName, boolean isPowerUser, String scopeName) throws ServiceException {
        UserAreasEntity userAreaById = repository.findUserAreaById(Long.parseLong(areaTypeEntry.getId()), userName, isPowerUser, scopeName);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(userAreaById, Map.class);
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
            throw new SpatialServiceException(INTERNAL_APPLICATION_ERROR);
        }
    }

    @Override
    public List<eu.europa.ec.fisheries.uvms.spatial.service.dto.area.UserAreaDto> searchUserAreasByCriteria(String userName, String scopeName, String searchCriteria, boolean isPowerUser) throws ServiceException {

        List<UserAreasEntity> userAreas = repository.listUserAreaByCriteria(userName, scopeName, searchCriteria, isPowerUser);
        final List<eu.europa.ec.fisheries.uvms.spatial.service.dto.area.UserAreaDto> userAreaDtos = new ArrayList<>();
        Iterator it = userAreas.iterator();

        while (it.hasNext( )) {
            UserAreasEntity next = (UserAreasEntity) it.next();
            it.remove(); // avoids a ConcurrentModificationException
            Geometry geom = next.getGeom();
            if (geom != null){
                Geometry envelope = next.getGeom().getEnvelope();
                userAreaDtos.add(new eu.europa.ec.fisheries.uvms.spatial.service.dto.area.UserAreaDto(next.getId(), next.getName(),
                        StringUtils.isNotBlank(next.getAreaDesc()) ? next.getAreaDesc() : StringUtils.EMPTY,
                        GeometryMapper.INSTANCE.geometryToWkt(envelope).getValue(), next.getUserName()));
            }
        }
        return userAreaDtos;
    }

    @Override
    public List<UserAreaGeoJsonDto> searchUserAreasByType(String userName, String scopeName, String type, boolean isPowerUser) throws ServiceException {
        List<UserAreasEntity> userAreas = repository.findUserAreasByType(userName, scopeName, type, isPowerUser);
        return mapper.fromEntityListToDtoList(userAreas, false);
    }

    // UT
    public void setDialect(DatabaseDialect dialect) {
        this.dialect = dialect;
    }

}