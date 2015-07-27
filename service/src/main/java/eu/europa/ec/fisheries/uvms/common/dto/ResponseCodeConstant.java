/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.europa.ec.fisheries.uvms.common.dto;

/**
 *
 * @author jojoha
 */
public enum ResponseCodeConstant {

    OK(200),
    
    VESSEL_ERROR(501),
    
    INPUT_ERROR(511),
	MAPPING_ERROR(512),
	
	SERVICE_ERROR(521),
	MODEL_ERROR(522),
	DOMAIN_ERROR(523),
	
	UNDEFINED_ERROR(500);
    
    private final int code;

    private ResponseCodeConstant(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
