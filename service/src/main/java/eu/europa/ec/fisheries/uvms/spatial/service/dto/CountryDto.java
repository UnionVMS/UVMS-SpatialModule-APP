package eu.europa.ec.fisheries.uvms.spatial.service.dto;

import eu.europa.ec.fisheries.uvms.spatial.entity.CountryEntity;

/**
 * //TODO create test
 */
public class CountryDto extends CountryEntity {

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
