package eu.europa.ec.fisheries.uvms.spatial.service;

import eu.europa.ec.fisheries.uvms.spatial.message.event.*;

import javax.ejb.Local;
import javax.enterprise.event.Observes;

@Local
public interface SpatialEventService {

    public void getAreaByLocation(@Observes @GetAreaByLocationEvent SpatialMessageEvent message);

    public void getClosestArea(@Observes @GetClosestAreaEvent SpatialMessageEvent message);

    public void getClosestLocation(@Observes @GetClosestLocationEvent SpatialMessageEvent message);

    public void getSpatialEnrichment(@Observes @GetSpatialEnrichmentEvent SpatialMessageEvent message);

    public void getAreaTypeNames(@Observes @GetAreaTypeNamesEvent SpatialMessageEvent message);

}
