package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaByLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaExtendedIdentifierType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.repository.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.AreaExtendedIdentifierDto;
import eu.europa.ec.fisheries.uvms.spatial.util.SqlPropertyHolder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.List;

import static eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialUtils.convertToPointInWGS84;

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
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public List<AreaExtendedIdentifierType> getAreaTypesByLocation(AreaByLocationSpatialRQ request) {
        Point point = convertToPointInWGS84(request.getPoint());

        List<AreaLocationTypesEntity> systemAreaTypes =
                repository.findEntityByNamedQuery(AreaLocationTypesEntity.class, QueryNameConstants.FIND_SYSTEM_AREAS);
        List<AreaExtendedIdentifierType> areaTypes = Lists.newArrayList();
        for (AreaLocationTypesEntity areaType : systemAreaTypes) {
            String areaDbTable = areaType.getAreaDbTable();
            AreaType areaTypeName = AreaType.valueOf(areaType.getTypeName());

            List<AreaExtendedIdentifierDto> resultList = repository.findAreasIdByLocation(point, areaDbTable);
            for (AreaExtendedIdentifierDto area : resultList) {
                AreaExtendedIdentifierType areaIdentifier = new AreaExtendedIdentifierType(area.getId(), areaTypeName, area.getCode(), area.getName());
                areaTypes.add(areaIdentifier);
            }
        }
        return areaTypes;
    }

    @Override
    @SuppressWarnings("unchecked")
    @SneakyThrows
    public List<AreaExtendedIdentifierDto> getAreaTypesByLocation(double lat, double lon, int crs) {
        Point point = convertToPointInWGS84(lon, lat, crs);
        List<AreaLocationTypesEntity> systemAreaTypes =
                repository.findEntityByNamedQuery(AreaLocationTypesEntity.class, QueryNameConstants.FIND_SYSTEM_AREAS);
        List<AreaExtendedIdentifierDto> areaTypes = Lists.newArrayList();
        for (AreaLocationTypesEntity areaType : systemAreaTypes) {
            String areaDbTable = areaType.getAreaDbTable();
            String areaTypeName = areaType.getTypeName();
            List<AreaExtendedIdentifierDto> resultList = repository.findAreasIdByLocation(point, areaDbTable);
            for (AreaExtendedIdentifierDto area : resultList) {
                area.setAreaType(areaTypeName);
                areaTypes.add(area);
            }
        }

        return areaTypes;
    }
}
