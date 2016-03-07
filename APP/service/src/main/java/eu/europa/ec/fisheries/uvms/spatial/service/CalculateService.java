package eu.europa.ec.fisheries.uvms.spatial.service;

public interface CalculateService {

    String calculateBuffer(Double latitude, Double longitude, Double buffer);

    String transform(Double tx, Double ty, String wkt, Boolean nativeQuery);

}
