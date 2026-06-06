package org.mpk.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.mpk.Departure;
import java.util.List;

public class DepartureDao {
    public void save(Departure departure) {
        try (EntityManager em = HibernateUtil.getEntityManager()) {
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
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                System.err.println("Błąd podczas zapisywania odjazdu: " + e.getMessage());
            }
        }
    }

    public List<Departure> findAll() {
        try (EntityManager em = HibernateUtil.getEntityManager()) {
            return em.createQuery("SELECT d FROM Departure d", Departure.class).getResultList();
        }
    }
}