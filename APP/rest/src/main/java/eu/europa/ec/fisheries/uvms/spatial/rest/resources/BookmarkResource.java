package eu.europa.ec.fisheries.uvms.spatial.rest.resources;

import eu.europa.ec.fisheries.uvms.constants.AuthConstants;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.spatial.model.bookmark.Bookmark;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ExceptionInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.bookmark.BookmarkService;
import lombok.extern.slf4j.Slf4j;
import javax.ejb.EJB;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

@Path("/bookmark")
@Slf4j
public class BookmarkResource extends UnionVMSResource {

    public static final String USM_APPLICATION = "usmApplication";

    @EJB
    private BookmarkService bookmarkService;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response list(@Context HttpServletRequest request,
                         @HeaderParam(AuthConstants.HTTP_HEADER_SCOPE_NAME) String scopeName,
                         @HeaderParam(AuthConstants.HTTP_HEADER_ROLE_NAME) String roleName) throws ServiceException {

        final String username = request.getRemoteUser();

        final String applicationName = request.getServletContext().getInitParameter(USM_APPLICATION);

        try {

            Set<Bookmark> bookmarks = bookmarkService.listByUsername(username, scopeName, roleName, applicationName);
            return createSuccessResponse(bookmarks);

        }
        catch (ServiceException e){
            return createErrorResponse("Unable to get user features from USM. Reason: " + e.getMessage());
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createBookmark(@Context HttpServletRequest request,
                                   @Context HttpServletResponse response,
                                   @HeaderParam(AuthConstants.HTTP_HEADER_SCOPE_NAME) String scopeName,
                                   @HeaderParam(AuthConstants.HTTP_HEADER_ROLE_NAME) String roleName,
                                   final Bookmark bookmark) {

        final String username = request.getRemoteUser();

        final String applicationName = request.getServletContext().getInitParameter(USM_APPLICATION);

        try {

            final Bookmark result = bookmarkService.create(bookmark, username, scopeName, roleName, applicationName);
            return createSuccessResponse(result);

        }
        catch (ServiceException e){
            return createErrorResponse("Unable to get user features from USM. Reason: " + e.getMessage());
        }

    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteReport(@Context HttpServletRequest request,
                                 @Context HttpServletResponse response,
                                 @PathParam("id") Long id,
                                 @HeaderParam(AuthConstants.HTTP_HEADER_SCOPE_NAME) String scopeName,
                                 @HeaderParam(AuthConstants.HTTP_HEADER_ROLE_NAME) String roleName) {

        final String username = request.getRemoteUser();

        final String applicationName = request.getServletContext().getInitParameter(USM_APPLICATION);

        try {

            bookmarkService.delete(id, username, scopeName, roleName, applicationName);
            return createSuccessResponse();

        }
        catch (ServiceException e){
            return createErrorResponse("Unable to get user features from USM. Reason: " + e.getMessage());
        }

    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateBookmark(@Context HttpServletRequest request,
                                   @Context HttpServletResponse response,
                                   Bookmark bookmark, @PathParam("id") Long id,
                                   @HeaderParam(AuthConstants.HTTP_HEADER_SCOPE_NAME) String scopeName,
                                   @HeaderParam(AuthConstants.HTTP_HEADER_ROLE_NAME) String roleName) {

        final String username = request.getRemoteUser();

        final String applicationName = request.getServletContext().getInitParameter(USM_APPLICATION);

        try {
            bookmark.setId(id);
            bookmarkService.update(bookmark, username, scopeName, roleName, applicationName);
            return createSuccessResponse();

        }
        catch (ServiceException e){
            return createErrorResponse("Unable to get user features from USM. Reason: " + e.getMessage());
        }
    }
}

