/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured;

import javax.ejb.EJB;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eu.europa.ec.fisheries.uvms.constants.AuthConstants;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ExceptionInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.BookmarkService;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.bookmark.Bookmark;
import lombok.extern.slf4j.Slf4j;

@Path("/bookmark")
@Slf4j
public class BookmarkResource extends UnionVMSResource {

    @EJB
    private BookmarkService bookmarkService;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response list(@Context HttpServletRequest request,
                         @HeaderParam(AuthConstants.HTTP_HEADER_SCOPE_NAME) String scopeName,
                         @HeaderParam(AuthConstants.HTTP_HEADER_ROLE_NAME) String roleName) throws ServiceException {

        final String username = request.getRemoteUser();
        List<Bookmark> bookmarks = new ArrayList<>(bookmarkService.listByUsername(username));
        Collections.sort(bookmarks);
        return createSuccessResponse(bookmarks);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response createBookmark(@Context HttpServletRequest request,
                                   @HeaderParam(AuthConstants.HTTP_HEADER_SCOPE_NAME) String scopeName,
                                   @HeaderParam(AuthConstants.HTTP_HEADER_ROLE_NAME) String roleName,
                                   final Bookmark bookmark) throws ServiceException {

        final String username = request.getRemoteUser();
        final Bookmark result = bookmarkService.create(bookmark, username);
        return createSuccessResponse(result);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response deleteReport(@Context HttpServletRequest request,
                                 @PathParam("id") Long id,
                                 @HeaderParam(AuthConstants.HTTP_HEADER_SCOPE_NAME) String scopeName,
                                 @HeaderParam(AuthConstants.HTTP_HEADER_ROLE_NAME) String roleName) throws ServiceException {

        final String username = request.getRemoteUser();
        bookmarkService.delete(id, username);
        return createSuccessResponse();
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response updateBookmark(@Context HttpServletRequest request,
                                   Bookmark bookmark, @PathParam("id") Long id,
                                   @HeaderParam(AuthConstants.HTTP_HEADER_SCOPE_NAME) String scopeName,
                                   @HeaderParam(AuthConstants.HTTP_HEADER_ROLE_NAME) String roleName) throws ServiceException {

        final String username = request.getRemoteUser();
        bookmark.setId(id);
        bookmarkService.update(bookmark, username);
        return createSuccessResponse();
    }
}