package eu.europa.ec.fisheries.uvms.spatial.util;

import eu.europa.ec.fisheries.uvms.spatial.dao.Oracle;
import eu.europa.ec.fisheries.uvms.spatial.dao.util.SpatialFunction;
import eu.europa.ec.fisheries.uvms.spatial.dao.util.PostGres;

public class SpatialFunctionFactory {

    private PropertiesBean properties;

    public SpatialFunctionFactory(PropertiesBean properties) {
        this.properties = properties;
    }

    public SpatialFunction getInstance(){

        if ("ORACLE".equals(properties.getProperty("db"))){
            return new Oracle();
        }
        else {
            return new PostGres();
        }
    }

}
