package eu.europa.ec.fisheries.uvms.spatial.util;

import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Singleton
@Startup
public class SqlPropertyHolder {

    private static final String SQL_FILE_NAME = "nativeSql.properties";

    private Properties prop;

    @PostConstruct
    public void init() throws IOException {
        prop = new Properties();
        InputStream properties = this.getClass().getClassLoader().getResourceAsStream(SQL_FILE_NAME);

        if (properties != null) {
            prop.load(properties);
        } else {
            throw new FileNotFoundException("Property file '" + SQL_FILE_NAME + "' not found in the classpath");
        }
    }

    public String getProperty(String queryName) {
        String property = prop.getProperty(queryName);
        if (property == null) {
            throw new SpatialServiceException(SpatialServiceErrors.WRONG_NATIVE_SQL_CONFIGURATION_ERROR, queryName);
        }
        return property;
    }
}