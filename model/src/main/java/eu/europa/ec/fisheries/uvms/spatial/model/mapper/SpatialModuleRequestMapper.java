package eu.europa.ec.fisheries.uvms.spatial.model.mapper;

import eu.europa.ec.fisheries.uvms.message.AbstractJAXBMarshaller;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMarshallException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AllAreaTypesRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaByLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.FilterAreasSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.PointType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ScopeAreasType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialEnrichmentRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialModuleMethod;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.UnitType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.UserAreasType;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.JAXBException;
import java.util.List;

@Slf4j
public class SpatialModuleRequestMapper extends AbstractJAXBMarshaller {

    public String mapToCreateAreaByLocationRequest(PointType point) throws SpatialModelMarshallException {
        AreaByLocationSpatialRQ request = new AreaByLocationSpatialRQ();
        request.setMethod(SpatialModuleMethod.GET_AREA_BY_LOCATION);
        request.setPoint(point);
        try {
            return marshallJaxBObjectToString(request);
        } catch (JAXBException ex) {
            SpatialModuleRequestMapper.log.error("[ Error when marshalling object to string ] {} ", ex.getMessage());
            throw new SpatialModelMarshallException("[ Error when marshalling Object to String ]", ex);
        }
    }

    public String mapToCreateAllAreaTypesRequest() throws SpatialModelMarshallException {
        AllAreaTypesRequest request = new AllAreaTypesRequest();
        request.setMethod(SpatialModuleMethod.GET_AREA_TYPES);
        try {
            return marshallJaxBObjectToString(request);
        } catch (JAXBException ex) {
            SpatialModuleRequestMapper.log.error("[ Error when marshalling object to string ] {} ", ex.getMessage());
            throw new SpatialModelMarshallException("[ Error when marshalling Object to String ]", ex);
        }
    }

    public String mapToCreateClosestAreaRequest(PointType point, UnitType unit, List<AreaType> areaTypes) throws SpatialModelMarshallException {
        ClosestAreaSpatialRQ request = new ClosestAreaSpatialRQ();
        request.setMethod(SpatialModuleMethod.GET_CLOSEST_AREA);
        request.setPoint(point);
        request.setUnit(unit);
        ClosestAreaSpatialRQ.AreaTypes area = new ClosestAreaSpatialRQ.AreaTypes();
        if(areaTypes != null) {
            area.getAreaTypes().addAll(areaTypes);
        }
        request.setAreaTypes(area);
        try {
            return marshallJaxBObjectToString(request);
        } catch (JAXBException ex) {
            SpatialModuleRequestMapper.log.error("[ Error when marshalling object to string ] {} ", ex.getMessage());
            throw new SpatialModelMarshallException("[ Error when marshalling Object to String ]", ex);
        }
    }

    public String mapToCreateClosestLocationRequest(PointType point, UnitType unit, List<LocationType> locationTypes) throws SpatialModelMarshallException {
        ClosestLocationSpatialRQ request = new ClosestLocationSpatialRQ();
        request.setMethod(SpatialModuleMethod.GET_CLOSEST_LOCATION);
        request.setPoint(point);
        request.setUnit(unit);
        ClosestLocationSpatialRQ.LocationTypes loc = new ClosestLocationSpatialRQ.LocationTypes();
        if(locationTypes != null) {
            loc.getLocationTypes().addAll(locationTypes);
        }
        request.setLocationTypes(loc);
        try {
            return marshallJaxBObjectToString(request);
        } catch (JAXBException ex) {
            SpatialModuleRequestMapper.log.error("[ Error when marshalling object to string ] {} ", ex.getMessage());
            throw new SpatialModelMarshallException("[ Error when marshalling Object to String ]", ex);
        }
    }

    public String mapToCreateSpatialEnrichmentRequest(PointType point, UnitType unit, List<LocationType> locationTypes, List<AreaType> areaTypes) throws SpatialModelMarshallException {
        SpatialEnrichmentRQ request = new SpatialEnrichmentRQ();
        request.setMethod(SpatialModuleMethod.GET_ENRICHMENT);
        request.setPoint(point);
        request.setUnit(unit);
        SpatialEnrichmentRQ.LocationTypes loc = new SpatialEnrichmentRQ.LocationTypes();
        if(locationTypes != null) {
            loc.getLocationTypes().addAll(locationTypes);
        }
        request.setLocationTypes(loc);

        SpatialEnrichmentRQ.AreaTypes area = new SpatialEnrichmentRQ.AreaTypes();
        if(areaTypes != null) {
            area.getAreaTypes().addAll(areaTypes);
        }
        request.setAreaTypes(area);

        try {
            return marshallJaxBObjectToString(request);
        } catch (JAXBException ex) {
            SpatialModuleRequestMapper.log.error("[ Error when marshalling object to string ] {} ", ex.getMessage());
            throw new SpatialModelMarshallException("[ Error when marshalling Object to String ]", ex);
        }
    }
    
    public String mapToFilterAreaSpatialRequest(List<AreaIdentifierType> scopeAreaList, List<AreaIdentifierType> userAreaList) throws SpatialModelMarshallException {
    	try {
        	FilterAreasSpatialRQ request = new FilterAreasSpatialRQ();
        	ScopeAreasType scopeAreas = new ScopeAreasType();
        	UserAreasType userAreas = new UserAreasType();
        	scopeAreas.getScopeAreas().addAll(scopeAreaList); // Set scope areas received
        	userAreas.getUserAreas().addAll(userAreaList); // Set user areas received
        	request.setMethod(SpatialModuleMethod.GET_FILTERED_AREA);
        	request.setScopeAreas(scopeAreas);
        	request.setUserAreas(userAreas);
        	return marshallJaxBObjectToString(request);
    	} catch (JAXBException ex) {
            SpatialModuleRequestMapper.log.error("[ Error when marshalling object to string ] {} ", ex.getMessage());
            throw new SpatialModelMarshallException("[ Error when marshalling Object to String ]", ex);
        }     	
    }
}
