package eu.europa.ec.fisheries.uvms.spatial.rest.resources.unsecured;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaTypeNamesService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.MapConfigService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.impl.SpatialEnrichmentServiceBean;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
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
public class SpatialResource {

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
