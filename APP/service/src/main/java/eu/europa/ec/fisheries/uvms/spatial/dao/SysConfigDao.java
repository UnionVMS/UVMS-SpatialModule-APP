package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.spatial.entity.config.SysConfigEntity;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

import static eu.europa.ec.fisheries.uvms.service.QueryParameter.*;
import static eu.europa.ec.fisheries.uvms.spatial.entity.config.SysConfigEntity.*;

public class SysConfigDao extends AbstractDAO<SysConfigEntity> {

    private EntityManager em;

    public SysConfigDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<SysConfigEntity> findSystemConfigs() {
        TypedQuery<SysConfigEntity> query = em.createQuery("FROM SysConfigEntity sysConfig", SysConfigEntity.class);
        return query.getResultList();
    }

    public void updateSystemConfigs(List<SysConfigEntity> sysConfigs) {
        List<SysConfigEntity> oldConfigs = this.findSystemConfigs();

        List<SysConfigEntity> mergedConfigs = new ArrayList<>();

        //the following while loop is updating the already existing configurations
        while ( oldConfigs.size() > 0 ) {
            SysConfigEntity oldEntity = oldConfigs.remove(0);

            for(SysConfigEntity sysConfig: sysConfigs) {
                if (sysConfig.getName().equalsIgnoreCase(oldEntity.getName())) {
                    oldEntity.setValue(sysConfig.getValue());
                    sysConfigs.remove(sysConfig);
                    break;
                }
            }

            mergedConfigs.add(oldEntity);
        }

        //and now let's add the new config entries
        if (!sysConfigs.isEmpty()) {
            for(SysConfigEntity sysConfig: sysConfigs) {
                mergedConfigs.add(sysConfig);
            }
        }

        for (SysConfigEntity mergedEntity: mergedConfigs) {
            em.merge(mergedEntity);
        }

        em.flush();

    }

    public List<SysConfigEntity> findSystemConfigByName(String name) throws ServiceException {
        return findEntityByNamedQuery(SysConfigEntity.class, FIND_CONFIG_BY_NAME, with("name", name).parameters(), 1);
    }
}
