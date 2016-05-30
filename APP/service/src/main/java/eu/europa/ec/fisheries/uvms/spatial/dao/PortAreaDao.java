package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.PortAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.PortEntity;
import lombok.extern.slf4j.Slf4j;
import javax.persistence.EntityManager;
import java.util.Map;

import static eu.europa.ec.fisheries.uvms.spatial.entity.PortAreasEntity.*;

@Slf4j
public class PortAreaDao extends AbstractSpatialDao<PortAreasEntity> {

    private EntityManager em;

    public PortAreaDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    protected String getIntersectNamedQuery() {
        return PORT_AREA_BY_COORDINATE;
    }

    @Override
    protected String getSearchNamedQuery() {
        return SEARCH_PORTAREAS;
    }

    @Override
    protected Class<PortAreasEntity> getClazz() {
        return PortAreasEntity.class;
    }

    @Override
    protected PortAreasEntity createEntity(Map<String, Object> values) throws ServiceException {
        return new PortAreasEntity(values);
    }

    @Override
    protected String getDisableAreaNamedQuery() {
        return PortEntity.DISABLE;
    }

}
