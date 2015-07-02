/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.europa.ec.fisheries.uvms.spatial.rest.dto;

/**
 *
 * @author jojoha
 */
public enum ResponseCode implements RestResponseCode {

    OK("200"),
    ERROR("500");

    private final String code;

    private ResponseCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }

}
