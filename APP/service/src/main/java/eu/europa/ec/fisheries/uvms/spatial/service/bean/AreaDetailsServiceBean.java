package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.BaseAreaEntity;
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
import org.mapstruct.ap.internal.util.Collections;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Stateless
@Local(AreaDetailsService.class)
@Slf4j
public class AreaDetailsServiceBean implements AreaDetailsService {

    @EJB
    private SpatialRepository repository;

    @Override
    @Transactional
    public AreaDetails getAreaDetails(AreaDetailsSpatialRequest request) throws ServiceException {
        return getAreaDetailsById(request.getAreaType()); // FIXME calling public ejb method from self is considered bad practice and should be avoided
    }

    @Override
    @Transactional
    public AreaDetails getAreaDetailsById(AreaTypeEntry areaTypeEntry) throws ServiceException {

        AreaType areaType = areaTypeEntry.getAreaType();

        if (areaType == null) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR, StringUtils.EMPTY);
        }

        if (!StringUtils.isNumeric(areaTypeEntry.getId())) {
            throw new SpatialServiceException(SpatialServiceErrors.INVALID_ID_TYPE, areaTypeEntry.getId());
        }

        List<AreaLocationTypesEntity> areasLocationTypes =
                repository.findAreaLocationTypeByTypeName(areaTypeEntry.getAreaType().value().toUpperCase());

        if (areasLocationTypes.isEmpty()) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR, areasLocationTypes);
        }

        AreaLocationTypesEntity areaLocationTypesEntity = areasLocationTypes.get(0);

        Integer id = Integer.parseInt(areaTypeEntry.getId());

        BaseAreaEntity areaEntity = repository.findAreaByTypeAndId(areaLocationTypesEntity.getTypeName(), id.longValue());

        if (areaEntity == null) {
            throw new SpatialServiceException(SpatialServiceErrors.ENTITY_NOT_FOUND, areaLocationTypesEntity.getTypeName());
        }
        Map<String, Object> properties = areaEntity.getFieldMap();

        return createAreaDetailsSpatialResponse(properties, areaTypeEntry);

    }

    @Override
    @Transactional
    public List<AreaDetails> getAreaDetailsByLocation(AreaTypeEntry areaTypeEntry) throws ServiceException {

        AreaType areaType = areaTypeEntry.getAreaType();

        if (areaType == null) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR, StringUtils.EMPTY);
        }

        List<AreaLocationTypesEntity> areasLocationTypes =
                repository.findAreaLocationTypeByTypeName(areaTypeEntry.getAreaType().value().toUpperCase());

        if (areasLocationTypes.isEmpty()) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR, areasLocationTypes);
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
            Map<String, Object> properties = ((BaseAreaEntity)allArea).getFieldMap();
            areaDetailsList.add(createAreaDetailsSpatialResponse(properties, areaTypeEntry));
        }
        return areaDetailsList;

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
}
