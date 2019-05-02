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
import java.util.Set;


@Entity
@Table(name = "service_layer")
@NamedQueries({
        @NamedQuery(name = ServiceLayerEntity2.BY_SYSTEM_AREA_TYPE,
                query = "FROM ServiceLayerEntity2 s JOIN FETCH s.areaType a WHERE a.isSystemWide = true AND upper(s.areaType.typeName) = upper(:systemAreaType)"),
        @NamedQuery(name = ServiceLayerEntity2.FIND_SERVICE_LAYERS_BY_ID,
                query = "SELECT serviceLayer FROM ServiceLayerEntity2 serviceLayer WHERE serviceLayer.id in (:ids) order by serviceLayer.id"),
        @NamedQuery(name = ServiceLayerEntity2.FIND_SERVICE_LAYER_BY_SUBTYPE,
                query = "SELECT serviceLayer.id AS id, serviceLayer.name AS name, serviceLayer.layerDesc AS layerDesc, serviceLayer.subType as subType, serviceLayer.areaType.typeName as areaLocationTypeName  " +
                        "FROM ServiceLayerEntity2 serviceLayer WHERE serviceLayer.subType in (:subTypes) order by serviceLayer.id"),
        @NamedQuery(name = ServiceLayerEntity2.FIND_SERVICE_LAYER_BY_SUBTYPE_WITHOUT_BING,
                query = "SELECT serviceLayer.id AS id, serviceLayer.name AS name, serviceLayer.layerDesc AS layerDesc, serviceLayer.subType as subType, serviceLayer.areaType.typeName as areaLocationTypeName " +
                        "FROM ServiceLayerEntity2 serviceLayer INNER JOIN serviceLayer.providerFormat providerFormat " +
                        "WHERE serviceLayer.subType in (:subTypes) AND providerFormat.serviceType <> 'BING' order by serviceLayer.id")
})
public class ServiceLayerEntity2 {

    public static final String FIND_SERVICE_LAYER_BY_SUBTYPE = "serviceLayer2.findServiceLayerBySubType";
    public static final String FIND_SERVICE_LAYER_BY_SUBTYPE_WITHOUT_BING = "serviceLayer2.findServiceLayerBySubTypeWithoutBing";
    public static final String BY_SYSTEM_AREA_TYPE = "serviceLayer2.bySystemAreaType";
    public static final String FIND_SERVICE_LAYERS_BY_ID ="ReportLayerConfig2.findServiceLayerById";

	@Id
	@Column(name = "id")
	@SequenceGenerator(name="SEQ_GEN", sequenceName="service_layer_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_GEN")
	private Long id;

    @ManyToOne
    @JoinColumn(name = "provider_format_id", nullable = false)
    private ProviderFormatEntity2 providerFormat;

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

    @Column(name = "is_internal", nullable = false)
    private Boolean internal = false;
    
    @Column(name = "style_geom")
    private String styleGeom;
    
    @Column(name = "style_label")
    private String styleLabel;
    
    @Column(name = "style_label_geom")
    private String styleLabelGeom;

    @Column(name = "subtype")
    private String subType;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "serviceLayer", cascade = CascadeType.ALL)
    private AreaLocationTypesEntity2 areaType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "serviceLayer", cascade = CascadeType.ALL)
    private Set<ReportConnectServiceAreasEntity2> reportConnectServiceAreas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProviderFormatEntity2 getProviderFormat() {
        return providerFormat;
    }

    public void setProviderFormat(ProviderFormatEntity2 providerFormat) {
        this.providerFormat = providerFormat;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLayerDesc() {
        return layerDesc;
    }

    public void setLayerDesc(String layerDesc) {
        this.layerDesc = layerDesc;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getGeoName() {
        return geoName;
    }

    public void setGeoName(String geoName) {
        this.geoName = geoName;
    }

    public Integer getSrsCode() {
        return srsCode;
    }

    public void setSrsCode(Integer srsCode) {
        this.srsCode = srsCode;
    }

    public String getShortCopyright() {
        return shortCopyright;
    }

    public void setShortCopyright(String shortCopyright) {
        this.shortCopyright = shortCopyright;
    }

    public String getLongCopyright() {
        return longCopyright;
    }

    public void setLongCopyright(String longCopyright) {
        this.longCopyright = longCopyright;
    }

    public Boolean getInternal() {
        return internal;
    }

    public void setInternal(Boolean internal) {
        this.internal = internal;
    }

    public String getStyleGeom() {
        return styleGeom;
    }

    public void setStyleGeom(String styleGeom) {
        this.styleGeom = styleGeom;
    }

    public String getStyleLabel() {
        return styleLabel;
    }

    public void setStyleLabel(String styleLabel) {
        this.styleLabel = styleLabel;
    }

    public String getStyleLabelGeom() {
        return styleLabelGeom;
    }

    public void setStyleLabelGeom(String styleLabelGeom) {
        this.styleLabelGeom = styleLabelGeom;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public AreaLocationTypesEntity2 getAreaType() {
        return areaType;
    }

    public void setAreaType(AreaLocationTypesEntity2 areaType) {
        this.areaType = areaType;
    }

    public Set<ReportConnectServiceAreasEntity2> getReportConnectServiceAreas() {
        return reportConnectServiceAreas;
    }

    public void setReportConnectServiceAreas(Set<ReportConnectServiceAreasEntity2> reportConnectServiceAreas) {
        this.reportConnectServiceAreas = reportConnectServiceAreas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceLayerEntity2 that = (ServiceLayerEntity2) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(providerFormat, that.providerFormat) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(layerDesc, that.layerDesc) &&
                Objects.equals(serviceUrl, that.serviceUrl) &&
                Objects.equals(geoName, that.geoName) &&
                Objects.equals(srsCode, that.srsCode) &&
                Objects.equals(shortCopyright, that.shortCopyright) &&
                Objects.equals(longCopyright, that.longCopyright) &&
                Objects.equals(internal, that.internal) &&
                Objects.equals(styleGeom, that.styleGeom) &&
                Objects.equals(styleLabel, that.styleLabel) &&
                Objects.equals(styleLabelGeom, that.styleLabelGeom) &&
                Objects.equals(subType, that.subType) &&
                Objects.equals(areaType, that.areaType) &&
                Objects.equals(reportConnectServiceAreas, that.reportConnectServiceAreas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, providerFormat, userId, name, layerDesc, serviceUrl, geoName, srsCode, shortCopyright, longCopyright, internal, styleGeom, styleLabel, styleLabelGeom, subType, areaType, reportConnectServiceAreas);
    }
}