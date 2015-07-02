/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.europa.ec.fisheries.uvms.spatial.service.exception;

/**
 *
 * @author jojoha
 */
public class MovementServiceException extends Exception {

    public MovementServiceException() {
    }

    public MovementServiceException(String message) {
        super(message);
    }

    public MovementServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
