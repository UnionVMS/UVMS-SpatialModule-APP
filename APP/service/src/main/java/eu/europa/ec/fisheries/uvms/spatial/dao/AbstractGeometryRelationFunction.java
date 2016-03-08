package eu.europa.ec.fisheries.uvms.spatial.dao;

import javax.persistence.EntityManager;

public abstract class AbstractGeometryRelationFunction implements GeometryRelationFunction {

    protected EntityManager em;

}
