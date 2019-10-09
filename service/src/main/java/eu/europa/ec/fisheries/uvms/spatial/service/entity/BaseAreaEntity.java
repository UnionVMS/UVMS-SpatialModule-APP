/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.spatial.service.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTWriter;
import eu.europa.ec.fisheries.uvms.commons.date.DateUtils;
import eu.europa.ec.fisheries.uvms.commons.domain.BaseEntity;
import eu.europa.ec.fisheries.uvms.commons.domain.CharBooleanConverter;
import eu.europa.ec.fisheries.uvms.commons.geometry.mapper.GeometryMapper;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.upload.UploadMappingProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.hibernate.annotations.Type;
import org.opengis.feature.Property;

@MappedSuperclass
@Slf4j
@AttributeOverride(name = "id", column = @Column(name = "GID"))
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseAreaEntity extends BaseEntity {

    private static final String ISO_8859_1 = "ISO-8859-1";
    private static final String UTF_8 = "UTF-8";

    @JsonIgnore
    @Column(columnDefinition = "Geometry")
    private Geometry geom;

    private String name;

    @Column(length = 20)
    private String code;

    //@Convert(converter = CharBooleanConverter.class)
    @Column(nullable = false, length = 1)
    private Boolean enabled = true;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "enabled_on")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date enabledOn;

    public String getGeometry(){
        return GeometryMapper.INSTANCE.geometryToWkt(geom).getValue();
    }

    @JsonIgnore
    public String getGeometryType(){
        String geometryType = null;
        if (geom !=null){
            geometryType = geom.getGeometryType();
        }
        return geometryType;
    }

    public String getExtent() {
        String extent = null;
        if (geom != null) {
            extent = new WKTWriter().write(geom.getEnvelope());
        }
        return extent;
    }

    public String getCentroid(){
        String centroid = null;
        if (geom != null) {
            centroid = new WKTWriter().write(geom.getCentroid());
        }
        return centroid;
    }

    public BaseAreaEntity(Map<String, Object> values, List<UploadMappingProperty> mapping) throws ServiceException {

        try {
            geom = (Geometry) values.get("the_geom"); // shape file standard
            enabled = true;
            enabledOn = new Date();
            if (mapping != null){
                for (UploadMappingProperty property : mapping){
                    Object value = values.get(property.getTarget());
                    if ("code".equals(property.getSource())){
                        if (value!= null){
                            code = String.valueOf(value);
                        }
                    }
                    else if ("name".equals(property.getSource())){
                        if (value!= null){
                            name = String.valueOf(value);
                        }
                    }
                    else {
                        FieldUtils.writeDeclaredField(this, property.getSource(), value, true);
                    }
                }
            }

        } catch (IllegalAccessException e) {
            throw new ServiceException("ERROR WHILE MAPPING ENTITY", e);
        }
    }

    public BaseAreaEntity() {
        // why JPA why
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
}