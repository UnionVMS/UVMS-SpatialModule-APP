package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.uvms.spatial.dao.PostGres;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.area.SystemAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaByLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaExtendedIdentifierType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
import java.util.List;

@Stateless
@Local(AreaByLocationService.class)
@Slf4j
public class AreaByLocationServiceBean implements AreaByLocationService {

    // we can not use typed entities here because tables can be made on the fly thus we don't
    // have all area type entities pre-defined
    private @PersistenceContext(unitName = "spatialPU") EntityManager em;
    private @EJB SpatialRepository repository;

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

            List<SystemAreaDto> resultList = fetchIntersecting(crs, latitude, longitude, function, areaType);

            for (SystemAreaDto area : resultList) {
                AreaExtendedIdentifierType areaIdentifier = new AreaExtendedIdentifierType(String.valueOf(area.getGid()), AreaType.valueOf(areaType.getTypeName()), area.getCode(), area.getName());
                areaTypes.add(areaIdentifier);
            }
        }
        return areaTypes;
    }

    @Override
    @Transactional
    @SneakyThrows
    public List<SystemAreaDto> getAreaTypesByLocation(final Double latitude, final Double longitude, final Integer crs) {

        List<AreaLocationTypesEntity> systemAreaTypes = repository.findEntityByNamedQuery(AreaLocationTypesEntity.class, QueryNameConstants.FIND_SYSTEM_AREAS);

        List<SystemAreaDto> areaTypes = Lists.newArrayList();

        PostGres function = new PostGres();

        for (AreaLocationTypesEntity areaType : systemAreaTypes) {

            List<SystemAreaDto> resultList = fetchIntersecting(crs, latitude, longitude, function, areaType);

            for (SystemAreaDto area : resultList) {
                area.setAreaType(areaType.getTypeName());
                areaTypes.add(area);
            }
        }

        return areaTypes;
    }

    private List<SystemAreaDto> fetchIntersecting(final Integer crs, final Double latitude, final Double longitude,
                                                  final PostGres function, final AreaLocationTypesEntity areaType) {
        String areaDbTable = areaType.getAreaDbTable();

        String queryString = "SELECT gid, name, code FROM spatial." + areaDbTable +
                " WHERE " + function.stIntersects(latitude, longitude, crs) + " AND enabled = 'Y'";

        Query emNativeQuery = em.createNativeQuery(queryString);

        emNativeQuery.unwrap(SQLQuery.class)
                .addScalar("gid", StandardBasicTypes.INTEGER)
                .addScalar("code", StandardBasicTypes.STRING)
                .addScalar("name", StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.aliasToBean(SystemAreaDto.class));

        return emNativeQuery.getResultList();
    }
}
