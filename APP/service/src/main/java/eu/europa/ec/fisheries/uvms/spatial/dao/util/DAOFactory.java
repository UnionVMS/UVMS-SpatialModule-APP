package eu.europa.ec.fisheries.uvms.spatial.dao.util;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.dao.AbstractSpatialDao;
import eu.europa.ec.fisheries.uvms.spatial.dao.EezDao;
import eu.europa.ec.fisheries.uvms.spatial.dao.FaoDao;
import eu.europa.ec.fisheries.uvms.spatial.dao.FmzDao;
import eu.europa.ec.fisheries.uvms.spatial.dao.GfcmDao;
import eu.europa.ec.fisheries.uvms.spatial.dao.PortAreaDao;
import eu.europa.ec.fisheries.uvms.spatial.dao.PortDao;
import eu.europa.ec.fisheries.uvms.spatial.dao.RacDao;
import eu.europa.ec.fisheries.uvms.spatial.dao.RfmoDao;
import eu.europa.ec.fisheries.uvms.spatial.dao.StatRectDao;
import eu.europa.ec.fisheries.uvms.spatial.dao.UserAreaDao;
import javax.persistence.EntityManager;

public abstract class DAOFactory {

    public static AbstractSpatialDao getAbstractSpatialDao(EntityManager em, String name) throws ServiceException {

        AbstractSpatialDao dao;

        switch(name){
            case "eez":
            case "EEZ":
            case "Eez":
                dao = new EezDao(em);
                break;
            case "fao":
            case "FAO":
            case "Fao":
                dao = new FaoDao(em);
                break;
            case "rfmo":
            case "RFMO":
            case "Rfmo":
                dao = new RfmoDao(em);
                break;
            case "port":
            case "PORT":
            case "Port":
                dao = new PortDao(em);
                break;
            case "portarea":
            case "portareas":
            case "PORTAREA":
            case "PORTAREAS":
            case "PORT_AREA":
            case "PORT_AREAS":
            case "port_area":
            case "port_areas":
            case "PortArea":
            case "portArea":
            case "portAreas":
            case "Port_Area":
            case "Port_Areas":
                dao = new PortAreaDao(em);
                break;
            case "gfcm":
            case "GFCM":
            case "Gfcm":
                dao = new GfcmDao(em);
                break;
            case "rac":
            case "RAC":
            case "Rac":
                dao = new RacDao(em);
                break;
            case "statrect":
            case "STATRECT":
            case "STAT_RECT":
            case "StatRect":
            case "statRect":
            case "stat_rect":
            case "Stat_Rect":
                dao = new StatRectDao(em);
                break;
            case "USERAREA":
            case "userArea":
            case "userarea":
            case "UserArea":
                dao = new UserAreaDao(em);
                break;
            case "FMZ":
            case "fmz":
            case "Fmz":
                dao = new FmzDao(em);
                break;
            default:
                throw new ServiceException("Type not supported");

        }
        return dao;
    }

}
