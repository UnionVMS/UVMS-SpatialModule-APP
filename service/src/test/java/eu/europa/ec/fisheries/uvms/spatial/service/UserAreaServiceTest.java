/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/


package eu.europa.ec.fisheries.uvms.spatial.service;

import static org.junit.Assert.assertEquals;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.util.Assert;
import eu.europa.ec.fisheries.uvms.BaseUnitilsTest;
import eu.europa.ec.fisheries.uvms.TestToolBox;
import eu.europa.ec.fisheries.uvms.rest.security.bean.USMService;
import eu.europa.ec.fisheries.uvms.spatial.message.service.SpatialConsumerBean;
import eu.europa.ec.fisheries.uvms.spatial.message.service.UserProducerBean;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaProperty;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaTypeNamesService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.impl.UserAreaServiceBean;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.util.PostGres;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.area.UserAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.geojson.UserAreaGeoJsonDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.layer.UserAreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.UserAreasEntity;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.mock.Mock;

public class UserAreaServiceTest extends BaseUnitilsTest {

    @TestedObject
    private UserAreaServiceBean service = new UserAreaServiceBean();

    @InjectIntoByType
    private Mock<SpatialRepository> repoMock;

    @InjectIntoByType
    private Mock<AreaTypeNamesService> namesServiceMock;

    @InjectIntoByType
    private Mock<USMService> usmServiceMock;

    @InjectIntoByType
    private Mock<UserProducerBean> userProducer;

    @InjectIntoByType
    private Mock<SpatialConsumerBean> spatialConsumerBeanMock;

    private GeometryFactory geomFactory = new GeometryFactory();

    private Point point;

    @Before
    public void setup(){
        point = geomFactory.createPoint(new Coordinate(20.0535983848415, 31.1417484902222));
    }

    @Test
    @SneakyThrows
    public void testSearchUserAreasByCriteria(){

        UserAreasEntity build = UserAreasEntity.builder().geom(point).build();

        List<UserAreasEntity> list = new ArrayList<>();
        list.add(build);
        repoMock.returns(list).listUserAreaByCriteria(null, null, null, false);

        List<UserAreaDto> userAreaDtos = service.searchUserAreasByCriteria("rep_power", "EC", "invalid", false);

        Assert.equals("POINT (20.0535983848415 31.1417484902222)", userAreaDtos.get(0).getExtent());
        Assert.equals("USERAREA", userAreaDtos.get(0).getAreaType());

    }

    @Test
    @SneakyThrows
    public void getUserAreaDetailsWithExtentByIdWithId(){

        // Given
        AreaTypeEntry areaTypeEntry = new AreaTypeEntry();
        areaTypeEntry.setId("1");
        repoMock.returns(UserAreasEntity.builder().geom(point).build()).findUserAreaById(null, null, null, null);

        // When
        List<AreaDetails> userAreaDetailsWithExtentById = service.getUserAreaDetailsWithExtentById(areaTypeEntry, "", true, "");

        // Then
        AreaDetails areaDetails = userAreaDetailsWithExtentById.get(0);
        List<AreaProperty> areaProperties = areaDetails.getAreaProperties();

        assertEquals(12, areaProperties.size());

    }


    @Test
    @SneakyThrows
    public void getUserAreaDetailsWithExtentByIdWithoutId(){

        AreaTypeEntry areaTypeEntry = new AreaTypeEntry();
        areaTypeEntry.setId("1");
        repoMock.returns(null).findUserAreaById(null, null, null, null);

        List<AreaDetails> userAreaDetailsWithExtentById = service.getUserAreaDetailsWithExtentById(areaTypeEntry, "", true, "");

        AreaDetails areaDetails = userAreaDetailsWithExtentById.get(0);
        List<AreaProperty> areaProperties = areaDetails.getAreaProperties();

        Assert.equals("1", areaDetails.getAreaType().getId());
        Assert.equals(0, areaProperties.size());

    }


    @Test
    @SneakyThrows
    public void getUserAreaLayerDefinition(){

        // Given
        UserAreaLayerDto dto = UserAreaLayerDto.builder()
                .isInternal(true).isLocation(true).geoName("geoName").areaTypeDesc("desc").serviceType("serviceType")
                .serviceUrl("serviceUrl").style("style").typeName("typeName").idList(Arrays.asList(100L)).build();
        namesServiceMock.returns(Arrays.asList(dto)).listUserAreaLayerMapping();

        UserAreasEntity userAreasEntity = new UserAreasEntity();
        Field id = userAreasEntity.getClass().getDeclaredField("id");
        TestToolBox.makeModifiable(id);
        TestToolBox.setValue(userAreasEntity, id, 2L);
        repoMock.returns(Arrays.asList(userAreasEntity)).findUserAreaByUserNameAndScopeName(null, null);

        // When
        UserAreaLayerDto response = service.getUserAreaLayerDefinition(null, null);

        // Then
        assertEquals(1, response.getIdList().size());
        assertEquals(userAreasEntity.getId(), response.getIdList().get(0), 0);
        assertEquals(dto.getIsLocation(), response.getIsLocation());
        assertEquals(dto.getAreaTypeDesc(), response.getAreaTypeDesc());
        assertEquals(dto.getGeoName(), response.getGeoName());
        assertEquals(dto.getIsInternal(), response.getIsInternal());
        assertEquals(dto.getServiceType(), response.getServiceType());
        assertEquals(dto.getStyle(), response.getStyle());
        assertEquals(dto.getServiceUrl(), response.getServiceUrl());
        assertEquals(dto.getTypeName(), response.getTypeName());

    }

    @Test(expected = Exception.class)
    public void testUpdateUserAreaHappy() throws Exception {

        // Given
        UserAreaGeoJsonDto userAreaDto = createUserArea("name", UUID.randomUUID().toString(), "desc", null);
        userAreaDto.setId(2L);
        UserAreasEntity userAreasEntity = new UserAreasEntity();
        userAreasEntity.setDatasetName("dataSet");
        Field id = userAreasEntity.getClass().getDeclaredField("id");
        TestToolBox.makeModifiable(id);
        TestToolBox.setValue(userAreasEntity, id, 2L);

        repoMock.returns(userAreasEntity).findUserAreaById(null, null, null, null);

        repoMock.returns(userAreasEntity).update(null);

        // When
        service.setDialect(new PostGres());
        Long result = service.updateUserArea(userAreaDto, "rep_power", true, "");


    }

    private UserAreaGeoJsonDto createUserArea(String name, String datasetName, String desc, Long gid) throws ParseException {
        Geometry geometry = new WKTReader().read("MULTIPOLYGON(((1 1,5 1,5 5,1 5,1 1),(2 2, 3 2, 3 3, 2 3,2 2)),((3 3,6 2,6 4,3 3)))");
        geometry.setSRID(4326);

        UserAreaGeoJsonDto userAreaDto = new UserAreaGeoJsonDto();
        userAreaDto.setGeometry(geometry);
        userAreaDto.setId(gid);
        userAreaDto.setName(name);
        userAreaDto.setDatasetName(datasetName);
        userAreaDto.setDesc(desc);
        return userAreaDto;
    }

    private class TestTextMessage implements TextMessage {
        @Override public String getText() throws JMSException {
            return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                    "<ns2:filterDatasetResponse xmlns:ns2=\"module.user.wsdl.fisheries.ec.europa.eu\">\n" +
                    "    <datasetList>\n" +
                    "        <list>\n" +
                    "            <name>ccc</name>\n" +
                    "            <description>Automatically created by Spatial module dataset.</description>\n" +
                    "            <category>USERAREA_22</category>\n" +
                    "            <discriminator>USERAREA_22</discriminator>\n" +
                    "            <applicationName>Spatial</applicationName>\n" +
                    "        </list>\n" +
                    "        <list>\n" +
                    "            <name>bb</name>\n" +
                    "            <description>Automatically created by Spatial module dataset.</description>\n" +
                    "            <category>USERAREA_20</category>\n" +
                    "            <discriminator>USERAREA_20</discriminator>\n" +
                    "            <applicationName>Spatial</applicationName>\n" +
                    "        </list>\n" +
                    "        <list>\n" +
                    "            <name>ezezezssss</name>\n" +
                    "            <description>Automatically created by Spatial module dataset.</description>\n" +
                    "            <category>USERAREA_25</category>\n" +
                    "            <discriminator>USERAREA_25</discriminator>\n" +
                    "            <applicationName>Spatial</applicationName>\n" +
                    "        </list>\n" +
                    "        <list>\n" +
                    "            <name>rr</name>\n" +
                    "            <description>Automatically created by Spatial module dataset.</description>\n" +
                    "            <category>USERAREA_19</category>\n" +
                    "            <discriminator>USERAREA_19</discriminator>\n" +
                    "            <applicationName>Spatial</applicationName>\n" +
                    "        </list>\n" +
                    "        <list>\n" +
                    "            <name>ezzezezezeze</name>\n" +
                    "            <description>Automatically created by Spatial module dataset.</description>\n" +
                    "            <category>USERAREA_27</category>\n" +
                    "            <discriminator>USERAREA_27</discriminator>\n" +
                    "            <applicationName>Spatial</applicationName>\n" +
                    "        </list>\n" +
                    "        <list>\n" +
                    "            <name>ezezez</name>\n" +
                    "            <description>Automatically created by Spatial module dataset.</description>\n" +
                    "            <category>USERAREA_24</category>\n" +
                    "            <discriminator>USERAREA_24</discriminator>\n" +
                    "            <applicationName>Spatial</applicationName>\n" +
                    "        </list>\n" +
                    "        <list>\n" +
                    "            <name>houston</name>\n" +
                    "            <description>Automatically created by Spatial module dataset.</description>\n" +
                    "            <category>USERAREA_23</category>\n" +
                    "            <discriminator>USERAREA_23</discriminator>\n" +
                    "            <applicationName>Spatial</applicationName>\n" +
                    "        </list>\n" +
                    "        <list>\n" +
                    "            <name>cccc</name>\n" +
                    "            <description>Automatically created by Spatial module dataset.</description>\n" +
                    "            <category>USERAREA_26</category>\n" +
                    "            <discriminator>USERAREA_26</discriminator>\n" +
                    "            <applicationName>Spatial</applicationName>\n" +
                    "        </list>\n" +
                    "        <list>\n" +
                    "            <name>ee</name>\n" +
                    "            <description>Automatically created by Spatial module dataset.</description>\n" +
                    "            <category>USERAREA_18</category>\n" +
                    "            <discriminator>USERAREA_18</discriminator>\n" +
                    "            <applicationName>Spatial</applicationName>\n" +
                    "        </list>\n" +
                    "    </datasetList>\n" +
                    "</ns2:filterDatasetResponse>";
        }

        @Override public void setText(String s) throws JMSException {

        }

        @Override public String getJMSMessageID() throws JMSException {
            return null;
        }

        @Override public void setJMSMessageID(String s) throws JMSException {

        }

        @Override public long getJMSTimestamp() throws JMSException {
            return 0;
        }

        @Override public void setJMSTimestamp(long l) throws JMSException {

        }

        @Override public byte[] getJMSCorrelationIDAsBytes() throws JMSException {
            return new byte[0];
        }

        @Override public void setJMSCorrelationIDAsBytes(byte[] bytes) throws JMSException {

        }

        @Override public String getJMSCorrelationID() throws JMSException {
            return "whatever";
        }

        @Override public void setJMSCorrelationID(String s) throws JMSException {

        }

        @Override public Destination getJMSReplyTo() throws JMSException {
            return null;
        }

        @Override public void setJMSReplyTo(Destination destination) throws JMSException {

        }

        @Override public Destination getJMSDestination() throws JMSException {
            return null;
        }

        @Override public void setJMSDestination(Destination destination) throws JMSException {

        }

        @Override public int getJMSDeliveryMode() throws JMSException {
            return 0;
        }

        @Override public void setJMSDeliveryMode(int i) throws JMSException {

        }

        @Override public boolean getJMSRedelivered() throws JMSException {
            return false;
        }

        @Override public void setJMSRedelivered(boolean b) throws JMSException {

        }

        @Override public String getJMSType() throws JMSException {
            return null;
        }

        @Override public void setJMSType(String s) throws JMSException {

        }

        @Override public long getJMSExpiration() throws JMSException {
            return 0;
        }

        @Override public void setJMSExpiration(long l) throws JMSException {

        }

        @Override public long getJMSDeliveryTime() throws JMSException {
            return 0;
        }

        @Override public void setJMSDeliveryTime(long l) throws JMSException {

        }

        @Override public int getJMSPriority() throws JMSException {
            return 0;
        }

        @Override public void setJMSPriority(int i) throws JMSException {

        }

        @Override public void clearProperties() throws JMSException {

        }

        @Override public boolean propertyExists(String s) throws JMSException {
            return false;
        }

        @Override public boolean getBooleanProperty(String s) throws JMSException {
            return false;
        }

        @Override public byte getByteProperty(String s) throws JMSException {
            return 0;
        }

        @Override public short getShortProperty(String s) throws JMSException {
            return 0;
        }

        @Override public int getIntProperty(String s) throws JMSException {
            return 0;
        }

        @Override public long getLongProperty(String s) throws JMSException {
            return 0;
        }

        @Override public float getFloatProperty(String s) throws JMSException {
            return 0;
        }

        @Override public double getDoubleProperty(String s) throws JMSException {
            return 0;
        }

        @Override public String getStringProperty(String s) throws JMSException {
            return null;
        }

        @Override public Object getObjectProperty(String s) throws JMSException {
            return null;
        }

        @Override public Enumeration getPropertyNames() throws JMSException {
            return null;
        }

        @Override public void setBooleanProperty(String s, boolean b) throws JMSException {

        }

        @Override public void setByteProperty(String s, byte b) throws JMSException {

        }

        @Override public void setShortProperty(String s, short i) throws JMSException {

        }

        @Override public void setIntProperty(String s, int i) throws JMSException {

        }

        @Override public void setLongProperty(String s, long l) throws JMSException {

        }

        @Override public void setFloatProperty(String s, float v) throws JMSException {

        }

        @Override public void setDoubleProperty(String s, double v) throws JMSException {

        }

        @Override public void setStringProperty(String s, String s1) throws JMSException {

        }

        @Override public void setObjectProperty(String s, Object o) throws JMSException {

        }

        @Override public void acknowledge() throws JMSException {

        }

        @Override public void clearBody() throws JMSException {

        }

        @Override public <T> T getBody(Class<T> aClass) throws JMSException {
            return null;
        }

        @Override public boolean isBodyAssignableTo(Class aClass) throws JMSException {
            return false;
        }
    }
}
