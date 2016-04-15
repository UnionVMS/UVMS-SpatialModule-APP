package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaLayerDto;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import javax.persistence.EntityManager;
import java.util.List;

import static eu.europa.ec.fisheries.uvms.service.QueryParameter.*;
import static eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity.*;

public class AreaLocationTypesDao extends AbstractDAO<AreaLocationTypesEntity> {

    private EntityManager em;

    public AreaLocationTypesDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<AreaLocationTypesEntity> findByTypeName(final String typeName) throws ServiceException {
        return findEntityByNamedQuery(AreaLocationTypesEntity.class,FIND_TYPE_BY_NAME,with("typeName", typeName).parameters());
    }

    public List<AreaLocationTypesEntity> findByIsLocationAndIsSystemWide(Boolean isLocation, Boolean isSystemWide ) throws ServiceException {
        return findEntityByNamedQuery(AreaLocationTypesEntity.class, FIND_ALL_IS_LOCATION_IS_SYSTEM_WIDE,
                with("isLocation", isLocation).and("isSystemWide", isSystemWide).parameters());
    }

    public List<AreaLocationTypesEntity> findByIsLocation(Boolean isLocation) throws ServiceException {
        return findEntityByNamedQuery(AreaLocationTypesEntity.class, FIND_ALL_IS_LOCATION,
                with("isLocation", isLocation).parameters());
    }

    public List findUserAreaLayerMapping() {
        Query query = em.unwrap(Session.class).getNamedQuery(QueryNameConstants.FIND_USER_AREA_LAYER);
        query.setResultTransformer(Transformers.aliasToBean(UserAreaLayerDto.class));
        return query.list();
    }

    public List<AreaLayerDto> findSystemAreaLayerMapping() {
        Query query = em.unwrap(Session.class).getNamedQuery(AreaLocationTypesEntity.FIND_SYSTEM_AREA_LAYER);
        return query.setResultTransformer(Transformers.aliasToBean(AreaLayerDto.class)).list();
    }

    public List<AreaLayerDto> findSystemAreaAndLocationLayerMapping() {
        Query query = em.unwrap(Session.class).getNamedQuery(AreaLocationTypesEntity.FIND_SYSTEM_AREA_AND_LOCATION_LAYER);
        return query.setResultTransformer(Transformers.aliasToBean(AreaLayerDto.class)).list();
    }

    public List<AreaLocationTypesEntity> findAll() throws ServiceException {
        return findEntityByNamedQuery(AreaLocationTypesEntity.class, AreaLocationTypesEntity.FIND_ALL_AREA_AND_LOCATION_TYPE_NAMES);
    }
}
