package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import java.io.InputStream;

import javax.ejb.Singleton;
import javax.ejb.Startup;

import eu.europa.ec.fisheries.uvms.init.AbstractModuleInitializerBean;

@Singleton
@Startup
public class SpatialInitializerBean extends AbstractModuleInitializerBean {

    @Override
    protected InputStream getDeploymentDescriptorRequest() {
        return this.getClass().getClassLoader().getResourceAsStream("usmDeploymentDescriptor.xml");
    }

    @Override
    protected boolean mustRedeploy() {
        return true;
    }
}