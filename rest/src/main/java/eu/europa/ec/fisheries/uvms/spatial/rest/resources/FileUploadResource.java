package eu.europa.ec.fisheries.uvms.spatial.rest.resources;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.service.interceptor.ValidationInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ExceptionInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaUploadService;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.ejb.EJB;
import javax.interceptor.Interceptors;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/files")
public class FileUploadResource extends UnionVMSResource {

    @EJB
    private AreaUploadService areaUploadService;

    @POST
    @Consumes("multipart/form-data")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/upload")
    @Interceptors(value = {ValidationInterceptor.class, ExceptionInterceptor.class})
    public Response uploadAreaFile(@MultipartForm FileUploadForm form) throws IOException, ServiceException {

        areaUploadService.uploadArea(form.getData(), form.getAreaType(), form.getCrsCode());

        return createSuccessResponse();
    }

}
