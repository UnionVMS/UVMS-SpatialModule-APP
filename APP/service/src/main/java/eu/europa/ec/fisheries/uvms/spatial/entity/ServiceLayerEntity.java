package eu.europa.ec.fisheries.uvms.spatial.entity;

import java.io.Serializable;
import java.util.Set;
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
import javax.persistence.Table;
import eu.europa.ec.fisheries.uvms.spatial.entity.converter.CharBooleanConverter;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.LayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.StylesDto;
import org.apache.commons.lang3.StringUtils;

@Entity
@Table(name = "service_layer", schema = "spatial")
@NamedQueries({
        @NamedQuery(name = ServiceLayerEntity.BY_NAME,
                query = "FROM ServiceLayerEntity s WHERE upper(s.name) = upper(:name)"),
        @NamedQuery(name = QueryNameConstants.FIND_SERVICE_LAYERS_BY_ID,
                query = "SELECT serviceLayer FROM ServiceLayerEntity serviceLayer WHERE serviceLayer.id in (:ids) order by serviceLayer.id"),
        @NamedQuery(name = QueryNameConstants.FIND_SERVICE_LAYER_BY_SUBTYPE,
                query = "SELECT serviceLayer.id AS id, serviceLayer.name AS name, serviceLayer.layerDesc AS layerDesc, serviceLayer.subType as subType " +
                        "From ServiceLayerEntity serviceLayer WHERE serviceLayer.subType in (:subTypes) order by serviceLayer.id"),
        @NamedQuery(name = QueryNameConstants.FIND_SERVICE_LAYER_BY_SUBTYPE_WITHOUT_BING,
                query = "SELECT serviceLayer.id AS id, serviceLayer.name AS name, serviceLayer.layerDesc AS layerDesc, serviceLayer.subType as subType " +
                        "From ServiceLayerEntity serviceLayer INNER JOIN serviceLayer.providerFormat providerFormat " +
                        "WHERE serviceLayer.subType in (:subTypes) AND providerFormat.serviceType <> 'BING' order by serviceLayer.id")
})
public class ServiceLayerEntity extends BaseEntity {

    public static final String BY_NAME = "ServiceLayer.byName";

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
    }

    public ProviderFormatEntity getProviderFormat() {
        return this.providerFormat;
    }

    public void setProviderFormat(ProviderFormatEntity providerFormat) {
        this.providerFormat = providerFormat;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLayerDesc() {
        return this.layerDesc;
    }

    public void setLayerDesc(String layerDesc) {
        this.layerDesc = layerDesc;
    }

    public String getServiceUrl() {
        return this.serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getGeoName() {
        return this.geoName;
    }

    public void setGeoName(String geoName) {
        this.geoName = geoName;
    }

    public Integer getSrsCode() {
        return this.srsCode;
    }

    public void setSrsCode(Integer srsCode) {
        this.srsCode = srsCode;
    }

    public String getShortCopyright() {
        return this.shortCopyright;
    }

    public void setShortCopyright(String shortCopyright) {
        this.shortCopyright = shortCopyright;
    }

    public String getLongCopyright() {
        return this.longCopyright;
    }

    public void setLongCopyright(String longCopyright) {
        this.longCopyright = longCopyright;
    }

    public Boolean getIsInternal() {
        return this.isInternal;
    }

    public void setIsInternal(Boolean isInternal) {
        this.isInternal = isInternal;
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

	public AreaLocationTypesEntity getAreaType() {
        return this.areaType;
    }

    public void setAreaType(AreaLocationTypesEntity areaType) {
        this.areaType = areaType;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public Set<ReportConnectServiceAreasEntity> getReportConnectServiceAreas() {
        return this.reportConnectServiceAreas;
    }

    public void setReportConnectServiceAreas(Set<ReportConnectServiceAreasEntity> reportConnectServiceAreas) {
        this.reportConnectServiceAreas = reportConnectServiceAreas;
    }

    public boolean isStyleEmpty() {
        return (StringUtils.isEmpty(styleGeom) && StringUtils.isEmpty(styleLabel) && StringUtils.isEmpty(styleLabelGeom));
    }

    public LayerDto convertToServiceLayer(String geoServerUrl, String bingApiKey, boolean isBaseLayer) {
        LayerDto layerDto = new LayerDto();
        String type = getProviderFormat().getServiceType();
        layerDto.setType(type);
        layerDto.setTitle(getName());
        layerDto.setIsBaseLayer(isBaseLayer);
        layerDto.setShortCopyright(getShortCopyright());
        layerDto.setLongCopyright(getLongCopyright());
        if(!(type.equalsIgnoreCase("OSM") || type.equalsIgnoreCase("OSEA") || type.equalsIgnoreCase("BING"))) {
            layerDto.setUrl(geoServerUrl.concat(getProviderFormat().getServiceType().toLowerCase()));
        }
        layerDto.setServerType(getIsInternal() ? GEOSERVER : null);
        layerDto.setLayerGeoName(getGeoName());
        setStyle(layerDto);
        if (type.equalsIgnoreCase("BING")) {
            layerDto.setApiKey(bingApiKey);
        }
        return layerDto;
    }

    private void setStyle(LayerDto layerDto) {
        if(!isStyleEmpty()) {
            layerDto.setStyles(new StylesDto(getStyleGeom(), getStyleLabel(), getStyleLabelGeom()));
        }
    }
}
