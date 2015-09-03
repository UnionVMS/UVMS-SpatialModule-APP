package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.service.CrudService;
import eu.europa.ec.fisheries.uvms.spatial.dao.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestLocationSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.ClosestLocationDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.handler.ExceptionHandlerInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.handler.SpatialExceptionHandler;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Michal Kopyczok on 03-Sep-15.
 */
@Stateless
@Local(ClosestLocationService.class)
@Transactional
@Slf4j
public class ClosestLocationServiceBean implements ClosestLocationService {

    @EJB
    private SpatialRepository repository;

    @EJB
    private CrudService crudService;

    @Override
    @SpatialExceptionHandler(responseType = ClosestLocationSpatialRS.class)
    @Interceptors(value = ExceptionHandlerInterceptor.class)
    public ClosestLocationSpatialRS getClosestLocations(ClosestLocationSpatialRQ request) {
        return null;
    }

    @Override
    public List<ClosestLocationDto> getClosestLocationsRest(double lat, double lon, int crs, String unit, List<String> locationTypes) {
        return null;
    }
}
