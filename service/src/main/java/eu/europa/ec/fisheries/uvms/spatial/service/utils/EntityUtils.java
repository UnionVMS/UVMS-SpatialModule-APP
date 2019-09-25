/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.utils;

import com.google.common.collect.Maps;
import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.upload.AreaUploadMappingProperty;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.*;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.opengis.feature.Property;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
            case "USERAREA":
            case "userArea":
                entity = new UserAreasEntity();
                break;
            default:
                throw new IllegalArgumentException("Type " + value + " not supported");

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

    public static Map<String, Object> createAttributesMap(List<Property> properties) {
        Map<String, Object> resultMap = Maps.newHashMap();
        for (Property property : properties) {
            String name = property.getName().toString();
            Object value = property.getValue();
            resultMap.put(name, value);
        }
        return resultMap;
    }

    public static <T extends BaseAreaEntity> T populateAtributes(T entity, Map<String, Object> values, List<AreaUploadMappingProperty> mapping) {

        try {
            entity.setGeom((Geometry) values.get("the_geom")); // shape file standard
            entity.setEnabled(true);
            entity.setEnabledOn(Instant.now());
            if (mapping != null){
                for (AreaUploadMappingProperty property : mapping){
                    Object value = values.get(property.getTarget());
                    if ("code".equals(property.getSource())){
                        if (value!= null){
                            entity.setCode(String.valueOf(value));
                        }
                    }
                    else if ("name".equals(property.getSource())){
                        if (value!= null){
                            entity.setName(String.valueOf(value));
                        }
                    }
                    else {
                        if(value instanceof Date){
                            value = ((Date) value).toInstant();
                        }
                        FieldUtils.writeDeclaredField(entity, property.getSource(), value, true);
                    }
                }
            }

        } catch (IllegalAccessException e) {
            throw new RuntimeException("ERROR WHILE MAPPING ENTITY", e);
        }
        return entity;
    }
}