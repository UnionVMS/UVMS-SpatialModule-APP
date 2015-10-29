package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.*;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.ArrayList;

@Stateless
@Local(MapConfigService.class)
@Transactional
@Slf4j
public class MapConfigServiceBean implements MapConfigService {

    private static final String LABEL = "label";
    private static final String LABEL_GEOM = "labelGeom";

    @Override
    public MapConfig getMockReportConfig(int reportId) {
        Map map = new Map(createProjection(), createControls(), createTbControls(), createLayers());
        FlagState flagState = createFlagState();
        VectorStyles vectorStyles = new VectorStyles(flagState, new Speed("#1a9641", "#a6d96a", "#fdae61", "#d7191c"));

        return new MapConfig(map, vectorStyles);
    }

    private FlagState createFlagState() {
        return new FlagState()
                .withFlagState("dnk", "#0066FF")
                .withFlagState("swe", "#FF0066");
    }

    private ArrayList<Layer> createLayers() {
        ArrayList<Layer> layers = Lists.newArrayList();
        Styles eezStyles = new Styles("eez")
                .withAdditionalProperty(LABEL, "eez_label")
                .withAdditionalProperty(LABEL_GEOM, "eez_label_geom");
        layers.add(new Layer("WMS", "area", "EEZ", false, "Custom layer from UnionVMS", "http://localhost:8080/geoserver/wms", "geoserver", "uvms:eez", eezStyles));

        Styles rfmoStyles = new Styles("rfmo")
                .withAdditionalProperty(LABEL, "rfmo_label")
                .withAdditionalProperty(LABEL_GEOM, "rfmo_label_geom");
        layers.add(new Layer("WMS", "area", "RFMO", false, "Custom layer from UnionVMS", "http://localhost:8080/geoserver/wms", "geoserver", "uvms:rfmo", rfmoStyles));

        layers.add(new Layer().withType("OSEA").withTitle("OpenSeaMap").withIsBaseLayer(false));
        layers.add(new Layer().withType("OSM").withTitle("OpenStreetMap").withIsBaseLayer(true));

        layers.add(new Layer("WMS", "other", "Countries", true, "Custom layer from UnionVMS", "http://localhost:8080/geoserver/wms", "geoserver", "uvms:countries", new Styles("polygon")));

        return layers;
    }

    private ArrayList<TbControl> createTbControls() {
        ArrayList<TbControl> controls = Lists.newArrayList();
        controls.add(new TbControl("measure"));
        return controls;
    }

    private ArrayList<Control> createControls() {
        ArrayList<Control> controls = Lists.newArrayList();
        controls.add(new Control("zoom"));
        controls.add(new Control("drag"));
        controls.add(new Control("scale").withUnits("nautical"));
        controls.add(new Control("fullscreen"));
        controls.add(new Control("mousecoords").withEpsgCode(4326).withFormat("dd"));
        controls.add(new Control("history"));
        controls.add(new Control("measure"));
        return controls;
    }

    private Projection createProjection() {
        return new Projection(3857, "m", true);
    }

}
