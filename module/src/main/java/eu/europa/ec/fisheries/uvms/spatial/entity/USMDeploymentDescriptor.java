package eu.europa.ec.fisheries.uvms.spatial.entity;

/**
 * //TODO create test
 */

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "application", namespace = "deployment.domain.administration.usm.mare.ec.europa.eu")
public class USMDeploymentDescriptor implements Serializable {

    //TODO maybe add the other attributes if needed?
    //TODO entity is a confusing name for the package name
    private String name;
    private String description;
    private String parent;

    // Must have no-argument constructor
    public USMDeploymentDescriptor() {
    }

    public String getName() {
        return name;
    }

    @XmlElement(required = true)
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    @XmlElement
    public void setDescription(String description) {
        this.description = description;
    }

    public String getParent() {
        return parent;
    }

    @XmlElement(required = true)
    public void setParent(String parent) {
        this.parent = parent;
    }

}