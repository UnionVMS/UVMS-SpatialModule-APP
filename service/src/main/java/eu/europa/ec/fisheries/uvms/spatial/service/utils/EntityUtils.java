/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.utils;

import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class EntityUtils {

    private EntityUtils(){

    }

    public static BaseAreaEntity getInstance(String value) {

        BaseAreaEntity entity;

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
                entity = new PortAreaEntity();
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
            default:
                throw new IllegalArgumentException("Type not supported");

        }
        return entity;
    }

    public static List<Field> listMembers(BaseAreaEntity entity){
        List<Field> fields = new ArrayList<>();
        try {
            Field[] declaredFields = entity.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                if(!field.getName().contains("this") && !field.isSynthetic() &&
                        field.getModifiers() != Modifier.STATIC + Modifier.PUBLIC + Modifier.FINAL
                        && field.getModifiers() != Modifier.STATIC + Modifier.PRIVATE + Modifier.FINAL) {
                    fields.add(field);
                }
            }
        } catch (Exception e){
            //Handle your exception here.
        }
        return fields;
    }
}