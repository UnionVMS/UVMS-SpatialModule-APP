package eu.europa.ec.fisheries.uvms.spatial.dao;

import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.hibernate.transform.Transformers;

public abstract class CommonDao {
	
	protected EntityManager em;

	public CommonDao(EntityManager em) {
		this.em = em;
	}

	protected Query createNamedNativeQuery(String nativeQueryString, Map<String, Object> parameters) {
		Query query = getSession().getNamedQuery(nativeQueryString);
		for (Map.Entry<String, Object> entry : parameters.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		return query;
	}

	protected <T> Query createNamedNativeQuery(String nativeQueryString, Map<String, Object> parameters, Class<T> dtoClass) {
		Query query = createNamedNativeQuery(nativeQueryString, parameters);
		query.setResultTransformer(Transformers.aliasToBean(dtoClass));
		return query;
	}

	protected Query createNamedQuery(String nativeQuery) {
		Query query = getSession().getNamedQuery(nativeQuery);
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		return query;
	}

	protected Query createNamedQuery(String nativeQuery, Map<String, Object> parameters) {
		Query query = getSession().getNamedQuery(nativeQuery);
		for (Map.Entry<String, Object> entry : parameters.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		return query;
	}

	protected Query createNamedQueryWithParameterList(String nativeQuery, Map<String, List<Long>> parameters) {
		Query query = getSession().getNamedQuery(nativeQuery);
		for (Map.Entry<String, List<Long>> entry : parameters.entrySet()) {
			query.setParameterList(entry.getKey(), entry.getValue());
		}
		return query;
	}

	protected Session getSession() {
		return em.unwrap(Session.class);
	}

}
