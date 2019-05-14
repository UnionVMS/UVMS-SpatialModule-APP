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
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialFeaturesEnum;
import eu.europa.ec.fisheries.uvms.spatial.rest.constants.ErrorCodes;
import eu.europa.ec.fisheries.uvms.spatial.rest.mapper.AreaLocationMapper;
import eu.europa.ec.fisheries.uvms.spatial.rest.util.ExceptionInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaServiceBean;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.AreaDao;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.UserAreasEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @implicitParam roleName|string|header|true||||||
 * @implicitParam scopeName|string|header|true|EC|||||
 * @implicitParam authorization|string|header|true||||||jwt token
 */
@Path("/userarea")
@Stateless
public class UserAreaResource extends UnionVMSResource {

    private static final Logger log = LoggerFactory.getLogger(ConfigResource.class);

    @Inject
    AreaServiceBean areaServiceBean;

    private AreaLocationMapper areaLocationMapper = AreaLocationMapper.mapper();

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/")
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response upsertUserArea(UserAreasEntity userArea, @Context HttpServletRequest servletRequest) {
        String userName = servletRequest.getRemoteUser();
        log.info("{} is requesting createUserArea(...)", userName);
        userArea.setUserName(userName);
        return createSuccessResponse(areaServiceBean.upsertUserArea(userArea));
    }




    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/layers")
    @Interceptors(value = {ExceptionInterceptor.class})
    public Response getUserAreaLayerMapping(@HeaderParam("scopeName") String scopeName, @Context HttpServletRequest servletRequest) {
        log.debug("UserName from security : " + servletRequest.getRemoteUser());
        return createSuccessResponse(areaServiceBean.getUserAreaLayerDefinition(servletRequest.getRemoteUser(), scopeName));
    }


    private boolean isPowerUser(HttpServletRequest request) {
        return request.isUserInRole("MANAGE_ANY_USER_AREA");
    }


    @Inject
    AreaDao areaDao;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/list")
    public Response listUserAreas(@HeaderParam("scopeName") String scopeName, @Context HttpServletRequest servletRequest) {
        Response response;

        if (servletRequest.isUserInRole(SpatialFeaturesEnum.MANAGE_USER_DEFINED_AREAS.toString())) {
            response = createSuccessResponse(areaDao.findByUserNameScopeNameAndPowerUser(servletRequest.getRemoteUser(), scopeName, isPowerUser(servletRequest)));
        } else {
            response = createErrorResponse(ErrorCodes.NOT_AUTHORIZED);
        }
        return response;
    }

}