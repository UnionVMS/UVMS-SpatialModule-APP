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
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMapperException;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMarshallException;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelValidationException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBException;
import java.util.List;

public final class SpatialModuleResponseMapper {

    final static Logger LOG = LoggerFactory.getLogger(SpatialModuleResponseMapper.class);

    private SpatialModuleResponseMapper() {
    }

    private static void validateResponse(TextMessage response, String correlationId) throws SpatialModelValidationException {

        try {
            if (response == null) {
                throw new SpatialModelValidationException("Error when validating response in ResponseMapper: Response is Null");
            }

            if (response.getJMSCorrelationID() == null) {
                throw new SpatialModelValidationException("No correlationId in response (Null) . Expected was: " + correlationId);
            }

            if (!correlationId.equalsIgnoreCase(response.getJMSCorrelationID())) {
                throw new SpatialModelValidationException("Wrong correlationId in response. Expected was: " + correlationId + " But actual was: " + response.getJMSCorrelationID());
            }

            isValid(response);

        } catch (JMSException e) {
            LOG.error("JMS throwException during validation ", e);
            throw new SpatialModelValidationException("JMS throwException during validation " + e.getMessage());
        }
    }

    private static void isValid(TextMessage response) throws SpatialModelValidationException {
        try{
            SpatialFault fault = JAXBUtils.unMarshallMessage(response.getText(), SpatialFault.class);
            throw new SpatialModelValidationException(fault.getCode() + " : " + fault.getFault());
        } catch (JMSException | JAXBException e) {
            LOG.debug("Expected Exception");
        }
    }

    public static String mapAreaByLocationResponse(final List<AreaExtendedIdentifierType> areasByLocation) throws SpatialModelMarshallException {
        try {
            AreaByLocationSpatialRS response = new AreaByLocationSpatialRS();
            AreasByLocationType areasByLocationType = new AreasByLocationType();
            if (areasByLocation != null) {
                areasByLocationType.getAreas().addAll(areasByLocation);
            }
            response.setAreasByLocation(areasByLocationType);
            return JAXBUtils.marshallJaxBObjectToString(response);
        } catch (JAXBException e) {
            return throwException(areasByLocation, e);
        }
    }

    public static AreasByLocationType mapToAreasByLocationTypeFromResponse(TextMessage response, String correlationId) throws SpatialModelMapperException {
        try {
            validateResponse(response, correlationId);
            AreaByLocationSpatialRS areaByLocationSpatialRS = JAXBUtils.unMarshallMessage(response.getText(), AreaByLocationSpatialRS.class);
            return areaByLocationSpatialRS.getAreasByLocation();
        } catch (JMSException | JAXBException e) {
            return throwException(e);
        }
    }

    public static String mapAreaTypeNamesResponse(final List<String> areaTypeNames) throws SpatialModelMarshallException {
        try {

            AreaTypeNamesSpatialRS response = new AreaTypeNamesSpatialRS();
            AreasNameType areasNameType = new AreasNameType();
            if (areaTypeNames != null) {
                for (String areaType: areaTypeNames) {
                    areasNameType.getAreaTypes().add(AreaType.fromValue(areaType));
                }
            }
            response.setAreaTypes(areasNameType);
            return JAXBUtils.marshallJaxBObjectToString(response);
        } catch (JAXBException e) {
            return throwException(areaTypeNames, e);
        }
    }

    public static AreasNameType mapToAreasNameTypeFromResponse(TextMessage response, String correlationId) throws SpatialModelMapperException {
        try {
            validateResponse(response, correlationId);
            AreaTypeNamesSpatialRS areaTypeNamesSpatialRS = JAXBUtils.unMarshallMessage(response.getText(), AreaTypeNamesSpatialRS.class);
            return areaTypeNamesSpatialRS.getAreaTypes();
        } catch (JMSException | JAXBException e) {
            return throwException(e);
        }
    }

    public static String mapClosestLocationResponse(List<Location> closestLocations) throws SpatialModelMarshallException {
        try {
            ClosestLocationSpatialRS response = new ClosestLocationSpatialRS();
            ClosestLocationsType closestLocationsType = new ClosestLocationsType();
            if (closestLocations != null) {
                closestLocationsType.getClosestLocations().addAll(closestLocations);
            }
            response.setClosestLocations(closestLocationsType);
            return JAXBUtils.marshallJaxBObjectToString(response);
        } catch (JAXBException e) {
            return throwException(closestLocations, e);
        }
    }

    public static ClosestLocationsType mapToClosestLocationsTypeFromResponse(TextMessage response, String correlationId) throws SpatialModelMapperException {
        try {
            validateResponse(response, correlationId);
            ClosestLocationSpatialRS closestLocationSpatialRS = null;
            closestLocationSpatialRS = JAXBUtils.unMarshallMessage(response.getText(), ClosestLocationSpatialRS.class);
            return closestLocationSpatialRS.getClosestLocations();
        } catch (JMSException | JAXBException e) {
            return throwException(e);
        }
    }

    public static String mapClosestAreaResponse(final List<Area> closestAreas) throws SpatialModelMarshallException {
        try {
            ClosestAreaSpatialRS response = new ClosestAreaSpatialRS();
            ClosestAreasType closestAreasType = new ClosestAreasType();
            if (closestAreas != null) {
                closestAreasType.getClosestAreas().addAll(closestAreas);
            }
            response.setClosestArea(closestAreasType);
            return JAXBUtils.marshallJaxBObjectToString(response);
        } catch (JAXBException e) {
            return throwException(closestAreas, e);
        }
    }

    public static ClosestAreasType mapToClosestAreasTypeFromResponse(TextMessage response, String correlationId) throws SpatialModelMapperException {
        try {
            validateResponse(response, correlationId);
            ClosestAreaSpatialRS closestAreaSpatialRS = JAXBUtils.unMarshallMessage(response.getText(), ClosestAreaSpatialRS.class);
            return closestAreaSpatialRS.getClosestArea();
        } catch (JMSException | JAXBException e) {
            return throwException(e);
        }
    }

    public static String mapEnrichmentResponse(final SpatialEnrichmentRS spatialEnrichmentRS) throws SpatialModelMarshallException {
        try {
            return JAXBUtils.marshallJaxBObjectToString(spatialEnrichmentRS);
        } catch (JAXBException e) {
            return throwException(spatialEnrichmentRS, e);
        }
    }

    public static String mapToBatchEnrichmentResponse(BatchSpatialEnrichmentRS spatialBatchEnrichmentRS) throws SpatialModelMarshallException {
        try {
            return JAXBUtils.marshallJaxBObjectToString(spatialBatchEnrichmentRS);
        } catch (JAXBException e) {
            return throwException(spatialBatchEnrichmentRS, e);
        }
    }

    public static SpatialEnrichmentRS mapToSpatialEnrichmentRSFromResponse(TextMessage response, String correlationId) throws SpatialModelMapperException {
        try {
            validateResponse(response, correlationId);
            return JAXBUtils.unMarshallMessage(response.getText(), SpatialEnrichmentRS.class);
        } catch (JAXBException | JMSException e) {
            return throwException(e);
        }
    }

    public static BatchSpatialEnrichmentRS mapToBatchSpatialEnrichmentRSFromResponse(TextMessage response, String correlationId) throws SpatialModelMapperException {
        try {
            validateResponse(response, correlationId);
            return JAXBUtils.unMarshallMessage(response.getText(), BatchSpatialEnrichmentRS.class);
        } catch (JAXBException | JMSException e) {
            return throwException(e);
        }
    }

    public static String mapFilterAreasResponse(FilterAreasSpatialRS filterAreasSpatialRS) throws SpatialModelMarshallException {
        try {
            return JAXBUtils.marshallJaxBObjectToString(filterAreasSpatialRS);
        } catch (JAXBException e) {
            return throwException(filterAreasSpatialRS, e);
        }
    }

    public static String mapSpatialGetMapConfigurationResponse(SpatialGetMapConfigurationRS spatialGetMapConfigurationRS) throws SpatialModelMarshallException {
        try {
            return JAXBUtils.marshallJaxBObjectToString(spatialGetMapConfigurationRS);
        } catch (JAXBException e) {
            return throwException(spatialGetMapConfigurationRS, e);
        }
    }

    public static String mapPingResponse(PingRS pingRS) throws SpatialModelMarshallException {
        try {
            return JAXBUtils.marshallJaxBObjectToString(pingRS);
        } catch (JAXBException e) {
            return throwException(pingRS, e);
        }
    }

    public static String mapGeometryByPortCodeResponse(GeometryByPortCodeResponse geometryByPortCodeResponse) throws SpatialModelMarshallException {
        try {
            return JAXBUtils.marshallJaxBObjectToString(geometryByPortCodeResponse);
        } catch (JAXBException e) {
            return throwException(geometryByPortCodeResponse, e);
        }
    }

    public static FilterAreasSpatialRS mapToFilterAreasSpatialRSFromResponse(TextMessage response, String correlationId) throws SpatialModelMapperException {
        try {
            validateResponse(response, correlationId);
            return JAXBUtils.unMarshallMessage(response.getText(), FilterAreasSpatialRS.class);
        } catch (JMSException | JAXBException e) {
            return throwException(e);
        }
    }

    public static String mapSpatialSaveOrUpdateMapConfigurationRSToString(SpatialSaveOrUpdateMapConfigurationRS spatialSaveOrUpdateMapConfigurationRS) throws SpatialModelMarshallException {
        try {
            return JAXBUtils.marshallJaxBObjectToString(spatialSaveOrUpdateMapConfigurationRS);
        } catch (JAXBException e) {
            return throwException(spatialSaveOrUpdateMapConfigurationRS, e);
        }
    }


    public static String mapAreaByCodeResponseToString(AreaByCodeResponse areaByCodeResponse) throws SpatialModelMarshallException {
        try {
            return JAXBUtils.marshallJaxBObjectToString(areaByCodeResponse);
        } catch (JAXBException e) {
            return throwException(areaByCodeResponse, e);
        }
    }

    public static AreaByCodeResponse mapAreaByCodeResponse(TextMessage response, String correlationId) throws SpatialModelMapperException {
        try {
            validateResponse(response, correlationId);
            return JAXBUtils.unMarshallMessage(response.getText(), AreaByCodeResponse.class);
        } catch (JMSException | JAXBException  e) {
            return throwException(e);
        }
    }

    public static SpatialDeleteMapConfigurationRS mapToSpatialDeleteMapConfigurationRS(TextMessage response, String correlationId) throws SpatialModelMapperException {
        try {
            validateResponse(response, correlationId);
            return JAXBUtils.unMarshallMessage(response.getText(), SpatialDeleteMapConfigurationRS.class);
        } catch (JMSException | JAXBException  e) {
            return throwException(e);
        }
    }

    public static SpatialSaveOrUpdateMapConfigurationRS mapToSpatialSaveOrUpdateMapConfigurationRS(TextMessage response, String correlationId) throws SpatialModelMapperException {
        try {
            validateResponse(response, correlationId);
            return JAXBUtils.unMarshallMessage(response.getText(), SpatialSaveOrUpdateMapConfigurationRS.class);
        } catch (JMSException | JAXBException  e) {
            return throwException(e);
        }
    }

    public static SpatialGetMapConfigurationRS mapToSpatialGetMapConfigurationRS(TextMessage response, String correlationId) throws SpatialModelMapperException {
        try {
            validateResponse(response, correlationId);
            return JAXBUtils.unMarshallMessage(response.getText(), SpatialGetMapConfigurationRS.class);
        } catch (JMSException | JAXBException  e) {
            return throwException(e);
        }
    }

    public static GeometryByPortCodeResponse mapGeometryByPortCodeResponseToString(TextMessage geometryByPortCodeResponse, String correlationId) throws SpatialModelMarshallException, SpatialModelValidationException {
        try {
            validateResponse(geometryByPortCodeResponse, correlationId);
            return JAXBUtils.unMarshallMessage(geometryByPortCodeResponse.getText(), GeometryByPortCodeResponse.class);
        } catch (JMSException | JAXBException e) {
            return throwException(e);
        }
    }

    private static <T> String throwException(T data, Exception e) throws SpatialModelMarshallException {
        LOG.error("[ Error when marshalling data. ] {}", e);
        throw new SpatialModelMarshallException("Error when marshalling " + data.getClass().getName() + " to String");
    }

    private static <T> T throwException(Exception e) throws SpatialModelMarshallException {
        LOG.error("[ Error when marshalling data. ] {}", e);
        throw new SpatialModelMarshallException("Error when marshalling object to String", e);
    }

}