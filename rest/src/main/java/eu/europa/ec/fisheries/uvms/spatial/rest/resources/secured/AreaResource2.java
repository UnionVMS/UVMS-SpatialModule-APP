/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured;

import eu.europa.ec.fisheries.uvms.constants.AuthConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ExceptionInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaServiceBean;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.AreaLocationTypesDao;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.BaseAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.PortDistanceInfoDto;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.utils.AreaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * @implicitParam roleName|string|header|true||||||
 * @implicitParam scopeName|string|header|true|EC|||||
 * @implicitParam authorization|string|header|true||||||jwt token
 */
@Path("/area")
@Stateless
public class AreaResource2 {

    private static final Logger log = LoggerFactory.getLogger(AreaResource2.class);

    @Inject
    private AreaServiceBean areaServiceBean;

    @Inject
    private AreaLocationTypesDao areaLocationTypesDao;


    /**
     * Return the list of all area types.
     *
     *
     * @responseMessage 200 ok
     * @responseMessage 404 not found
     */
    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Interceptors(value = {ExceptionInterceptor.class})
    @Path("/types")
    public Response getAreaTypes2() {
        log.info("Getting user areas list");
        List<AreaLocationTypesEntity> areaList = areaLocationTypesDao.findByIsLocation(false);
        List<String> response = new ArrayList<>();
        for (AreaLocationTypesEntity entity: areaList) {
            response.add(entity.getTypeName());
        }
        return Response.ok(response).build();
    }


    @POST
    @Produces(value = {MediaType.APPLICATION_XML})
    @Consumes(value = {MediaType.APPLICATION_XML})
    @Path("/location/details")
    public ClosestLocationSpatialRS getLocationByPoint2(ClosestLocationSpatialRQ request) {

        ClosestLocationSpatialRS response = new ClosestLocationSpatialRS();
        PortDistanceInfoDto closestPort = areaServiceBean.findClosestPortByPosition(request.getPoint().getLatitude(), request.getPoint().getLongitude());

        if (closestPort != null){
            Location location = new Location(String.valueOf(closestPort.getPort().getId()), String.valueOf(closestPort.getPort().getId()), LocationType.PORT, closestPort.getPort().getCode(), closestPort.getPort().getName(), closestPort.getDistance(), UnitType.METERS, closestPort.getPort().getCentroid(), closestPort.getPort().getGeometry(), closestPort.getPort().getExtent(), closestPort.getPort().getEnabled(), closestPort.getPort().getCountryCode());
            ClosestLocationsType locationType = new ClosestLocationsType();
            locationType.getClosestLocations().add(location);
            response.setClosestLocations(locationType);
        }

        return response;

    }


    @POST
    @Produces(value = {MediaType.APPLICATION_XML})
    @Consumes(value = {MediaType.APPLICATION_XML})
    @Path("/details")
    public AreaByLocationSpatialRS getAreasByPoint2(AreaByLocationSpatialRQ request) {

        AreaByLocationSpatialRS response = new AreaByLocationSpatialRS();
        List<BaseAreaDto> areaList = areaServiceBean.getAreasByPoint(request.getPoint().getLatitude(), request.getPoint().getLatitude());
        if(areaList != null){
            List<AreaExtendedIdentifierType> areaExtendedIdentifierTypes = AreaMapper.mapToAreaExtendedIdentifierType(areaList);
            AreasByLocationType areasByLocationType = new AreasByLocationType();
            areasByLocationType.getAreas().addAll(areaExtendedIdentifierTypes);
            response.setAreasByLocation(areasByLocationType);
        }

        return response;
    }

    @POST
    @Produces(value = {MediaType.APPLICATION_XML})
    @Consumes(value = {MediaType.APPLICATION_XML})
    @Path("/closest")
    public ClosestAreaSpatialRS getClosestAreasToPointByType(ClosestAreaSpatialRQ request) {
        ClosestAreaSpatialRS response = new ClosestAreaSpatialRS();
        Double lat = request.getPoint().getLatitude();
        Double lon = request.getPoint().getLongitude();
        Integer crs = request.getPoint().getCrs();
        UnitType unit = request.getUnit();

        List<BaseAreaDto> closestAreas = areaServiceBean.getClosestAreasByPoint(lat, lon);
        List<Area> areaList = new ArrayList<>();
        for (BaseAreaDto base: closestAreas) {
            Area area = new Area(String.valueOf(base.getGid()), base.getType(), base.getCode(), base.getName(), base.getDistance(), UnitType.METERS);
            areaList.add(area);
        }
        if (areaList != null) {
            ClosestAreasType closestAreasType = new ClosestAreasType();
            closestAreasType.getClosestAreas().addAll(areaList);
            response.setClosestArea(closestAreasType);
        }
        return response;
    }



    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/type/code")
    @Interceptors(value = { ExceptionInterceptor.class})
    public Response byCode(AreaByCodeRequest areaByCodeRequest) {

        List<AreaSimpleType> areaSimpleTypeList = areaServiceBean.getAreasByCode(areaByCodeRequest);
        AreaByCodeResponse response = new AreaByCodeResponse();
        response.setAreaSimples(areaSimpleTypeList);
        return Response.ok(response).build();
    }


    //TODO: Remove the headers
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/layers")
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response getSystemAreaLayerMapping(@Context HttpServletRequest request,
                                              @HeaderParam(AuthConstants.HTTP_HEADER_SCOPE_NAME) String scopeName,
                                              @HeaderParam(AuthConstants.HTTP_HEADER_ROLE_NAME) String roleName)  {
        return Response.ok(areaLocationTypesDao.findSystemAreaLayerMapping()).build();
    }

}