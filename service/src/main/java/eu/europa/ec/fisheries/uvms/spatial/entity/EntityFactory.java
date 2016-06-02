package eu.europa.ec.fisheries.uvms.spatial.entity;

import eu.europa.ec.fisheries.uvms.domain.BaseEntity;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;

public abstract class EntityFactory {

    public static BaseEntity getInstance(String value) throws ServiceException {

        BaseEntity entity;

        switch(value){
            case "eez":
            case "EEZ":
            case "Eez":
                entity = new EezEntity();
                break;
            case "fao":
            case "FAO":
            case "Fao":
                entity = new FaoEntity();
                break;
            case "rfmo":
            case "RFMO":
            case "Rfmo":
                entity = new RfmoEntity();
                break;
            case "port":
            case "PORT":
            case "Port":
                entity = new PortEntity();
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
                entity = new PortAreasEntity();
                break;
            case "gfcm":
            case "GFCM":
            case "Gfcm":
                entity = new GfcmEntity();
                break;
            case "statrect":
            case "STATRECT":
            case "STAT_RECT":
            case "StatRect":
            case "statRect":
            case "stat_rect":
            case "Stat_Rect":
                entity = new StatRectEntity();
                break;
            case "FMZ":
            case "fmz":
            case "Fmz":
                entity = new FmzEntity();
                break;
            default:
                throw new ServiceException("Type not supported");

        }
        return entity;
    }
}
