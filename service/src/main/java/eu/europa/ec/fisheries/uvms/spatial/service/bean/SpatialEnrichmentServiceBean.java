package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.service.CrudService;
import eu.europa.ec.fisheries.uvms.spatial.dao.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaDto;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Michal Kopyczok on 01-Sep-15.
 */
@Stateless
@Local(SpatialEnrichmentService.class)
@Transactional
@Slf4j
public class SpatialEnrichmentServiceBean implements SpatialEnrichmentService {

    @EJB
    private SpatialRepository repository;

    @EJB
    private CrudService crudService;

    @Override
    public List<AreaDto> getClosestLocationRest(double lat, double lon, int crs, List<String> locationTypes, String unit) {
        return null;
    }

}
