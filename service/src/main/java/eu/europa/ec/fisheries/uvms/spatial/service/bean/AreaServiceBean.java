package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.common.SpatialUtils;
import eu.europa.ec.fisheries.uvms.service.CommonGenericDAO;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaEntry;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialResponse;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UnitType;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import eu.europa.ec.fisheries.uvms.util.SqlPropertyHolder;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
@Local(AreaService.class)
@Transactional
@Slf4j
public class AreaServiceBean implements AreaService {

    @EJB // FIXME ambigious EJB reference
    private CommonGenericDAO dao;

    @EJB
    private SqlPropertyHolder prop;

    @Override
    public ClosestAreaSpatialResponse getClosestArea(final ClosestAreaSpatialRequest request) {

        ClosestAreaSpatialResponse response = new ClosestAreaSpatialResponse();

        List<AreaType> areaTypes = request.getArea();// TODO get table name

        try {

            SQLQuery sqlQuery;

            if (SpatialUtils.isDefaultCrs(request.getCrs())) {
                String queryString = prop.getProperty("sql.closestArea").replace("{tableName}", "countries");
                sqlQuery = createSQLQuery(queryString, request);
            } else {
                String queryString = prop.getProperty("sql.closestAreaWithTransform").replace("{tableName}", "countries");
                sqlQuery = createSQLQuery(queryString, request);
                sqlQuery.setInteger("otherCrs", request.getCrs());
            }

            List queryResult = sqlQuery.list();

            if (queryResult != null && queryResult.size() == 1) {
                ClosestAreaEntry entry = (ClosestAreaEntry) queryResult.get(0);
                response.getClosestArea().add(entry);
            }
        }
        catch (Exception e) {
            log.error("Error occurred during calculation nearest point request : {}", request);
            log.error("Exception cause: ", e.getCause());
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
        }

        return response;
    }

    private SQLQuery createSQLQuery(String queryString, ClosestAreaSpatialRequest request) {
        SQLQuery sqlQuery = dao.getEntityManager().unwrap(Session.class).createSQLQuery(queryString);
        sqlQuery.setResultTransformer(Transformers.aliasToBean(ClosestAreaEntry.class));
        sqlQuery.setString("wkt", request.getWkt());
        sqlQuery.setInteger("defaultCrs", SpatialUtils.DEFAULT_CRS);
        sqlQuery.setDouble("unit", UnitType.valueOf(request.getUnit().name()).getUnit());
        return sqlQuery;
    }
}
