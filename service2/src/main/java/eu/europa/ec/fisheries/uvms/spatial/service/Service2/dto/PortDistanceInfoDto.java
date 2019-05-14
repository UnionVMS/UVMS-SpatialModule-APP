package eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto;

import eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.PortEntity;

public class PortDistanceInfoDto {


    private PortEntity port;
    private double distance;

    public PortDistanceInfoDto(PortEntity port, double distance) {
        this.port = port;
        this.distance = distance;
    }

    public PortEntity getPort() {
        return port;
    }

    public void setPort(PortEntity port) {
        this.port = port;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
