package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
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
import org.apache.commons.collections.CollectionUtils;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @EJB
    private AreaTypeNamesService areaTypeNamesService;

    @Override
    public long storeUserArea(UserAreaGeomDto userAreaDto, String userName) throws ServiceException {
        UserAreasEntity userAreasEntity = prepareNewEntity(userAreaDto, userName);
        UserAreasEntity persistedEntity = (UserAreasEntity) repository.createEntity(userAreasEntity);
        return persistedEntity.getGid();
    }

    private UserAreasEntity prepareNewEntity(UserAreaGeomDto userAreaDto, String userName) {
        UserAreasEntity userAreasEntity = UserAreaMapper.mapper().fromDtoToEntity(userAreaDto);
        userAreasEntity.setUserName(userName);
        userAreasEntity.setCreatedOn(new Date());
        return userAreasEntity;
    }

    @Override
    public long updateUserArea(UserAreaGeomDto userAreaDto, String userName) throws ServiceException {
        Long id = userAreaDto.getId();
        validateGid(id);

        List<UserAreasEntity> persistentUserAreas = repository.findUserAreaById(id, userName);
        validateNotNull(id, persistentUserAreas);

        UserAreasEntity persistentUserArea = persistentUserAreas.get(0);

        UserAreasEntity userAreasEntityToUpdate = prepareNewEntity(userAreaDto, userName);
        userAreasEntityToUpdate.setCreatedOn(persistentUserArea.getCreatedOn());
        userAreasEntityToUpdate.setGid(persistentUserArea.getGid());

        UserAreasEntity persistedUpdatedEntity = (UserAreasEntity) repository.updateEntity(userAreasEntityToUpdate);
        return persistedUpdatedEntity.getGid();
    }

    private void validateGid(Long gid) {
        if (gid == null) {
            throw new SpatialServiceException(SpatialServiceErrors.MISSING_USER_AREA_ID);
        }
    }

    @Override
    public void deleteUserArea(Long userAreaId, String userName) throws ServiceException {
        List<UserAreasEntity> persistentUserAreas = repository.findUserAreaById(userAreaId, userName);
        validateNotNull(userAreaId, persistentUserAreas);

        repository.deleteEntity(persistentUserAreas.get(0));
    }

    @Override
    public List<String> getUserAreaTypes(String userName) throws ServiceException {
        return repository.getUserAreaTypes(userName);
    }

    private void validateNotNull(Long userAreaId, List<UserAreasEntity> persistentUserAreas) {
        if (CollectionUtils.isEmpty(persistentUserAreas)) {
            throw new SpatialServiceException(SpatialServiceErrors.USER_AREA_DOES_NOT_EXIST, userAreaId);
        }
    }

    @Override
    public UserAreaLayerDto getUserAreaLayerDefination(String userName, String scopeName) {
        List<UserAreaLayerDto> userAreaLayerDtoList = areaTypeNamesService.listUserAreaLayerMapping();
        UserAreaLayerDto userAreaLayerDto = userAreaLayerDtoList.get(0);
        userAreaLayerDto.setIdList(getUserAreaGuid(userName, scopeName));
        return userAreaLayerDto;
    }

    @Override
    public List<UserAreaDto> getUserAreaDetailsWithExtentByLocation(Coordinate coordinate, String userName) {
        Point point = convertToPointInWGS84(coordinate.getLongitude(), coordinate.getLatitude(), coordinate.getCrs());
        List<UserAreaDto> userAreaDetails = repository.findUserAreaDetailsWithExtentByLocation(userName, point);
        return userAreaDetails;
    }

    @Override
    public List<AreaDetails> getUserAreaDetailsByLocation(AreaTypeEntry areaTypeEntry, String userName) {
        Point point = convertToPointInWGS84(areaTypeEntry.getLongitude(), areaTypeEntry.getLatitude(), areaTypeEntry.getCrs());
        List<UserAreasEntity> userAreaDetails = repository.findUserAreaDetailsByLocation(userName, point);
        return getAllAreaDetails(userAreaDetails, areaTypeEntry);
    }

    @Override
    public AreaDetails getUserAreaDetailsWithExtentById(AreaTypeEntry areaTypeEntry, String userName) throws ServiceException {
        List<UserAreasEntity> userAreasDetails = repository.findUserAreaById(Long.parseLong(areaTypeEntry.getId()), userName);
        if (CollectionUtils.isNotEmpty(userAreasDetails)) {
            return getAllAreaDetails(userAreasDetails, areaTypeEntry).get(0);
        } else {
            AreaDetails areaDetails = new AreaDetails();
            areaDetails.setAreaType(areaTypeEntry);
            return areaDetails;
        }
    }

    @Override
    public List<AreaDetails> getUserAreaDetailsById(AreaTypeEntry areaTypeEntry, String userName) throws ServiceException {
        List<UserAreasEntity> userAreaDetails = repository.findUserAreaById(Long.parseLong(areaTypeEntry.getId()), userName);
        return getAllAreaDetails(userAreaDetails, areaTypeEntry);
    }

    private List<AreaDetails> getAllAreaDetails(List allAreas, AreaTypeEntry areaTypeEntry) {
        List<AreaDetails> areaDetailsList = Lists.newArrayList();
        for (int i = 0; i < allAreas.size(); i++) {
            Map<String, Object> properties = getFieldMap(allAreas.get(i));
            areaDetailsList.add(createAreaDetailsSpatialResponse(properties, areaTypeEntry));
        }
        return areaDetailsList;
    }

    private AreaDetails createAreaDetailsSpatialResponse(Map<String, Object> properties, AreaTypeEntry areaTypeEntry) {
        List<AreaProperty> areaProperties = newArrayList();
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

    public List<UserAreaDto> searchUserAreasByCriteria(String userName, String scopeName, String searchCriteria) {
        List<UserAreaDto> userAreaDetails = repository.findUserAreaDetailsBySearchCriteria(userName, scopeName, searchCriteria);
        return userAreaDetails;
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
