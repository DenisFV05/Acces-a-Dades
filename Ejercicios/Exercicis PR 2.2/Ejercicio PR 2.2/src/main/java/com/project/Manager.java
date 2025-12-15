package com.project;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class Manager {
    private static SessionFactory factory;

    public static void createSessionFactory() {
        try {
            factory = new Configuration()
                        .configure() // busca hibernate.cfg.xml
                        .addAnnotatedClass(Ciutat.class)
                        .addAnnotatedClass(Ciutada.class)
                        .buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Error creando SessionFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Ciutat addCiutat(String nom, String pais, int poblacio) {
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        Ciutat c = new Ciutat(nom, pais, poblacio);
        session.persist(c); // persist en lugar de save
        tx.commit();
        session.close();
        return c;
    }

    public static Ciutada addCiutada(String nom, String cognom, int edat) {
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        Ciutada c = new Ciutada(nom, cognom, edat);
        session.persist(c); // persist en lugar de save
        tx.commit();
        session.close();
        return c;
    }

    public static void updateCiutada(long id, String nom, String cognom, int edat) {
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        Ciutada c = session.get(Ciutada.class, id);
        if (c != null) {
            c.setNom(nom);
            c.setCognom(cognom);
            c.setEdat(edat);
            session.merge(c); // merge en lugar de update
        }
        tx.commit();
        session.close();
    }

    public static void updateCiutat(long id, String nom, String pais, int poblacio, Set<Ciutada> ciutadans) {
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        Ciutat c = session.get(Ciutat.class, id);
        if (c != null) {
            c.setNom(nom);
            c.setPais(pais);
            c.setPoblacio(poblacio);
            c.setCiutadans(ciutadans);
            session.merge(c); // merge en lugar de update
        }
        tx.commit();
        session.close();
    }

    public static List<?> listCollection(Class<?> clazz, String condition) {
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        List<?> list = session.createQuery("from " + clazz.getSimpleName(), clazz).getResultList();
        tx.commit();
        session.close();
        return list;
    }

    public static void delete(Class<?> clazz, long id) {
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        Object obj = session.get(clazz, id);
        if (obj != null) {
            session.remove(obj); // remove en lugar de delete
        }
        tx.commit();
        session.close();
    }

    public static Ciutat getCiutatWithCiutadans(long id) {
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        Ciutat c = session.get(Ciutat.class, id);
        tx.commit();
        session.close();
        return c;
    }

    public static String collectionToString(Class<?> clazz, List<?> list) {
        return list.stream().map(Object::toString).collect(Collectors.joining("\n"));
    }

    public static void close() {
        if (factory != null) factory.close();
    }
}
