package eu.europa.ec.fisheries.uvms.spatial.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.Where;

@Entity
@Where(clause = "enabled = 'Y'")
@Table(name = "rac", schema = "spatial")
public class RacEntity extends BaseAreaEntity {

	public RacEntity() {
        // No args constructor for use in serialization
    }

}
