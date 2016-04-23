package eu.europa.ec.fisheries.uvms.spatial.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.Where;

@Entity
@Where(clause = "enabled = 'Y'")
@Table(name = "stat_rect")
public class StatRectEntity extends BaseAreaEntity {

	public StatRectEntity() {
        // why JPA why
    }

}
