package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.rest.security.bean.USMService;
import eu.europa.ec.fisheries.uvms.spatial.entity.UserAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.UserScopeEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.constants.USMSpatial;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaProperty;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Coordinate;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.UserAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.geojson.UserAreaGeoJsonDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.UserAreaMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.geotools.geometry.jts.WKTReader2;
import org.geotools.geometry.jts.WKTWriter2;
import org.hibernate.SQLQuery;
import org.hibernate.spatial.GeometryType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static eu.europa.ec.fisheries.uvms.spatial.util.ColumnAliasNameHelper.getFieldMap;
import static eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialUtils.convertToPointInWGS84;

@Stateless
@Local(UserAreaService.class)
@Transactional
@Slf4j
public class UserAreaServiceBean implements UserAreaService {

    private @PersistenceContext(unitName = "spatialPU") EntityManager em;
    private @EJB SpatialRepository repository;
    private @EJB AreaTypeNamesService areaTypeNamesService;
    private @EJB USMService usmService;

    @Override
    public long storeUserArea(UserAreaGeoJsonDto userAreaDto, String userName) throws ServiceException {

        UserAreasEntity userAreasEntity = UserAreaMapper.mapper().fromDtoToEntity(userAreaDto);
        userAreasEntity.setUserName(userName);
        userAreasEntity.setCreatedOn(new Date());

        UserAreasEntity persistedEntity = (UserAreasEntity) repository.createEntity(userAreasEntity);

        if (StringUtils.isNotBlank(persistedEntity.getDatasetName())) {
            usmService.createDataset(USMSpatial.APPLICATION_NAME, persistedEntity.getDatasetName(), createDescriminator(persistedEntity), USMSpatial.USM_DATASET_CATEGORY, USMSpatial.USM_DATASET_DESCRIPTION);
        }
        return persistedEntity.getGid();
    }

    @Override
    public long updateUserArea(UserAreaGeoJsonDto userAreaDto, String userName, String scopeName) throws ServiceException {
        return updateUserArea(userAreaDto, userName, false, scopeName);
    }

    @Override
    public void deleteUserArea(Long userAreaId, String userName, String scopeName) throws ServiceException {
        deleteUserArea(userAreaId, userName, false, scopeName);
    }

    private String createDescriminator(UserAreasEntity persistedEntity) {
        return AreaType.USERAREA.value() + USMSpatial.DELIMITER + persistedEntity.getGid();
    }

    @Override
    public long updateUserArea(UserAreaGeoJsonDto userAreaDto, String userName, boolean isPowerUser, String scopeName) throws ServiceException {
        Long id = userAreaDto.getId();
        validateGid(id);

        List<UserAreasEntity> persistentUserAreas = repository.findUserAreaById(id, userName, isPowerUser, scopeName);
        validateNotNull(id, persistentUserAreas);

        UserAreasEntity persistentUserArea = persistentUserAreas.get(0);

        if (!persistentUserArea.getUserName().equals(userName) && !isPowerUser) {
            throw new ServiceException("user_not_authorised");
        }

        if (StringUtils.isNotBlank(userAreaDto.getDatasetName()) && !userAreaDto.getDatasetName().equals(persistentUserArea.getDatasetName())) {
            updateUSMDataset(persistentUserArea, userAreaDto.getDatasetName());
        }

        UserAreasEntity userAreasEntityToUpdate = UserAreaMapper.mapper().fromDtoToEntity(userAreaDto);
        userAreasEntityToUpdate.setUserName(userName);
        userAreasEntityToUpdate.setCreatedOn(new Date());

        userAreasEntityToUpdate.setCreatedOn(persistentUserArea.getCreatedOn());
        userAreasEntityToUpdate.setGid(persistentUserArea.getGid());
        userAreasEntityToUpdate.setScopeSelection(createScopeSelection(userAreaDto, persistentUserArea));

        UserAreasEntity persistedUpdatedEntity = (UserAreasEntity) repository.updateEntity(userAreasEntityToUpdate);

        return persistedUpdatedEntity.getGid();
    }

    //BUG FIX: Caused by: org.hibernate.HibernateException: A collection with cascade="all-delete-orphan" was no longer referenced by the owning entity instance: eu.europa.ec.fisheries.uvms.spatial.entity.UserAreasEntity.scopeSelection
    private Set<UserScopeEntity> createScopeSelection(UserAreaGeoJsonDto userAreaDto, UserAreasEntity persistentUserArea) {
        Set<UserScopeEntity> scopeSelection = persistentUserArea.getScopeSelection();
        scopeSelection.clear();
        scopeSelection.addAll(UserAreaMapper.fromScopeArrayToEntity(userAreaDto.getScopeSelection()));
        return scopeSelection;
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

    private void validateGid(Long gid) {
        if (gid == null) {
            throw new SpatialServiceException(SpatialServiceErrors.MISSING_USER_AREA_ID);
        }
    }

    @Override
    public void deleteUserArea(Long userAreaId, String userName, boolean isPowerUser, String scopeName) throws ServiceException {
        List<UserAreasEntity> persistentUserAreas = repository.findUserAreaById(userAreaId, userName, isPowerUser, scopeName);
        validateNotNull(userAreaId, persistentUserAreas);

        if (!persistentUserAreas.get(0).getUserName().equals(userName) && !isPowerUser) {
            throw new ServiceException("user_not_authorised");
        }

        repository.deleteEntity(persistentUserAreas.get(0));
    }

    @Override
    public List<String> getUserAreaTypes(String userName, String scopeName, boolean isPowerUser) throws ServiceException {
        return repository.getUserAreaTypes(userName, scopeName, isPowerUser);
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

        List<UserAreasEntity> userAreaDetailsWithExtentByLocation = repository.findUserAreaDetailsByLocation(userName, point);

        List<UserAreaDto> userAreaDtos = newArrayList();
        for (UserAreasEntity userAreaDetails : userAreaDetailsWithExtentByLocation){
            UserAreaDto userAreaDto = new UserAreaDto();
            userAreaDto.setGid(userAreaDetails.getGid());
            userAreaDto.setDesc(userAreaDetails.getAreaDesc());
            userAreaDto.setExtent(new WKTWriter2().write(userAreaDetails.getGeom().getEnvelope()));
            userAreaDto.setName(userAreaDetails.getName());
            userAreaDto.setAreaType(userAreaDetails.getType());
            userAreaDtos.add(userAreaDto);
        }

        return userAreaDtos;
    }

    @Override
    public List<AreaDetails> getUserAreaDetailsByLocation(AreaTypeEntry areaTypeEntry, String userName) throws ServiceException {
        Point point = convertToPointInWGS84(areaTypeEntry.getLongitude(), areaTypeEntry.getLatitude(), areaTypeEntry.getCrs());
        List<UserAreasEntity> userAreaDetails = repository.findUserAreaDetailsByLocation(userName, point);
        try {
            List<AreaDetails> areaDetailsList = Lists.newArrayList();
            for (int i = 0; i < userAreaDetails.size(); i++) {
                Map<String, Object> properties = getFieldMap(userAreaDetails.get(i));
                addCentroidToProperties(properties);
                areaDetailsList.add(createAreaDetailsSpatialResponse(properties, areaTypeEntry));
            }
            return areaDetailsList;

        } catch (ParseException e) {
            String error = "Error while trying to parse geometry";
            log.error(error);
            throw new ServiceException(error);
        }
    }

    @Override
    public List<AreaDetails> getUserAreaDetailsWithExtentById(AreaTypeEntry areaTypeEntry, String userName, String scopeName) throws ServiceException {
        return getUserAreaDetailsWithExtentById(areaTypeEntry, userName, false, scopeName);
    }

    @Override
    public List<AreaDetails> getUserAreaDetailsById(AreaTypeEntry areaTypeEntry, String userName, String scopeName) throws ServiceException {
        return getUserAreaDetailsById(areaTypeEntry, userName, false, scopeName);
    }

    @Override
    public List<AreaDetails> getUserAreaDetailsWithExtentById(AreaTypeEntry areaTypeEntry, String userName, boolean isPowerUser, String scopeName) throws ServiceException {
        List<UserAreasEntity> userAreasDetails = repository.findUserAreaById(Long.parseLong(areaTypeEntry.getId()), userName, isPowerUser, scopeName);
        try {
            if (CollectionUtils.isNotEmpty(userAreasDetails)) {

                List<AreaDetails> areaDetailsList = Lists.newArrayList();
                for (int i = 0; i < userAreasDetails.size(); i++) {
                    Map<String, Object> properties = getFieldMap(userAreasDetails.get(i));
                    addCentroidToProperties(properties);
                    areaDetailsList.add(createAreaDetailsSpatialResponse(properties, areaTypeEntry));
                }
                return areaDetailsList;

            } else {
                AreaDetails areaDetails = new AreaDetails();
                areaDetails.setAreaType(areaTypeEntry);
                return Arrays.asList(areaDetails);
            }
        } catch (ParseException e) {
            String error = "Error while trying to parse geometry";
            log.error(error);
            throw new ServiceException(error);
        }
    }

    @Override
    public List<AreaDetails> getUserAreaDetailsById(AreaTypeEntry areaTypeEntry, String userName, boolean isPowerUser, String scopeName) throws ServiceException {
        List<UserAreasEntity> userAreaDetails = repository.findUserAreaById(Long.parseLong(areaTypeEntry.getId()), userName, isPowerUser, scopeName);
        try {

            List<AreaDetails> areaDetailsList = Lists.newArrayList();
            for (int i = 0; i < userAreaDetails.size(); i++) {
                Map<String, Object> properties = getFieldMap(userAreaDetails.get(i));
                addCentroidToProperties(properties);
                areaDetailsList.add(createAreaDetailsSpatialResponse(properties, areaTypeEntry));
            }
            return areaDetailsList;

        } catch (ParseException e) {
            String error = "Error while trying to parse geometry";
            log.error(error);
            throw new ServiceException(error);
        }
    }

    private void addCentroidToProperties(Map<String, Object> properties) throws ParseException {
        Object geometry = properties.get("geometry");
        if(geometry != null){
            Geometry centroid = new WKTReader2().read(String.valueOf(geometry)).getCentroid();
            properties.put("centroid", new WKTWriter2().write(centroid));
        }
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

    @SuppressWarnings("unchecked")
    private List<Long> getUserAreaGuid(String userName, String scopeName) {
        try {
            Map<String, String> parameters = ImmutableMap.<String, String>builder().put(USMSpatial.USER_NAME, userName).put(USMSpatial.SCOPE_NAME, scopeName).build();
            return repository.findEntityByNamedQuery(Long.class, QueryNameConstants.FIND_GID_BY_USER, parameters);
        } catch (ServiceException e) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
        }
    }

    @Override
    public List<UserAreaDto> searchUserAreasByCriteria(String userName, String scopeName, String searchCriteria, boolean isPowerUser) throws ServiceException {

        final String queryString = "SELECT gid, name, area_desc, geom FROM spatial.user_areas area LEFT JOIN spatial.user_scope scopeSelection"
                + " ON area.gid = scopeSelection.user_area_id"
                + " WHERE ((1 = " + (isPowerUser ? 1 : 0) + ") OR (area.user_name = '" + userName + "' OR scopeSelection.scope_name = '" + scopeName + "'))"
                + " AND (UPPER(area.name) LIKE(UPPER('%" + searchCriteria + "%')) OR UPPER(area.area_desc) LIKE(UPPER('%" + searchCriteria + "%'))) group by area.gid";// TODO Move to DAO

        List<UserAreaDto> userAreaDtos = new ArrayList<>();

        final Query emNativeQuery = em.createNativeQuery(queryString);

        emNativeQuery.unwrap(SQLQuery.class)
                .addScalar("gid", IntegerType.INSTANCE)
                .addScalar("name", StringType.INSTANCE)
                .addScalar("area_desc", StringType.INSTANCE)
                .addScalar("geom", GeometryType.INSTANCE);

        final List records = emNativeQuery.getResultList();
        Iterator it = records.iterator();

        WKTWriter2 wktWriter2 = new WKTWriter2();
        while (it.hasNext( )) {
            final Object[] result = (Object[])it.next();
            it.remove(); // avoids a ConcurrentModificationException
            final Geometry envelope = ((Geometry) result[3]).getEnvelope();
            String desc = (String) result[2];
            userAreaDtos.add(new UserAreaDto(Integer.valueOf(String.valueOf(result[0])), String.valueOf(result[1]),
                            StringUtils.isNotBlank(desc) ? desc : StringUtils.EMPTY, wktWriter2.write(envelope)));
        }

        return userAreaDtos;

    }

    @Override
    public List<UserAreaGeoJsonDto> searchUserAreasByType(String userName, String scopeName, String type, boolean isPowerUser) throws ServiceException {
        List<UserAreasEntity> userAreas = repository.findUserAreasByType(userName, scopeName, type, isPowerUser);
        return  UserAreaMapper.mapper().fromEntityListToDtoList(userAreas, false);
    }
}
