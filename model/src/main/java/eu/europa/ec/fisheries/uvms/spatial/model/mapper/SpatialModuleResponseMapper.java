package eu.europa.ec.fisheries.uvms.spatial.model.mapper;

import eu.europa.ec.fisheries.uvms.message.JAXBMarshallerUtils;
import eu.europa.ec.fisheries.uvms.spatial.model.FaultCode;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMapperException;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMarshallException;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelValidationException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import lombok.extern.slf4j.Slf4j;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBException;
import java.util.List;

@Slf4j
public class SpatialModuleResponseMapper {

    private static void validateResponse(TextMessage response, String correlationId) throws SpatialModelValidationException, JMSException {

        if (response == null) {
            throw new SpatialModelValidationException("Error when validating response in ResponseMapper: Response is Null");
        }

        if (response.getJMSCorrelationID() == null) {
            throw new SpatialModelValidationException("No correlationId in response (Null) . Expected was: " + correlationId);
        }

        if (!correlationId.equalsIgnoreCase(response.getJMSCorrelationID())) {
            throw new SpatialModelValidationException("Wrong correlationId in response. Expected was: " + correlationId + " But actual was: " + response.getJMSCorrelationID());
        }

        try {
            SpatialFault fault = JAXBMarshaller.unmarshallTextMessage(response, SpatialFault.class);
            throw new SpatialModelValidationException(fault.getCode() + " : " + fault.getFault());
        } catch (SpatialModelMarshallException e) {
            //everything is well
        }
    }

    public static SpatialFault createFaultMessage(FaultCode code, String message) {
        SpatialFault fault = new SpatialFault();
        fault.setCode(code.getCode());
        fault.setFault(message);
        return fault;
    }

    public static String mapAreaByLocationResponse(final List<AreaTypeEntry> areasByLocation) throws SpatialModelMarshallException {
        try {
            AreaByLocationSpatialRS response = new AreaByLocationSpatialRS();
            AreasByLocationType areasByLocationType = new AreasByLocationType();
            if(areasByLocation != null){
                areasByLocationType.getArea().addAll(areasByLocation);
            }
            response.setAreasByLocation(areasByLocationType);
            return JAXBMarshallerUtils.marshallJaxBObjectToString(response);
        } catch (JAXBException e) {
            return exception(areasByLocation, e);
        }
    }

    public static String mapAreaTypeNamesResponse(final List<String> areaTypeNames) throws SpatialModelMarshallException {
        try {

            AreaTypeNamesSpatialRS response = new AreaTypeNamesSpatialRS();
            AreasNameType areasNameType = new AreasNameType();
            if(areaTypeNames != null){
                areasNameType.getAreaType().addAll(areaTypeNames);
            }
            response.setAreaTypes(areasNameType);
            return JAXBMarshallerUtils.marshallJaxBObjectToString(response);
        } catch (JAXBException e) {
            return exception(areaTypeNames, e);
        }
    }

    public static AreasByLocationType mapToAreasByLocationTypeFromResponse(TextMessage response, String correlationId) throws SpatialModelMapperException {
        try {
            validateResponse(response, correlationId);
            return JAXBMarshallerUtils.unmarshallTextMessage(response, AreaByLocationSpatialRS.class);
        } catch (JAXBException | JMSException e) {
            log.error("[ Error when mapping response to AreasByLocationType response. ] {}", e.getMessage());
            throw new SpatialModelMapperException("Error when returning AreasByLocationType from response in ResponseMapper: " + e.getMessage());
        }
    }

    public static String mapClosestLocationResponse(List<Location> closestLocations) throws SpatialModelMarshallException {
        try {
            ClosestLocationSpatialRS response = new ClosestLocationSpatialRS();
            ClosestLocationsType closestLocationsType = new ClosestLocationsType();
            if(closestLocations != null){
                closestLocationsType.getClosestLocations().addAll(closestLocations);
            }
            response.setClosestLocations(closestLocationsType);
            return JAXBMarshallerUtils.marshallJaxBObjectToString(response);
        } catch (JAXBException e) {
            return exception(closestLocations, e);
        }
    }

    public static String mapClosestAreaResponse(final List<Area> closestAreas) throws SpatialModelMarshallException {
        try {
            ClosestAreaSpatialRS response = new ClosestAreaSpatialRS();
            ClosestAreasType closestAreasType = new ClosestAreasType();
            if(closestAreas != null){
                closestAreasType.getClosestArea().addAll(closestAreas);
            }
            response.setClosestArea(closestAreasType);
            return JAXBMarshallerUtils.marshallJaxBObjectToString(response);
        } catch (JAXBException e) {
            return exception(closestAreas, e);
        }
    }

    public static String mapEnrichmentResponse(final SpatialEnrichmentRS spatialEnrichmentRS) throws SpatialModelMarshallException {
        try {
            return JAXBMarshallerUtils.marshallJaxBObjectToString(spatialEnrichmentRS);
        } catch (JAXBException e) {
            return exception(spatialEnrichmentRS, e);
        }
    }

    public static String mapAreaDetailsSpatialResponse(final AreaDetails areaDetails) throws SpatialModelMarshallException {
        try {
            AreaDetailsSpatialResponse response = new AreaDetailsSpatialResponse();
            response.setAreaDetails(areaDetails);
            return JAXBMarshallerUtils.marshallJaxBObjectToString(response);
        } catch (JAXBException e) {
            return exception(areaDetails, e);
        }
    }

    private static <T> String exception(T data, JAXBException e) throws SpatialModelMarshallException {
        log.error("[ Error when marshalling data. ] {}", e.getMessage());
        throw new SpatialModelMarshallException("Error when marshalling " + data.getClass().getName() + " to String");
    }

}
