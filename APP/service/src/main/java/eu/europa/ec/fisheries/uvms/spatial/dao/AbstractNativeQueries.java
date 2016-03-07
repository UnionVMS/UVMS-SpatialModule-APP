package eu.europa.ec.fisheries.uvms.spatial.dao;

import javax.persistence.EntityManager;

public abstract class AbstractNativeQueries implements NativeQueries {

    protected EntityManager em;

}
