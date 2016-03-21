package eu.europa.ec.fisheries.uvms.spatial.util;

import eu.europa.ec.fisheries.uvms.spatial.dao.Oracle;
import eu.europa.ec.fisheries.uvms.spatial.dao.util.SpatialFunction;
import eu.europa.ec.fisheries.uvms.spatial.dao.util.PostGres;

public class DatabaseDialectFactory {

    private PropertiesBean properties;

    public DatabaseDialectFactory(PropertiesBean properties) {
        this.properties = properties;
    }

    public SpatialFunction getInstance(){

        if ("oracle".equals(properties.getProperty("database.dialect"))){
            return new Oracle();
        }
        else {
            return new PostGres();
        }
    }

}
