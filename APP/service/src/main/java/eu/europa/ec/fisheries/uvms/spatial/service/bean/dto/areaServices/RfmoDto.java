package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices;

import com.vividsolutions.jts.geom.Geometry;

import java.util.Date;

public class RfmoDto {

    private Geometry geometry;
    private String code;
    private String name;
    private String tuna;
    private Boolean enabled;
    private Date enabledOn;

    public Geometry getGeometry() {
        return this.geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTuna() {
        return this.tuna;
    }

    public void setTuna(String tuna) {
        this.tuna = tuna;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Date getEnabledOn() {
        return enabledOn;
    }

    public void setEnabledOn(Date enabledOn) {
        this.enabledOn = enabledOn;
    }

}
