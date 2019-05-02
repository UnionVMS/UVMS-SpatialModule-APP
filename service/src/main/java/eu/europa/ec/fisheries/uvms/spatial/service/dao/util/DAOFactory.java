/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.dao.util;

import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class DAOFactory {

    private static Map<String, Class> syncMap;

    static {

        Map<String, Class> map = new HashMap<>();

        map.put("EEZ", EezDao.class);
        map.put("FAO", FaoDao.class);
        map.put("RFMO", RfmoDao.class);
        map.put("PORT", PortDao.class);
        map.put("GFCM", GfcmDao.class);
        map.put("FMZ", FmzDao.class);
        map.put("STATRECT", StatRectDao.class);
        map.put("STAT_RECT", StatRectDao.class);
        map.put("USERAREA", UserAreaDao.class);
        map.put("PORTAREA", PortAreaDao.class);
        map.put("PORT_AREA", PortAreaDao.class);
        map.put("PORTAREAS", PortAreaDao.class);
        map.put("PORT_AREAS", PortAreaDao.class);

        syncMap = Collections.synchronizedMap(map);
    }

    private DAOFactory() {

    }

    @Deprecated
    public static AbstractAreaDao getAbstractSpatialDao(final EntityManager em, final String name) throws ServiceException {

        AbstractAreaDao dao;

        try {
            dao = (AbstractAreaDao) syncMap.get(name.toUpperCase()).getDeclaredConstructor(EntityManager.class).newInstance(em);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("DAO NOT FOUND");
        }

        return dao;
    }
}