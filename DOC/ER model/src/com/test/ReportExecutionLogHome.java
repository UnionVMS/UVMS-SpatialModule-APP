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
 * Home object for domain model class ReportExecutionLog.
 * @see com.test.ReportExecutionLog
 * @author Hibernate Tools
 */
public class ReportExecutionLogHome {

	private static final Log log = LogFactory
			.getLog(ReportExecutionLogHome.class);

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

	public void persist(ReportExecutionLog transientInstance) {
		log.debug("persisting ReportExecutionLog instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(ReportExecutionLog instance) {
		log.debug("attaching dirty ReportExecutionLog instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ReportExecutionLog instance) {
		log.debug("attaching clean ReportExecutionLog instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(ReportExecutionLog persistentInstance) {
		log.debug("deleting ReportExecutionLog instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ReportExecutionLog merge(ReportExecutionLog detachedInstance) {
		log.debug("merging ReportExecutionLog instance");
		try {
			ReportExecutionLog result = (ReportExecutionLog) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ReportExecutionLog findById(long id) {
		log.debug("getting ReportExecutionLog instance with id: " + id);
		try {
			ReportExecutionLog instance = (ReportExecutionLog) sessionFactory
					.getCurrentSession().get("com.test.ReportExecutionLog", id);
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

	public List<ReportExecutionLog> findByExample(ReportExecutionLog instance) {
		log.debug("finding ReportExecutionLog instance by example");
		try {
			List<ReportExecutionLog> results = (List<ReportExecutionLog>) sessionFactory
					.getCurrentSession()
					.createCriteria("com.test.ReportExecutionLog")
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
