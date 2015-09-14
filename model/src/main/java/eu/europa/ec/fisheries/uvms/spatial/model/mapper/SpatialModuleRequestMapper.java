package eu.europa.ec.fisheries.uvms.spatial.model.mapper;

import eu.europa.ec.fisheries.uvms.message.JAXBMarshallerUtils;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMarshallException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.JAXBException;
import java.util.List;

@Slf4j
public class SpatialModuleRequestMapper {

    public static String mapToCreateAreaByLocationRequest(PointType point) throws SpatialModelMarshallException {
        AreaByLocationSpatialRQ request = new AreaByLocationSpatialRQ();
        request.setMethod(SpatialModuleMethod.GET_AREA_BY_LOCATION);
        request.setPoint(point);
        try {
            return JAXBMarshallerUtils.marshallJaxBObjectToString(request);
        } catch (JAXBException ex) {
            SpatialModuleRequestMapper.log.error("[ Error when marshalling object to string ] {} ", ex.getMessage());
            throw new SpatialModelMarshallException("[ Error when marshalling Object to String ]", ex);
        }
    }

    public static String mapToCreateAllAreaTypesRequest() throws SpatialModelMarshallException {
        AllAreaTypesRequest request = new AllAreaTypesRequest();
        request.setMethod(SpatialModuleMethod.GET_AREA_TYPES);
        try {
            return JAXBMarshallerUtils.marshallJaxBObjectToString(request);
        } catch (JAXBException ex) {
            SpatialModuleRequestMapper.log.error("[ Error when marshalling object to string ] {} ", ex.getMessage());
            throw new SpatialModelMarshallException("[ Error when marshalling Object to String ]", ex);
        }
    }

    public static String mapToCreateClosestAreaRequest(PointType point, UnitType unit, List<AreaType> areaTypes) throws SpatialModelMarshallException {
        ClosestAreaSpatialRQ request = new ClosestAreaSpatialRQ();
        request.setMethod(SpatialModuleMethod.GET_CLOSEST_AREA);
        request.setPoint(point);
        request.setUnit(unit);
        ClosestAreaSpatialRQ.AreaTypes area = new ClosestAreaSpatialRQ.AreaTypes();
        if(areaTypes != null) {
            area.getAreaType().addAll(areaTypes);
        }
        request.setAreaTypes(area);
        try {
            return JAXBMarshallerUtils.marshallJaxBObjectToString(request);
        } catch (JAXBException ex) {
            SpatialModuleRequestMapper.log.error("[ Error when marshalling object to string ] {} ", ex.getMessage());
            throw new SpatialModelMarshallException("[ Error when marshalling Object to String ]", ex);
        }
    }

    public static String mapToCreateClosestLocationRequest(PointType point, UnitType unit, List<LocationType> locationTypes) throws SpatialModelMarshallException {
        ClosestLocationSpatialRQ request = new ClosestLocationSpatialRQ();
        request.setMethod(SpatialModuleMethod.GET_CLOSEST_LOCATION);
        request.setPoint(point);
        request.setUnit(unit);
        ClosestLocationSpatialRQ.LocationTypes loc = new ClosestLocationSpatialRQ.LocationTypes();
        if(locationTypes != null) {
            loc.getLocationType().addAll(locationTypes);
        }
        request.setLocationTypes(loc);
        try {
            return JAXBMarshallerUtils.marshallJaxBObjectToString(request);
        } catch (JAXBException ex) {
            SpatialModuleRequestMapper.log.error("[ Error when marshalling object to string ] {} ", ex.getMessage());
            throw new SpatialModelMarshallException("[ Error when marshalling Object to String ]", ex);
        }
    }

    public static String mapToCreateSpatialEnrichmentRequest(PointType point, UnitType unit, List<LocationType> locationTypes, List<AreaType> areaTypes) throws SpatialModelMarshallException {
        SpatialEnrichmentRQ request = new SpatialEnrichmentRQ();
        request.setMethod(SpatialModuleMethod.GET_ENRICHMENT);
        request.setPoint(point);
        request.setUnit(unit);
        SpatialEnrichmentRQ.LocationTypes loc = new SpatialEnrichmentRQ.LocationTypes();
        if(locationTypes != null) {
            loc.getLocationType().addAll(locationTypes);
        }
        request.setLocationTypes(loc);

        SpatialEnrichmentRQ.AreaTypes area = new SpatialEnrichmentRQ.AreaTypes();
        if(areaTypes != null) {
            area.getAreaType().addAll(areaTypes);
        }
        request.setAreaTypes(area);

        try {
            return JAXBMarshallerUtils.marshallJaxBObjectToString(request);
        } catch (JAXBException ex) {
            SpatialModuleRequestMapper.log.error("[ Error when marshalling object to string ] {} ", ex.getMessage());
            throw new SpatialModelMarshallException("[ Error when marshalling Object to String ]", ex);
        }
    }
}
