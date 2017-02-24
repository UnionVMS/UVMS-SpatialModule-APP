/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.dao.util;

import eu.europa.ec.fisheries.uvms.spatial.service.bean.impl.PropertiesBean;

public class DatabaseDialectFactory {

    private PropertiesBean properties;
    // only for testing
    private static int callIndex = 0;
    private static Oracle oracleDialect = null;
    private static PostGres postgresDialect = null;
	
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
	
	
	public static DatabaseDialect bounceInstance(){
    	DatabaseDialect dialect;
        if(isOracle()){
        	if (oracleDialect==null)
        		oracleDialect = new Oracle();
            dialect =  oracleDialect;
        }
        else {
        	if (postgresDialect == null)
        		postgresDialect = new PostGres();
            dialect =  postgresDialect;
        }    	
        
        return dialect;        
    }
    
    public static void switchDatabase(){
        callIndex++;    	
    }
    
    public static boolean isOracle(){
        boolean isOracle = false;
        if (callIndex % 2 == 0){
        	isOracle = true;
        }
        return isOracle;
    }

}