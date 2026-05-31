package org.mpk.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.mpk.BusLine;

public class BusLineDao {
    public void save(BusLine busLine) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            if (busLine.getId() == 0) {
                em.persist(busLine);
            } else {
                em.merge(busLine);
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
