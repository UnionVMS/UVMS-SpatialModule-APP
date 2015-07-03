package eu.europa.ec.fisheries.uvms.spatial.entity;

/**
 * Created by georgige on 7/3/2015.
 */

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ns2:application")
public class USMDeploymentDescriptor {

    //TODO maybe add the other attributes if needed?
    private String name;
    private String description;

    // Must have no-argument constructor
    public USMDeploymentDescriptor() {
    }

    public String getName() {
        return name;
    }

    @XmlElement
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
}