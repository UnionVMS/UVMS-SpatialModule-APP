package eu.europa.ec.fisheries.uvms.spatial.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * //TODO create test
 */
@Entity
@Table(name = "countries", schema = "spatial")
@Data
@EqualsAndHashCode
@ToString
public class CountryEntity implements Serializable {

    @Id
    @Column(name = "id")
    private int id; // TODO convert to Long

    @Column(name = "sovereignt")
    private String sovereign;


}
