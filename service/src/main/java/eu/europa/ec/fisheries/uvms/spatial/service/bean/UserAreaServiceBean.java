package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.ImmutableMap;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.UserAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaProperty;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Coordinate;
import eu.europa.ec.fisheries.uvms.spatial.repository.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaGeomDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.UserAreaMapper;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.*;

import static com.google.common.collect.Lists.newArrayList;
import static eu.europa.ec.fisheries.uvms.spatial.util.ColumnAliasNameHelper.getFieldMap;
import static eu.europa.ec.fisheries.uvms.spatial.util.SpatialUtils.convertToPointInWGS84;

@Stateless
@Local(UserAreaService.class)
@Transactional
public class UserAreaServiceBean implements UserAreaService {

    private static final String USER_NAME = "userName";
    private static final String SCOPE_NAME = "scopeName";

    @EJB
    private SpatialRepository repository;

    @Override
    public boolean storeUserArea(UserAreaGeomDto userAreaDto, String userName, String scopeName) throws ServiceException {
        UserAreasEntity userAreasEntity = prepareNewEntity(userAreaDto, userName, scopeName);
        UserAreasEntity persistedEntity = (UserAreasEntity) repository.createEntity(userAreasEntity);
        return persistedEntity != null;
    }

    private UserAreasEntity prepareNewEntity(UserAreaGeomDto userAreaDto, String userName, String scopeName) {
        UserAreasEntity userAreasEntity = UserAreaMapper.mapper().fromDtoToEntity(userAreaDto);
        userAreasEntity.setUserName(userName);
        userAreasEntity.setScopeName(scopeName);
        userAreasEntity.setCreatedOn(new Date());
        return userAreasEntity;
    }

    @Override
    public boolean updateUserArea(UserAreaGeomDto userAreaDto, String userName, String scopeName) throws ServiceException {
        Long gid = userAreaDto.getGid();
        validateGid(gid);

        UserAreasEntity persistentUserArea = repository.findUserAreaById(gid, userName, scopeName);
        validateNotNull(gid, persistentUserArea);

        UserAreasEntity userAreasEntity = prepareUpdateEntity(persistentUserArea, userAreaDto, userName, scopeName);
        UserAreasEntity persistedEntity = (UserAreasEntity) repository.updateEntity(userAreasEntity);
        return persistedEntity != null;
    }

    private UserAreasEntity prepareUpdateEntity(UserAreasEntity persistentUserArea, UserAreaGeomDto userAreaDto, String userName, String scopeName) {
        persistentUserArea.setUserName(userName);
        persistentUserArea.setScopeName(scopeName);
        if (userAreaDto.getName() != null) {
            persistentUserArea.setName(userAreaDto.getName());
        }
        if (userAreaDto.getDesc() != null) {
            persistentUserArea.setAreaDesc(userAreaDto.getDesc());
        }
        if (userAreaDto.getGeometry() != null) {
            persistentUserArea.setGeom(userAreaDto.getGeometry());
        }
        if (userAreaDto.isShared() != null) {
            persistentUserArea.setIsShared(userAreaDto.isShared());
        }
        return persistentUserArea;
    }

    private void validateGid(Long gid) {
        if (gid == null) {
            throw new SpatialServiceException(SpatialServiceErrors.MISSING_USER_AREA_ID);
        }
    }

    @Override
    public void deleteUserArea(Long userAreaId, String userName, String scopeName) throws ServiceException {
        UserAreasEntity persistentUserArea = repository.findUserAreaById(userAreaId, userName, scopeName);
        validateNotNull(userAreaId, persistentUserArea);

        repository.deleteEntity(persistentUserArea);
    }

    private void validateNotNull(Long userAreaId, UserAreasEntity persistentUserArea) {
        if (persistentUserArea == null) {
            throw new SpatialServiceException(SpatialServiceErrors.USER_AREA_DOES_NOT_EXIST, userAreaId);
        }
    }

    public UserAreaLayerDto getUserAreaLayerDefination(String userName, String scopeName) {
        List<UserAreaLayerDto> userAreaLayerDtoList = getUserAreaLayerMapping();
        UserAreaLayerDto userAreaLayerDto = userAreaLayerDtoList.get(0);
        userAreaLayerDto.setIdList(getUserAreaGuid(userName, scopeName));
        return userAreaLayerDto;
    }

    @Override
    public List<UserAreaDto> getUserAreaDetailsWithExtentByLocation(Coordinate coordinate, String userName, String scopeName) {
        Point point = convertToPointInWGS84(coordinate.getLongitude(), coordinate.getLatitude(), coordinate.getCrs());
        List<UserAreaDto> userAreaDetails = repository.findUserAreaDetailsWithExtentByLocation(userName, scopeName, point);
        return userAreaDetails;
    }

    @Override
    public List<AreaDetails> getUserAreaDetailsByLocation(AreaTypeEntry areaTypeEntry, String userName, String scopeName) {
        Point point = convertToPointInWGS84(areaTypeEntry.getLongitude(), areaTypeEntry.getLatitude(), areaTypeEntry.getCrs());
        List<UserAreasEntity> userAreaDetails = repository.findUserAreaDetailsByLocation(userName, scopeName, point);
        return getAllAreaDetails(userAreaDetails, areaTypeEntry);
    }

    @Override
    public List<AreaDetails> getUserAreaDetailsById(AreaTypeEntry areaTypeEntry, String userName, String scopeName) throws ServiceException {
        UserAreasEntity userAreaDetails = repository.findUserAreaById(Long.parseLong(areaTypeEntry.getId()), userName, scopeName);
        if (userAreaDetails != null) {
            return getAllAreaDetails(newArrayList(userAreaDetails), areaTypeEntry);
        } else {
            return Collections.emptyList();
        }
    }

    private List<AreaDetails> getAllAreaDetails(List allAreas, AreaTypeEntry areaTypeEntry) {
        List<AreaDetails> areaDetailsList = new ArrayList<AreaDetails>();
        for (int i = 0; i < allAreas.size(); i++) {
            Map<String, String> properties = getFieldMap(allAreas.get(i));
            areaDetailsList.add(createAreaDetailsSpatialResponse(properties, areaTypeEntry));
        }
        return areaDetailsList;
    }

    private AreaDetails createAreaDetailsSpatialResponse(Map<String, String> properties, AreaTypeEntry areaTypeEntry) {
        List<AreaProperty> areaProperties = newArrayList();
        for (Map.Entry<String, String> entry : properties.entrySet()) {
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

    public List<UserAreaDto> searchUserAreasByCriteria(String userName, String scopeName, String searchCriteria) {
        List<UserAreaDto> userAreaDetails = repository.findUserAreaDetailsBySearchCriteria(userName, scopeName, searchCriteria);
        return userAreaDetails;
    }

    private List<UserAreaLayerDto> getUserAreaLayerMapping() {
        return repository.findUserAreaLayerMapping();
    }

    @SuppressWarnings("unchecked")
    private List<Long> getUserAreaGuid(String userName, String scopeName) {
        try {
            Map<String, String> parameters = ImmutableMap.<String, String>builder().put(USER_NAME, userName).put(SCOPE_NAME, scopeName).build();
            return repository.findEntityByNamedQuery(Long.class, QueryNameConstants.FIND_GID_BY_USER, parameters);
        } catch (ServiceException e) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
        }
    }
}
