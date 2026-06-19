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

    public Departure findById(int id) {
        try (EntityManager em = HibernateUtil.getEntityManager()) {
            return em.find(Departure.class, id);
        }
    }

    public List<Departure> findAll() {
        try (EntityManager em = HibernateUtil.getEntityManager()) {
            return em.createQuery(
                    "SELECT d FROM Departure d ORDER BY d.departureTime",
                    Departure.class
            ).getResultList();
        }
    }

    public void delete(int id) {
        try (EntityManager em = HibernateUtil.getEntityManager()) {
            EntityTransaction transaction = em.getTransaction();

            try {
                transaction.begin();

                em.createQuery("DELETE FROM Departure d WHERE d.id = :id")
                        .setParameter("id", id)
                        .executeUpdate();

                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                System.err.println("Błąd podczas usuwania odjazdu: " + e.getMessage());
            }
        }
    }

    public void deleteByLineId(int lineId) {
        try (EntityManager em = HibernateUtil.getEntityManager()) {
            EntityTransaction transaction = em.getTransaction();

            try {
                transaction.begin();

                em.createQuery("DELETE FROM Departure d WHERE d.line.id = :lineId")
                        .setParameter("lineId", lineId)
                        .executeUpdate();

                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                System.err.println("Błąd podczas usuwania odjazdów linii: " + e.getMessage());
            }
        }
    }

    public void deleteByStopId(int stopId) {
        try (EntityManager em = HibernateUtil.getEntityManager()) {
            EntityTransaction transaction = em.getTransaction();

            try {
                transaction.begin();

                em.createQuery("DELETE FROM Departure d WHERE d.busStop.id = :stopId")
                        .setParameter("stopId", stopId)
                        .executeUpdate();

                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                System.err.println("Błąd podczas usuwania odjazdów przystanku: " + e.getMessage());
            }
        }
    }
}