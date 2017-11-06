/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.enterprise.event.Event;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.Arrays;

import eu.europa.ec.fisheries.uvms.BaseUnitilsTest;
import eu.europa.ec.fisheries.uvms.commons.message.api.Fault;
import eu.europa.ec.fisheries.uvms.spatial.message.bean.SpatialEventMDB;
import eu.europa.ec.fisheries.uvms.spatial.message.bean.SpatialProducer;
import eu.europa.ec.fisheries.uvms.spatial.message.event.SpatialMessageEvent;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMarshallException;
import eu.europa.ec.fisheries.uvms.spatial.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.spatial.model.mapper.SpatialModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaByLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.FilterAreasSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.PointType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialEnrichmentRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialModuleMethod;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialModuleRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.UnitType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SpatialEventMDBTest extends BaseUnitilsTest {

    private static final double LATITUDE = 45.11557, LONGITUDE = -7.14925;
    private static final int CRS = 3857;

    @InjectMocks
    private SpatialEventMDB mdb = new SpatialEventMDB();

    @Mock
    private SpatialProducer producer;

    @Mock
    private Event<SpatialMessageEvent> areaByLocationSpatialEvent;

    @Mock
    private Event<SpatialMessageEvent> typeNamesEvent;

    @Mock
    private Event<SpatialMessageEvent> closestAreaSpatialEvent;

    @Mock
    private Event<SpatialMessageEvent> enrichmentSpatialEvent;

    @Mock
    private Event<SpatialMessageEvent> closestLocationSpatialEvent;

    @Mock
    private Event<SpatialMessageEvent> filterAreaSpatialEvent;

    @Mock
    TextMessage textMessage;

    @Test
    public void testOnMessageWithGetAreaByLocation() throws SpatialModelMarshallException, JMSException {
        PointType point = new PointType();
        point.setLongitude(LONGITUDE);
        point.setLatitude(LATITUDE);
        point.setCrs(CRS);
        String requestString = SpatialModuleRequestMapper.mapToCreateAreaByLocationRequest(point);

        when(textMessage.getText()).thenReturn(requestString);

        SpatialModuleRequest request = new AreaByLocationSpatialRQ();
        request.setMethod(SpatialModuleMethod.GET_AREA_BY_LOCATION);

        mdb.onMessage(textMessage);

        verify(areaByLocationSpatialEvent, times(1)).fire(any(SpatialMessageEvent.class));
        verify(producer, times(0)).sendFault(any(TextMessage.class), any(Fault.class));
    }

    @Test
    public void testOnMessageWithClosestAreaRequest() throws SpatialModelMarshallException, JMSException {
        PointType point = new PointType();
        point.setLongitude(LONGITUDE);
        point.setLatitude(LATITUDE);
        point.setCrs(CRS);

        String requestString = SpatialModuleRequestMapper.mapToCreateClosestAreaRequest(point, UnitType.METERS, Arrays.asList(AreaType.EEZ));
        when(textMessage.getText()).thenReturn(requestString);

        SpatialModuleRequest request = new ClosestAreaSpatialRQ();
        request.setMethod(SpatialModuleMethod.GET_CLOSEST_AREA);

        mdb.onMessage(textMessage);

        verify(closestAreaSpatialEvent, times(1)).fire(any(SpatialMessageEvent.class));
        verify(producer, times(0)).sendFault(any(TextMessage.class), any(Fault.class));
    }

    @Test
    public void testOnMessageWithClosestLocationRequest() throws SpatialModelMarshallException, JMSException {
        PointType point = new PointType();
        point.setLongitude(LONGITUDE);
        point.setLatitude(LATITUDE);
        point.setCrs(CRS);

        String requestString = SpatialModuleRequestMapper.mapToCreateClosestLocationRequest(point, UnitType.METERS, Arrays.asList(LocationType.PORT));
        when(textMessage.getText()).thenReturn(requestString);

        SpatialModuleRequest request = new ClosestLocationSpatialRQ();
        request.setMethod(SpatialModuleMethod.GET_CLOSEST_LOCATION);

        mdb.onMessage(textMessage);

        verify(closestLocationSpatialEvent, times(1)).fire(any(SpatialMessageEvent.class));
        verify(producer, times(0)).sendFault(any(TextMessage.class), any(Fault.class));
    }

    @Test
    public void testOnMessageWithEnrichmentRequest() throws SpatialModelMarshallException, JMSException {
        PointType point = new PointType();
        point.setLongitude(LONGITUDE);
        point.setLatitude(LATITUDE);
        point.setCrs(CRS);

        String requestString = SpatialModuleRequestMapper.mapToCreateSpatialEnrichmentRequest(point, UnitType.METERS, Arrays.asList(LocationType.PORT), Arrays.asList(AreaType.EEZ));
        when(textMessage.getText()).thenReturn(requestString);

        SpatialModuleRequest request = new SpatialEnrichmentRQ();
        request.setMethod(SpatialModuleMethod.GET_ENRICHMENT);

        mdb.onMessage(textMessage);

        verify(enrichmentSpatialEvent, times(1)).fire(any(SpatialMessageEvent.class));
        verify(producer, times(0)).sendFault(any(TextMessage.class), any(Fault.class));
    }

    @Test
    public void testOnMessageWithUnimplementedMethod() throws SpatialModelMarshallException, JMSException {
        PointType point = new PointType();
        point.setLongitude(LONGITUDE);
        point.setLatitude(LATITUDE);
        point.setCrs(CRS);

        SpatialModuleRequest request = new SpatialEnrichmentRQ();
        request.setMethod(SpatialModuleMethod.GET_BUFFER_GEOM);
        when(textMessage.getText()).thenReturn(JAXBMarshaller.marshall(request));

        mdb.onMessage(textMessage);

        verify(producer, times(1)).sendFault(any(TextMessage.class), any(Fault.class));
    }

    @Test
    public void testOnMessageWithFilterAreasMethod() throws SpatialModelMarshallException, JMSException {
        AreaIdentifierType areaType = new AreaIdentifierType();
        areaType.setAreaType(AreaType.EEZ);
        areaType.setId("1");

        String requestString = SpatialModuleRequestMapper.mapToFilterAreaSpatialRequest(Arrays.asList(areaType), Arrays.asList(areaType));
        when(textMessage.getText()).thenReturn(requestString);

        FilterAreasSpatialRQ request = new FilterAreasSpatialRQ();
        request.setMethod(SpatialModuleMethod.GET_FILTER_AREA);

        mdb.onMessage(textMessage);

        verify(filterAreaSpatialEvent, times(1)).fire(any(SpatialMessageEvent.class));
        verify(producer, times(0)).sendFault(any(TextMessage.class), any(Fault.class));
    }

}