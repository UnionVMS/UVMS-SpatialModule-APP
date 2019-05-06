package eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto;

import eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.PortEntity2;

public class PortDistanceInfoDto {


    private PortEntity2 port;
    private double distance;

    public PortDistanceInfoDto(PortEntity2 port, double distance) {
        this.port = port;
        this.distance = distance;
    }

    public PortEntity2 getPort() {
        return port;
    }

    public void setPort(PortEntity2 port) {
        this.port = port;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
