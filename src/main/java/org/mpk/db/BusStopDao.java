package org.mpk.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.mpk.BusStop;

import java.util.List;

public class BusStopDao {

    public void save(BusStop busStop) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            if (busStop.getId() == 0) {
                em.persist(busStop);
            } else {
                em.merge(busStop);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public BusStop findById(int id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(BusStop.class, id);
        } finally {
            em.close();
        }
    }

    public List<BusStop> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery("SELECT b FROM BusStop b", BusStop.class).getResultList();
        } finally {
            em.close();
        }
    }
}