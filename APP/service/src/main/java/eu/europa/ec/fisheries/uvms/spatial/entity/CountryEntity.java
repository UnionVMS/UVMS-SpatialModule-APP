package eu.europa.ec.fisheries.uvms.spatial.entity;

import lombok.EqualsAndHashCode;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({
		@NamedQuery(name = CountryEntity.FIND_ALL,
        query = "FROM CountryEntity country WHERE country.code IN (SELECT DISTINCT c.code FROM CountryEntity c)")
})
@Table(name = "countries")
@EqualsAndHashCode(callSuper = true)
public class CountryEntity extends BaseSpatialEntity {

    public static final String FIND_ALL = "countryEntity.findAll";

}
