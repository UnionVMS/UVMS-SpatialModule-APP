package eu.europa.ec.fisheries.uvms.spatial.entity.config;

import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Georgi on 23-Nov-15.
 */

@Entity
@Table(name = "system_configurations", schema = "spatial")
@EqualsAndHashCode
@NamedQueries({
        @NamedQuery(name= QueryNameConstants.FIND_CONFIG_BY_NAME,
        query = "SELECT config.value FROM SysConfigEntity config WHERE config.name = :name")
})
public class SysConfigEntity implements Serializable {

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
