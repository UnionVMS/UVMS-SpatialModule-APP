package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.Maps;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.dao.GisFunction;
import eu.europa.ec.fisheries.uvms.spatial.dao.PostGres;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Location;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.UnitType;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.ClosestLocationDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.util.MeasurementUnit;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import lombok.extern.slf4j.Slf4j;
import org.geotools.geometry.jts.WKTReader2;
import org.geotools.referencing.GeodeticCalculator;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

@Stateless
@Local(ClosestLocationService.class)
@Transactional
@Slf4j
public class ClosestLocationServiceBean implements ClosestLocationService {

    private @PersistenceContext(unitName = "spatialPU") EntityManager em;
    private @EJB SpatialRepository repository;

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public List<Location> getClosestLocationByLocationType(ClosestLocationSpatialRQ request) throws ServiceException {

        try {

            final List<Location> locations = newArrayList();

            final Double latitude = request.getPoint().getLatitude();
            final Double longitude = request.getPoint().getLongitude();
            final Integer crs = request.getPoint().getCrs();
            final UnitType unit = request.getUnit();
            final MeasurementUnit measurementUnit = MeasurementUnit.getMeasurement(unit.name());
            final Map<String, String> areaType2TableName = getLocationType2TableNameMap();
            final List<Location> closestLocations = newArrayList();
            final GisFunction gisFunction = new PostGres();
            final WKTReader2 wktReader2 = new WKTReader2();
            final GeodeticCalculator calc = new GeodeticCalculator();

            for (LocationType locationType : request.getLocationTypes().getLocationTypes()) {

                Location closestLocation = new Location();
                closestLocation.setDistance(Double.MAX_VALUE);

                final String areaDbTable = areaType2TableName.get(locationType.value());

                final String queryString = "SELECT gid, code, name, "+ gisFunction.toWkt("geom")+ ", " + gisFunction.stDistance(longitude, latitude, crs) + " / " + measurementUnit.getRatio()
                        + " AS distance " +
                        "FROM spatial." + areaDbTable + " WHERE NOT " + gisFunction.isEmptyGeom() + " AND enabled = 'Y' ORDER BY distance";

                final Query emNativeQuery = em.createNativeQuery(queryString);
                final List records = emNativeQuery.getResultList();

                for (Object record : records) {

                    final Object[] result = (Object[]) record;
                    final String wkt = result[3].toString();
                    final Geometry geometry = wktReader2.read(wkt);
                    final Point centroid = geometry.getCentroid();
                    calc.setStartingGeographicPoint(centroid.getX(), centroid.getY());
                    calc.setDestinationGeographicPoint(longitude, latitude);
                    double orthodromicDistance = calc.getOrthodromicDistance();

                    if (closestLocation.getDistance() > orthodromicDistance) {
                        closestLocation.setId(result[0].toString());
                        closestLocation.setDistance(orthodromicDistance);
                        closestLocation.setUnit(unit);
                        closestLocation.setCode(result[1].toString());
                        closestLocation.setName(result[2].toString());
                        closestLocation.setLocationType(locationType);
                    }

                }

                if (closestLocation.getDistance() != Double.MAX_VALUE){
                    locations.add(closestLocation);
                }

            }

            return closestLocations;

        } catch (ParseException ex) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR, ex);
        }

    }

    @Override
    public List<ClosestLocationDto> getClosestLocations(final Double lat, final Double lon, final Integer crs,
                                                        final String unit, final List<String> locations)
            throws ServiceException {

        try {

            final List<ClosestLocationDto> locationList = newArrayList();
            final List<String> locationTypes = SpatialUtils.toUpperCase(locations);
            final Map<String, String> areaType2TableName = getLocationType2TableNameMap();
            final GisFunction gisFunction = new PostGres();
            final WKTReader2 wktReader2 = new WKTReader2();
            final GeodeticCalculator calc = new GeodeticCalculator();

            for (String locationType : locationTypes) {

                ClosestLocationDto closestLocation = new ClosestLocationDto();
                closestLocation.setDistance(Double.MAX_VALUE);

                final String areaDbTable = areaType2TableName.get(locationType);

                final String queryString = "SELECT gid, code, name, "+ gisFunction.toWkt("geom")+ ", " + gisFunction.stDistance(lon, lat, crs) + " / " + MeasurementUnit.getMeasurement(unit).getRatio()
                        + " AS distance " +
                        "FROM spatial." + areaDbTable + " WHERE NOT " + gisFunction.isEmptyGeom() + " AND enabled = 'Y' ORDER BY distance";

                final Query emNativeQuery = em.createNativeQuery(queryString);
                final List records = emNativeQuery.getResultList();

                for (Object record : records) {

                    final Object[] result = (Object[]) record;
                    final String wkt = result[3].toString();
                    final Geometry geometry = wktReader2.read(wkt);
                    final Point centroid = geometry.getCentroid();
                    calc.setStartingGeographicPoint(centroid.getX(), centroid.getY());
                    calc.setDestinationGeographicPoint(lon, lat);
                    double orthodromicDistance = calc.getOrthodromicDistance();

                    if (closestLocation.getDistance() > orthodromicDistance) {
                        closestLocation.setId(result[0].toString());
                        closestLocation.setDistance(orthodromicDistance);
                        closestLocation.setUnit(unit);
                        closestLocation.setCode(result[1].toString());
                        closestLocation.setName(result[2].toString());
                        closestLocation.setLocationType(locationType);
                    }

                }

                if (closestLocation.getDistance() != Double.MAX_VALUE){
                    locationList.add(closestLocation);
                }

            }

            return locationList;

        } catch (ParseException ex) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR, ex);
        }
    }

    private Map<String, String> getLocationType2TableNameMap() throws ServiceException {
        List<AreaLocationTypesEntity> locations = repository.findEntityByNamedQuery(AreaLocationTypesEntity.class, QueryNameConstants.FIND_ALL_LOCATIONS);
        Map<String, String> locationMap = Maps.newHashMap();
        for (AreaLocationTypesEntity location : locations) {
            locationMap.put(location.getTypeName().toUpperCase(), location.getAreaDbTable());
        }
        return locationMap;
    }

}

//sql.findClosestLocation = SELECT CAST(gid AS text) AS id, code, name, ST_Distance_Spheroid(geom, st_geomfromtext(CAST(:wktPoint as text), :crs), 'SPHEROID["WGS 84",6378137,298.257223563]') /:unit AS distance FROM spatial.{tableName} where not ST_IsEmpty(geom) and enabled = 'Y' order by distance limit 1

