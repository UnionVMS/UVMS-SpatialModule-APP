package eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.service.interceptor.ValidationInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.model.upload.UploadMetadata;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ExceptionInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.AreaService;
import java.io.IOException;
import javax.websocket.server.PathParam;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import javax.ejb.EJB;
import javax.interceptor.Interceptors;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.opengis.referencing.FactoryException;

@Path("/files")
public class FileUploadResource extends UnionVMSResource {

    @EJB
    private AreaService areaService;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/upload")
    @Interceptors(value = {ValidationInterceptor.class, ExceptionInterceptor.class})
    public Response uploadAreaFile(@MultipartForm FileUploadForm form) throws ServiceException {

        areaService.uploadArea(form.getData(), form.getAreaType(), form.getCrsCode());

        return createSuccessResponse();
    }

    @POST
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Path("/upload2") // TODO add path crs and areatype
    @Interceptors(value = {ValidationInterceptor.class, ExceptionInterceptor.class})
    public Response upload(UploadMetadata metadata,
                           @PathParam(value = "type") String type,
                           @PathParam(value = "code") String code) throws ServiceException, FactoryException, IOException { // TODO Excetopn wrap

        areaService.upload(metadata, type, code);
        return createSuccessResponse();
    }


    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/metadata")
    @Interceptors(value = {ValidationInterceptor.class, ExceptionInterceptor.class})
    public Response metadata(@MultipartForm FileUploadForm form) throws ServiceException {

        UploadMetadata metadata = areaService.metadata(form.getData(), form.getAreaType());

        return createSuccessResponse(metadata);
    }

}
