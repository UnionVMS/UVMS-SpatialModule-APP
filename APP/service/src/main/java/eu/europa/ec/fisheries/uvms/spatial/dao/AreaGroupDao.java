package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.google.common.collect.ImmutableMap;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaGroupEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaGroup.AreaGroupTypeDto;
import eu.europa.ec.fisheries.uvms.spatial.util.SqlPropertyHolder;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

/**
 * Created by padhyad on 1/25/2016.
 */
public class AreaGroupDao extends CommonDao {

    private SqlPropertyHolder propertyHolder;

    private static String USER_ID = "userId";
    private static String ID = "id";

    public AreaGroupDao(EntityManager em, SqlPropertyHolder propertyHolder) {
        super(em);
        this.propertyHolder = propertyHolder;
    }

    public List<AreaGroupEntity> getAreaGroups(String userName) {
        Map<String, Object> parameters = ImmutableMap.<String, Object>builder().put(USER_ID, userName).build();
        return createNamedQuery(QueryNameConstants.FIND_ALL_AREA_GROUP_BY_NAME, parameters).list();
    }

    public List<AreaGroupTypeDto> getAreasByGid(String sqlQuery) {
        Query query = getSession().createSQLQuery(sqlQuery);
        query.setResultTransformer(Transformers.aliasToBean(AreaGroupTypeDto.class));
        return query.list();
    }

    public AreaGroupEntity getAreaGroup(Long groupId) {
        Map<String, Object> parameters = ImmutableMap.<String, Object>builder().put(ID, groupId).build();
        List<AreaGroupEntity> areaGroupEntities = createNamedQuery(QueryNameConstants.FIND_AREA_GROUP_BY_ID, parameters).list();
        if (areaGroupEntities != null && !areaGroupEntities.isEmpty()) {
            return areaGroupEntities.get(0);
        }
        return null;
    }
}
