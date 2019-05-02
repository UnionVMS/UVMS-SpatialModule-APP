/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Set;

import eu.europa.ec.fisheries.uvms.commons.domain.BaseEntity;
import eu.europa.ec.fisheries.uvms.commons.domain.CharBooleanConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Entity
@Table(name = "service_layer")
@NamedQueries({
        @NamedQuery(name = ServiceLayerEntity.BY_SYSTEM_AREA_TYPE,
                query = "FROM ServiceLayerEntity s JOIN FETCH s.areaType a WHERE a.isSystemWide = 'Y' AND upper(s.areaType.typeName) = upper(:systemAreaType)"),
        @NamedQuery(name = ServiceLayerEntity.FIND_SERVICE_LAYERS_BY_ID,
                query = "SELECT serviceLayer FROM ServiceLayerEntity serviceLayer WHERE serviceLayer.id in (:ids) order by serviceLayer.id"),
        @NamedQuery(name = ServiceLayerEntity.FIND_SERVICE_LAYER_BY_SUBTYPE,
                query = "SELECT serviceLayer.id AS id, serviceLayer.name AS name, serviceLayer.layerDesc AS layerDesc, serviceLayer.subType as subType, serviceLayer.areaType.typeName as areaLocationTypeName  " +
                        "FROM ServiceLayerEntity serviceLayer WHERE serviceLayer.subType in (:subTypes) order by serviceLayer.id"),
        @NamedQuery(name = ServiceLayerEntity.FIND_SERVICE_LAYER_BY_SUBTYPE_WITHOUT_BING,
                query = "SELECT serviceLayer.id AS id, serviceLayer.name AS name, serviceLayer.layerDesc AS layerDesc, serviceLayer.subType as subType, serviceLayer.areaType.typeName as areaLocationTypeName " +
                        "FROM ServiceLayerEntity serviceLayer INNER JOIN serviceLayer.providerFormat providerFormat " +
                        "WHERE serviceLayer.subType in (:subTypes) AND providerFormat.serviceType <> 'BING' order by serviceLayer.id")
})
@EqualsAndHashCode(callSuper = true, exclude = {"reportConnectServiceAreas"})
@Data
@ToString(exclude = {"reportConnectServiceAreas"})
public class ServiceLayerEntity extends BaseEntity {

    public static final String FIND_SERVICE_LAYER_BY_SUBTYPE = "serviceLayer.findServiceLayerBySubType";
    public static final String FIND_SERVICE_LAYER_BY_SUBTYPE_WITHOUT_BING = "serviceLayer.findServiceLayerBySubTypeWithoutBing";
    public static final String BY_SYSTEM_AREA_TYPE = "serviceLayer.bySystemAreaType";
    public static final String FIND_SERVICE_LAYERS_BY_ID ="ReportLayerConfig.findServiceLayerById";

	@Id
	@Column(name = "id")
	@SequenceGenerator(name="SEQ_GEN", sequenceName="service_layer_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_GEN")
	private Long id;

    @ManyToOne
    @JoinColumn(name = "provider_format_id", nullable = false)
    private ProviderFormatEntity providerFormat;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "layer_desc")
    private String layerDesc;

    @Column(name = "service_url")
    private String serviceUrl;

    @Column(name = "geo_name")
    private String geoName;

    @Column(name = "srs_code")
    private Integer srsCode;

    @Column(name = "short_copyright")
    private String shortCopyright;

    @Column(columnDefinition = "text", name = "long_copyright")
    private String longCopyright;

    @Convert(converter = CharBooleanConverter.class)
    @Column(name = "is_internal", nullable = false, length = 1)
    private Boolean isInternal = false;
    
    @Column(name = "style_geom")
    private String styleGeom;
    
    @Column(name = "style_label")
    private String styleLabel;
    
    @Column(name = "style_label_geom")
    private String styleLabelGeom;

    @Column(name = "subtype")
    private String subType;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "serviceLayer", cascade = CascadeType.ALL)
    private AreaLocationTypesEntity areaType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "serviceLayer", cascade = CascadeType.ALL)
    private Set<ReportConnectServiceAreasEntity> reportConnectServiceAreas;

    public ServiceLayerEntity() {
        // why JPA why.
    }

}