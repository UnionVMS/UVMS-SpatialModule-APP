package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialEnrichmentRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialEnrichmentRS;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.EnrichmentDto;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.List;

import static eu.europa.ec.fisheries.uvms.util.ModelUtils.createSuccessResponseMessage;

/**
 * Created by Cegeka on 01-Sep-15.
 */
@Stateless
@Local(SpatialEnrichmentSupport.class)
@Transactional
@Slf4j
public class SpatialEnrichmentServiceBean implements SpatialEnrichmentService {

    @EJB(beanName="AreaByLocationServiceBean")
    private SpatialEnrichmentSupport handler;

    @Override
    public SpatialEnrichmentRS getSpatialEnrichment(SpatialEnrichmentRQ spatialEnrichmentRQ) {
        return handler.handleRequest(spatialEnrichmentRQ, createSuccessSpatialEnrichmentResponse());
    }

    @Override
    public EnrichmentDto getSpatialEnrichment(double lat, double lon, int crs, String unit, List<String> areaTypes, List<String> locationTypes) {
        return handler.handleRequest(lat, lon, crs, unit, areaTypes, locationTypes, new EnrichmentDto());
    }

    private SpatialEnrichmentRS createSuccessSpatialEnrichmentResponse() {
        return new SpatialEnrichmentRS(createSuccessResponseMessage(), null, null, null);
    }
}
