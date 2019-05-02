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
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

import eu.europa.ec.fisheries.uvms.commons.rest.constants.ErrorCodes;
import eu.europa.ec.fisheries.uvms.commons.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.rest.security.bean.USMService;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.MapConfigurationType;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.MapSettingsType;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.bean.MapConfigService;
import eu.europa.ec.fisheries.uvms.spatial.service.util.ServiceLayerUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @implicitParam roleName|string|header|true||||||
 * @implicitParam scopeName|string|header|true|EC|||||
 * @implicitParam authorization|string|header|true||||||jwt token
 */
@Path("/mapconfig")
@Slf4j
@Stateless
public class MapConfigResource extends UnionVMSResource {

    @EJB
    private MapConfigService mapConfigService;

    @EJB
    private USMService usmService;

    @Context
    private HttpServletRequest servletRequest;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("{id}")
    public Response getMapConfigBy(@PathParam("id") Integer reportId, @HeaderParam("scopeName") String scopeName, @HeaderParam("roleName") String roleName) {

        log.info("Getting mapDefaultSRIDToEPSG settings for report with id = {}", reportId);
        Response response;

        try {
            Collection<String> permittedLayersNames = ServiceLayerUtils.getUserPermittedLayersNames(usmService, servletRequest.getRemoteUser(), roleName, scopeName);
            MapConfigurationType mapConfigurationType = mapConfigService.getMapConfigurationType(Long.valueOf(reportId), permittedLayersNames);
            response = createSuccessResponse(new MapSettingsType(mapConfigurationType));

        } catch (ServiceException ex) {
            log.error("[ Error when getting mapDefaultSRIDToEPSG settings. ] ", ex);
            response = createErrorResponse(ErrorCodes.INTERNAL_SERVER_ERROR);
        }
        return response;

    }

}