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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @implicitParam roleName|string|header|true||||||
 * @implicitParam scopeName|string|header|true|EC|||||
 * @implicitParam authorization|string|header|true||||||jwt token
 */
//@Path("/files")
public class FileUploadResource extends UnionVMSResource {

    private static final Logger log = LoggerFactory.getLogger(FileUploadResource.class);

/*


    @EJB
    private AreaService areaService;

    @POST
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Path("/upload/{type}/{code}")
    @Interceptors(value = {ValidationInterceptor.class, ExceptionInterceptor.class})
    public Response upload(UploadMapping mapping,
                           @PathParam("type") String type,
                           @PathParam("code") int code) throws ServiceException {

        areaService.upload(mapping, type, code);
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


 */
}