package eu.europa.ec.fisheries.uvms.spatial.dao;

import java.util.Map;

import javax.persistence.EntityManager;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.hibernate.transform.Transformers;

import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaExtendedIdentifierDto;

public abstract class AbstractDao {
	
	protected EntityManager em;
	
	private static final String CRS = "crs";
	private static final String WKT = "wktPoint";
	private static final String UNIT = "unit";
	private static final String GID = "gid";
	
	public AbstractDao(EntityManager em) {
		this.em = em;
	}

	@SuppressWarnings("rawtypes")
	protected SQLQuery createSQLQuery(String queryString, String wktPoint, int crs, double unit, Class resultClass) {
		SQLQuery sqlQuery = getSession().createSQLQuery(queryString);
		sqlQuery.setResultTransformer(Transformers.aliasToBean(resultClass));
		sqlQuery.setString(WKT, wktPoint);
		sqlQuery.setInteger(CRS, crs);
		sqlQuery.setDouble(UNIT, unit);
		return sqlQuery;
	}

	protected SQLQuery createSQLQuery(String queryString) {
		SQLQuery sqlQuery = getSession().createSQLQuery(queryString);
		sqlQuery.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		return sqlQuery;
	}

	protected Query createNamedNativeQuery(String nativeQueryString, String wktPoint, int crs) {
		Query query = getSession().getNamedQuery(nativeQueryString);
		query.setParameter(WKT, wktPoint);
		query.setParameter(CRS, crs);
		return query;
	}
	
	protected <T> Query createNamedNativeQuery(String nativeQueryString, Map<String, Object> parameters, Class<T> dtoClass) {
		Query query = getSession().getNamedQuery(nativeQueryString);
		for (String key : parameters.keySet()) {
			query.setParameter(key, parameters.get(key));
		}
		query.setResultTransformer(Transformers.aliasToBean(dtoClass));
		return query;
	}

	protected Query createNamedQuery(String namedQueryString, Number gid) {
		Query query = getSession().getNamedQuery(namedQueryString);
		query.setParameter(GID, gid);
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		return query;
	}

	protected SQLQuery createSQLQueryForClosestArea(String queryString, String wktPoint, int crs) {
		SQLQuery sqlQuery = getSession().createSQLQuery(queryString);
		sqlQuery.setString(WKT, wktPoint);
		sqlQuery.setInteger(CRS, crs);
		sqlQuery.setResultTransformer(Transformers.aliasToBean(AreaExtendedIdentifierDto.class));
		return sqlQuery;
	}

	protected <T> Query createQuery(String nativeQuery, Class<T> dtoClass) {
		Query query = getSession().getNamedQuery(nativeQuery);
		query.setResultTransformer(Transformers.aliasToBean(dtoClass));
		return query;
	}

	protected Session getSession() {
		return em.unwrap(Session.class);
	}

}
