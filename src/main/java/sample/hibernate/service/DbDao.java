package sample.hibernate.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import sample.hibernate.entities.Dbinfo;

@Slf4j
public class DbDao implements IDbDao {

    private SessionFactory sessionFactory = null;

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public String getDbVersionText() {

        log.debug("GetDbVersionText");

        Session session = getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Dbinfo> cq = cb.createQuery(Dbinfo.class);
            Root<Dbinfo> root = cq.from(Dbinfo.class);

            Predicate predicateFirst = cb.equal(root.get("id"), 1);

            cq.select(root).where(predicateFirst);
            Dbinfo dbInfo = session.createQuery(cq).uniqueResult();

            tx.commit();

            //--------------------------------------------------------------------------------

            log.debug("GetDbVersionText - successfully.");

            return dbInfo.toString();

        } catch (RuntimeException re) {
            log.error("GetDbVersionText - failed!", re);
            if (tx != null)
                tx.rollback();
            return null;

        } finally {
            session.close();
        }

    }

    private Session getSession() {
        log.debug("getSession");
        Session result = null;
        try {
            result = sessionFactory.openSession();
            log.debug("getCurrentSession (openSession) - successfully.");
        } catch (HibernateException he) {
            log.info("getCurrentSession - failed, try to open new session.", he);
            result = sessionFactory.openSession();
        }
        return result;
    }

}
