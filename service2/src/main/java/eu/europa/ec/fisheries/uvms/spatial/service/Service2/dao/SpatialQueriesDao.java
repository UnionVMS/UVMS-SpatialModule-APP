package eu.europa.ec.fisheries.uvms.spatial.service.Service2.dao;

import com.vividsolutions.jts.geom.Point;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto.BaseAreaDto;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.List;


@Stateless
public class SpatialQueriesDao {


    @PersistenceContext
    private EntityManager em;


    public List<BaseAreaDto> getAreasByPoint(Point point) {

        Query q = em.createNativeQuery("SELECT type,gid,code,name from  spatial.get_areas_by_point( :point)", "BaseAreaDtoMapping")     //BaseAreaDtoMapping is defined in BaseAreaEntity2
                .setParameter("point", point);

        return q.getResultList();

    }

    public List<BaseAreaDto> getClosestAreaByPoint(Point point) {
        Query q = em.createNativeQuery("SELECT type,gid,code,name from  spatial.get_closest_areas( :point)", "BaseAreaDtoMapping")
                .setParameter("point", point);

        return q.getResultList();

    }


}
