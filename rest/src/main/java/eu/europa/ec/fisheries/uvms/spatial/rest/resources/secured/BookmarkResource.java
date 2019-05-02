/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured;

import eu.europa.ec.fisheries.uvms.commons.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ExceptionInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.BookmarkService;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.bookmark.Bookmark;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @implicitParam roleName|string|header|true||||||
 * @implicitParam scopeName|string|header|true|EC|||||
 * @implicitParam authorization|string|header|true||||||jwt token
 */
@Path("/bookmark")
@Slf4j
public class BookmarkResource extends UnionVMSResource {

    @EJB
    private BookmarkService bookmarkService;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response list(@Context HttpServletRequest servletRequest) throws ServiceException {

        final String username = servletRequest.getRemoteUser();
        List<Bookmark> bookmarks = new ArrayList<>(bookmarkService.listByUsername(username));
        Collections.sort(bookmarks);
        return createSuccessResponse(bookmarks);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response createBookmark(final Bookmark bookmark, @Context HttpServletRequest servletRequest) throws ServiceException {

        final String username = servletRequest.getRemoteUser();
        final Bookmark result = bookmarkService.create(bookmark, username);
        return createSuccessResponse(result);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response deleteReport(@PathParam("id") Long id, @Context HttpServletRequest servletRequest) throws ServiceException {

        final String username = servletRequest.getRemoteUser();
        bookmarkService.delete(id, username);
        return createSuccessResponse();
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response updateBookmark(Bookmark bookmark, @PathParam("id") Long id, @Context HttpServletRequest servletRequest) throws ServiceException {

        final String username = servletRequest.getRemoteUser();
        bookmark.setId(id);
        bookmarkService.update(bookmark, username);
        return createSuccessResponse();
    }
}