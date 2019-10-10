/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service;

import static eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType.EEZ;
import static java.util.Arrays.asList;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKTReader;
import eu.europa.ec.fisheries.uvms.BaseUnitilsTest;
import eu.europa.ec.fisheries.uvms.commons.geometry.mapper.GeometryMapper;
import eu.europa.ec.fisheries.uvms.commons.geometry.utils.GeometryUtils;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Area;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaExtendedIdentifierType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Coordinate;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.FilterAreasSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.FilterAreasSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Location;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.PointType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ScopeAreasType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.UnitType;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.impl.AreaServiceBean;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.util.DAOFactory;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.util.PostGres;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.BaseAreaEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.EezEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.exception.SpatialServiceException;
import lombok.SneakyThrows;
import org.geotools.geometry.jts.GeometryBuilder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest({DAOFactory.class, EntityManager.class})
public class AreaServiceTest extends BaseUnitilsTest {

    @Mock
    private SpatialRepository repository;

    @Mock
    private GeometryMapper mapper;

    private ClosestAreaSpatialRQ closestAreaRequest;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private AreaServiceBean service;

    @Before
    public void initMocks() {
        PowerMockito.mockStatic(DAOFactory.class);
        MockitoAnnotations.initMocks(this);

        closestAreaRequest = new ClosestAreaSpatialRQ();
        closestAreaRequest.setUnit(UnitType.KILOMETERS);
        ClosestAreaSpatialRQ.AreaTypes areaTypes = new ClosestAreaSpatialRQ.AreaTypes();
        areaTypes.setAreaTypes(asList(AreaType.EEZ));
        closestAreaRequest.setAreaTypes(areaTypes);

        PointType point = new PointType();
        point.setCrs(3216);
        point.setLatitude((double) 50);
        point.setLongitude((double) -21);
        closestAreaRequest.setPoint(point);
    }

    @Test
    @SneakyThrows
    @Ignore
    public void testGetSelectedAreaColumns(){
        service.getAreasByIds(null);
    }

    @Test
    @SneakyThrows
    public void testGetAreaDetailsById() {

        when(repository.findAreaLocationTypeByTypeName(Mockito.anyString())).thenReturn(new AreaLocationTypesEntity());

        BaseAreaEntity eezEntity = new EezEntity();
        eezEntity.setGeom(new GeometryBuilder().point(1, 11));
        when(repository.findAreaById(Mockito.anyLong(), (AreaType) Mockito.any())).thenReturn(eezEntity);

        Map<String, Object> objectMap = service.getAreaById(1L, EEZ);
        assertEquals("POINT (1 11)", objectMap.get("geometry"));
        assertEquals("POINT (1 11)", objectMap.get("centroid"));
        assertEquals("POINT (1 11)", objectMap.get("extent"));
        assertEquals(true, objectMap.get("enabled"));

    }

    @Test(expected = SpatialServiceException.class)
    @SneakyThrows
    public void testGetAreaDetailsByIdNonExistingAreaType() {
        service.getAreaById(1L, EEZ);
    }

    @Test
    @SneakyThrows
    public void testGetClosestArea(){

        Object[][] areas  = new Object[1][6];
        areas[0][0] = "EEZ";
        areas[0][1] = 231;
        areas[0][2] = "MAR";
        areas[0][3] = "Moroccan Exclusive Zone";
        areas[0][5] = 12920292.22;

        Point point = (Point) new WKTReader().read("POINT (-75.347781567 89.9794456)");
        point.setSRID(4326);
        areas[0][4] = point;
        when(repository.closestAreaByPoint(anyListOf(AreaLocationTypesEntity.class), any(PostGres.class), any(Point.class))).thenReturn(asList(areas));

        List<Area> closestArea = service.getClosestArea(-75.347781567, 88.9794456, point.getSRID(), UnitType.KILOMETERS);
        assertEquals(12920.292220000001, closestArea.get(0).getDistance());
    }

    @Test
    @SneakyThrows
    public void testUserAreaDetailsForInvalidUserNameAndScopeName() {
        //Given
        Coordinate coordinate = new Coordinate(20.0535983848415, 31.1417484902222, 4326);
        //List<UserAreaDto> userAreas = service.getUserAreaByPoint(coordinate, "00000");

        //Test
        //assertNotNull(userAreas);
        //assertTrue(userAreas.isEmpty());
    }

    @Test
    @SneakyThrows
    @Ignore //FIXME
    public void testGetClosestPointByPoint() {

        EezEntity eezEntity = new EezEntity();
        eezEntity.setLongitude(23.9);
        eezEntity.setLatitude(32.8);

        Object[] object = new Object[6];
        object[0] = "PORT";
        object[1] = 4610;
        object[2] = "KMYVA";
        object[3] = "Moroni";
        object[4] = new GeometryBuilder().point(12.2,34.1);
        object[5] = 234.19;

        Object[][] objects = new Object[1][1];
        objects[0][0] = object;

        List<Object> list = new ArrayList<>();
        list.add(object);
        //when(repository.closestPointByPoint(anyListOf(AreaLocationTypesEntity.class), any(DatabaseDialect.class), any(Point.class))).thenReturn(asList(areas));

        PointType point = new PointType();
        point.setCrs(GeometryUtils.DEFAULT_EPSG_SRID);
        point.setLatitude(23.2);
        point.setLongitude(21.3);

        ClosestLocationSpatialRQ req = new ClosestLocationSpatialRQ();
        req.setPoint(point);
        req.setUnit(UnitType.KILOMETERS);

        List<LocationType> locationTypeList = new ArrayList<>();
        locationTypeList.add(LocationType.PORT);
        ClosestLocationSpatialRQ.LocationTypes locationTypes = new ClosestLocationSpatialRQ.LocationTypes();
        locationTypes.setLocationTypes(locationTypeList);

        req.setLocationTypes(locationTypes);

        List<Location> closestPointToPointByType = service.getClosestPointByPoint(req);

        assertEquals("4610", closestPointToPointByType.get(0).getId());
        assertEquals("Moroni", closestPointToPointByType.get(0).getName());
        assertEquals(1498.670248834928, closestPointToPointByType.get(0).getDistance());
        assertEquals(UnitType.KILOMETERS, closestPointToPointByType.get(0).getUnit());
        assertEquals(LocationType.PORT, closestPointToPointByType.get(0).getLocationType());

    }

    @Test
    @SneakyThrows
    @Ignore // FIXME
    public void testComputeAreaFilter(){

        List<AreaLocationTypesEntity> entities = new ArrayList<>();

        AreaLocationTypesEntity entity = new AreaLocationTypesEntity();
        entity.setAreaDbTable("eez");
        entity.setTypeName("EEZ");

        entities.add(entity);
       // repo.returns(4326).mapToEpsgSRID(4326);
       // repo.returns(entities).findAllIsLocation(false);

        Object[] object = new Object[6];
        object[0] = "PORT";
        Point point = new GeometryBuilder().point(12.2, 34.1);
        point.setSRID(GeometryUtils.DEFAULT_EPSG_SRID);
        object[1] = point;

        Object[][] objects = new Object[1][1];
        objects[0][0] = object;

        List<Object> baseAreaList = new ArrayList<>();
        baseAreaList.add(object);
        //repo.returns(baseAreaList).listBaseAreaList(null);

        ScopeAreasType scopeAreasType = new ScopeAreasType();
        List<AreaIdentifierType> list = new ArrayList<>();
        AreaIdentifierType areaT = new AreaExtendedIdentifierType();
        areaT.setAreaType(AreaType.EEZ);
        areaT.setId("1");
        list.add(areaT);
        scopeAreasType.setScopeAreas(list);

        FilterAreasSpatialRQ req = new FilterAreasSpatialRQ();
        req.setScopeAreas(scopeAreasType);

        FilterAreasSpatialRS filterAreasSpatialRS = service.computeAreaFilter(req);

        assertEquals("POINT (12.2 34.1)", filterAreasSpatialRS.getGeometry());
        assertEquals(2, filterAreasSpatialRS.getCode());

    }
}