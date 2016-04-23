package eu.europa.ec.fisheries.uvms.spatial.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.Where;

@Entity
@Where(clause = "enabled = 'Y'")
@Table(name = "rac")
public class RacEntity extends BaseAreaEntity {

	public RacEntity() {
        // why JPA why
    }

}
