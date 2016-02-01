package com.test;

// Generated Jul 28, 2015 2:11:57 PM by Hibernate Tools 4.3.1

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import static org.hibernate.criterion.Example.create;

/**
 * Home object for domain model class SpatialConfiguration.
 * @see com.test.SpatialConfiguration
 * @author Hibernate Tools
 */
public class SpatialConfigurationHome {

	private static final Log log = LogFactory
			.getLog(SpatialConfigurationHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext()
					.lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException(
					"Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(SpatialConfiguration transientInstance) {
		log.debug("persisting SpatialConfiguration instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(SpatialConfiguration instance) {
		log.debug("attaching dirty SpatialConfiguration instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(SpatialConfiguration instance) {
		log.debug("attaching clean SpatialConfiguration instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(SpatialConfiguration persistentInstance) {
		log.debug("deleting SpatialConfiguration instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public SpatialConfiguration merge(SpatialConfiguration detachedInstance) {
		log.debug("merging SpatialConfiguration instance");
		try {
			SpatialConfiguration result = (SpatialConfiguration) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public SpatialConfiguration findById(int id) {
		log.debug("getting SpatialConfiguration instance with id: " + id);
		try {
			SpatialConfiguration instance = (SpatialConfiguration) sessionFactory
					.getCurrentSession().get("com.test.SpatialConfiguration",
							id);
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instance found");
			}
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<SpatialConfiguration> findByExample(
			SpatialConfiguration instance) {
		log.debug("finding SpatialConfiguration instance by example");
		try {
			List<SpatialConfiguration> results = (List<SpatialConfiguration>) sessionFactory
					.getCurrentSession()
					.createCriteria("com.test.SpatialConfiguration")
					.add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
