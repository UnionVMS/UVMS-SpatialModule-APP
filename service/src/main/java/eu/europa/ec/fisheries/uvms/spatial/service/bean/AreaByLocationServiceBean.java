package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.service.DAO;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaByLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.repository.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaDto;
import eu.europa.ec.fisheries.uvms.spatial.util.SqlPropertyHolder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;

import static eu.europa.ec.fisheries.uvms.spatial.util.SpatialUtils.convertToPointInWGS84;

import java.util.List;

@Stateless
@Local(AreaByLocationService.class)
@Transactional
@Slf4j
public class AreaByLocationServiceBean implements AreaByLocationService {

    @EJB
    private SpatialRepository repository;

    @EJB
    private SqlPropertyHolder sqlPropertyHolder;

    @Override
    @SuppressWarnings("unchecked")
    @SneakyThrows
    public List<AreaTypeEntry> getAreaTypesByLocation(AreaByLocationSpatialRQ request) {
        Point point = convertToPointInWGS84(request.getPoint());

        List<AreaLocationTypesEntity> systemAreaTypes =
                repository.findEntityByNamedQuery(AreaLocationTypesEntity.class, QueryNameConstants.FIND_SYSTEM_AREAS);
        List<AreaTypeEntry> areaTypes = Lists.newArrayList();
        for (AreaLocationTypesEntity areaType : systemAreaTypes) {
            String areaDbTable = areaType.getAreaDbTable();
            String areaTypeName = areaType.getTypeName();

            List<Integer> resultList = repository.findAreasIdByLocation(point, areaDbTable);
            for (Integer id : resultList) {
                AreaTypeEntry areaTypeEntry = new AreaTypeEntry();
                areaTypeEntry.setAreaType(areaTypeName);
                areaTypeEntry.setId(String.valueOf(id));
                areaTypes.add(areaTypeEntry);
            }
        }
        return areaTypes;
    }

    @Override
    @SuppressWarnings("unchecked")
    @SneakyThrows
    public List<AreaDto> getAreaTypesByLocation(double lat, double lon, int crs) {
        Point point = convertToPointInWGS84(lon, lat, crs);
        List<AreaLocationTypesEntity> systemAreaTypes =
                repository.findEntityByNamedQuery(AreaLocationTypesEntity.class, QueryNameConstants.FIND_SYSTEM_AREAS);
        List<AreaDto> areaTypes = Lists.newArrayList();
        for (AreaLocationTypesEntity areaType : systemAreaTypes) {
            String areaDbTable = areaType.getAreaDbTable();
            String areaTypeName = areaType.getTypeName();
            List<Integer> resultList = repository.findAreasIdByLocation(point, areaDbTable);
            for (Integer id : resultList) {
                AreaDto areaDto = new AreaDto(String.valueOf(id), areaTypeName);
                areaTypes.add(areaDto);
            }
        }

        return areaTypes;
    }
}
