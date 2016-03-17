package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetailsSpatialRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaProperty;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.mapstruct.ap.internal.util.Collections;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static eu.europa.ec.fisheries.uvms.spatial.util.ColumnAliasNameHelper.getFieldMap;
import static eu.europa.ec.fisheries.uvms.spatial.util.SpatialTypeEnum.getEntityClassByType;

@Stateless
@Local(AreaDetailsService.class)
@Slf4j
public class AreaDetailsServiceBean implements AreaDetailsService {

    protected static final String TYPE_NAME = "typeName";

    @EJB
    private SpatialRepository repository;

    @Override
    @Transactional
    public AreaDetails getAreaDetails(AreaDetailsSpatialRequest request) throws ServiceException {
        return getAreaDetailsById(request.getAreaType()); // FIXME calling another transactional method is bad practice
    }

    @Override
    @Transactional
    public AreaDetails getAreaDetailsById(AreaTypeEntry areaTypeEntry) throws ServiceException {

        AreaType areaType = areaTypeEntry.getAreaType();

        if (areaType == null) {
            throw new SpatialServiceException(SpatialServiceErrors.INVALID_AREA_LOCATION_TYPE, StringUtils.EMPTY);
        }

        if (!StringUtils.isNumeric(areaTypeEntry.getId())) {
            throw new SpatialServiceException(SpatialServiceErrors.INVALID_ID_TYPE, areaTypeEntry.getId());
        }

        Map<String, String> parameters = ImmutableMap.<String, String>builder().put(TYPE_NAME, areaType.value().toUpperCase()).build();
        List<AreaLocationTypesEntity> areasLocationTypes = repository.findEntityByNamedQuery(AreaLocationTypesEntity.class, AreaLocationTypesEntity.FIND_TYPE_BY_NAME, parameters, 1); // FIXME greg use daa

        if (areasLocationTypes.isEmpty()) {
            throw new SpatialServiceException(SpatialServiceErrors.INVALID_AREA_LOCATION_TYPE, areasLocationTypes);
        }

        AreaLocationTypesEntity areaLocationTypesEntity = areasLocationTypes.get(0);

        Integer id = Integer.parseInt(areaTypeEntry.getId());

        Object object = repository.findEntityById(getEntityClassByType(areaLocationTypesEntity.getTypeName()), id.longValue());

        if (object == null) {
            throw new SpatialServiceException(SpatialServiceErrors.ENTITY_NOT_FOUND, areaLocationTypesEntity.getTypeName());
        }
        Map<String, Object> properties = getFieldMap(object);

        return createAreaDetailsSpatialResponse(properties, areaTypeEntry);

    }

    @Override
    @Transactional
    public List<AreaDetails> getAreaDetailsByLocation(AreaTypeEntry areaTypeEntry) throws ServiceException {

        AreaType areaType = areaTypeEntry.getAreaType();

        if (areaType == null) {
            throw new SpatialServiceException(SpatialServiceErrors.INVALID_AREA_LOCATION_TYPE, StringUtils.EMPTY);
        }

        Map<String, String> parameters = ImmutableMap.<String, String>builder().put(TYPE_NAME, areaTypeEntry.getAreaType().value().toUpperCase()).build();
        List<AreaLocationTypesEntity> areasLocationTypes = repository.findEntityByNamedQuery(AreaLocationTypesEntity.class, AreaLocationTypesEntity.FIND_TYPE_BY_NAME, parameters, 1); // FIXME greg replace by dao

        if (areasLocationTypes.isEmpty()) {
            throw new SpatialServiceException(SpatialServiceErrors.INVALID_AREA_LOCATION_TYPE, areasLocationTypes);
        }

        AreaLocationTypesEntity areaLocationTypesEntity = areasLocationTypes.get(0);

        Point point = SpatialUtils.convertToPointInWGS84(areaTypeEntry.getLongitude(), areaTypeEntry.getLatitude(), areaTypeEntry.getCrs());

        List allAreas = Collections.newArrayList();

        switch (areaLocationTypesEntity.getTypeName().toUpperCase()){
            case "EEZ" :
                allAreas = repository.findEezByIntersect(point);
                break;
            case "PORTAREA" :
                allAreas = repository.findPortAreaByIntersect(point);
                break;
            case "RFMO" :
                allAreas = repository.findRfmoByIntersect(point);
                break;
            case "USERAREA" :
                allAreas = repository.findUserAreaByIntersect(point);
                break;
            case "PORT" :
                allAreas = repository.findUserAreaByIntersect(point);
                break;
            default:
                break;
        }
        // more areas here

        List<AreaDetails> areaDetailsList = new ArrayList<>();

        for (Object allArea : allAreas) {
            Map<String, Object> properties = getFieldMap(allArea);
            areaDetailsList.add(createAreaDetailsSpatialResponse(properties, areaTypeEntry));
        }
        return areaDetailsList;

    }

    private AreaDetails createAreaDetailsSpatialResponse(Map<String, Object> properties, AreaTypeEntry areaTypeEntry) {
        List<AreaProperty> areaProperties = Lists.newArrayList();
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
}
