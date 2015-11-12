package eu.europa.ec.fisheries.uvms.spatial.model.mapper;

import eu.europa.ec.fisheries.uvms.spatial.model.FaultCode;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMapperException;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMarshallException;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelValidationException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import lombok.extern.slf4j.Slf4j;

import javax.jms.JMSException;
import javax.jms.TextMessage;
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
            SpatialFault fault = JAXBMarshaller.unmarshall(response, SpatialFault.class);
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

    public static String mapAreaByLocationResponse(final List<AreaExtendedIdentifierType> areasByLocation) throws SpatialModelMarshallException {
        try {
            AreaByLocationSpatialRS response = new AreaByLocationSpatialRS();
            AreasByLocationType areasByLocationType = new AreasByLocationType();
            if(areasByLocation != null){
                areasByLocationType.getAreas().addAll(areasByLocation);
            }
            response.setAreasByLocation(areasByLocationType);
            return JAXBMarshaller.marshall(response);
        } catch (SpatialModelMarshallException e) {
            return exception(areasByLocation, e);
        }
    }

    public static AreasByLocationType mapToAreasByLocationTypeFromResponse(TextMessage response, String correlationId) throws SpatialModelMapperException {
        try {
            validateResponse(response, correlationId);
            AreaByLocationSpatialRS areaByLocationSpatialRS = JAXBMarshaller.unmarshall(response, AreaByLocationSpatialRS.class);
            return areaByLocationSpatialRS.getAreasByLocation();
        } catch (SpatialModelMarshallException | JMSException e) {
            log.error("[ Error when mapping response to AreasByLocationType response. ] {}", e.getMessage());
            throw new SpatialModelMapperException("Error when returning AreasByLocationType from response in ResponseMapper: " + e.getMessage());
        }
    }

    public static String mapAreaTypeNamesResponse(final List<String> areaTypeNames) throws SpatialModelMarshallException {
        try {

            AreaTypeNamesSpatialRS response = new AreaTypeNamesSpatialRS();
            AreasNameType areasNameType = new AreasNameType();
            if(areaTypeNames != null){
                areasNameType.getAreaTypes().addAll(areaTypeNames);
            }
            response.setAreaTypes(areasNameType);
            return JAXBMarshaller.marshall(response);
        } catch (SpatialModelMarshallException e) {
            return exception(areaTypeNames, e);
        }
    }

    public static AreasNameType mapToAreasNameTypeFromResponse(TextMessage response, String correlationId) throws SpatialModelMapperException {
        try {
            validateResponse(response, correlationId);
            AreaTypeNamesSpatialRS areaTypeNamesSpatialRS = JAXBMarshaller.unmarshall(response, AreaTypeNamesSpatialRS.class);
            return areaTypeNamesSpatialRS.getAreaTypes();
        } catch (SpatialModelMarshallException | JMSException e) {
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
            return JAXBMarshaller.marshall(response);
        } catch (SpatialModelMarshallException e) {
            return exception(closestLocations, e);
        }
    }

    public static ClosestLocationsType mapToClosestLocationsTypeFromResponse(TextMessage response, String correlationId) throws SpatialModelMapperException {
        try {
            validateResponse(response, correlationId);
            ClosestLocationSpatialRS closestLocationSpatialRS = JAXBMarshaller.unmarshall(response, ClosestLocationSpatialRS.class);
            return closestLocationSpatialRS.getClosestLocations();
        } catch (SpatialModelMarshallException | JMSException e) {
            log.error("[ Error when mapping response to AreasByLocationType response. ] {}", e.getMessage());
            throw new SpatialModelMapperException("Error when returning AreasByLocationType from response in ResponseMapper: " + e.getMessage());
        }
    }

    public static String mapClosestAreaResponse(final List<Area> closestAreas) throws SpatialModelMarshallException {
        try {
            ClosestAreaSpatialRS response = new ClosestAreaSpatialRS();
            ClosestAreasType closestAreasType = new ClosestAreasType();
            if(closestAreas != null){
                closestAreasType.getClosestAreas().addAll(closestAreas);
            }
            response.setClosestArea(closestAreasType);
            return JAXBMarshaller.marshall(response);
        } catch (SpatialModelMarshallException e) {
            return exception(closestAreas, e);
        }
    }

    public static ClosestAreasType mapToClosestAreasTypeFromResponse(TextMessage response, String correlationId) throws SpatialModelMapperException {
        try {
            validateResponse(response, correlationId);
            ClosestAreaSpatialRS closestAreaSpatialRS = JAXBMarshaller.unmarshall(response, ClosestAreaSpatialRS.class);
            return closestAreaSpatialRS.getClosestArea();
        } catch (SpatialModelMarshallException | JMSException e) {
            log.error("[ Error when mapping response to AreasByLocationType response. ] {}", e.getMessage());
            throw new SpatialModelMapperException("Error when returning AreasByLocationType from response in ResponseMapper: " + e.getMessage());
        }
    }

    public static String mapEnrichmentResponse(final SpatialEnrichmentRS spatialEnrichmentRS) throws SpatialModelMarshallException {
        try {
            return JAXBMarshaller.marshall(spatialEnrichmentRS);
        } catch (SpatialModelMarshallException e) {
            return exception(spatialEnrichmentRS, e);
        }
    }

    public static SpatialEnrichmentRS mapToSpatialEnrichmentRSFromResponse(TextMessage response, String correlationId) throws SpatialModelMapperException {
        try {
            validateResponse(response, correlationId);
            return JAXBMarshaller.unmarshall(response, SpatialEnrichmentRS.class);
        } catch (SpatialModelMarshallException | JMSException e) {
            log.error("[ Error when mapping response to AreasByLocationType response. ] {}", e.getMessage());
            throw new SpatialModelMapperException("Error when returning AreasByLocationType from response in ResponseMapper: " + e.getMessage());
        }
    }
    
	public static String mapFilterAreasResponse(FilterAreasSpatialRS filterAreasSpatialRS) throws SpatialModelMarshallException {
		try {
			return JAXBMarshaller.marshall(filterAreasSpatialRS);
		} catch (SpatialModelMarshallException e) {
            return exception(filterAreasSpatialRS, e);
        }
	}

    public static String mapPingResponse(PingRS pingRS) throws SpatialModelMarshallException {
        try {
            return JAXBMarshaller.marshall(pingRS);
        } catch (SpatialModelMarshallException e) {
            return exception(pingRS, e);
        }
    }

	public static FilterAreasSpatialRS mapToFilterAreasSpatialRSFromResponse(TextMessage response, String correlationId) throws SpatialModelMapperException {
		try {
			validateResponse(response, correlationId);
			return JAXBMarshaller.unmarshall(response, FilterAreasSpatialRS.class);
		} catch (SpatialModelMarshallException | JMSException e) {
			log.error("[ Error when mapping response to filter area response. ] {}", e.getMessage());
            throw new SpatialModelMapperException("Error when returning filter area from response in ResponseMapper: " + e.getMessage());
		}		
	}

    private static <T> String exception(T data, SpatialModelMarshallException e) throws SpatialModelMarshallException {
        log.error("[ Error when marshalling data. ] {}", e.getMessage());
        throw new SpatialModelMarshallException("Error when marshalling " + data.getClass().getName() + " to String");
    }
}
