package service.dao;

import com.vividsolutions.jts.geom.Point;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import service.dto.BaseAreaDto;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class SpatialQueriesDao {

    public static final  String GET_AREAS_BY_POINT = "SpatialQueriesDao.getAreasByPoint";

    @PersistenceContext
    private EntityManager em;

    public List<BaseAreaDto> getAreasByPoint(Point point) {

        Session session = em.unwrap(Session.class);

        return session.createNativeQuery("SELECT type,gid,code,name from  spatial.getareasbypoint( :point))")
                .setParameter("point", point)
                .setResultTransformer(Transformers.aliasToBean(BaseAreaDto.class))
                .list();

    }
}
