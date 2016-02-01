package eu.europa.ec.fisheries.uvms.spatial.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "area_connect_group", schema = "spatial")
public class AreaConnectGroupEntity implements Serializable {

    private static final long serialVersionUID = 6797853213499502855L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "area_group_id", nullable = false)
    private AreaGroupEntity areaGroup;

    @ManyToOne
    @JoinColumn(name = "area_type_id", nullable = false)
    private AreaLocationTypesEntity areaLocationTypes;

    @Column(name = "area_id", nullable = false)
    private long areaId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "areaConnectGroup", cascade = CascadeType.ALL)
    private Set<ReportLayerConfigEntity> reportLayerConfigs;

    public AreaConnectGroupEntity() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AreaGroupEntity getAreaGroup() {
        return this.areaGroup;
    }

    public void setAreaGroup(AreaGroupEntity areaGroup) {
        this.areaGroup = areaGroup;
    }

    public AreaLocationTypesEntity getAreaLocationTypes() {
        return this.areaLocationTypes;
    }

    public void setAreaLocationTypes(AreaLocationTypesEntity areaLocationTypes) {
        this.areaLocationTypes = areaLocationTypes;
    }

    public long getAreaId() {
        return this.areaId;
    }

    public void setAreaId(long areaId) {
        this.areaId = areaId;
    }

    public Set<ReportLayerConfigEntity> getReportLayerConfigs() {
        return this.reportLayerConfigs;
    }

    public void setReportLayerConfigs(Set<ReportLayerConfigEntity> reportLayerConfigs) {
        this.reportLayerConfigs = reportLayerConfigs;
    }

}