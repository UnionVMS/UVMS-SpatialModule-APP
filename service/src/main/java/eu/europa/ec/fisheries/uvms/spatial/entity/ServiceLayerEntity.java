/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.entity;

import eu.europa.ec.fisheries.uvms.domain.CharBooleanConverter;

import java.util.*;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import eu.europa.ec.fisheries.uvms.domain.BaseEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.LayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.StylesDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm.ReferenceDataPropertiesDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

@Entity
@Table(name = "service_layer")
@NamedQueries({
        @NamedQuery(name = ServiceLayerEntity.BY_LOCATION_TYPE,
                query = "FROM ServiceLayerEntity s JOIN FETCH s.areaType a WHERE a.isSystemWide = 'Y' AND upper(s.areaType.typeName) = upper(:locationType)"),
        @NamedQuery(name = ServiceLayerEntity.FIND_SERVICE_LAYERS_BY_ID,
                query = "SELECT serviceLayer FROM ServiceLayerEntity serviceLayer WHERE serviceLayer.id in (:ids) order by serviceLayer.id"),
        @NamedQuery(name = QueryNameConstants.FIND_SERVICE_LAYER_BY_SUBTYPE,
                query = "SELECT serviceLayer.id AS id, serviceLayer.name AS name, serviceLayer.layerDesc AS layerDesc, serviceLayer.subType as subType, serviceLayer.areaType.typeName as areaLocationTypeName  " +
                        "From ServiceLayerEntity serviceLayer WHERE serviceLayer.subType in (:subTypes) order by serviceLayer.id"),
        @NamedQuery(name = QueryNameConstants.FIND_SERVICE_LAYER_BY_SUBTYPE_WITHOUT_BING,
                query = "SELECT serviceLayer.id AS id, serviceLayer.name AS name, serviceLayer.layerDesc AS layerDesc, serviceLayer.subType as subType, serviceLayer.areaType.typeName as areaLocationTypeName " +
                        "From ServiceLayerEntity serviceLayer INNER JOIN serviceLayer.providerFormat providerFormat " +
                        "WHERE serviceLayer.subType in (:subTypes) AND providerFormat.serviceType <> 'BING' order by serviceLayer.id"),
        @NamedQuery(name = ServiceLayerEntity.BY_AREA_LOCATION_TYPE,
                query = "FROM ServiceLayerEntity serviceLayer WHERE upper(serviceLayer.areaType.typeName) = upper(:typeName)")
})
@EqualsAndHashCode(callSuper = true, exclude = "reportConnectServiceAreas")
@Data
public class ServiceLayerEntity extends BaseEntity {

    public static final String BY_LOCATION_TYPE = "ServiceLayer.byLocationType";
    public static final String FIND_SERVICE_LAYERS_BY_ID ="ReportLayerConfig.findServiceLayerById";
    public static final String BY_AREA_LOCATION_TYPE = "ServiceLayer.byAreaLocationType";
    private static final String GEOSERVER = "geoserver";

    @ManyToOne
    @JoinColumn(name = "provider_format_id", nullable = false)
    private ProviderFormatEntity providerFormat;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "layer_desc", length = 255)
    private String layerDesc;

    @Column(name = "service_url")
    private String serviceUrl;

    @Column(name = "geo_name", length = 255)
    private String geoName;

    @Column(name = "srs_code")
    private Integer srsCode;

    @Column(name = "short_copyright", length = 255)
    private String shortCopyright;

    @Column(columnDefinition = "text", name = "long_copyright")
    private String longCopyright;

    @Convert(converter = CharBooleanConverter.class)
    @Column(name = "is_internal", nullable = false, length = 1)
    private Boolean isInternal = false;
    
    @Column(name = "style_geom", length = 255)
    private String styleGeom;
    
    @Column(name = "style_label", length = 255)
    private String styleLabel;
    
    @Column(name = "style_label_geom", length = 255)
    private String styleLabelGeom;

    @Column(name = "subtype", length = 255)
    private String subType;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "serviceLayer", cascade = CascadeType.ALL)
    private AreaLocationTypesEntity areaType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "serviceLayer", cascade = CascadeType.ALL)
    private Set<ReportConnectServiceAreasEntity> reportConnectServiceAreas;

    public ServiceLayerEntity() {
        // why JPA why
    }
}