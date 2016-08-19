/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialEnrichmentService;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialService;
import lombok.extern.slf4j.Slf4j;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * This class is a resource to call services exposed tru the JMS channel
 */
@Path("/xml")
@Slf4j
public class XMLResource {

    private @EJB SpatialEnrichmentService enrichmentService;
    private @EJB SpatialService spatialService;

    @POST
    @Produces(value = {MediaType.APPLICATION_XML})
    @Consumes(value = {MediaType.APPLICATION_XML})
    @Path("/enrichment")
    public SpatialEnrichmentRS spatialEnrichment(SpatialEnrichmentRQ request) throws ServiceException {

        return enrichmentService.getSpatialEnrichment(request);

    }

    @POST
    @Produces(value = {MediaType.APPLICATION_XML})
    @Consumes(value = {MediaType.APPLICATION_XML})
    @Path("/filter-areas")
    public FilterAreasSpatialRS computeAreaFilter(FilterAreasSpatialRQ request) throws ServiceException {

        return  spatialService.computeAreaFilter(request);

    }

    @POST
    @Produces(value = {MediaType.APPLICATION_XML})
    @Consumes(value = {MediaType.APPLICATION_XML})
    @Path("/areas-by-location")
    public AreaByLocationSpatialRS getAreasByPoint(AreaByLocationSpatialRQ request) throws ServiceException {

        AreaByLocationSpatialRS response = new AreaByLocationSpatialRS();
        List<AreaExtendedIdentifierType> areaTypesByLocation = spatialService.getAreasByPoint(request);

        if(areaTypesByLocation != null){
            AreasByLocationType areasByLocationType = new AreasByLocationType();
            areasByLocationType.getAreas().addAll(areaTypesByLocation);
            response.setAreasByLocation(areasByLocationType);
        }

        return response;

    }

    @POST
    @Produces(value = {MediaType.APPLICATION_XML})
    @Consumes(value = {MediaType.APPLICATION_XML})
    @Path("/closest-areas")
    public ClosestAreaSpatialRS getClosestAreasToPointByType(ClosestAreaSpatialRQ request) throws ServiceException {

        ClosestAreaSpatialRS response = new ClosestAreaSpatialRS();
        List<Area> closestAreas = spatialService.getClosestArea(request);

        if (closestAreas != null) {
            ClosestAreasType closestAreasType = new ClosestAreasType();
            closestAreasType.getClosestAreas().addAll(closestAreas);
            response.setClosestArea(closestAreasType);
        }

        return response;

    }

    @POST
    @Produces(value = {MediaType.APPLICATION_XML})
    @Consumes(value = {MediaType.APPLICATION_XML})
    @Path("/closest-locations")
    public ClosestLocationSpatialRS getClosestPointToPointByType(ClosestLocationSpatialRQ request) throws ServiceException {

        ClosestLocationSpatialRS response = new ClosestLocationSpatialRS();
        List<Location> closestLocations = spatialService.getClosestPointToPointByType(request);

        if (closestLocations != null){
            ClosestLocationsType locationType = new ClosestLocationsType();
            locationType.getClosestLocations().addAll(closestLocations);
            response.setClosestLocations(locationType);
        }

        return response;

    }

}