/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.entity;

import eu.europa.ec.fisheries.uvms.domain.BaseEntity;
import eu.europa.ec.fisheries.uvms.domain.CharBooleanConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.ToString;

@Entity
@NamedQueries({
        @NamedQuery(name = AreaLocationTypesEntity.FIND_ALL_AREA_AND_LOCATION_TYPE_NAMES, query = "FROM AreaLocationTypesEntity area"),
        @NamedQuery(name = AreaLocationTypesEntity.FIND_ALL_IS_LOCATION, query = "FROM AreaLocationTypesEntity area WHERE isLocation = :isLocation and area.areaDbTable <> 'NA'"),
        @NamedQuery(name = AreaLocationTypesEntity.FIND_ALL_IS_LOCATION_IS_SYSTEM_WIDE, query = "FROM AreaLocationTypesEntity WHERE isLocation = :isLocation AND isSystemWide = :isSystemWide"),
        @NamedQuery(name = AreaLocationTypesEntity.FIND_TYPE_BY_NAME, query = "FROM AreaLocationTypesEntity WHERE typeName= :typeName"),
        @NamedQuery(name = AreaLocationTypesEntity.FIND_TYPE_BY_NAMES, query = "FROM AreaLocationTypesEntity WHERE typeName in (:typeNames)"),
        @NamedQuery(name = AreaLocationTypesEntity.FIND_SYSTEM_AREA_LAYER, query = "select area.typeName as typeName, area.areaTypeDesc as areaTypeDesc,"
                + " layer.geoName as geoName, layer.isInternal as isInternal, layer.styleLabelGeom as style,"
                + " provider.serviceType as serviceType FROM AreaLocationTypesEntity as area INNER JOIN area.serviceLayer as layer"
                + " INNER JOIN layer.providerFormat as provider WHERE area.isSystemWide = 'Y' AND area.isLocation =  'N'"
                + " AND area.serviceLayer = layer AND layer.providerFormat = provider"),
        @NamedQuery(name = AreaLocationTypesEntity.FIND_SYSTEM_AREA_AND_LOCATION_LAYER, query = "select area.typeName as typeName, area.areaTypeDesc as areaTypeDesc, area.isLocation as isLocation,"
                + " layer.geoName as geoName, layer.isInternal as isInternal, layer.styleLabelGeom as style,"
                + " provider.serviceType as serviceType FROM AreaLocationTypesEntity as area INNER JOIN area.serviceLayer as layer"
                + " INNER JOIN layer.providerFormat as provider WHERE area.isSystemWide = 'Y'"
                + " AND area.serviceLayer = layer AND layer.providerFormat = provider"),
        @NamedQuery(name = AreaLocationTypesEntity.FIND_USER_AREA_LAYER, query = "SELECT area.typeName as typeName, layer.geoName as geoName, layer.isInternal as isInternal, layer.serviceUrl as serviceUrl, layer.styleLabelGeom as style,"
                + " provider.serviceType as serviceType FROM AreaLocationTypesEntity as area INNER JOIN area.serviceLayer as layer"
                + " INNER JOIN layer.providerFormat as provider WHERE area.isSystemWide = 'N' AND area.isLocation =  'N'"
                + " AND area.serviceLayer = layer AND area.areaDbTable = 'user_areas' AND layer.providerFormat = provider")
})
@Table(name = "area_location_types", uniqueConstraints = @UniqueConstraint(columnNames = "type_name"))
@EqualsAndHashCode(callSuper = true, exclude = "serviceLayer")
@Data
@ToString(exclude = "serviceLayer")
public class AreaLocationTypesEntity extends BaseEntity {

    public static final String FIND_USER_AREA_LAYER = "areaLocationType.findUserAreaLayerMappings";
    public static final String FIND_ALL_IS_LOCATION  = "areaLocationType.findAllIsLocation";
    public static final String FIND_TYPE_BY_NAME = "areaLocationType.findAreaByName";
    public static final String FIND_ALL_IS_LOCATION_IS_SYSTEM_WIDE = "AreaLocationType.findAllByIsLocationIsSystemWide";
    public static final String FIND_SYSTEM_AREA_LAYER = "AreaLocationType.findSystemAreaLayerMappings";
    public static final String FIND_SYSTEM_AREA_AND_LOCATION_LAYER = "AreaLocationType.findSystemAreaAndLocationLayerMappings";
    public static final String FIND_ALL_AREA_AND_LOCATION_TYPE_NAMES = "AreaLocationType.findAllAreaAndLocationTypeNames";
    public static final String FIND_TYPE_BY_NAMES = "AreaLocationType.findAreaByNames";

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_layer_id", nullable = false)
    private ServiceLayerEntity serviceLayer;

    @Column(name = "type_name", unique = true, nullable = false)
    private String typeName;

    @Column(name = "area_type_desc")
    private String areaTypeDesc;

    @Column(name = "area_db_table", nullable = false)
    private String areaDbTable;

    @Convert(converter = CharBooleanConverter.class)
    @Column(name = "is_system_wide", nullable = false, length = 1)
    private Boolean isSystemWide = false;

    @Convert(converter = CharBooleanConverter.class)
    @Column(name = "is_location", nullable = false, length = 1)
    private Boolean isLocation = false;

    public AreaLocationTypesEntity() {
        // why JPA why
    }
}