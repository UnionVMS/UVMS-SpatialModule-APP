package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.dao.GisFunction;
import eu.europa.ec.fisheries.uvms.spatial.dao.PostGres;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.area.SystemAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.util.MeasurementUnit;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.geotools.geometry.jts.WKTReader2;
import org.geotools.referencing.GeodeticCalculator;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Stateless
@Local(SpatialService.class)
@Transactional
@Slf4j
public class SpatialServiceBean implements SpatialService {

    private @PersistenceContext(unitName = "spatialPU") EntityManager em;
    private @EJB SpatialRepository repository;

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public List<Location> getClosestLocationByLocationType(final ClosestLocationSpatialRQ request) throws ServiceException {

        try {

            final List<Location> closestLocations = new ArrayList<>();
            final Double latitude = request.getPoint().getLatitude();
            final Double longitude = request.getPoint().getLongitude();
            final Integer crs = request.getPoint().getCrs();
            final UnitType unit = request.getUnit();
            final MeasurementUnit measurementUnit = MeasurementUnit.getMeasurement(unit.name());

            List<AreaLocationTypesEntity> locationTypesEntities = repository.findEntityByNamedQuery(AreaLocationTypesEntity.class, AreaLocationTypesEntity.FIND_ALL_LOCATIONS);

            Map<String, String> locationMap = Maps.newHashMap();

            for (AreaLocationTypesEntity location : locationTypesEntities) {
                locationMap.put(location.getTypeName().toUpperCase(), location.getAreaDbTable());
            }

            final GisFunction gisFunction = new PostGres();
            final WKTReader2 wktReader2 = new WKTReader2();
            final GeodeticCalculator calc = new GeodeticCalculator();

            for (LocationType locationType : request.getLocationTypes().getLocationTypes()) {

                Location closestLocation = null;
                Double closestDistance = Double.MAX_VALUE;

                final String areaDbTable = locationMap.get(locationType.value());

                final String queryString = "SELECT gid, code, name, "+ gisFunction.toWkt("geom")+ ", " + gisFunction.stDistance(longitude, latitude, crs) + " AS distance " +
                        "FROM spatial." + areaDbTable + " WHERE enabled = 'Y' ORDER BY distance ASC " + gisFunction.limit(15);

                final Query emNativeQuery = em.createNativeQuery(queryString);
                final List records = emNativeQuery.getResultList();

                for (Object record : records) {

                    final Object[] result = (Object[]) record;
                    final String wkt = result[3].toString();
                    final Geometry geometry = wktReader2.read(wkt);
                    final Point centroid = geometry.getCentroid();
                    calc.setStartingGeographicPoint(centroid.getX(), centroid.getY());
                    calc.setDestinationGeographicPoint(longitude, latitude);
                    Double orthodromicDistance = calc.getOrthodromicDistance();

                    if (closestDistance > orthodromicDistance) {
                        closestDistance = orthodromicDistance;
                        closestLocation = new Location();
                        closestLocation.setDistance(orthodromicDistance);
                        closestLocation.setId(result[0].toString());
                        closestLocation.setDistance(orthodromicDistance / measurementUnit.getRatio());
                        closestLocation.setUnit(unit);
                        closestLocation.setCode(result[1].toString());
                        closestLocation.setName(result[2].toString());
                        closestLocation.setLocationType(locationType);
                    }

                }

                if (closestLocation != null){
                    closestLocations.add(closestLocation);
                }

            }

            return closestLocations;

        } catch (ParseException ex) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR, ex);
        }
    }

    @Override
    @SneakyThrows
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public List<AreaExtendedIdentifierType> getAreaTypesByLocation(final AreaByLocationSpatialRQ request) {

        final Integer crs = request.getPoint().getCrs();
        final double latitude = request.getPoint().getLatitude();
        final double longitude = request.getPoint().getLongitude();

        List<AreaLocationTypesEntity> systemAreaTypes = repository.findEntityByNamedQuery(AreaLocationTypesEntity.class, QueryNameConstants.FIND_SYSTEM_AREAS);

        List<AreaExtendedIdentifierType> areaTypes = Lists.newArrayList();

        PostGres function = new PostGres();

        for (AreaLocationTypesEntity areaType : systemAreaTypes) {

            String areaDbTable = areaType.getAreaDbTable();

            String queryString = "SELECT gid, name, code FROM spatial." + areaDbTable +
                    " WHERE " + function.stIntersects(latitude, longitude, crs) + " AND enabled = 'Y'";

            Query emNativeQuery = em.createNativeQuery(queryString);

            emNativeQuery.unwrap(SQLQuery.class)
                    .addScalar("gid", StandardBasicTypes.INTEGER)
                    .addScalar("code", StandardBasicTypes.STRING)
                    .addScalar("name", StandardBasicTypes.STRING)
                    .setResultTransformer(Transformers.aliasToBean(SystemAreaDto.class));

            List<SystemAreaDto> resultList = emNativeQuery.getResultList();

            for (SystemAreaDto area : resultList) {
                AreaExtendedIdentifierType areaIdentifier = new AreaExtendedIdentifierType(String.valueOf(area.getGid()), AreaType.valueOf(areaType.getTypeName()), area.getCode(), area.getName());
                areaTypes.add(areaIdentifier);
            }
        }
        return areaTypes;
    }

}
