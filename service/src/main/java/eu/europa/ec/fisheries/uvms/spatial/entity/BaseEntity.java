package eu.europa.ec.fisheries.uvms.spatial.entity;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
@ToString
@EqualsAndHashCode
public class BaseEntity implements Serializable {

    private final @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

    protected BaseEntity(){
        this.id = null;
    }

    public Long getId() {

        return id;
    }

}
