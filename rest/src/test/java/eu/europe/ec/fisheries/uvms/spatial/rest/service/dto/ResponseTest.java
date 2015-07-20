/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.europe.ec.fisheries.uvms.spatial.rest.service.dto;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseDto;

/**
 *
 * @author jojoha
 */
public class ResponseTest {

    public ResponseTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void checkDtoReturnsValid() {

        String VALUE = "HELLO_DTO";
        ResponseDto dto = new ResponseDto(VALUE,  eu.europa.ec.fisheries.uvms.spatial.service.dto.ResponseCode.OK);
        Assert.assertEquals(dto.getCode(), ResponseCode.OK.getCode());
        Assert.assertEquals(dto.getData(), VALUE);

        dto = new ResponseDto( eu.europa.ec.fisheries.uvms.spatial.service.dto.ResponseCode.OK.ERROR);
        Assert.assertEquals(dto.getCode(), ResponseCode.ERROR.getCode());
        Assert.assertEquals(dto.getData(), null);

    }
}
