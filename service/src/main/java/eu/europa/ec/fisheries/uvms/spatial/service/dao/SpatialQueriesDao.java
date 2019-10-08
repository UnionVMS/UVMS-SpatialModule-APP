package eu.europa.ec.fisheries.uvms.spatial.service.dao;

import eu.europa.ec.fisheries.uvms.spatial.service.dto.BaseAreaDto;
import org.locationtech.jts.geom.Point;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;


@Stateless
public class SpatialQueriesDao {


    @PersistenceContext
    private EntityManager em;


    public List<BaseAreaDto> getAreasByPoint(Point point) {

        Query q = em.createNativeQuery("SELECT type,gid,code,name from  spatial.get_areas_by_point( :point)", "BaseAreaDtoMapping")     //BaseAreaDtoMapping is defined in BaseAreaEntity
                .setParameter("point", point);

        return q.getResultList();

    }

    public List<BaseAreaDto> getClosestAreaByPoint(Point point) {
        Query q = em.createNativeQuery("SELECT type,gid,code,name,dist from  spatial.get_closest_areas( :point)", "BaseAreaDtoMappingWithDist")
                .setParameter("point", point);

        return q.getResultList();

    }


}
