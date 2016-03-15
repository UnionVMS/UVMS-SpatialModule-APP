package eu.europa.ec.fisheries.uvms.spatial.util;

import eu.europa.ec.fisheries.uvms.spatial.dao.GisFunction;
import eu.europa.ec.fisheries.uvms.spatial.dao.OracleGis;
import eu.europa.ec.fisheries.uvms.spatial.dao.PostGres;

public class GisFunctionFactory {

    public static GisFunction getGisFunction(String type){

        if ("ORACLE".equals(type)){
            return new OracleGis();
        }
        else {
            return new PostGres();
        }
    }

}
