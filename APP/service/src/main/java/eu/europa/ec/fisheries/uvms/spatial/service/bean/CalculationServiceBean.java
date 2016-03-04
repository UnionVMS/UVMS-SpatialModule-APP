package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.service.CalculateService;

import javax.ejb.Local;
import javax.ejb.Stateless;

@Stateless
@Local(CalculateService.class)
public class CalculationServiceBean implements CalculateService {

    @Override
    public String calculateBuffer(final Double latitude, final Double longitude, final Double buffer) {

        return SpatialUtils.calculateBuffer(latitude, longitude, buffer);

    }
}
