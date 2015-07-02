/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.europa.ec.fisheries.uvms.spatial.model;

import eu.europa.ec.fisheries.uvms.spatial.model.exception.MovementModelException;
import javax.ejb.Local;

/**
 *
 * @author jojoha
 */
@Local
public interface Model {

    public void sendData(Object dto) throws MovementModelException;

    public Object getData() throws MovementModelException;

}
