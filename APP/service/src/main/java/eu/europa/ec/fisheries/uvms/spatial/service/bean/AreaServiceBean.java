package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.Maps;
import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.*;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaProperty;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.EezDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.PortAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.PortLocationDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.RfmoDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.EezMapper;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.PortAreaMapper;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.PortLocationMapper;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.RfmoMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opengis.feature.Property;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
@Local(AreaService.class)
@Slf4j
public class AreaServiceBean implements AreaService {

    private static final String CODE = "code";
    private static final String NAME = "name";

    private @EJB SpatialRepository repository;

    @Override
    public Map<String, String> getAllCountriesDesc() {
        Map<String, String> countries = new HashMap<String, String>();
        List<Map<String, String>> countryList = repository.findAllCountriesDesc();
        for (Map<String, String> country : countryList) {
            countries.put(country.get(CODE), country.get(NAME));
        }
        return countries;
    }

    @Override
    @Transactional
    public void replaceEezArea(Map<String, List<Property>> features) {
        try {
            repository.disableAllEezAreas();

            Date enabledOn = new Date();
            for (List<Property> properties : features.values()) {
                Map<String, Object> values = createAttributesMap(properties);

                EezDto eezDto = new EezDto();
                eezDto.setName(readStringProperty(values, "name"));
                eezDto.setCountry(readStringProperty(values, "country"));
                eezDto.setSovereign(readStringProperty(values, "sovereign"));
                eezDto.setRemarks(readStringProperty(values, "remarks"));
                eezDto.setSovId((Long) values.get("sov_id"));
                eezDto.setEezId((Long) values.get("eez_id"));
                eezDto.setCode(readStringProperty(values, "code"));
                eezDto.setMrgid(BigInteger.valueOf(((Double) values.get("mrgid")).longValue()));
                eezDto.setDateChang(readStringProperty(values, "date_chang"));
                eezDto.setAreaM2((Double) values.get("area_m2"));
                eezDto.setLongitude((Double) values.get("longitude"));
                eezDto.setLatitude((Double) values.get("latitude"));
                eezDto.setMrgidEez((Long) values.get("mrgid_eez"));
                eezDto.setGeometry((Geometry) values.get("the_geom"));
                eezDto.setEnabledOn(enabledOn);
                eezDto.setEnabled(true);

                EezEntity eezEntity = EezMapper.INSTANCE.eezDtoToEezEntity(eezDto);
                repository.createEntity(eezEntity);
            }
        } catch (Exception e) {
            throw new SpatialServiceException(SpatialServiceErrors.INVALID_UPLOAD_AREA_DATA, e);
        }
    }

    @Override
    @Transactional
    public void replaceRfmo(Map<String, List<Property>> features) {
        try {
            repository.disableAllRfmoAreas();

            Date enabledOn = new Date();
            for (List<Property> properties : features.values()) {
                Map<String, Object> values = createAttributesMap(properties);

                RfmoDto rfmoDto = new RfmoDto();
                rfmoDto.setGeometry((Geometry) values.get("the_geom"));
                rfmoDto.setCode(readStringProperty(values, "code"));
                rfmoDto.setName(readStringProperty(values, "name"));
                rfmoDto.setTuna(readStringProperty(values, "tuna"));
                rfmoDto.setEnabled(true);
                rfmoDto.setEnabledOn(enabledOn);

                RfmoEntity rfmoEntity = RfmoMapper.INSTANCE.rfmoDtoToRfmoEntity(rfmoDto);
                repository.create(rfmoEntity);
            }
        } catch (Exception e) {
            throw new SpatialServiceException(SpatialServiceErrors.INVALID_UPLOAD_AREA_DATA, e);
        }
    }

    @Override
    @Transactional
    public void replacePort(Map<String, List<Property>> features) {
        try {
            repository.disableAllPortLocations();

            Date enabledOn = new Date();
            for (List<Property> properties : features.values()) {
                Map<String, Object> values = createAttributesMap(properties);
                PortLocationDto portLocationDto = new PortLocationDto();
                portLocationDto.setGeometry((Geometry) values.get("the_geom"));
                portLocationDto.setCode(readStringProperty(values, "code"));
                portLocationDto.setName(readStringProperty(values, "name"));
                portLocationDto.setCountryCode(readStringProperty(values, "country_co"));
                portLocationDto.setFishingPort(readStringProperty(values, "fishing_po"));
                portLocationDto.setLandingPlace(readStringProperty(values, "landing_pl"));
                portLocationDto.setCommercialPort(readStringProperty(values, "commercial"));
                portLocationDto.setEnabled(true);
                portLocationDto.setEnabledOn(enabledOn);

                PortEntity portsEntity = PortLocationMapper.INSTANCE.portLocationDtoToPortsEntity(portLocationDto);
                repository.createEntity(portsEntity);
            }
        } catch (Exception e) {
            throw new SpatialServiceException(SpatialServiceErrors.INVALID_UPLOAD_AREA_DATA, e);
        }
    }

    @Override
    @Transactional
    public void replacePortArea(Map<String, List<Property>> features) {
        try {
            repository.disableAllPortAreas();
            Date enabledOn = new Date();
            for (List<Property> properties : features.values()) {
                Map<String, Object> values = createAttributesMap(properties);
                PortAreaDto portAreaDto = new PortAreaDto();
                portAreaDto.setGeometry((Geometry) values.get("the_geom"));
                portAreaDto.setCode(readStringProperty(values, "code"));
                portAreaDto.setName(readStringProperty(values, "name"));
                portAreaDto.setEnabled(true);
                portAreaDto.setEnabledOn(enabledOn);

                PortAreasEntity portAreasEntity = PortAreaMapper.INSTANCE.portAreaDtoToPortAreasEntity(portAreaDto);
                repository.createEntity(portAreasEntity);
            }
        } catch (Exception e) {
            throw new SpatialServiceException(SpatialServiceErrors.INVALID_UPLOAD_AREA_DATA, e);
        }
    }

    private Map<String, Object> createAttributesMap(List<Property> properties) {
        Map<String, Object> resultMap = Maps.newHashMap();
        for (Property property : properties) {
            String name = property.getName().toString();
            Object value = property.getValue();
            resultMap.put(name, value);
        }
        return resultMap;
    }

    private String readStringProperty(Map<String, Object> values, String propertyName) throws UnsupportedEncodingException {
        return new String(((String) values.get(propertyName)).getBytes("ISO-8859-1"), "UTF-8");
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
