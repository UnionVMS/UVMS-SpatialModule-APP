/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity;


import javax.persistence.*;
import java.util.Objects;


@Entity
@NamedQueries({
        @NamedQuery(name = AreaLocationTypesEntity2.FIND_ALL_AREA_AND_LOCATION_TYPE_NAMES, query = "FROM AreaLocationTypesEntity2 area"),
        @NamedQuery(name = AreaLocationTypesEntity2.FIND_ALL_IS_LOCATION, query = "FROM AreaLocationTypesEntity2 area WHERE location = :isLocation and area.areaDbTable <> 'NA'"),
        @NamedQuery(name = AreaLocationTypesEntity2.FIND_ALL_IS_LOCATION_IS_SYSTEM_WIDE, query = "FROM AreaLocationTypesEntity2 WHERE location = :location AND systemWide = :systemWide"),
        @NamedQuery(name = AreaLocationTypesEntity2.FIND_TYPE_BY_NAME, query = "FROM AreaLocationTypesEntity2 WHERE typeName= :typeName"),
        @NamedQuery(name = AreaLocationTypesEntity2.FIND_TYPE_BY_NAMES, query = "FROM AreaLocationTypesEntity2 WHERE typeName in (:typeNames)"),
        @NamedQuery(name = AreaLocationTypesEntity2.FIND_SYSTEM_AREA_LAYER, query = "select area.typeName as typeName, area.areaTypeDesc as areaTypeDesc,"
                + " layer.geoName as geoName, layer.internal as internal, layer.styleLabelGeom as style,"
                + " provider.serviceType as serviceType FROM AreaLocationTypesEntity2 as area INNER JOIN area.serviceLayer as layer"
                + " INNER JOIN layer.providerFormat as provider WHERE area.systemWide = true AND area.location =  false"
                + " AND area.serviceLayer = layer AND layer.providerFormat = provider AND area.areaDbTable <> 'user_areas'"),
        @NamedQuery(name = AreaLocationTypesEntity2.FIND_SYSTEM_AREA_AND_LOCATION_LAYER, query = "select area.typeName as typeName, area.areaTypeDesc as areaTypeDesc, area.location as location,"
                + " layer.geoName as geoName, layer.internal as internal, layer.styleLabelGeom as style,"
                + " provider.serviceType as serviceType FROM AreaLocationTypesEntity2 as area INNER JOIN area.serviceLayer as layer"
                + " INNER JOIN layer.providerFormat as provider WHERE area.systemWide = true"
                + " AND area.serviceLayer = layer AND layer.providerFormat = provider AND area.areaDbTable <> 'user_areas'"),
        @NamedQuery(name = AreaLocationTypesEntity2.FIND_USER_AREA_LAYER, query = "SELECT new eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto.AreaLayerDto2(area.typeName, layer.geoName, area.areaTypeDesc, layer.styleLabelGeom, provider.serviceType"
                + " FROM AreaLocationTypesEntity2 as area INNER JOIN area.serviceLayer as layer"
                + " INNER JOIN layer.providerFormat as provider WHERE area.systemWide = true AND area.location = false"
                + " AND area.serviceLayer = layer AND area.areaDbTable = 'user_areas' AND layer.providerFormat = provider")
})
@Table(name = "area_location_types", uniqueConstraints = @UniqueConstraint(columnNames = "type_name"))
public class AreaLocationTypesEntity2 {

    public static final String FIND_USER_AREA_LAYER = "areaLocationType2.findUserAreaLayerMappings";
    public static final String FIND_ALL_IS_LOCATION  = "areaLocationType2.findAllIsLocation";
    public static final String FIND_TYPE_BY_NAME = "areaLocationType2.findAreaByName";
    public static final String FIND_ALL_IS_LOCATION_IS_SYSTEM_WIDE = "AreaLocationType2.findAllByIsLocationIsSystemWide";
    public static final String FIND_SYSTEM_AREA_LAYER = "AreaLocationType2.findSystemAreaLayerMappings";
    public static final String FIND_SYSTEM_AREA_AND_LOCATION_LAYER = "AreaLocationType2.findSystemAreaAndLocationLayerMappings";
    public static final String FIND_ALL_AREA_AND_LOCATION_TYPE_NAMES = "AreaLocationType2.findAllAreaAndLocationTypeNames";
    public static final String FIND_TYPE_BY_NAMES = "AreaLocationType2.findAreaByNames";
	
	
	@Id
	@Column(name = "id")
	@SequenceGenerator(name="SEQ_LOC_TYPE_GEN", sequenceName="area_location_types_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_LOC_TYPE_GEN")
	private Long id;
	
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_layer_id", nullable = false)
    private ServiceLayerEntity2 serviceLayer;

    @Column(name = "type_name", unique = true, nullable = false)
    private String typeName;

    @Column(name = "area_type_desc")
    private String areaTypeDesc;

    @Column(name = "area_db_table", nullable = false)
    private String areaDbTable;

    @Column(name = "is_system_wide", nullable = false)
    private Boolean systemWide = false;

    @Column(name = "is_location", nullable = false)
    private Boolean location = false;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ServiceLayerEntity2 getServiceLayer() {
        return serviceLayer;
    }

    public void setServiceLayer(ServiceLayerEntity2 serviceLayer) {
        this.serviceLayer = serviceLayer;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getAreaTypeDesc() {
        return areaTypeDesc;
    }

    public void setAreaTypeDesc(String areaTypeDesc) {
        this.areaTypeDesc = areaTypeDesc;
    }

    public String getAreaDbTable() {
        return areaDbTable;
    }

    public void setAreaDbTable(String areaDbTable) {
        this.areaDbTable = areaDbTable;
    }

    public Boolean getSystemWide() {
        return systemWide;
    }

    public void setSystemWide(Boolean systemWide) {
        this.systemWide = systemWide;
    }

    public Boolean getLocation() {
        return location;
    }

    public void setLocation(Boolean location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AreaLocationTypesEntity2 that = (AreaLocationTypesEntity2) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(serviceLayer, that.serviceLayer) &&
                Objects.equals(typeName, that.typeName) &&
                Objects.equals(areaTypeDesc, that.areaTypeDesc) &&
                Objects.equals(areaDbTable, that.areaDbTable) &&
                Objects.equals(systemWide, that.systemWide) &&
                Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serviceLayer, typeName, areaTypeDesc, areaDbTable, systemWide, location);
    }
}