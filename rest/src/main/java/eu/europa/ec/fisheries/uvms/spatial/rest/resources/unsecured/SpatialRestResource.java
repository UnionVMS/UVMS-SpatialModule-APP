package eu.europa.ec.fisheries.uvms.spatial.rest.resources.unsecured;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTReader;
import eu.europa.ec.fisheries.schema.movement.v1.MovementType;
import eu.europa.ec.fisheries.schema.movement.v1.SegmentCategoryType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.AreaTransitionsDTO;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaTypeNamesService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.MapConfigService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.impl.SpatialEnrichmentServiceBean;
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

//    private static final ObjectMapper MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).registerModule(new JavaTimeModule());
//    private static final ObjectMapper MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


    @Inject
    private SpatialEnrichmentServiceBean enrichmentService;

    @Inject
    private AreaService areaService;

    @Inject
    private AreaTypeNamesService areaTypeNamesService;

    @Inject
    private MapConfigService mapConfigService;


    @POST
    @Path("getAreaByLocation")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getAreaByLocation( AreaByLocationSpatialRQ areaByLocationSpatialRQ)  {

        try {
            List<AreaExtendedIdentifierType> response = areaService.getAreasByPoint(areaByLocationSpatialRQ);
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
    public Response getAreaTypes(AllAreaTypesRequest allAreaTypesRequest) {

        try {
            List<String> response = areaTypeNamesService.listAllAreaTypeNames();
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
    public Response getClosestArea(ClosestAreaSpatialRQ request)  {

        try {
            Double lat = request.getPoint().getLatitude();
            Double lon = request.getPoint().getLongitude();
            Integer crs = request.getPoint().getCrs();
            UnitType unit = request.getUnit();
            List<Area> response = null /*areaService.getClosestArea(lon, lat, crs, unit)*/;
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
    public Response getClosestLocation(ClosestLocationSpatialRQ closestLocationSpatialRQ) {

        try {
            List<Location> response = null /*areaService.getClosestPointByPoint(closestLocationSpatialRQ)*/;
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
    public Response getSegmentCategoryType(List<MovementType> movements) {

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

           // Point movePoint1 = (Point) GeometryUtils.toGeographic(move1.getPosition().getLatitude(), move1.getPosition().getLongitude(), 4326);     //this magical int is the World Geodetic System 1984, aka EPSG:4326. See: https://en.wikipedia.org/wiki/World_Geodetic_System or http://spatialreference.org/ref/epsg/wgs-84/
           // Point movePoint2 = (Point) GeometryUtils.toGeographic(move2.getPosition().getLatitude(), move2.getPosition().getLongitude(), 4326);

            Point movePoint1 = null;
            Point movePoint2 = null;
            List<AreaExtendedIdentifierType> portsForMove1 = null /*areaService.getPortAreasByPoint(movePoint1)*/;
            List<AreaExtendedIdentifierType> portsForMove2 = null /*areaService.getPortAreasByPoint(movePoint2)*/;


            List<AreaSimpleType> portRequestList = new ArrayList<>();
            //for the first move
            for(AreaExtendedIdentifierType area : portsForMove1){
                AreaSimpleType ast = new AreaSimpleType(AreaType.PORT.value(), area.getCode(), null);
                portRequestList.add(ast);
            }
            //and for the second move
            for(AreaExtendedIdentifierType area : portsForMove2){
                AreaSimpleType ast = new AreaSimpleType(AreaType.PORT.value(), area.getCode(), null);
                portRequestList.add(ast);
            }

            WKTReader reader = new WKTReader();


            List<AreaSimpleType> portList = null /*areaService.getAreasByCode(portRequestList)*/;
            double movePortDistance1 = 2778d;               // 1.5 nautical miles is 2778 meters, aka the radius of the port area
            double movePortDistance2 = 2778d;
            AreaSimpleType closestPort1 = null;
            AreaSimpleType closestPort2 = null;

            //Note: The port areas in the DB seems to be slightly different then the definition of 1.5 nautical miles around point p. Thus we can get some unexpected results when the DB says that we are not in area a while the distance to point p is lower then 2778 meters. The most notably effect of this is that track logic regarding when to create new tracks occasionally fails.
            //Since the current track logic is not very useful (or sane.....) anyway I will not fix this for now
            for(AreaSimpleType port : portList){   //loop over ports

                MultiPoint portMultiPoint = (MultiPoint)reader.read(port.getWkt());  //why do we store single points as multipoints?????
                GeometryFactory geoFactory = new GeometryFactory();
                Point portPoint = geoFactory.createPoint(portMultiPoint.getCoordinate());

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
    public Response getEnrichmentAndTransitions(@QueryParam(value = "firstLongitude") Double firstLongitude, @QueryParam(value = "firstLatitude") Double firstLatitude, @QueryParam(value = "secondLongitude") Double secondLongitude, @QueryParam(value = "secondLatitude") Double secondLatitude) {

        try {
            if(secondLongitude == null || secondLatitude == null){
                log.error("Null as indata on the second long or latitude");
                return Response.status(400).entity("Null as indata on the second long or latitude").build();
            }

            PointType point;
            ArrayList<AreaExtendedIdentifierType> firstAreas = new ArrayList<>();
            if(firstLongitude != null && firstLatitude != null) {
                 point = new PointType(firstLongitude, firstLatitude, 4326);   //this magical int is the World Geodetic System 1984, aka EPSG:4326. See: https://en.wikipedia.org/wiki/World_Geodetic_System or http://spatialreference.org/ref/epsg/wgs-84/
                AreaByLocationSpatialRQ request = new AreaByLocationSpatialRQ();
                request.setPoint(point);
                firstAreas = (ArrayList<AreaExtendedIdentifierType>) areaService.getAreasByPoint(request);
            }

            point = new PointType(secondLongitude, secondLatitude, 4326);

            SpatialEnrichmentRQ spatialEnrichmentRQ = new SpatialEnrichmentRQ(null, point, new SpatialEnrichmentRQ.AreaTypes(), new SpatialEnrichmentRQ.LocationTypes(), UnitType.NAUTICAL_MILES);
            SpatialEnrichmentRS spatialEnrichmentRS = enrichmentService.getSpatialEnrichment(spatialEnrichmentRQ);

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
    public Response getEnrichment(SpatialEnrichmentRQ spatialEnrichmentRQ) {

        try {
            SpatialEnrichmentRS response = enrichmentService.getSpatialEnrichment(spatialEnrichmentRQ);
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
    public Response getEnrichmentBatch(BatchSpatialEnrichmentRQ batchSpatialEnrichmentRQ){

        try {
            BatchSpatialEnrichmentRS response = enrichmentService.getBatchSpatialEnrichment(batchSpatialEnrichmentRQ);
            return Response.ok(response).build();
        } catch (Exception e) {
            log.error(e.toString(),e);
            return Response.status(500).build();
        }
    }

    @POST
    @Path("getFilterArea")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getFilterArea(FilterAreasSpatialRQ filterAreasSpatialRQ) {

        try {
            FilterAreasSpatialRS response = null/*areaService.computeAreaFilter(filterAreasSpatialRQ)*/;
            return Response.ok(response).build();
        } catch (Exception e) {
            log.error(e.toString(),e);
            return Response.status(500).build();
        }
    }


    @POST
    @Path("getMapConfiguration")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getMapConfiguration(SpatialGetMapConfigurationRQ spatialGetMapConfigurationRQ) {

        try {
            SpatialGetMapConfigurationRS response = null /*mapConfigService.getMapConfiguration(spatialGetMapConfigurationRQ)*/;
            return Response.ok(response).build();
        } catch (Exception e) {
            log.error(e.toString(),e);
            return Response.status(500).build();
        }
    }
    
    @POST
    @Path("ping")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response ping(PingRQ pingrq) {

        try {
            PingRS response = new PingRS();
            response.setResponse("pong");

            return Response.ok(response).build();
        } catch (Exception e) {
            log.error(e.toString(), e);
            return Response.status(500).build();
        }
    }

    @GET
    @Path("ping")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response pong2() {
        return Response.ok("Pong").build();
    }


    @POST
    @Path("getAreaByCode")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getAreaByCode(AreaByCodeRequest areaByCodeRequest) {

        try {
            List<AreaSimpleType> areaSimples = areaByCodeRequest.getAreaSimples();
            List<AreaSimpleType> areaSimpleTypeList = null/*areaService.getAreasByCode(areaSimples)*/;
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
    public Response getGeometryByPortCode(GeometryByPortCodeRequest geometryByPortCodeRequest) {
        try {
            String portCode=geometryByPortCodeRequest.getPortCode();
            String geometry= areaService.getGeometryForPort(portCode);
            GeometryByPortCodeResponse response = new GeometryByPortCodeResponse();
            response.setPortGeometry(geometry);
            return Response.ok(response).build();

        } catch (Exception e) {
            log.error(e.toString(), e);
            return Response.status(500).build();
        }
    }
}
