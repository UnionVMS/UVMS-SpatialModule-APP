package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import static eu.europa.ec.fisheries.uvms.spatial.util.SpatialTypeEnum.getNamedQueryByType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import com.google.common.collect.ImmutableMap;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.dao.GisFunction;
import eu.europa.ec.fisheries.uvms.spatial.dao.PostGres;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.area.SystemAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.geotools.geometry.jts.WKTReader2;
import org.geotools.geometry.jts.WKTWriter2;

@Stateless
@Local(SearchAreaService.class)
@Slf4j
public class SearchAreaServiceBean implements SearchAreaService {

    private @PersistenceContext(unitName = "spatialPU") EntityManager em;

    private static final String GID = "gid";
    private static final String TYPE_NAME = "typeName";
    private static final String AREA_TYPE = "areaType";

    @EJB
    private SpatialRepository repository;

    @Override
    @Transactional
    public List<SystemAreaDto> getAreasByFilter(final String tableName, final String filter) throws ServiceException {

        try {

            AreaLocationTypesEntity areaLocationType = repository.findAreaLocationTypeByTypeName(tableName.toUpperCase());

            if (areaLocationType == null) {
                throw new SpatialServiceException(SpatialServiceErrors.INVALID_AREA_LOCATION_TYPE, areaLocationType);
            }

            GisFunction gisFunction = new PostGres();
            final String toUpperCase = filter.toUpperCase();
            final String queryString= "SELECT gid, name, code, CAST(" + gisFunction.geomToWkt() +" AS " + gisFunction.castAsUnlimitedLength() + ")" +
                    " FROM spatial." + tableName + " WHERE UPPER(name) LIKE '%" + toUpperCase + "%' OR code LIKE '%" + toUpperCase + "%' GROUP BY gid";

            final Query emNativeQuery = em.createNativeQuery(queryString);
            final List records = emNativeQuery.getResultList();
            Iterator it = records.iterator();

            final ArrayList<SystemAreaDto> systemAreaByFilterRecords = new ArrayList<>();
            final WKTReader2 wktReader2 = new WKTReader2();
            final WKTWriter2 wktWriter2 = new WKTWriter2();

            while (it.hasNext( )) {
                final Object[] result = (Object[])it.next();
                final String wkt = result[3].toString();
                Geometry geometry = wktReader2.read(wkt);
                final Geometry envelope = geometry.getEnvelope();

                final String wktEnvelope = wktWriter2.write(envelope);
                systemAreaByFilterRecords.add(
                        new SystemAreaDto(Integer.valueOf(result[0].toString()),
                                result[2].toString(), tableName.toUpperCase(), wktEnvelope, result[1].toString()));
            }

            return systemAreaByFilterRecords;

        } catch (ParseException e) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR, e.getMessage());
        }
    }

    @Override
    @Transactional
    public List<Map<String, String>> getSelectedAreaColumns(List<AreaTypeEntry> areaTypes) {
        List<Map<String, String>> columnMapList = new ArrayList<>();
        for(AreaTypeEntry areaTypeEntry : areaTypes) {
            validateArea(areaTypeEntry.getAreaType().value(), areaTypeEntry.getId());
            columnMapList.add(getSelectedColumnMap(areaTypeEntry.getAreaType().value(), areaTypeEntry.getId()));
        }
        return columnMapList;
    }

    private Map<String, String> getSelectedColumnMap(String areaType, String gid) {
        String namedQuery = getNamedQueryByType(areaType);
        Map<String, String> columnMap = getFirstMap(repository.findSelectedAreaColumns(namedQuery, Long.parseLong(gid)));
        columnMap.put(GID, gid);
        columnMap.put(AREA_TYPE, areaType.toUpperCase());
        return columnMap;
    }

    private Map<String, String> getFirstMap(List<Map<String, String>> listMap) {
        if (listMap.isEmpty()) {
            throw new SpatialServiceException(SpatialServiceErrors.ENTITY_NOT_FOUND);
        }
        return listMap.get(0);
    }

    private void validateArea(String areaType, String gid) {
        validateAreaType(areaType);
        validateId(gid);
    }

    protected void validateId(String id) {
        if (!StringUtils.isNumeric(id)) {
            throw new SpatialServiceException(SpatialServiceErrors.INVALID_ID_TYPE, id);
        }
    }

    @SneakyThrows
    private AreaLocationTypesEntity getAreaLocationType(String type) { // FIXME to be removed
        Map<String, String> parameters = ImmutableMap.<String, String>builder().put(TYPE_NAME, type.toUpperCase()).build();
        List<AreaLocationTypesEntity> areasLocationTypes = repository.findEntityByNamedQuery(AreaLocationTypesEntity.class, AreaLocationTypesEntity.FIND_TYPE_BY_NAME, parameters, 1);
        if (areasLocationTypes.isEmpty()) {
            throw new SpatialServiceException(SpatialServiceErrors.INVALID_AREA_LOCATION_TYPE, areasLocationTypes);
        }
        return areasLocationTypes.get(0);
    }
    private void validateAreaType(String areaType) {
        getAreaLocationType(areaType);
    }
}
