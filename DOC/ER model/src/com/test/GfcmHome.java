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
 * Home object for domain model class Gfcm.
 * @see com.test.Gfcm
 * @author Hibernate Tools
 */
public class GfcmHome {

	private static final Log log = LogFactory.getLog(GfcmHome.class);

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

	public void persist(Gfcm transientInstance) {
		log.debug("persisting Gfcm instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Gfcm instance) {
		log.debug("attaching dirty Gfcm instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Gfcm instance) {
		log.debug("attaching clean Gfcm instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Gfcm persistentInstance) {
		log.debug("deleting Gfcm instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Gfcm merge(Gfcm detachedInstance) {
		log.debug("merging Gfcm instance");
		try {
			Gfcm result = (Gfcm) sessionFactory.getCurrentSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Gfcm findById(int id) {
		log.debug("getting Gfcm instance with id: " + id);
		try {
			Gfcm instance = (Gfcm) sessionFactory.getCurrentSession().get(
					"com.test.Gfcm", id);
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

	public List<Gfcm> findByExample(Gfcm instance) {
		log.debug("finding Gfcm instance by example");
		try {
			List<Gfcm> results = (List<Gfcm>) sessionFactory
					.getCurrentSession().createCriteria("com.test.Gfcm")
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
