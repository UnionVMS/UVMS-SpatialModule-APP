/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured;

import eu.europa.ec.fisheries.uvms.constants.AuthConstants;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.rest.constants.ErrorCodes;
import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.rest.security.bean.USMService;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.MapConfigurationType;
import eu.europa.ec.fisheries.uvms.spatial.rest.type.MapSettingsType;
import eu.europa.ec.fisheries.uvms.spatial.service.MapConfigService;
import eu.europa.ec.fisheries.uvms.spatial.util.ServiceLayerUtils;
import eu.europa.ec.fisheries.wsdl.user.types.Dataset;
import lombok.extern.slf4j.Slf4j;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Path("/mapconfig")
@Slf4j
@Stateless
public class MapConfigResource extends UnionVMSResource {

    @EJB
    private MapConfigService mapConfigService;

    @EJB
    private USMService usmService;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("{id}")
    public Response getMapConfigBy(@PathParam("id") Integer reportId,
                                   @Context HttpServletRequest request,
                                   @HeaderParam(AuthConstants.HTTP_HEADER_SCOPE_NAME) String scopeName,
                                   @HeaderParam(AuthConstants.HTTP_HEADER_ROLE_NAME) String roleName) {

        log.info("Getting map settings for report with id = {}", reportId);

        Response response;

        try {
            Collection<String> permittedLayersNames = ServiceLayerUtils.getUserPermittedLayersNames(usmService, request.getRemoteUser(), roleName, scopeName);

            MapConfigurationType mapConfigurationType = mapConfigService.getMapConfigurationType(Long.valueOf(reportId), permittedLayersNames);

            response = createSuccessResponse(new MapSettingsType(mapConfigurationType));

        } catch (ServiceException ex) {

            log.error("[ Error when getting map settings. ] ", ex);

            response = createErrorResponse(ErrorCodes.INTERNAL_SERVER_ERROR);

        }

        return response;

    }

}