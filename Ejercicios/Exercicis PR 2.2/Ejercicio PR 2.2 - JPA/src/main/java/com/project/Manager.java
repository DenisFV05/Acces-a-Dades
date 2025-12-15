package com.project;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Set;

public class Manager {

    private static EntityManagerFactory emf;

    // Inicializar JPA
    public static void init() {
        emf = Persistence.createEntityManagerFactory("unitatJPA");
    }
    public static Ciutat addCiutat(String nom, String pais, int poblacio) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Ciutat c = new Ciutat(nom, pais, poblacio);
        em.persist(c);

        em.getTransaction().commit();
        em.close();
        return c;
    }



    // CREATE CIUTADA
    public static Ciutada addCiutada(String nom, String cognom, int edat, Ciutat ciutat) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Ciutada c = new Ciutada(nom, cognom, edat);
        c.setCiutat(ciutat);

        em.persist(c);

        em.getTransaction().commit();
        em.close();
        return c;
    }

    // UPDATE CIUTAT
    public static void updateCiutat(Long id, String nom, String pais, int poblacio, Set<Ciutada> ciutadans) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Ciutat c = em.find(Ciutat.class, id);
        if (c != null) {
            c.setNom(nom);
            c.setPais(pais);
            c.setPoblacio(poblacio);

            c.getCiutadans().clear();
            for (Ciutada ciu : ciutadans) {
                ciu.setCiutat(c);
                if (ciu.getId() == null) {        // nuevo ciudadano
                    em.persist(ciu);
                } else {                          // ciudadano existente
                    em.merge(ciu);
                }
                c.getCiutadans().add(ciu);
            }

            em.merge(c); // actualiza la ciudad y la relación
        }

        em.getTransaction().commit();
        em.close();
    }




    // UPDATE CIUTADA
    public static void updateCiutada(Long id, String nom, String cognom, int edat) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Ciutada c = em.find(Ciutada.class, id);
        if (c != null) {
            c.setNom(nom);
            c.setCognom(cognom);
            c.setEdat(edat);
        }

        em.getTransaction().commit();
        em.close();
    }

    // DELETE CIUTAT
    public static void deleteCiutat(Long id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Ciutat c = em.find(Ciutat.class, id);
        if (c != null) {
            em.remove(c);
        }

        em.getTransaction().commit();
        em.close();
    }

    // DELETE CIUTADA
    public static void deleteCiutada(Long id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Ciutada c = em.find(Ciutada.class, id);
        if (c != null) {
            em.remove(c);
        }

        em.getTransaction().commit();
        em.close();
    }

    // READ CIUTATS
    public static void listCiutats() {
        EntityManager em = emf.createEntityManager();
        List<Ciutat> list = em.createQuery("FROM Ciutat", Ciutat.class).getResultList();
        list.forEach(System.out::println);
        em.close();
    }

    // READ CIUTADANS
    public static void listCiutadans() {
        EntityManager em = emf.createEntityManager();
        List<Ciutada> list = em.createQuery("FROM Ciutada", Ciutada.class).getResultList();
        list.forEach(System.out::println);
        em.close();
    }

    // GET CIUTAT AMB CIUTADANS
    public static Ciutat getCiutatWithCiutadans(Long id) {
        EntityManager em = emf.createEntityManager();
        Ciutat c = em.find(Ciutat.class, id);
        if (c != null) {
            c.getCiutadans().size(); // fuerza carga
        }
        em.close();
        return c;
    }

    // CLOSE
    public static void close() {
        if (emf != null) {
            emf.close();
        }
    }
}
