/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.bean.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import java.util.List;

import eu.europa.ec.fisheries.uvms.commons.message.api.Fault;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.spatial.message.bean.SpatialProducer;
import eu.europa.ec.fisheries.uvms.spatial.message.event.AreaByCodeEvent;
import eu.europa.ec.fisheries.uvms.spatial.message.event.DeleteMapConfigurationEvent;
import eu.europa.ec.fisheries.uvms.spatial.message.event.GetAreaByLocationEvent;
import eu.europa.ec.fisheries.uvms.spatial.message.event.GetAreaTypeNamesEvent;
import eu.europa.ec.fisheries.uvms.spatial.message.event.GetClosestAreaEvent;
import eu.europa.ec.fisheries.uvms.spatial.message.event.GetClosestLocationEvent;
import eu.europa.ec.fisheries.uvms.spatial.message.event.GetFilterAreaEvent;
import eu.europa.ec.fisheries.uvms.spatial.message.event.GetMapConfigurationEvent;
import eu.europa.ec.fisheries.uvms.spatial.message.event.GetSpatialEnrichmentEvent;
import eu.europa.ec.fisheries.uvms.spatial.message.event.PingEvent;
import eu.europa.ec.fisheries.uvms.spatial.message.event.SaveOrUpdateMapConfigurationEvent;
import eu.europa.ec.fisheries.uvms.spatial.message.event.SpatialMessageEvent;
import eu.europa.ec.fisheries.uvms.spatial.model.enums.FaultCode;
import eu.europa.ec.fisheries.uvms.spatial.model.mapper.SpatialModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Area;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaByCodeRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaByCodeResponse;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaExtendedIdentifierType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaSimpleType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.FilterAreasSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.FilterAreasSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Location;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.PingRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialDeleteMapConfigurationRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialEnrichmentRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialGetMapConfigurationRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialSaveOrUpdateMapConfigurationRS;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaTypeNamesService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.MapConfigService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialEnrichmentService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialEventService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialService;
import lombok.extern.slf4j.Slf4j;

@Stateless
@Slf4j
public class SpatialEventServiceBean implements SpatialEventService {

    private static final String MODULE_NAME = "spatial";

    @EJB
    private SpatialService spatialService;

    @EJB
    private SpatialEnrichmentService enrichmentService;

    @EJB
    private AreaService areaService;

    @EJB
    private MapConfigService mapConfigService;

    @EJB
    private AreaTypeNamesService areaTypeNamesService;

    @EJB
    private SpatialProducer messageProducer;

    @Override
    public void getAreaByLocation(@Observes @GetAreaByLocationEvent SpatialMessageEvent message) {
        log.info("Getting area by location.");
        try {
            List<AreaExtendedIdentifierType> areaTypesByLocation = spatialService.getAreasByPoint(message.getAreaByLocationSpatialRQ());
            log.debug("Send back areaByLocation response.");
            messageProducer.sendModuleResponseMessage(message.getMessage(), SpatialModuleResponseMapper.mapAreaByLocationResponse(areaTypesByLocation), MODULE_NAME);
        } catch (Exception e) {
            sendError(message, e);
        }
    }

    @Override
    public void getAreaTypeNames(@Observes @GetAreaTypeNamesEvent SpatialMessageEvent message) {
        log.info("Getting area names.");
        try {
            List<String> areaTypeNames = areaTypeNamesService.listAllAreaTypeNames();
            log.debug("Send back area types response.");
            messageProducer.sendModuleResponseMessage(message.getMessage(), SpatialModuleResponseMapper.mapAreaTypeNamesResponse(areaTypeNames), MODULE_NAME);
        } catch (Exception e) {
            sendError(message, e);
        }
    }

    private void sendError(SpatialMessageEvent message, Exception e) {
        log.error("[ Error in spatial module. ] ", e);
        messageProducer.sendFault(message.getMessage(), new Fault(FaultCode.SPATIAL_MESSAGE.getCode(),"Exception in spatial [ " + e.getMessage()));
    }

    @Override
    public void getClosestArea(@Observes @GetClosestAreaEvent SpatialMessageEvent message) {
        log.info("Getting closest area.");
        try {
            List<Area> closestAreas = spatialService.getClosestArea(message.getClosestAreaSpatialRQ());
            log.debug("Send back closestAreas response.");
            messageProducer.sendModuleResponseMessage(message.getMessage(), SpatialModuleResponseMapper.mapClosestAreaResponse(closestAreas), MODULE_NAME);
        } catch (Exception e) {
            sendError(message, e);
        }
    }

    @Override
    public void getClosestLocation(@Observes @GetClosestLocationEvent SpatialMessageEvent message) {
        log.info("Getting closest locations.");
        try {
            List<Location> closestLocations = spatialService.getClosestPointToPointByType(message.getClosestLocationSpatialRQ());
            log.debug("Send back closest locations response.");
            messageProducer.sendModuleResponseMessage(message.getMessage(), SpatialModuleResponseMapper.mapClosestLocationResponse(closestLocations), MODULE_NAME);
        } catch (Exception e) {
            sendError(message, e);
        }
    }

    @Override
    public void areaByCode(@Observes @AreaByCodeEvent SpatialMessageEvent message) {
        log.info("Getting area by code");
        try {
            AreaByCodeRequest areaByCodeRequest = message.getAreaByCodeRequest();
            List<AreaSimpleType> areaSimples = areaByCodeRequest.getAreaSimples();
            List<AreaSimpleType> areaSimpleTypeList = areaService.byCode(areaSimples);

            AreaByCodeResponse areaByCodeRes = new AreaByCodeResponse();
            areaByCodeRes.setAreaSimples(areaSimpleTypeList);

            messageProducer.sendModuleResponseMessage(message.getMessage(), SpatialModuleResponseMapper.mapAreaByCodeResponseToString(areaByCodeRes), MODULE_NAME);

        } catch (Exception e) {
            log.error("[ Error when responding to area by code. ] ", e);
            sendError(message, e);
        }
    }

    @Override
    public void getSpatialEnrichment(@Observes @GetSpatialEnrichmentEvent SpatialMessageEvent message) {
        log.info("Getting spatial enrichment.");
        try {
            SpatialEnrichmentRS spatialEnrichmentRS = enrichmentService.getSpatialEnrichment(message.getSpatialEnrichmentRQ());
            log.debug("Send back enrichment response.");
            messageProducer.sendModuleResponseMessage(message.getMessage(), SpatialModuleResponseMapper.mapEnrichmentResponse(spatialEnrichmentRS), MODULE_NAME);
        } catch (Exception e) {
            sendError(message, e);
        }
    }

    @Override
    public void deleteMapConfiguration(@Observes @DeleteMapConfigurationEvent SpatialMessageEvent message) {
        log.info("Delete mapDefaultSRIDToEPSG configurations.");
        try {
            mapConfigService.handleDeleteMapConfiguration(message.getSpatialDeleteMapConfigurationRQ());
            log.debug("Send back mapDefaultSRIDToEPSG configurations response.");
            String value = JAXBUtils.marshallJaxBObjectToString(JAXBUtils.marshallJaxBObjectToString(new SpatialDeleteMapConfigurationRS()));
            messageProducer.sendModuleResponseMessage(message.getMessage(), value, MODULE_NAME);
        } catch (Exception e) {
            sendError(message, e);
        }
    }

    @Override
    public void getFilterAreas(@Observes @GetFilterAreaEvent SpatialMessageEvent message) {
        log.info("Getting Filter Areas");
        try {
            FilterAreasSpatialRQ filterAreaSpatialRQ = message.getFilterAreasSpatialRQ();
            FilterAreasSpatialRS filterAreasSpatialRS = spatialService.computeAreaFilter(filterAreaSpatialRQ);
            log.debug("Send back filtered Areas");
            messageProducer.sendModuleResponseMessage(message.getMessage(), SpatialModuleResponseMapper.mapFilterAreasResponse(filterAreasSpatialRS), MODULE_NAME);
        } catch (Exception e) {
            sendError(message, e);
        }
    }

    @Override
    public void saveOrUpdateSpatialMapConfiguration(@Observes @SaveOrUpdateMapConfigurationEvent SpatialMessageEvent message) {
        log.info("Saving/Updating mapDefaultSRIDToEPSG configurations.");
        try {
            SpatialSaveOrUpdateMapConfigurationRS saveOrUpdateMapConfigurationRS = mapConfigService.handleSaveOrUpdateSpatialMapConfiguration(message.getSpatialSaveOrUpdateMapConfigurationRQ());
            log.debug("Send back mapDefaultSRIDToEPSG configurations response.");
            String response = SpatialModuleResponseMapper.mapSpatialSaveOrUpdateMapConfigurationRSToString(saveOrUpdateMapConfigurationRS);
            messageProducer.sendModuleResponseMessage(message.getMessage(), response, MODULE_NAME);
        } catch (Exception e) {
            sendError(message, e);
        }
    }

    @Override
    public void getMapConfiguration(@Observes @GetMapConfigurationEvent SpatialMessageEvent message) {
        log.info("Getting mapDefaultSRIDToEPSG configurations.");
        try {
            SpatialGetMapConfigurationRS mapConfigurationRS = mapConfigService.getMapConfiguration(message.getSpatialGetMapConfigurationRQ());
            log.debug("Send back mapDefaultSRIDToEPSG configurations response.");
            String response = SpatialModuleResponseMapper.mapSpatialGetMapConfigurationResponse(mapConfigurationRS);
            messageProducer.sendModuleResponseMessage(message.getMessage(), response, MODULE_NAME);
        } catch (Exception e) {
            sendError(message, e);
        }
    }

    @Override
    public void ping(@Observes @PingEvent SpatialMessageEvent message) {
        log.info("Getting Filter Areas");
        try {
            PingRS pingRS = new PingRS();
            pingRS.setResponse("pong");
            messageProducer.sendModuleResponseMessage(message.getMessage(), SpatialModuleResponseMapper.mapPingResponse(pingRS), MODULE_NAME);
        } catch (Exception e) {
            log.error("[ Error when responding to ping. ] ", e);
            sendError(message, e);
        }
    }

}