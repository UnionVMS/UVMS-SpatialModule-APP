package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.ejb.Singleton;
import javax.ejb.Startup;

import eu.europa.ec.fisheries.uvms.init.AbstractModuleInitializerBean;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
public class SpatialInitializerBean extends AbstractModuleInitializerBean {

    public static final String PROP_FILE_NAME = "config.properties";
    public static final String PROP_USM_DESCRIPTOR_FORCE_UPDATE = "usm.deployment.descriptor.force-update";

    @Override
    protected InputStream getDeploymentDescriptorRequest() {
        return this.getClass().getClassLoader().getResourceAsStream("usmDeploymentDescriptor.xml");
    }

    @Override
    protected boolean mustRedeploy() {
        boolean isMustRedploy = false;
        try {
            Properties moduleConfigs = retrieveModuleConfigs();
            isMustRedploy = Boolean.valueOf(moduleConfigs.getProperty(PROP_USM_DESCRIPTOR_FORCE_UPDATE));
            log.info("{} file contains a configuration {}, with the following value {}", PROP_FILE_NAME, PROP_USM_DESCRIPTOR_FORCE_UPDATE, isMustRedploy);
        } catch (IOException e) {
            log.info("No {} file with property {} was configured. The default behavior is to skip USM deployment if application has already been deployed.", PROP_FILE_NAME, PROP_USM_DESCRIPTOR_FORCE_UPDATE);
            //in case we can't retrieve a configuration, the default behavior is skipping redeployment
        }
        return isMustRedploy;
    }

    private Properties retrieveModuleConfigs() throws IOException {
        Properties prop = new Properties();
        InputStream properties = getClass().getClassLoader().getResourceAsStream(PROP_FILE_NAME);
        if (properties != null) {
            prop.load(properties);
            return prop;
        } else {
            throw new FileNotFoundException("Property file '" + PROP_FILE_NAME + "' not found in the classpath");
        }
    }
}