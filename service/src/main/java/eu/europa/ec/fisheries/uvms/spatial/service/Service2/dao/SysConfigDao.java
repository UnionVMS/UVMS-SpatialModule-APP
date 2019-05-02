/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.Service2.dao;

import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.commons.service.dao.AbstractDAO;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.SysConfigEntity;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

import static eu.europa.ec.fisheries.uvms.commons.service.dao.QueryParameter.*;
import static eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.SysConfigEntity.*;

@Slf4j
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
        while (!oldConfigs.isEmpty()) {
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