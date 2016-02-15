package eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured;

import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ExceptionInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.CountryService;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by padhyad on 11/18/2015.
 */


@Path("/countries")
@Slf4j
@Stateless
public class CountryResource extends UnionVMSResource {

    @EJB
    private CountryService countryService;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response getAllCountriesDesc() {
        return createSuccessResponse(countryService.getAllCountriesDesc());
    }
}
