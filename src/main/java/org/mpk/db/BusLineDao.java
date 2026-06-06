package org.mpk.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.mpk.BusLine;
import java.util.List;

public class BusLineDao {
    public void save(BusLine busLine) {
        try (EntityManager em = HibernateUtil.getEntityManager()) {
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
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                System.err.println("Błąd podczas zapisywania linii autobusowej: " + e.getMessage());
            }
        }
    }

    public List<BusLine> findAll() {
        try (EntityManager em = HibernateUtil.getEntityManager()) {
            return em.createQuery("SELECT b FROM BusLine b", BusLine.class).getResultList();
        }
    }
}