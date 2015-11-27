package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.message.event.*;

import javax.ejb.Local;
import javax.enterprise.event.Observes;

@Local
public interface SpatialEventService {

    void getAreaByLocation(@Observes @GetAreaByLocationEvent SpatialMessageEvent message);

    void getClosestArea(@Observes @GetClosestAreaEvent SpatialMessageEvent message);

    void getClosestLocation(@Observes @GetClosestLocationEvent SpatialMessageEvent message);

    void getSpatialEnrichment(@Observes @GetSpatialEnrichmentEvent SpatialMessageEvent message);

    void getAreaTypeNames(@Observes @GetAreaTypeNamesEvent SpatialMessageEvent message);

    void getFilterAreas(@Observes @GetFilterAreaEvent SpatialMessageEvent message);

    void saveOrUpdateMapConfiguration(@Observes @SaveMapConfigurationEvent SpatialMessageEvent message);

    void ping(@Observes @PingEvent SpatialMessageEvent message);
}
