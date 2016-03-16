package eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured;

import java.io.IOException;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.vividsolutions.jts.io.ParseException;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.service.interceptor.ValidationInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationDetails;
import eu.europa.ec.fisheries.uvms.spatial.rest.type.geocoordinate.LocationCoordinateType;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.geojson.LocationDetailsGeoJsonDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.mapper.AreaLocationDtoMapper;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ExceptionInterceptor;
import lombok.extern.slf4j.Slf4j;

@Path("/")
@Slf4j
@Stateless
public class LocationResource extends UnionVMSResource {

    private @EJB SpatialService spatialService;
    private AreaLocationDtoMapper mapper = AreaLocationDtoMapper.mapper();
    
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/locationdetails") // FIXME native query alert
    @Interceptors(value = {ValidationInterceptor.class, ExceptionInterceptor.class})
    public Response getLocationDetails(LocationCoordinateType locationDto) throws IOException, ParseException, ServiceException {
    	LocationDetails locationDetails = spatialService.getLocationDetails(mapper.getLocationTypeEntry(locationDto));
    	LocationDetailsGeoJsonDto locationDetailsGeoJsonDto = mapper.getLocationDetailsDto(locationDetails);
    	return createSuccessResponse(locationDetailsGeoJsonDto.convert());
    }
}
