package eu.europa.ec.fisheries.uvms.spatial.entity;

import eu.europa.ec.fisheries.uvms.spatial.entity.converter.CharBooleanConverter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "service_layer", schema = "spatial")
public class ServiceLayerEntity implements Serializable {

    private static final long serialVersionUID = 6797853213499502871L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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

    @Column(name = "short_copyright", nullable = false, length = 255)
    private String shortCopyright;

    @Lob
    @Column(name = "long_copyright")
    private String longCopyright;

    @Convert(converter = CharBooleanConverter.class)
    @Column(name = "is_background", nullable = false, length = 1)
    private Boolean isBackground = false;

    @Convert(converter = CharBooleanConverter.class)
    @Column(name = "is_internal", nullable = false, length = 1)
    private Boolean isInternal = false;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "serviceLayer", cascade = CascadeType.ALL)
    private Set<AreaTypesEntity> areaTypeses;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "serviceLayer", cascade = CascadeType.ALL)
    private Set<ReportConnectServiceAreasEntity> reportConnectServiceAreases;

    public ServiceLayerEntity() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Boolean getIsBackground() {
        return this.isBackground;
    }

    public void setIsBackground(Boolean isBackground) {
        this.isBackground = isBackground;
    }

    public Boolean getIsInternal() {
        return this.isInternal;
    }

    public void setIsInternal(Boolean isInternal) {
        this.isInternal = isInternal;
    }

    public Set<AreaTypesEntity> getAreaTypeses() {
        return this.areaTypeses;
    }

    public void setAreaTypeses(Set<AreaTypesEntity> areaTypeses) {
        this.areaTypeses = areaTypeses;
    }

    public Set<ReportConnectServiceAreasEntity> getReportConnectServiceAreases() {
        return this.reportConnectServiceAreases;
    }

    public void setReportConnectServiceAreases(Set<ReportConnectServiceAreasEntity> reportConnectServiceAreases) {
        this.reportConnectServiceAreases = reportConnectServiceAreases;
    }

}
