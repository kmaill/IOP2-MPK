package org.mpk.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class HibernateUtil {
    private static final String PERSISTENCE_UNIT_NAME = "mpkPU";
    private static EntityManagerFactory factory;

    public static EntityManagerFactory getEntityManagerFactory() {
        if (factory == null) {
            try {
                factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error: ", e);
            }
        }
        return factory;
    }

    public static EntityManager getEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    public static void shutdown() {
        if (factory != null) {
            factory.close();
        }
    }
}