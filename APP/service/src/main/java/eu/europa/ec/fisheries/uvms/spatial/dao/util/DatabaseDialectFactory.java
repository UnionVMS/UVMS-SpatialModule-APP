package eu.europa.ec.fisheries.uvms.spatial.dao.util;

import eu.europa.ec.fisheries.uvms.spatial.util.PropertiesBean;

public class DatabaseDialectFactory {

    private PropertiesBean properties;

    public DatabaseDialectFactory(PropertiesBean properties) {
        this.properties = properties;
    }

    public DatabaseDialect getInstance(){

        if ("oracle".equals(properties.getProperty("database.dialect"))){
            return new Oracle();
        }
        else {
            return new PostGres();
        }
    }

}
