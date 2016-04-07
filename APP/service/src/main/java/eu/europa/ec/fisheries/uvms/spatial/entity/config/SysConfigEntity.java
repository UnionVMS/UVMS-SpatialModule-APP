package eu.europa.ec.fisheries.uvms.spatial.entity.config;

import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import lombok.EqualsAndHashCode;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "system_configurations", schema = "spatial")
@EqualsAndHashCode
@NamedQueries({
        @NamedQuery(name= SysConfigEntity.FIND_CONFIG_BY_NAME,
        query = "FROM SysConfigEntity config WHERE config.name = :name"),
        @NamedQuery(name= QueryNameConstants.FIND_CONFIG,
                query = "FROM SysConfigEntity config WHERE config.name = :name")
})
public class SysConfigEntity implements Serializable {

    public static final String FIND_CONFIG_BY_NAME = "SysConfig.findConfigById";

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private String value;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
