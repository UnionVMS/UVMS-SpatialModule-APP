package eu.europa.ec.fisheries.uvms.spatial.rest.resources.unsecured;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTReader;
import eu.europa.ec.fisheries.schema.movement.v1.MovementType;
import eu.europa.ec.fisheries.schema.movement.v1.SegmentCategoryType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.AreaTransitionsDTO;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.bean.AreaServiceBean;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.dao.AreaLocationTypesDao;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto.BaseAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto.PortDistanceInfoDto;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.PortAreaEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.PortEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.utils.AreaMapper;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.utils.GeometryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Rest endpoints for faster dataretrieval
 * We use POST for convinience also for fetch, since it it easy to pass Requests as java Objects
 * <p>
 * OBS we will initially mostly only implement methods  used from Movement and Reporting
 */

// TODO fix
@Path("json")
@SuppressWarnings("unchecked")
public class SpatialRestResource {


    private static final Logger log = LoggerFactory.getLogger(SpatialRestResource.class);


    @Inject
    private AreaServiceBean areaServiceBean;

    @Inject
    private AreaLocationTypesDao areaLocationTypesDao;


    @POST
    @Path("getAreaByLocation")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getAreaByLocation2( AreaByLocationSpatialRQ areaByLocationSpatialRQ)  {

        try {
            List<BaseAreaDto> areaList = areaServiceBean.getAreasByPoint(areaByLocationSpatialRQ.getPoint().getLatitude(), areaByLocationSpatialRQ.getPoint().getLatitude());
            List<AreaExtendedIdentifierType> response = AreaMapper.mapToAreaExtendedIdentifierType(areaList);
            return Response.ok(response).build();
        } catch (Exception e) {
            log.error(e.toString(),e);
            return Response.status(500).build();
        }
    }


    @POST
    @Path("getAreaTypes")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getAreaTypes2(AllAreaTypesRequest allAreaTypesRequest) {

        try {
            List<AreaLocationTypesEntity> areaList = areaLocationTypesDao.findByIsLocation(false);
            List<String> response = new ArrayList<>();
            for (AreaLocationTypesEntity entity: areaList) {
                response.add(entity.getTypeName());
            }
            return Response.ok(response).build();
        } catch (Exception e) {
            log.error(e.toString(),e);
            return Response.status(500).build();
        }
    }

    @POST
    @Path("getClosestArea")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getClosestArea2(ClosestAreaSpatialRQ request)  {

        try {
            Double lat = request.getPoint().getLatitude();
            Double lon = request.getPoint().getLongitude();

            List<BaseAreaDto> closestAreas = areaServiceBean.getClosestAreasByPoint(lat, lon);
            List<Area> response = new ArrayList<>();
            for (BaseAreaDto base: closestAreas) {
                Area area = new Area(String.valueOf(base.getGid()), base.getType(), base.getCode(), base.getName(), base.getDistance(), UnitType.METERS);
                response.add(area);
            }
            return Response.ok(response).build();
        } catch (Exception e) {
            log.error(e.toString(),e);
            return Response.status(500).build();
        }
    }


    @POST
    @Path("getClosestLocation")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getClosestLocation2(ClosestLocationSpatialRQ closestLocationSpatialRQ) {

        try {
            PortDistanceInfoDto closestPort = areaServiceBean.findClosestPortByPosition(closestLocationSpatialRQ.getPoint().getLatitude(), closestLocationSpatialRQ.getPoint().getLongitude());
            List<Location> response = new ArrayList<>();
            Location location = new Location(String.valueOf(closestPort.getPort().getId()), String.valueOf(closestPort.getPort().getId()), LocationType.PORT, closestPort.getPort().getCode(), closestPort.getPort().getName(), closestPort.getDistance(), UnitType.METERS, closestPort.getPort().getCentroid(), closestPort.getPort().getGeometry(), closestPort.getPort().getExtent(), closestPort.getPort().getEnabled(), closestPort.getPort().getCountryCode());
            response.add(location);
            return Response.ok(response).build();
        } catch (Exception e) {
            log.error(e.toString(),e);
            return Response.status(500).build();
        }
    }



    private static final double DISTANCE_TO_PORT_THRESHOLD_IN_NAUTICAL_MILES = 1.5;   //meters = 2778
    private static final double FACTOR_METER_PER_SECOND_TO_KNOTS = 1.9438444924574;
    private static final double NAUTICAL_MILE_ONE_METER = 0.000539956803;
    @POST
    @Path("getSegmentCategoryType")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getSegmentCategoryType2(List<MovementType> movements) {

        SegmentCategoryType returnVal = SegmentCategoryType.OTHER;
        try {
            if(movements == null || movements.size() < 2){
                log.error("Null as indata or not enough movements in the the input list, need at least two");
                return Response.status(400).entity("Null as indata or not enough movements in the the input list, need at least two").build();
            }

            MovementType move1 = null;
            MovementType move2 = null;
            if(movements.get(0).getPositionTime().before(movements.get(1).getPositionTime())) {   //if the first thing in the list is chronologically first as well
                 move1 = movements.get(0);
                 move2 = movements.get(1);
            }else {
                move1 = movements.get(1);
                move2 = movements.get(0);
            }

            Point movePoint1 = (Point) GeometryUtils.createPoint(move1.getPosition().getLatitude(), move1.getPosition().getLongitude());     //this magical int is the World Geodetic System 1984, aka EPSG:4326. See: https://en.wikipedia.org/wiki/World_Geodetic_System or http://spatialreference.org/ref/epsg/wgs-84/
            Point movePoint2 = (Point) GeometryUtils.createPoint(move2.getPosition().getLatitude(), move2.getPosition().getLongitude());

            List<PortAreaEntity> portsForMove1 = areaServiceBean.getPortAreasByPoint(movePoint1);
            List<PortAreaEntity> portsForMove2 = areaServiceBean.getPortAreasByPoint(movePoint2);


            List<String> portRequestList = new ArrayList<>();
            //for the first move
            for(PortAreaEntity area : portsForMove1){
                portRequestList.add(area.getCode());
            }
            //and for the second move
            for(PortAreaEntity area : portsForMove2){
                portRequestList.add(area.getCode());
            }

            WKTReader reader = new WKTReader();


            List<PortEntity> portList = areaServiceBean.getPortsByAreaCodes(portRequestList);
            double movePortDistance1 = 2778d;               // 1.5 nautical miles is 2778 meters, aka the radius of the port area
            double movePortDistance2 = 2778d;
            PortEntity closestPort1 = null;
            PortEntity closestPort2 = null;

            //Note: The port areas in the DB seems to be slightly different then the definition of 1.5 nautical miles around point p. Thus we can get some unexpected results when the DB says that we are not in area a while the distance to point p is lower then 2778 meters. The most notably effect of this is that track logic regarding when to create new tracks occasionally fails.
            //Since the current track logic is not very useful (or sane.....) anyway I will not fix this for now
            for(PortEntity port : portList){   //loop over ports
                Point portPoint = port.getGeom().getCentroid();

                double dist = distanceMeter(portPoint.getY(), portPoint.getX(), movePoint1.getY(), movePoint1.getX());
                if(dist < movePortDistance1){
                    movePortDistance1 = dist;
                    closestPort1 = port;
                }
                dist = distanceMeter(portPoint.getY(), portPoint.getX(), movePoint2.getY(), movePoint2.getX());
                if(dist < movePortDistance2){
                    movePortDistance2 = dist;
                    closestPort2 = port;
                }

            }

            //and the logic, first if we are nowhere near a port
            if(closestPort1 == null && closestPort2 == null){
                if(move1.getPositionTime().getTime() - move2.getPositionTime().getTime() == 0){    //no duration between moves
                    returnVal = SegmentCategoryType.NULL_DUR;
                }else if(((distanceMeter(movePoint1.getY(), movePoint1.getX(), movePoint2.getY(), movePoint2.getX()) / Math.abs(move1.getPositionTime().getTime() - move2.getPositionTime().getTime())) * FACTOR_METER_PER_SECOND_TO_KNOTS ) < 0.00001){    //if the average speed is 'zero'
                    returnVal = SegmentCategoryType.ANCHORED;
                }else if(((distanceMeter(movePoint1.getY(), movePoint1.getX(), movePoint2.getY(), movePoint2.getX()) / Math.abs(move1.getPositionTime().getTime() - move2.getPositionTime().getTime())) * FACTOR_METER_PER_SECOND_TO_KNOTS ) > 50 ||    //speed is over 50
                        ((distanceMeter(movePoint1.getY(), movePoint1.getX(), movePoint2.getY(), movePoint2.getX()) * NAUTICAL_MILE_ONE_METER) > 250 && Math.abs((move1.getPositionTime().getTime() / 1000) - (move2.getPositionTime().getTime()) / 1000) > 12 )){  //OR distance is grater the n250 nautical miles and duration is longer then 12 seconds (this last one feels wierd)
                    returnVal = SegmentCategoryType.JUMP;
                }else if(Math.abs((move1.getPositionTime().getTime() / 1000) - (move2.getPositionTime().getTime()) / 1000) > 12){  //if there is longer then 12 seconds between points
                    returnVal = SegmentCategoryType.GAP;
                }
            }else{   //and if we are
                if(closestPort1 != null && closestPort2 != null){   //if they are both in a port zone
                    if(closestPort1.equals(closestPort2)){          //if they are in the same port zone
                        returnVal = SegmentCategoryType.IN_PORT;    //if one move is in a port and the other move is in a different port, what kind of category is it supposed to be?????
                    }

                }else if(closestPort1 != null && closestPort2 == null){     //start in port
                    returnVal = SegmentCategoryType.EXIT_PORT;
                }else if(closestPort1 == null && closestPort2 != null) {    //end in port
                    returnVal = SegmentCategoryType.ENTER_PORT;
                }
            }




        } catch (Exception e) {
            log.error(e.toString(), e);
            return Response.status(500).entity(e.toString()).build();
        }
        return Response.status(200).entity(returnVal).build();
    }

    private static final int EARTH_RADIUS_METER = 6371000;
    private static double distanceMeter(double prevLat, double prevLon, double currentLat, double currentLon) {
        double lat1Rad = Math.toRadians(prevLat);
        double lat2Rad = Math.toRadians(currentLat);
        double deltaLonRad = Math.toRadians(currentLon - prevLon);

        return Math.acos(Math.sin(lat1Rad) * Math.sin(lat2Rad) + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.cos(deltaLonRad))
                * EARTH_RADIUS_METER;
    }


    @GET
    @Path("getEnrichmentAndTransitions")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getEnrichmentAndTransitions2(@QueryParam(value = "firstLongitude") Double firstLongitude, @QueryParam(value = "firstLatitude") Double firstLatitude, @QueryParam(value = "secondLongitude") Double secondLongitude, @QueryParam(value = "secondLatitude") Double secondLatitude) {

        try {
            if(secondLongitude == null || secondLatitude == null){
                log.error("Null as indata on the second long or latitude");
                return Response.status(400).entity("Null as indata on the second long or latitude").build();
            }

            //assuming that the incoming coordinates are in 4326 aka the magical int that is the World Geodetic System 1984, aka EPSG:4326. See: https://en.wikipedia.org/wiki/World_Geodetic_System or http://spatialreference.org/ref/epsg/wgs-84/
            PointType point;
            ArrayList<AreaExtendedIdentifierType> firstAreas = new ArrayList<>();
            if(firstLongitude != null && firstLatitude != null) {
                List<BaseAreaDto> areaList = areaServiceBean.getAreasByPoint(firstLatitude, firstLongitude);
                for (BaseAreaDto bad: areaList) {
                    AreaExtendedIdentifierType areaExtendedIdentifierType = new AreaExtendedIdentifierType(String.valueOf(bad.getGid()),bad.getType(),bad.getCode(), bad.getName());
                    firstAreas.add(areaExtendedIdentifierType);
                }
            }

            point = new PointType(secondLongitude, secondLatitude, 4326);

            SpatialEnrichmentRQ spatialEnrichmentRQ = new SpatialEnrichmentRQ(null, point, new SpatialEnrichmentRQ.AreaTypes(), new SpatialEnrichmentRQ.LocationTypes(), UnitType.NAUTICAL_MILES);
            SpatialEnrichmentRS spatialEnrichmentRS = areaServiceBean.getSpatialEnrichment(spatialEnrichmentRQ);           //this one is hardcoded to return distance in nautical miles right now

            ArrayList<AreaExtendedIdentifierType> secondAreas = (ArrayList<AreaExtendedIdentifierType>)spatialEnrichmentRS.getAreasByLocation().getAreas();


            ArrayList<AreaExtendedIdentifierType> exitedAreas = (ArrayList<AreaExtendedIdentifierType>)firstAreas.clone();
            exitedAreas.removeAll(secondAreas);

            ArrayList<AreaExtendedIdentifierType> enteredAreas = (ArrayList<AreaExtendedIdentifierType>)secondAreas.clone();
            enteredAreas.removeAll(firstAreas);

            AreaTransitionsDTO response = new AreaTransitionsDTO();
            response.setExitedAreas(exitedAreas);
            response.setEnteredAreas(enteredAreas);
            response.setSpatialEnrichmentRS(spatialEnrichmentRS);

            return Response.ok(response).build();


        } catch (Exception e) {
            log.error(e.toString(),e);
            return Response.status(500).entity(e.toString()).build();
        }
    }

    @POST
    @Path("getEnrichment")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getEnrichment2(SpatialEnrichmentRQ spatialEnrichmentRQ) {

        try {
            SpatialEnrichmentRS response = areaServiceBean.getSpatialEnrichment(spatialEnrichmentRQ);
            return Response.ok(response).build();
        } catch (Exception e) {
            log.error(e.toString(),e);
            return Response.status(500).build();
        }
    }


    @POST
    @Path("getEnrichmentBatch")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getEnrichmentBatch2(BatchSpatialEnrichmentRQ batchSpatialEnrichmentRQ){         //should we really support this?

        try {
            /*BatchSpatialEnrichmentRS response = enrichmentService.getBatchSpatialEnrichment(batchSpatialEnrichmentRQ);
            return Response.ok(response).build();*/
            return Response.status(501).build();
        } catch (Exception e) {
            log.error(e.toString(),e);
            return Response.status(500).build();
        }
    }

    @POST
    @Path("getFilterArea")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getFilterArea2(FilterAreasSpatialRQ filterAreasSpatialRQ) {

        try {
            /*FilterAreasSpatialRS response = areaService.computeAreaFilter(filterAreasSpatialRQ);
            return Response.ok(response).build();*/
            return Response.status(501).build();
        } catch (Exception e) {
            log.error(e.toString(),e);
            return Response.status(500).build();
        }
    }


    @POST
    @Path("getMapConfiguration")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getMapConfiguration2(SpatialGetMapConfigurationRQ spatialGetMapConfigurationRQ) {

        try {
            /*SpatialGetMapConfigurationRS response = mapConfigService.getMapConfiguration(spatialGetMapConfigurationRQ);
            return Response.ok(response).build();*/
            return Response.status(501).build();
        } catch (Exception e) {
            log.error(e.toString(),e);
            return Response.status(500).build();
        }
    }
    
    @POST
    @Path("ping")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response ping2(PingRQ pingrq) {

        try {
            PingRS response = new PingRS();
            response.setResponse("pong");

            return Response.ok(response).build();
        } catch (Exception e) {
            log.error(e.toString(), e);
            return Response.status(500).build();
        }
    }

    @POST
    @Path("getAreaByCode")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getAreaByCode2(AreaByCodeRequest areaByCodeRequest) {

        try {
            List<AreaSimpleType> areaSimpleTypeList = areaServiceBean.getAreasByCode(areaByCodeRequest);
            AreaByCodeResponse response = new AreaByCodeResponse();
            response.setAreaSimples(areaSimpleTypeList);
            return Response.ok(response).build();
        } catch (Exception e) {
            log.error(e.toString(), e);
            return Response.status(500).build();
        }
    }

    @POST
    @Path("getGeometryByPortCode")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getGeometryByPortCode2(GeometryByPortCodeRequest geometryByPortCodeRequest) {
        try {
            List<String> portCode = new ArrayList<>();
            portCode.add(geometryByPortCodeRequest.getPortCode());
            List<PortEntity> portList = areaServiceBean.getPortsByAreaCodes(portCode);
            String geometry = (portList.isEmpty() ? "" : portList.get(0).getGeometry());
            GeometryByPortCodeResponse response = new GeometryByPortCodeResponse();
            response.setPortGeometry(geometry);
            return Response.ok(response).build();

        } catch (Exception e) {
            log.error(e.toString(), e);
            return Response.status(500).build();
        }
    }
}
