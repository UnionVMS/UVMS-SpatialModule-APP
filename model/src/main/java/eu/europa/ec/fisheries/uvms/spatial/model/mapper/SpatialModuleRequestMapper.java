/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.spatial.model.mapper;

import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMarshallException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import java.util.List;

public final class SpatialModuleRequestMapper {

    final static Logger LOG = LoggerFactory.getLogger(SpatialModuleRequestMapper.class);

    private SpatialModuleRequestMapper() {
    }

    public static String mapToCreateAreaByLocationRequest(PointType point) throws SpatialModelMarshallException {
        AreaByLocationSpatialRQ request = new AreaByLocationSpatialRQ();
        request.setMethod(SpatialModuleMethod.GET_AREA_BY_LOCATION);
        request.setPoint(point);
        try {
            return JAXBUtils.marshallJaxBObjectToString(request);
        } catch (JAXBException ex) {
            return logException(ex);
        }
    }

    public static String mapToCreateAllAreaTypesRequest() throws SpatialModelMarshallException {
        AllAreaTypesRequest request = new AllAreaTypesRequest();
        request.setMethod(SpatialModuleMethod.GET_AREA_TYPES);
        try {
            return JAXBUtils.marshallJaxBObjectToString(request);
        } catch (JAXBException ex) {
            return logException(ex);
        }
    }

    public static String mapToCreateGetAreaByCodeRequest(List<AreaSimpleType> areaSimpleTypeList) throws SpatialModelMarshallException {

        AreaByCodeRequest areaByCodeRequest = new AreaByCodeRequest();
        areaByCodeRequest.setAreaSimples(areaSimpleTypeList);
        areaByCodeRequest.setMethod(SpatialModuleMethod.GET_AREA_BY_CODE);
        try {
            return JAXBUtils.marshallJaxBObjectToString(areaByCodeRequest);
        } catch (JAXBException ex) {
            return logException(ex);
        }
    }

    public static String mapToGeometryByPortCodeRequest(String portCode) throws SpatialModelMarshallException {

        GeometryByPortCodeRequest geometryByPortCodeRequest = new GeometryByPortCodeRequest();
        geometryByPortCodeRequest.setPortCode(portCode);
        geometryByPortCodeRequest.setMethod(SpatialModuleMethod.GET_GEOMETRY_BY_PORT_CODE);

        try {
            LOG.info("Inside mapToGeometryByPortCodeRequest.");
            String value= JAXBUtils.marshallJaxBObjectToString(geometryByPortCodeRequest);
            LOG.info("after getting value.");
            return value;
        } catch (JAXBException ex) {
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
            return JAXBUtils.marshallJaxBObjectToString(request);
        } catch (JAXBException ex) {
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
            return JAXBUtils.marshallJaxBObjectToString(request);
        } catch (JAXBException ex) {
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
            return JAXBUtils.marshallJaxBObjectToString(request);
        } catch (JAXBException ex) {
            return logException(ex);
        }
    }

    public static String mapToCreateBatchSpatialEnrichmentRequest(List<SpatialEnrichmentRQListElement> spatialEnrichmentRQListElements) throws SpatialModelMarshallException {
        BatchSpatialEnrichmentRQ batchRequest = new BatchSpatialEnrichmentRQ();
        batchRequest.setEnrichmentLists(spatialEnrichmentRQListElements);
        batchRequest.setMethod(SpatialModuleMethod.GET_ENRICHMENT_BATCH);
        try {
            return JAXBUtils.marshallJaxBObjectToString(batchRequest);
        } catch (JAXBException ex) {
            return logException(ex);
        }
    }


    public static SpatialEnrichmentRQListElement mapToCreateSpatialEnrichmentRQElement(PointType point, UnitType unit, List<LocationType> locationTypes,
                                                                                       List<AreaType> areaTypes) {
        SpatialEnrichmentRQListElement spatialEnrichmentRQListElement = new SpatialEnrichmentRQListElement();
        spatialEnrichmentRQListElement.setPoint(point);
        spatialEnrichmentRQListElement.setUnit(unit);
        spatialEnrichmentRQListElement.setLocationTypes(locationTypes);
        spatialEnrichmentRQListElement.setAreaTypes(areaTypes);
        return spatialEnrichmentRQListElement;
    }

    public static String mapToSpatialSaveOrUpdateMapConfigurationRQ(long reportId,
                                                                    Long spatialConnectId,
                                                                    Long mapProjectionId,
                                                                    Long displayProjectionId,
                                                                    CoordinatesFormat coordinatesFormat,
                                                                    ScaleBarUnits scaleBarUnits,
                                                                    StyleSettingsType styleSettings,
                                                                    VisibilitySettingsType visibilitySettings,
                                                                    LayerSettingsType layerSettingsType,
                                                                    List<ReferenceDataType> referenceDataType) throws SpatialModelMarshallException {
        try {
            MapConfigurationType mapConfiguration =
                    new MapConfigurationType(reportId, spatialConnectId, mapProjectionId, displayProjectionId,
                            coordinatesFormat, scaleBarUnits, styleSettings, visibilitySettings,
                            layerSettingsType, referenceDataType);
            return JAXBUtils.marshallJaxBObjectToString(new SpatialSaveOrUpdateMapConfigurationRQ(SpatialModuleMethod.SAVE_OR_UPDATE_MAP_CONFIGURATION, mapConfiguration));
        } catch (JAXBException ex) {
            return logException(ex);
        }
    }

    public static String mapToSpatialGetMapConfigurationRQ(long reportId, List<String> permittedServiceLayers) throws SpatialModelMarshallException {
        try {
            return JAXBUtils.marshallJaxBObjectToString(new SpatialGetMapConfigurationRQ(SpatialModuleMethod.GET_MAP_CONFIGURATION, reportId, permittedServiceLayers));
        } catch (JAXBException ex) {
            return logException(ex);
        }
    }

    public static String mapToSpatialDeleteMapConfigurationRQ(List<Long> spatialConnectIds) throws SpatialModelMarshallException {
        try {
            return JAXBUtils.marshallJaxBObjectToString(new SpatialDeleteMapConfigurationRQ(SpatialModuleMethod.DELETE_MAP_CONFIGURATION, spatialConnectIds));
        } catch (JAXBException ex) {
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
            return JAXBUtils.marshallJaxBObjectToString(request);
        } catch (JAXBException ex) {
            return logException(ex);
        }
    }

    private static String logException(Exception ex) throws SpatialModelMarshallException {
        LOG.error("[ Error when marshalling object to string ] ", ex);
        throw new SpatialModelMarshallException("[ Error when marshalling Object to String ]", ex);
    }
}