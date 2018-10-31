package eu.europa.ec.fisheries.uvms.spatial.rest.resources.unsecured;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTReader;
import eu.europa.ec.fisheries.schema.movement.v1.MovementMetaData;
import eu.europa.ec.fisheries.schema.movement.v1.MovementType;
import eu.europa.ec.fisheries.schema.movement.v1.SegmentCategoryType;
import eu.europa.ec.fisheries.uvms.commons.geometry.utils.GeometryUtils;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaTypeNamesService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.MapConfigService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.impl.SpatialEnrichmentServiceBean;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.PortDao;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.PortEntity;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Rest endpoints for faster dataretrieval
 * We use POST for convinience also for fetch, since it it easy to pass Requests as java Objects
 * <p>
 * OBS we will initially mostly only implement methods  used from Movement and Reporting
 */


@Path("json")
@SuppressWarnings("unchecked")
@Slf4j
public class SpatialRestResource {

//    private static final ObjectMapper MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).registerModule(new JavaTimeModule());
    private static final ObjectMapper MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


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
            List<Area> response = areaService.getClosestArea(lon, lat, crs, unit);
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
            List<Location> response = areaService.getClosestPointByPoint(closestLocationSpatialRQ);
            return Response.ok(response).build();
        } catch (Exception e) {
            log.error(e.toString(),e);
            return Response.status(500).build();
        }
    }


    @PersistenceContext(unitName = "spatialPUpostgres")
    private EntityManager postgres;


    PortDao portDao = new PortDao(postgres);

    @POST
    @Path("pong")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response pong(String s) {
        return Response.ok().build();
    }

    @GET
    @Path("pong2")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response pong2() {
        return Response.ok("Pong2").build();
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

            MovementType move1 = movements.get(0);
            MovementType move2 = movements.get(1);

            Point movePoint1 = (Point) GeometryUtils.toGeographic(move1.getPosition().getLatitude(), move1.getPosition().getLongitude(), 4326);     //this magical int is the World Geodetic System 1984, aka EPSG:4326. See: https://en.wikipedia.org/wiki/World_Geodetic_System or http://spatialreference.org/ref/epsg/wgs-84/
            Point movePoint2 = (Point) GeometryUtils.toGeographic(move2.getPosition().getLatitude(), move2.getPosition().getLongitude(), 4326);

            List<AreaExtendedIdentifierType> portsForMove1 = areaService.getPortAreasByPoint(movePoint1);
            List<AreaExtendedIdentifierType> portsForMove2 = areaService.getPortAreasByPoint(movePoint2);


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


            List<PortEntity> portList = portDao.byCode(portRequestList);
            double movePortDistance1 = Math.pow(2778, 2);               // 1.5 nautical miles is 2778 meters, 2778 ^ 2 to make it in the same ballpark as the distance test
            double movePortDistance2 = Math.pow(2778, 2);
            PortEntity closest1 = null;
            PortEntity closest2 = null;
            for(PortEntity port : portList){   //loop over ports

                Point portPoint = (Point)reader.read(port.getGeometry());
                double dist = distanceWoSquareRoot(portPoint, movePoint1);
                if(dist < movePortDistance1){
                    movePortDistance1 = dist;
                    closest1 = port;
                }
                if(dist < movePortDistance2){
                    movePortDistance2 = dist;
                    closest2 = port;
                }

            }


            //and the logic, first if we are nowhere near a port
            if(closest1 == null && closest2 == null){
                if(move1.getPositionTime().getTime() - move2.getPositionTime().getTime() == 0){    //no duration between moves
                    returnVal = SegmentCategoryType.NULL_DUR;
                }else if(((distanceMeter(movePoint1.getY(), movePoint1.getX(), movePoint2.getY(), movePoint2.getX()) / move1.getPositionTime().getTime() - move2.getPositionTime().getTime()) * FACTOR_METER_PER_SECOND_TO_KNOTS ) < 0.00001){    //if the average speed is 'zero'
                    returnVal = SegmentCategoryType.ANCHORED;
                }else if(((distanceMeter(movePoint1.getY(), movePoint1.getX(), movePoint2.getY(), movePoint2.getX()) / move1.getPositionTime().getTime() - move2.getPositionTime().getTime()) * FACTOR_METER_PER_SECOND_TO_KNOTS ) > 50 ||    //speed is over 50
                        ((distanceMeter(movePoint1.getY(), movePoint1.getX(), movePoint2.getY(), movePoint2.getX()) * NAUTICAL_MILE_ONE_METER) > 250 && Math.abs((move1.getPositionTime().getTime() / 1000) - (move2.getPositionTime().getTime()) / 1000) > 12 )){  //OR distance is grater the n250 nautical miles and duration is longer then 12 seconds (this last one feels wierd)
                    returnVal = SegmentCategoryType.JUMP;
                }else if(Math.abs((move1.getPositionTime().getTime() / 1000) - (move2.getPositionTime().getTime()) / 1000) > 12){  //if there is longer then 12 seconds between points
                    returnVal = SegmentCategoryType.GAP;
                }
            }else{   //and if we are
                if(closest1 != null && closest2 != null){   //if they are both in a port zone
                    if(closest1.equals(closest2)){          //if they are in the same port zone
                        returnVal = SegmentCategoryType.IN_PORT;    //if one move is in a port and the other move is in a different port, what kind of category is it supposed to be?????
                    }

                }else if(closest1 != null && closest2 == null){     //start in port
                    returnVal = SegmentCategoryType.EXIT_PORT;
                }else if(closest1 == null && closest2 != null) {    //end in port
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

    private double distanceWoSquareRoot(Point p1, Point p2){
        return Math.pow(p1.getX() - p2.getX() , 2) + Math.pow(p1.getY() - p2.getY() , 2);
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
            FilterAreasSpatialRS response = areaService.computeAreaFilter(filterAreasSpatialRQ);
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
            SpatialGetMapConfigurationRS response = mapConfigService.getMapConfiguration(spatialGetMapConfigurationRQ);
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


    @POST
    @Path("getAreaByCode")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getAreaByCode(AreaByCodeRequest areaByCodeRequest) {

        try {
            List<AreaSimpleType> areaSimples = areaByCodeRequest.getAreaSimples();
            List<AreaSimpleType> areaSimpleTypeList = areaService.getAreasByCode(areaSimples);
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
