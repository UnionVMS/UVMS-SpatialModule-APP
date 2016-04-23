package eu.europa.ec.fisheries.uvms.spatial.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.Where;

@Entity
@Where(clause = "enabled = 'Y'")
@Table(name = "gfcm")
public class GfcmEntity extends BaseAreaEntity {

	public GfcmEntity() {
        // why JPA why
    }

}
