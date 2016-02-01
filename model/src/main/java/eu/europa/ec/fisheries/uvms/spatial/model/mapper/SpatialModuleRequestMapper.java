package eu.europa.ec.fisheries.uvms.spatial.model.mapper;

import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMarshallException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public final class SpatialModuleRequestMapper {

    final static Logger LOG = LoggerFactory.getLogger(JAXBMarshaller.class);

    private SpatialModuleRequestMapper() {
    }

    public static String mapToCreateAreaByLocationRequest(PointType point) throws SpatialModelMarshallException {
        AreaByLocationSpatialRQ request = new AreaByLocationSpatialRQ();
        request.setMethod(SpatialModuleMethod.GET_AREA_BY_LOCATION);
        request.setPoint(point);
        try {
            return JAXBMarshaller.marshall(request);
        } catch (SpatialModelMarshallException ex) {
            return logException(ex);
        }
    }

    public static String mapToCreateAllAreaTypesRequest() throws SpatialModelMarshallException {
        AllAreaTypesRequest request = new AllAreaTypesRequest();
        request.setMethod(SpatialModuleMethod.GET_AREA_TYPES);
        try {
            return JAXBMarshaller.marshall(request);
        } catch (SpatialModelMarshallException ex) {
            return logException(ex);
        }
    }

    public static String mapToCreateClosestAreaRequest(PointType point, UnitType unit, List<AreaType> areaTypes) throws SpatialModelMarshallException {
        ClosestAreaSpatialRQ request = new ClosestAreaSpatialRQ();
        request.setMethod(SpatialModuleMethod.GET_CLOSEST_AREA);
        request.setPoint(point);
        request.setUnit(unit);
        ClosestAreaSpatialRQ.AreaTypes area = new ClosestAreaSpatialRQ.AreaTypes();
        if (areaTypes != null) {
            area.getAreaTypes().addAll(areaTypes);
        }
        request.setAreaTypes(area);
        try {
            return JAXBMarshaller.marshall(request);
        } catch (SpatialModelMarshallException ex) {
            return logException(ex);
        }
    }

    public static String mapToCreateClosestLocationRequest(PointType point, UnitType unit, List<LocationType> locationTypes) throws SpatialModelMarshallException {
        ClosestLocationSpatialRQ request = new ClosestLocationSpatialRQ();
        request.setMethod(SpatialModuleMethod.GET_CLOSEST_LOCATION);
        request.setPoint(point);
        request.setUnit(unit);
        ClosestLocationSpatialRQ.LocationTypes loc = new ClosestLocationSpatialRQ.LocationTypes();
        if (locationTypes != null) {
            loc.getLocationTypes().addAll(locationTypes);
        }
        request.setLocationTypes(loc);
        try {
            return JAXBMarshaller.marshall(request);
        } catch (SpatialModelMarshallException ex) {
            return logException(ex);
        }
    }

    public static String mapToCreateSpatialEnrichmentRequest(PointType point, UnitType unit, List<LocationType> locationTypes, List<AreaType> areaTypes) throws SpatialModelMarshallException {
        SpatialEnrichmentRQ request = new SpatialEnrichmentRQ();
        request.setMethod(SpatialModuleMethod.GET_ENRICHMENT);
        request.setPoint(point);
        request.setUnit(unit);
        SpatialEnrichmentRQ.LocationTypes loc = new SpatialEnrichmentRQ.LocationTypes();
        if (locationTypes != null) {
            loc.getLocationTypes().addAll(locationTypes);
        }
        request.setLocationTypes(loc);

        SpatialEnrichmentRQ.AreaTypes area = new SpatialEnrichmentRQ.AreaTypes();
        if (areaTypes != null) {
            area.getAreaTypes().addAll(areaTypes);
        }
        request.setAreaTypes(area);

        try {
            return JAXBMarshaller.marshall(request);
        } catch (SpatialModelMarshallException ex) {
            return logException(ex);
        }
    }

    public static String mapToSpatialSaveOrUpdateMapConfigurationRQ(long reportId, Long spatialConnectId, Long mapProjectionId, Long displayProjectionId, CoordinatesFormat coordinatesFormat, ScaleBarUnits scaleBarUnits) throws SpatialModelMarshallException {
        try {
            MapConfigurationType mapConfiguration =
                    new MapConfigurationType(reportId, spatialConnectId, mapProjectionId, displayProjectionId, coordinatesFormat, scaleBarUnits);
            return JAXBMarshaller.marshall(new SpatialSaveOrUpdateMapConfigurationRQ(SpatialModuleMethod.SAVE_OR_UPDATE_MAP_CONFIGURATION, mapConfiguration));
        } catch (SpatialModelMarshallException ex) {
            return logException(ex);
        }
    }

    public static String mapToSpatialGetMapConfigurationRQ(long reportId) throws SpatialModelMarshallException {
        try {
            return JAXBMarshaller.marshall(new SpatialGetMapConfigurationRQ(SpatialModuleMethod.GET_MAP_CONFIGURATION, reportId));
        } catch (SpatialModelMarshallException ex) {
            return logException(ex);
        }
    }

    public static String mapToSpatialDeleteMapConfigurationRQ(List<Long> spatialConnectIds) throws SpatialModelMarshallException {
        try {
            return JAXBMarshaller.marshall(new SpatialDeleteMapConfigurationRQ(SpatialModuleMethod.DELETE_MAP_CONFIGURATION, spatialConnectIds));
        } catch (SpatialModelMarshallException ex) {
            return logException(ex);
        }
    }

    public static String mapToFilterAreaSpatialRequest(List<AreaIdentifierType> scopeAreaList, List<AreaIdentifierType> userAreaList) throws SpatialModelMarshallException {
        try {
            FilterAreasSpatialRQ request = new FilterAreasSpatialRQ();
            ScopeAreasType scopeAreas = new ScopeAreasType();
            UserAreasType userAreas = new UserAreasType();
            scopeAreas.getScopeAreas().addAll(scopeAreaList); // Set scope areas received
            userAreas.getUserAreas().addAll(userAreaList); // Set user areas received
            request.setMethod(SpatialModuleMethod.GET_FILTER_AREA);
            request.setScopeAreas(scopeAreas);
            request.setUserAreas(userAreas);
            return JAXBMarshaller.marshall(request);
        } catch (SpatialModelMarshallException ex) {
            return logException(ex);
        }
    }

    private static String logException(SpatialModelMarshallException ex) throws SpatialModelMarshallException {
        LOG.error("[ Error when marshalling object to string ] ", ex);
        throw new SpatialModelMarshallException("[ Error when marshalling Object to String ]", ex);
    }
}