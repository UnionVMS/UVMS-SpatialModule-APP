/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.bean.impl;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import eu.europa.ec.fisheries.uvms.commons.date.XMLDateUtils;
import eu.europa.ec.fisheries.uvms.commons.geometry.utils.GeometryUtils;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Area;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaByLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaExtendedIdentifierType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeElement;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreasByLocationType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.BatchSpatialEnrichmentRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.BatchSpatialEnrichmentRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.BatchSpatialUserAreaEnrichmentRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreasType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestLocationsType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.CommonEnrichmentRSElement;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Location;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.PointType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialEnrichmentRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialEnrichmentRQListElement;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialEnrichmentRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialEnrichmentRSListElement;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.UnitType;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.Date;
import java.util.List;

@Stateless
@LocalBean
@Slf4j
public class SpatialEnrichmentServiceBean {

    @EJB
    private AreaService areaService;

    public SpatialEnrichmentRS getSpatialEnrichment(SpatialEnrichmentRQ request) {
        PointType point = request.getPoint();
        UnitType unit = request.getUnit();
        List<AreaType> areaTypes = request.getAreaTypes() != null ? request.getAreaTypes().getAreaTypes() : null;
        List<LocationType> locationTypes = request.getLocationTypes().getLocationTypes();
        return computeSpatialEnrichment(point, unit, locationTypes,areaTypes,SpatialEnrichmentRS.class);
    }

    public BatchSpatialEnrichmentRS getBatchSpatialEnrichment(BatchSpatialEnrichmentRQ spatialBatchEnrichmentRQ) {
        BatchSpatialEnrichmentRS batchResponse = new BatchSpatialEnrichmentRS();
        List<SpatialEnrichmentRSListElement> enrichmentRespList = batchResponse.getEnrichmentRespLists();
        for (SpatialEnrichmentRQListElement enrichmentListElement : spatialBatchEnrichmentRQ.getEnrichmentLists()) {
            SpatialEnrichmentRSListElement rsElement = computeSpatialEnrichment(enrichmentListElement.getPoint(), enrichmentListElement.getUnit(),
                    enrichmentListElement.getLocationTypes(),
                    enrichmentListElement.getAreaTypes(),
                    SpatialEnrichmentRSListElement.class);
            rsElement.setGuid(enrichmentListElement.getGuid());
            enrichmentRespList.add(rsElement);
        }
        return batchResponse;
    }

    public SpatialEnrichmentRS getUserAreaSpatialEnrichment(SpatialEnrichmentRQ request) throws ServiceException {
        PointType point = request.getPoint();
        Date activeDate = XMLDateUtils.xmlGregorianCalendarToDate(request.getUserAreaActiveDate());
        return computeUserAreaSpatialEnrichment(point,activeDate,SpatialEnrichmentRS.class);
    }

    public BatchSpatialEnrichmentRS getUserAreaSpatialEnrichmentByWkt(BatchSpatialUserAreaEnrichmentRQ request) throws ServiceException {
        List<AreaTypeElement> areaTypeElements = request.getAreaTypes();
        BatchSpatialEnrichmentRS batchResponse = new BatchSpatialEnrichmentRS();
        List<SpatialEnrichmentRSListElement> enrichmentRespList = batchResponse.getEnrichmentRespLists();
        WKTReader reader = new WKTReader();
        for(AreaTypeElement areaTypeElement : areaTypeElements) {
            Date activeDate = XMLDateUtils.xmlGregorianCalendarToDate(areaTypeElement.getUserAreaActiveDate());
            Geometry geometry;
            try {
                geometry = reader.read(areaTypeElement.getWkt());
            } catch (ParseException e) {
                throw new ServiceException("INVALID GEOMETRY DATA",e);
            }
            geometry.setSRID(4326);
            SpatialEnrichmentRSListElement rsElement = computeUserAreaSpatialEnrichment(geometry,
                    activeDate, SpatialEnrichmentRSListElement.class);
            enrichmentRespList.add(rsElement);
        }
        return batchResponse;
    }
    
    public BatchSpatialEnrichmentRS getUserAreaBatchSpatialEnrichment(BatchSpatialEnrichmentRQ spatialBatchEnrichmentRQ) throws ServiceException {
        BatchSpatialEnrichmentRS batchResponse = new BatchSpatialEnrichmentRS();
        List<SpatialEnrichmentRSListElement> enrichmentRespList = batchResponse.getEnrichmentRespLists();
        for (SpatialEnrichmentRQListElement enrichmentListElement : spatialBatchEnrichmentRQ.getEnrichmentLists()) {
            SpatialEnrichmentRSListElement rsElement = computeUserAreaSpatialEnrichment(enrichmentListElement.getPoint(),
                    XMLDateUtils.xmlGregorianCalendarToDate(enrichmentListElement.getUserAreaActiveDate()),
                    SpatialEnrichmentRSListElement.class);
            rsElement.setGuid(enrichmentListElement.getGuid());
            enrichmentRespList.add(rsElement);
        }
        return batchResponse;
    }
    
    private <R extends CommonEnrichmentRSElement> R computeUserAreaSpatialEnrichment(PointType point, Date activeDate, Class<R> responseClass)  throws ServiceException {
        if (point.getLongitude() == null || point.getLatitude() == null || point.getCrs()  == null){
            throw new ServiceException("MISSING MANDATORY FIELDS");
        }
        final Geometry incoming = GeometryUtils.toGeographic(point.getLatitude(), point.getLongitude(), point.getCrs());
        return computeUserAreaSpatialEnrichment(incoming,activeDate,responseClass);
    }
    
    @SneakyThrows
    private <R extends CommonEnrichmentRSElement> R computeUserAreaSpatialEnrichment(final Geometry incoming, Date activeDate, Class<R> responseClass) {
        R response = responseClass.newInstance();
        List<AreaExtendedIdentifierType> areaTypesByLocation = areaService.getUserAreasByPoint(activeDate, incoming);
        if(areaTypesByLocation != null){
            AreasByLocationType areasByLocationType = new AreasByLocationType();
            areasByLocationType.getAreas().addAll(areaTypesByLocation);
            response.setAreasByLocation(areasByLocationType);
        }
        return response;
    }
    
    @SneakyThrows
    private <R extends CommonEnrichmentRSElement> R computeSpatialEnrichment(PointType point, UnitType unit, List<LocationType> locationTypes, List<AreaType> areaTypes, Class<R> responseClass) {
        AreaByLocationSpatialRQ areaByLocationSpatialRQ = new AreaByLocationSpatialRQ();
        areaByLocationSpatialRQ.setPoint(point);
        List<AreaExtendedIdentifierType> areaTypesByLocation = areaService.getAreasByPoint(areaByLocationSpatialRQ);
        
        /*
            ClosestAreaSpatialRQ closestAreaSpatialRQ = new ClosestAreaSpatialRQ();
            ClosestAreaSpatialRQ.AreaTypes types = new ClosestAreaSpatialRQ.AreaTypes();
            types.getAreaTypes().addAll(areaTypes);
            closestAreaSpatialRQ.setAreaTypes(types);
            closestAreaSpatialRQ.setUnit(unit);
            closestAreaSpatialRQ.setPoint(point);
        */
                
        List<Area> closestAreas = areaService.getClosestArea(point.getLongitude(), point.getLatitude(), 3857, unit);

        ClosestLocationSpatialRQ closestLocationSpatialRQ = new ClosestLocationSpatialRQ();
        closestLocationSpatialRQ.setPoint(point);
        closestLocationSpatialRQ.setUnit(unit);
        ClosestLocationSpatialRQ.LocationTypes locationTp = new ClosestLocationSpatialRQ.LocationTypes();
        locationTp.getLocationTypes().addAll(locationTypes);
        closestLocationSpatialRQ.setLocationTypes(locationTp);

        List<Location> closestLocations = areaService.getClosestPointByPoint(closestLocationSpatialRQ);

        R response = responseClass.newInstance();
        if(areaTypesByLocation != null){
            AreasByLocationType areasByLocationType = new AreasByLocationType();
            areasByLocationType.getAreas().addAll(areaTypesByLocation);
            response.setAreasByLocation(areasByLocationType);
        }

        if (closestAreas != null) {
            ClosestAreasType closestAreasType = new ClosestAreasType();
            closestAreasType.getClosestAreas().addAll(closestAreas);
            response.setClosestAreas(closestAreasType);
        }

        if (closestLocations != null){
            ClosestLocationsType locationType = new ClosestLocationsType();
            locationType.getClosestLocations().addAll(closestLocations);
            response.setClosestLocations(locationType);
        }
        return response;
    }

}