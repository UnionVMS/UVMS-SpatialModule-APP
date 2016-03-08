package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialUtils;

import javax.persistence.EntityManager;

public class PostgreGeometryRelationFunction extends AbstractGeometryRelationFunction {

    public PostgreGeometryRelationFunction(EntityManager em){
        this.em = em;
    }

    @Override
    public String stAsText(String wkt) {

        return (String) em.createNativeQuery("select st_astext(" + wkt + ")").getSingleResult();
    }

    public Geometry st_intersects(Geometry geomA, Geometry geomB){
       return geomA.intersection(geomB);
    }

    public String stTranslate(Geometry geometry, Double dx, Double dy) {

       // SpatialUtils.translate(x,y, geometry)
        return null;
    }
}