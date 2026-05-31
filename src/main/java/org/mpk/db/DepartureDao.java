package org.mpk.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.mpk.Departure;

public class DepartureDao {
    public void save(Departure departure) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            if (departure.getId() == 0) {
                em.persist(departure);
            } else {
                em.merge(departure);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
