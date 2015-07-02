package eu.europa.ec.fisheries.uvms.spatial.model.config;

public enum ParameterKey {

    KEY("spatial.key.attribute");

    private final String key;

    private ParameterKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

}
