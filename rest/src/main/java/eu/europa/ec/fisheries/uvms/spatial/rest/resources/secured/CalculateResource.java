/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured;

import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialService;
import lombok.extern.slf4j.Slf4j;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("/calculate")
@Slf4j
public class CalculateResource extends UnionVMSResource {

    @EJB
    private SpatialService service;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/buffer")
    public Response buffer(Map<String, Object> payload) {

        Response response;

        try {
            Double latitude = Double.valueOf(String.valueOf(payload.get("latitude")));
            Double longitude = Double.valueOf(String.valueOf(payload.get("longitude")));
            Double buffer = Double.valueOf(String.valueOf(payload.get("buffer")));
            response = createSuccessResponse(service.calculateBuffer(latitude, longitude, buffer));
        } catch (Exception ex) {
            String error = "[ Error when calculating buffer. ] ";
            log.error(error, ex);
            response = createErrorResponse(error);
        }

        return response;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/translate")
    public Response translate(Map<String, Object> payload){

        Response response;

        try {
            Double latitude = Double.valueOf(String.valueOf(payload.get("x")));
            Double longitude = Double.valueOf(String.valueOf(payload.get("y")));
            String wkt = String.valueOf(String.valueOf(payload.get("wkt")));
            response = createSuccessResponse(service.translate(latitude, longitude, wkt));
        }

        catch (Exception ex){
            String error = "[ Error when calculating buffer. ] ";
            log.error(error, ex);
            response = createErrorResponse(error);
        }

        return response;
    }
}