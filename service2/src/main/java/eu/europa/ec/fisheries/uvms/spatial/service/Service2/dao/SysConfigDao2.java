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
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.SysConfigEntity2;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class SysConfigDao2  {

    @PersistenceContext
    private EntityManager em;


    public List<SysConfigEntity2> findSystemConfigs() {
        TypedQuery<SysConfigEntity2> query = em.createQuery("FROM SysConfigEntity2 sysConfig", SysConfigEntity2.class);
        return query.getResultList();
    }

    public void updateSystemConfigs(List<SysConfigEntity2> sysConfigs) {

        List<SysConfigEntity2> oldConfigs = this.findSystemConfigs();
        List<SysConfigEntity2> mergedConfigs = new ArrayList<>();

        //the following while loop is updating the already existing configurations
        while (!oldConfigs.isEmpty()) {
            SysConfigEntity2 oldEntity = oldConfigs.remove(0);
            for(SysConfigEntity2 sysConfig: sysConfigs) {
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
            for(SysConfigEntity2 sysConfig: sysConfigs) {
                mergedConfigs.add(sysConfig);
            }
        }

        for (SysConfigEntity2 mergedEntity: mergedConfigs) {
            em.merge(mergedEntity);
        }

        em.flush();

    }

    public List<SysConfigEntity2> findSystemConfigByName(String name) throws ServiceException {

        TypedQuery<SysConfigEntity2> query = em.createNamedQuery( SysConfigEntity2.FIND_CONFIG_BY_NAME,SysConfigEntity2.class );
        query.setParameter("name",name) ;
        return query.getResultList();
    }
}