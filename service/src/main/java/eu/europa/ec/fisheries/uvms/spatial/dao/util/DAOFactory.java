/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.dao.util;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.dao.AbstractAreaDao;
import eu.europa.ec.fisheries.uvms.spatial.dao.EezDao;
import eu.europa.ec.fisheries.uvms.spatial.dao.FaoDao;
import eu.europa.ec.fisheries.uvms.spatial.dao.FmzDao;
import eu.europa.ec.fisheries.uvms.spatial.dao.GfcmDao;
import eu.europa.ec.fisheries.uvms.spatial.dao.PortAreaDao;
import eu.europa.ec.fisheries.uvms.spatial.dao.PortDao;
import eu.europa.ec.fisheries.uvms.spatial.dao.RfmoDao;
import eu.europa.ec.fisheries.uvms.spatial.dao.StatRectDao;
import eu.europa.ec.fisheries.uvms.spatial.dao.UserAreaDao;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;

public abstract class DAOFactory {

    private DAOFactory() {

    }

    public static AbstractAreaDao getAbstractSpatialDao(final EntityManager em, final String name) throws ServiceException {

        AbstractAreaDao dao;

        Map<String, AbstractAreaDao> daoMap = new HashMap<>();

        daoMap.put("EEZ", new EezDao(em));
        daoMap.put("FAO", new FaoDao(em));
        daoMap.put("RFMO", new RfmoDao(em));
        daoMap.put("PORT", new PortDao(em));
        daoMap.put("GFCM", new GfcmDao(em));
        daoMap.put("FMZ", new FmzDao(em));
        daoMap.put("STATRECT", new StatRectDao(em));
        daoMap.put("USERAREA", new UserAreaDao(em));
        daoMap.put("PORTAREA", new PortAreaDao(em));

        dao = daoMap.get(name.toUpperCase());

        if (dao != null) {
            return dao;
        }
        else {
            throw new ServiceException("DAO NOT FOUND");
        }
    }
}