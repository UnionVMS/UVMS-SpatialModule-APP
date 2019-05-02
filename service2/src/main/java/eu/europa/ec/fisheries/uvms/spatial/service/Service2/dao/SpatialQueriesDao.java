package eu.europa.ec.fisheries.uvms.spatial.service.Service2.dao;

import com.vividsolutions.jts.geom.Point;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto.BaseAreaDto;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class SpatialQueriesDao {


    @PersistenceContext
    private EntityManager em;

    public List<BaseAreaDto> getAreasByPoint(Point point) {

        Session session = em.unwrap(Session.class);

        return session.createNativeQuery("SELECT type,gid,code,name from  spatial.get_areas_by_point( :point))")
                .setParameter("point", point)
                .setResultTransformer(Transformers.aliasToBean(BaseAreaDto.class))
                .list();

    }

    public List<BaseAreaDto> getClosestAreaByPoint(Point point) {

        Session session = em.unwrap(Session.class);

        return session.createNativeQuery("SELECT type,gid,code,name from  spatial.get_areas_by_point( :point))")
                .setParameter("point", point)
                .setResultTransformer(Transformers.aliasToBean(BaseAreaDto.class))
                .list();

    }


}
