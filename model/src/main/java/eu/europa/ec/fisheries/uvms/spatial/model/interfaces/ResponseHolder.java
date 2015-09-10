package eu.europa.ec.fisheries.uvms.spatial.model.interfaces;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ResponseMessageType;

/**
 * Created by Cegeka on 10-Sep-15.
 */
public interface ResponseHolder {
    ResponseMessageType getResponseMessage();

    void setResponseMessage(ResponseMessageType value);
}
