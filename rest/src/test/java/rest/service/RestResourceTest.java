/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest.service;

import eu.europa.ec.fisheries.schema.spatial.search.v1.MovementListQuery;
import eu.europa.ec.fisheries.schema.spatial.v1.MovementBaseType;
import eu.europa.ec.fisheries.uvms.spatial.rest.service.RestResource;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseDto;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.doReturn;
import org.mockito.MockitoAnnotations;
import eu.europa.ec.fisheries.uvms.spatial.service.MovementService;
import eu.europa.ec.fisheries.uvms.spatial.service.exception.MovementServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.mock.MockData;
import org.junit.Ignore;

/**
 *
 * @author jojoha
 */
public class RestResourceTest {

    private static final Long ID = 1L;
    private static final Integer LIST_SIZE = 3;

    List<MovementBaseType> DTO_LIST = MockData.getDtoList(LIST_SIZE);
    MovementBaseType DTO = MockData.getDto(ID);

    private final ResponseDto ERROR_RESULT;
    private final ResponseDto SUCCESS_RESULT;
    private final ResponseDto SUCCESS_RESULT_LIST;
    private final ResponseDto SUCCESS_RESULT_DTO;

    RestResource SERVICE_NULL = new RestResource();

    @Mock
    MovementService serviceLayer;

    @InjectMocks
    RestResource vesselResource;

    public RestResourceTest() {
        ERROR_RESULT = new ResponseDto(ResponseCode.ERROR);
        SUCCESS_RESULT = new ResponseDto(ResponseCode.OK);
        SUCCESS_RESULT_LIST = new ResponseDto(DTO_LIST, ResponseCode.OK);
        SUCCESS_RESULT_DTO = new ResponseDto(DTO, ResponseCode.OK);
    }

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test get list with a happy outcome
     *
     * @throws eu.europa.ec.fisheries.uvms.project.exception.ServiceException
     */
    @Test
    @Ignore
    public void testGetMovementList() throws MovementServiceException {
        MovementListQuery query = new MovementListQuery();
        doReturn(DTO_LIST).when(serviceLayer).getList(query);
        //ResponseDto result = vesselResource.getListByQuery(query);
        //assertEquals(SUCCESS_RESULT_LIST.toString(), result.toString());
    }

    /**
     * Test get list when the injected EJB is null
     *
     * @throws eu.europa.ec.fisheries.uvms.project.exception.ServiceException
     */
    @Test
    @Ignore
    public void testGetVesselListNull() throws MovementServiceException {
        MovementListQuery query = new MovementListQuery();
        //ResponseDto result = SERVICE_NULL.getListByQuery(query);
        //assertEquals(ERROR_RESULT.toString(), result.toString());
    }

    /**
     * Test get by id with a happy outcome
     *
     * @throws eu.europa.ec.fisheries.uvms.project.exception.ServiceException
     */
    @Test
    public void testGetVesselById() throws MovementServiceException {
        doReturn(DTO).when(serviceLayer).getById(ID);
        ResponseDto result = vesselResource.getById(ID);
        Mockito.verify(serviceLayer).getById(ID);
        assertEquals(SUCCESS_RESULT_DTO.toString(), result.toString());

    }

    /**
     * Test get by id when the injected EJB is null
     *
     * @throws eu.europa.ec.fisheries.uvms.project.exception.ServiceException
     */
    @Test
    public void testGetVesselByIdNull() throws MovementServiceException {
        ResponseDto result = SERVICE_NULL.getById(ID);
        assertEquals(ERROR_RESULT.toString(), result.toString());
    }

    /**
     * Test update with a happy outcome
     *
     * @throws eu.europa.ec.fisheries.uvms.project.exception.ServiceException
     */
    @Test
    public void testUpdateVessel() throws MovementServiceException {
        ResponseDto result = vesselResource.update(DTO);
        Mockito.verify(serviceLayer).update(DTO);
        assertEquals(SUCCESS_RESULT.toString(), result.toString());
    }

    /**
     * Test update when the injected EJB is null
     */
    @Test
    public void testUpdateVesselNull() {
        ResponseDto result = SERVICE_NULL.update(DTO);
        assertEquals(ERROR_RESULT.toString(), result.toString());
    }

}
