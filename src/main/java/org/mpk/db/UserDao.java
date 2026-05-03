package org.mpk.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.mpk.User;

import java.util.List;

public class UserDao {

    public void save(User user) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            // jesli obiekt nie ma id (id == 0 dla typu int z defaultu) to zapisujemy, jesli ma to możemy aktualizować, ale persist jest dla nowych
            if (user.getId() == 0) {
                em.persist(user);
            } else {
                em.merge(user);
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

    public User findById(int id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    public User findByUsername(String username) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            List<User> users = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getResultList();
            return users.isEmpty() ? null : users.get(0);
        } finally {
            em.close();
        }
    }

    public List<User> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM User u", User.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void delete(int id) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            User user = em.find(User.class, id);
            if (user != null) {
                em.remove(user);
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
}