package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRS;
import org.apache.commons.lang3.NotImplementedException;

/**
 * //TODO create test
 */
public class AreaTypeClosestAreaBean implements AreaTypeClosestArea {

    @Override
    public ClosestAreaSpatialRS getClosestArea(ClosestAreaSpatialRQ request) {
        throw new NotImplementedException("Not implemented yet");

    }
}
