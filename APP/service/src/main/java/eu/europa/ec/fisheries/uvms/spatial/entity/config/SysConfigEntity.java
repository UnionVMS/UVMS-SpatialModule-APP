package eu.europa.ec.fisheries.uvms.spatial.entity.config;

import eu.europa.ec.fisheries.uvms.spatial.entity.BaseEntity;
import lombok.EqualsAndHashCode;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "system_configurations", schema = "spatial")
@EqualsAndHashCode(callSuper = true)
@NamedQueries({
        @NamedQuery(name= SysConfigEntity.FIND_CONFIG_BY_NAME,
        query = "FROM SysConfigEntity config WHERE config.name = :name")
})
public class SysConfigEntity extends BaseEntity {

    public static final String FIND_CONFIG_BY_NAME = "SysConfig.findConfigById";
    public static final String FIND_CONFIG = "SysConfig.findConfig";

    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
