package com.project;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 * Classe MANAGER: Patró DAO (Data Access Object)
 * 
 * RESPONSABILITATS:
 * - Centralitza totes les operacions amb la base de dades
 * - Gestiona el cicle de vida de les sessions Hibernate
 * - Implementa operacions CRUD (Create, Read, Update, Delete)
 * - Manté la coherència de les relacions bidireccionals
 * 
 * PATRONS IMPLEMENTATS:
 * - DAO Pattern: Separa la lògica d'accés a dades de la lògica de negoci
 * - Session-per-request: Cada operació obre/tanca la seva sessió
 * - Try-with-resources: Gestió automàtica de recursos (sessions)
 * 
 * CONCEPTES CLAU HIBERNATE:
 * - SessionFactory: Fàbrica de sessions, és cara de crear, una per aplicació
 * - Session: Unitat de treball, curta durada, una per operació/request
 * - Transaction: Agrupa operacions atòmiques (tot o res)
 * 
 * API UTILITZADA:
 * Aquest Manager utilitza l'API NATIVA d'Hibernate (SessionFactory/Session)
 * NO l'API de JPA (EntityManagerFactory/EntityManager).
 * Les entitats però estan anotades amb anotacions JPA estàndard.
 * 
 * FETCH STRATEGY:
 * Totes les relacions usen FetchType.EAGER per simplicitat.
 * Això elimina qualsevol problema de LazyInitializationException.
 */
public class Manager {
    
    // ═══════════════════════════════════════════════════════════════════
    // ATRIBUTS ESTÀTICS
    // ═══════════════════════════════════════════════════════════════════
    
    /**
     * SessionFactory - Thread-safe i compartida per tota l'aplicació.
     * 
     * IMPORTANT:
     * - Crear-la és costós, per això només en tenim una (Singleton implícit)
     * - És immutable després de ser creada
     * - Pot ser compartida entre múltiples threads
     * - S'utilitza per crear Sessions (objectes lleugers)
     */
    private static SessionFactory factory;

    // ═══════════════════════════════════════════════════════════════════
    // MÈTODES DE CONFIGURACIÓ I INICIALITZACIÓ
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Crea la SessionFactory llegint hibernate.properties del classpath.
     * 
     * Aquest mètode carrega la configuració per defecte des de
     * src/main/resources/hibernate.properties
     * 
     * IMPORTANT: Les classes anotades s'han de registrar explícitament
     * amb addAnnotatedClass() quan NO utilitzem persistence.xml
     */
    public static void createSessionFactory() {
        createSessionFactory("hibernate.properties");
    }

    /**
     * Crea la SessionFactory amb un fitxer de propietats específic.
     * 
     * PROCÉS D'INICIALITZACIÓ:
     * 1. Configuration: Classe que configura Hibernate programàticament
     * 2. Registrar entitats: Cal afegir totes les classes @Entity
     * 3. Carregar propietats: Des del fitxer especificat
     * 4. ServiceRegistry: Gestiona els serveis interns d'Hibernate
     * 5. Construir SessionFactory: Operació costosa, només es fa un cop
     * 
     * FITXERS DE PROPIETATS COMUNS:
     * - hibernate.properties: Producció (SQLite)
     * - hibernate-test.properties: Tests (SQLite amb BD de test)
     * - hibernate-mysql.properties: MySQL (producció o test)
     * 
     * @param propertiesFileName Nom del fitxer de propietats al classpath
     * @throws ExceptionInInitializerError Si hi ha error en la configuració
     */
    public static void createSessionFactory(String propertiesFileName) {
        try {
            // ───────────────────────────────────────────────────────────────
            // PAS 1: CONFIGURATION
            // ───────────────────────────────────────────────────────────────
            
            // Configuration: Configura Hibernate programàticament
            Configuration configuration = new Configuration();
            
            // ───────────────────────────────────────────────────────────────
            // PAS 2: REGISTRAR ENTITATS
            // ───────────────────────────────────────────────────────────────
            
            // IMPORTANT: Cal registrar TOTES les classes @Entity que Hibernate ha de gestionar
            // Si afegeixes una nova entitat, cal registrar-la aquí!
            configuration.addAnnotatedClass(Ciutat.class);
            configuration.addAnnotatedClass(Ciutada.class);
            
            // ───────────────────────────────────────────────────────────────
            // PAS 3: CARREGAR PROPIETATS
            // ───────────────────────────────────────────────────────────────
            
            // Carreguem les propietats des del fitxer especificat
            // (URL BBDD, usuari, contrasenya, dialecte, etc.)
            Properties properties = new Properties();
            try (InputStream input = Manager.class.getClassLoader()
                    .getResourceAsStream(propertiesFileName)) {
                if (input == null) {
                    throw new IOException("No s'ha pogut trobar " + propertiesFileName);
                }
                properties.load(input);
            }
            configuration.addProperties(properties);
            
            // ───────────────────────────────────────────────────────────────
            // PAS 4: SERVICE REGISTRY
            // ───────────────────────────────────────────────────────────────
            
            // ServiceRegistry: Gestiona els serveis interns d'Hibernate
            // (connexions, cache, etc.)
            StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();
            
            // ───────────────────────────────────────────────────────────────
            // PAS 5: CONSTRUIR SESSIONFACTORY
            // ───────────────────────────────────────────────────────────────
            
            // Construïm el SessionFactory (operació costosa, només es fa un cop)
            factory = configuration.buildSessionFactory(serviceRegistry);
            
        } catch (Throwable ex) {
            // Si quelcom falla, imprimim l'error i llancem ExceptionInInitializerError
            // Això evita que l'aplicació continuï sense un SessionFactory vàlid
            System.err.println("Error creant SessionFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Tanca la SessionFactory i allibera recursos.
     * 
     * IMPORTANT: Cridar sempre al final de l'aplicació!
     * - Tanca el pool de connexions
     * - Allibera memòria
     * - Neteja recursos del sistema
     */
    public static void close() {
        if (factory != null && !factory.isClosed()) {
            factory.close();
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // CRUD - CREATE (Creació d'entitats)
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Crea una nova ciutat a la base de dades.
     * 
     * FLUX D'EXECUCIÓ:
     * 1. Obrir sessió (try-with-resources la tanca automàticament)
     * 2. Iniciar transacció
     * 3. Crear objecte i persistir
     * 4. Commit si tot OK, rollback si hi ha error
     * 
     * ESTATS DE L'OBJECTE:
     * - TRANSIENT: ciutat = new Ciutat(...) → No gestionat per Hibernate
     * - PERSISTENT: session.persist(ciutat) → Gestionat, canvis segueix
     * - DETACHED: return ciutat → Sessió tancada, objecte amb ID assignat
     * 
     * @param nom Nom de la ciutat (obligatori)
     * @param pais País on es troba
     * @param poblacio Nombre d'habitants
     * @return La ciutat amb l'ID assignat per la BD, o null si hi ha error
     */
    public static Ciutat addCiutat(String nom, String pais, Integer poblacio) {
        Ciutat result = null;
        // TRY-WITH-RESOURCES: La Session es tanca automàticament al final
        // (Session implementa AutoCloseable)
        try (Session session = factory.openSession()) {
            // TRANSACTION: Agrupa operacions. Si falla alguna, es pot fer rollback.
            Transaction tx = session.beginTransaction();
            try {
                // Creem l'objecte ciutat (estat TRANSIENT)
                result = new Ciutat(nom, pais, poblacio);
                
                // PERSIST: Guarda l'objecte a la BBDD i li assigna un ID
                // L'objecte passa a estat PERSISTENT (gestionat per Hibernate)
                session.persist(result);
                
                // COMMIT: Confirma els canvis a la BBDD
                // AQUÍ és quan s'executa l'INSERT real
                tx.commit();
            } catch (HibernateException e) {
                // ROLLBACK: Desfà tots els canvis si hi ha error
                if (tx != null && tx.isActive()) tx.rollback();
                System.err.println("Error creant Ciutat: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Crea un nou ciutadà a la base de dades.
     * 
     * Funciona exactament igual que addCiutat.
     * 
     * @param nom Nom del ciutadà (obligatori)
     * @param cognom Cognom del ciutadà
     * @param edat Edat del ciutadà
     * @return El ciutadà amb l'ID assignat per la BD, o null si hi ha error
     */
    public static Ciutada addCiutada(String nom, String cognom, Integer edat) {
        Ciutada result = null;
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                result = new Ciutada(nom, cognom, edat);
                session.persist(result);
                tx.commit();
            } catch (HibernateException e) {
                if (tx != null && tx.isActive()) tx.rollback();
                System.err.println("Error creant Ciutada: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return result;
    }

    // ═══════════════════════════════════════════════════════════════════
    // CRUD - UPDATE (Actualització d'entitats)
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Actualitza un ciutadà existent.
     * 
     * FUNCIONAMENT:
     * 1. session.get() carrega el ciutadà (estat MANAGED)
     * 2. Modificar les propietats (dirty checking automàtic)
     * 3. session.merge() sincronitza canvis (opcional, ja està MANAGED)
     * 4. tx.commit() executa l'UPDATE real
     * 
     * DIRTY CHECKING:
     * Hibernate detecta automàticament els canvis en objectes MANAGED.
     * No cal cridar update() o merge() explícitament, però ho fem per claredat.
     * 
     * @param ciutadaId ID del ciutadà a actualitzar
     * @param nom Nou nom
     * @param cognom Nou cognom
     * @param edat Nova edat
     */
    public static void updateCiutada(Long ciutadaId, String nom, String cognom, Integer edat) {
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                // GET: Recupera l'entitat per ID. Retorna null si no existeix.
                // L'objecte retornat està en estat MANAGED
                Ciutada ciutada = session.get(Ciutada.class, ciutadaId);
                
                if (ciutada != null) {
                    // Actualitzem les propietats (dirty checking automàtic)
                    ciutada.setNom(nom);
                    ciutada.setCognom(cognom);
                    ciutada.setEdat(edat);
                    
                    // MERGE: Sincronitza l'estat de l'objecte amb la BBDD
                    // No és estrictament necessari perquè ciutada ja és MANAGED,
                    // però ho deixem per claredat
                    session.merge(ciutada);
                }
                
                tx.commit();
            } catch (HibernateException e) {
                if (tx != null && tx.isActive()) tx.rollback();
                e.printStackTrace();
            }
        }
    }

    /**
     * Actualitza una ciutat existent amb els seus ciutadans.
     * 
     * GESTIÓ DE RELACIONS BIDIRECCIONALS:
     * Aquest mètode és més complex perquè manté la coherència de la relació
     * One-to-Many entre Ciutat i Ciutadans.
     * 
     * PROCESSOS:
     * 1. Carregar la ciutat existent
     * 2. Actualitzar propietats bàsiques (nom, pais, poblacio)
     * 3. Gestionar ciutadans:
     *    a) Eliminar ciutadans que ja no estan a la nova llista
     *    b) Afegir o actualitzar ciutadans de la nova llista
     * 4. Fer commit dels canvis
     * 
     * IMPORTANT - DETACHED vs MANAGED:
     * Els objectes ciutadans que venen com a paràmetre poden estar DETACHED
     * (creats fora de la sessió). Cal fer session.find() per obtenir la versió
     * MANAGED abans d'afegir-los a la ciutat.
     * 
     * @param ciutatId ID de la ciutat a actualitzar
     * @param nom Nou nom de la ciutat
     * @param pais Nou país
     * @param poblacio Nova població
     * @param ciutadans Nou conjunt de ciutadans (pot ser null per eliminar tots)
     */
    public static void updateCiutat(Long ciutatId, String nom, String pais, Integer poblacio, Set<Ciutada> ciutadans) {
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                // Carreguem la ciutat per ID
                Ciutat ciutat = session.get(Ciutat.class, ciutatId);
                
                if (ciutat == null) {
                    System.err.println("Ciutat no trobada amb id: " + ciutatId);
                    return;
                }
                
                // Actualitzem les propietats bàsiques
                ciutat.setNom(nom);
                ciutat.setPais(pais);
                ciutat.setPoblacio(poblacio);
                
                if (ciutadans != null) {
                    // ───────────────────────────────────────────────────────
                    // PAS 1: Eliminar ciutadans que ja no estan a la llista
                    // ───────────────────────────────────────────────────────
                    
                    // IMPORTANT: Fem una còpia per evitar ConcurrentModificationException
                    // mentre iterem i modifiquem la col·lecció
                    Set<Ciutada> currentCiutadans = new HashSet<>(ciutat.getCiutadans());
                    
                    for (Ciutada dbCiutada : currentCiutadans) {
                        if (!ciutadans.contains(dbCiutada)) {
                            // Usem el mètode helper per mantenir coherència bidireccional
                            // Això també marca el ciutadà per ser esborrat (orphanRemoval=true)
                            ciutat.removeCiutada(dbCiutada);
                        }
                    }
                    
                    // ───────────────────────────────────────────────────────
                    // PAS 2: Afegir o actualitzar ciutadans de la nova llista
                    // ───────────────────────────────────────────────────────
                    
                    for (Ciutada ciutadaInput : ciutadans) {
                        if (ciutadaInput.getCiutadaId() != null) {
                            // Ciutadà existent: Cal obtenir la versió MANAGED
                            // FIND: Similar a GET però segueix l'estàndard JPA
                            Ciutada managedCiutada = session.find(Ciutada.class, ciutadaInput.getCiutadaId());
                            
                            if (managedCiutada != null && !ciutat.getCiutadans().contains(managedCiutada)) {
                                // Usem el mètode helper per mantenir coherència
                                ciutat.addCiutada(managedCiutada);
                            }
                        } else {
                            // Ciutadà nou sense ID: s'afegeix i es persistirà per CASCADE
                            ciutat.addCiutada(ciutadaInput);
                        }
                    }
                } else {
                    // Si ciutadans és null, eliminem tots els ciutadans de la ciutat
                    new HashSet<>(ciutat.getCiutadans()).forEach(ciutat::removeCiutada);
                }
                
                // Merge per sincronitzar tots els canvis
                session.merge(ciutat);
                tx.commit();
                
            } catch (HibernateException e) {
                if (tx != null && tx.isActive()) tx.rollback();
                e.printStackTrace();
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // CRUD - READ (Lectura d'entitats)
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Obté una ciutat amb els seus ciutadans.
     * 
     * AMB EAGER LOADING:
     * Com la relació ciutadans té fetch=EAGER, els ciutadans es carreguen
     * automàticament quan es carrega la ciutat.
     * No cal JOIN FETCH ni Hibernate.initialize().
     * 
     * @param ciutatId ID de la ciutat a cercar
     * @return Ciutat amb ciutadans carregats, o null si no existeix
     */
    public static Ciutat getCiutatWithCiutadans(Long ciutatId) {
        try (Session session = factory.openSession()) {
            // GET: Carrega la ciutat
            // Els ciutadans es carreguen automàticament per FetchType.EAGER
            return session.get(Ciutat.class, ciutatId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Llista totes les entitats d'un tipus determinat.
     * 
     * MÈTODE GENÈRIC:
     * Funciona amb qualsevol classe Entity gràcies al paràmetre genèric <T>.
     * 
     * HQL (Hibernate Query Language):
     * - SQL: SELECT * FROM ciutats
     * - HQL: FROM Ciutat
     * 
     * DIFERÈNCIES HQL vs SQL:
     * - HQL usa noms de classes Java, no noms de taules
     * - HQL usa noms de propietats Java, no noms de columnes
     * - HQL és case-sensitive per als noms de classe
     * 
     * AMB EAGER LOADING:
     * Les relacions es carreguen automàticament.
     * No cal JOIN FETCH ni Hibernate.initialize().
     * 
     * @param <T> Tipus genèric de l'entitat
     * @param clazz Classe de l'entitat a llistar
     * @param orderBy Camp pel qual ordenar (opcional, pot ser null o buit)
     * @return Col·lecció amb totes les entitats del tipus especificat
     */
    public static <T> Collection<T> listCollection(Class<T> clazz, String orderBy) {
        Collection<T> result = Collections.emptyList();
        try (Session session = factory.openSession()) {
            // Construïm la consulta HQL
            // getSimpleName() retorna només el nom de la classe sense el paquet
            // Exemple: "Ciutat" en lloc de "com.project.Ciutat"
            String hql = "FROM " + clazz.getSimpleName();
            
            // Si s'especifica un camp d'ordenació, l'afegim
            if (orderBy != null && !orderBy.isEmpty()) {
                hql += " ORDER BY " + orderBy;
                // Exemple: "FROM Ciutat ORDER BY nom"
            }
            
            // Executem la query i obtenim els resultats
            // AMB EAGER: Les relacions es carreguen automàticament
            result = session.createQuery(hql, clazz).list();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Llista totes les ciutats.
     * 
     * AMB EAGER LOADING:
     * Els ciutadans es carreguen automàticament.
     * No cal DISTINCT ni JOIN FETCH.
     * 
     * @return Llista de totes les ciutats amb ciutadans carregats
     */
    public static List<Ciutat> findAllCiutatsWithCiutadans() {
        try (Session session = factory.openSession()) {
            // Consulta simple: EAGER loading carrega tot automàticament
            String hql = "FROM Ciutat";
            return session.createQuery(hql, Ciutat.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // CRUD - DELETE (Eliminació d'entitats)
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Esborra una entitat de la base de dades per ID.
     * 
     * MÈTODE GENÈRIC amb Serializable:
     * Funciona amb Long, Integer, String, etc. com a tipus d'ID.
     * 
     * FUNCIONAMENT:
     * 1. session.get() carrega l'entitat (ha d'estar MANAGED per esborrar-la)
     * 2. session.remove() marca l'objecte per esborrar (estat REMOVED)
     * 3. tx.commit() executa el DELETE real
     * 
     * CASCADE I RELACIONS:
     * - Si Ciutat té cascade=ALL, esborrar una ciutat esborra els seus ciutadans
     * - Si Ciutada no està en cap ciutat, s'esborra directament
     * - orphanRemoval=true també esborra ciutadans orfes
     * 
     * @param <T> Tipus genèric de l'entitat
     * @param clazz Classe de l'entitat a esborrar
     * @param id ID de l'entitat a esborrar
     */
    public static <T> void delete(Class<T> clazz, Serializable id) {
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                // GET: Carrega l'entitat per ID
                T obj = session.get(clazz, id);
                
                if (obj != null) {
                    // REMOVE: Elimina l'entitat de la BBDD
                    // L'objecte passa a estat REMOVED
                    session.remove(obj);
                    System.out.println("Eliminat objecte " + clazz.getSimpleName() + " amb id " + id);
                }
                
                // COMMIT: Executa el DELETE real
                tx.commit();
            } catch (HibernateException e) {
                if (tx != null && tx.isActive()) tx.rollback();
                e.printStackTrace();
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // UTILITATS
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Converteix una col·lecció a String per mostrar-la.
     * 
     * Mètode auxiliar per formatar col·leccions per a la consola.
     * No és específic d'Hibernate, però útil per debugging i mostrar resultats.
     * 
     * @param clazz Classe dels elements (per mostrar un missatge si està buida)
     * @param collection Col·lecció a convertir
     * @return String amb tots els elements (un per línia) o missatge si està buida
     */
    public static String collectionToString(Class<?> clazz, Collection<?> collection) {
        if (collection == null || collection.isEmpty()) {
            return "[Cap " + clazz.getSimpleName() + " trobat]";
        }
        
        StringBuilder sb = new StringBuilder();
        for (Object obj : collection) {
            sb.append(obj.toString()).append("\n");
        }
        return sb.toString();
    }
}