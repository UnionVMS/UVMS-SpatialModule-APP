package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.vividsolutions.jts.geom.Geometry;

import javax.persistence.EntityManager;

public class PostgreNativeQueries extends AbstractNativeQueries {

    public PostgreNativeQueries(EntityManager em){
        this.em = em;
    }

    @Override
    public String stAsText(String wkt) {

        StringBuffer sb = new StringBuffer("select st_astext(");
        sb.append(wkt).append(")");

      return (String) em.createNativeQuery(sb.toString()).getSingleResult();
    }

    public String stTransate(Geometry geometry, Double x, Double y) {
        return null;
    }
}