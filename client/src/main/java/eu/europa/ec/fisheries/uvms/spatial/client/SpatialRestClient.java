/*
﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
© European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.spatial.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ContextResolver;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Stateless
public class SpatialRestClient {

    private WebTarget webTarget;

    @Resource(name = "java:global/spatial_endpoint")
    private String spatialEndpoint;
    
    @PostConstruct
    public void initClient() {
        String url = spatialEndpoint + "/spatialnonsecure/json/";

        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        clientBuilder.connectTimeout(30, TimeUnit.SECONDS);
        clientBuilder.readTimeout(30, TimeUnit.SECONDS);
        Client client = clientBuilder.build();
        client.register(new ContextResolver<ObjectMapper>() {
            @Override
            public ObjectMapper getContext(Class<?> type) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JaxbAnnotationModule());
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                return mapper;
            }
        });
        webTarget = client.target(url);
    }

    public String getGeometryByPortCode(String portCode, SpatialModuleMethod method) {
        GeometryByPortCodeRequest geometryByPortCodeRequest = new GeometryByPortCodeRequest();
        geometryByPortCodeRequest.setPortCode(portCode);
        geometryByPortCodeRequest.setMethod(method);

        Response response = webTarget
                .path("getGeometryByPortCode")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(geometryByPortCodeRequest), Response.class);

        GeometryByPortCodeResponse geometryByPortCodeResponse = response.readEntity(GeometryByPortCodeResponse.class);
        return geometryByPortCodeResponse.getPortGeometry();
    }

    public String getFilteredAreaGeometry(Collection<AreaIdentifierType> areaIdentifiers) {
        FilterAreasSpatialRQ filterAreasSpatialRQ = new FilterAreasSpatialRQ();
        ScopeAreasType scopeAreas = new ScopeAreasType();
        UserAreasType userAreas = new UserAreasType();
        scopeAreas.getScopeAreas().addAll(areaIdentifiers); // Set scope areas received
        filterAreasSpatialRQ.setMethod(SpatialModuleMethod.GET_FILTER_AREA);
        filterAreasSpatialRQ.setScopeAreas(scopeAreas);
        filterAreasSpatialRQ.setUserAreas(userAreas);

        Response response = webTarget
                .path("getFilterArea")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(filterAreasSpatialRQ), Response.class);

        FilterAreasSpatialRS filterAreasSpatialRS = response.readEntity(FilterAreasSpatialRS.class);
        return filterAreasSpatialRS.getGeometry();
    }
}
