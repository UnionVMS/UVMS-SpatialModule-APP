package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config;

/**
 * Created by Georgi on 23-Nov-15.
 */
public class SysConfig {

    private String name;

    private String value;

    public SysConfig(){}


    public SysConfig( String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
