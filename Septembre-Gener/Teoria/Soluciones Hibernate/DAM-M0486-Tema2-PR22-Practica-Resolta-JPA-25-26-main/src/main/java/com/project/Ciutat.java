package com.project;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Entitat JPA que representa una ciutat.
 * 
 * ANOTACIONS JPA:
 * Aquesta classe utilitza anotacions de Jakarta Persistence (JPA) per definir
 * el mapatge amb la base de dades. Hibernate llegeix aquestes anotacions
 * per saber com persistir els objectes.
 */
@Entity  // Marca aquesta classe com una entitat JPA gestionada
@Table(name = "ciutats")  // Especifica el nom de la taula a la base de dades
public class Ciutat implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // ═══════════════════════════════════════════════════════════════════
    // CLAU PRIMÀRIA
    // ═══════════════════════════════════════════════════════════════════
    
    // @Id: Marca aquest camp com la clau primària de l'entitat
    // @GeneratedValue: Indica que el valor es genera automàticament
    // - strategy = IDENTITY: Utilitza AUTO_INCREMENT (MySQL/SQLite) o SERIAL (PostgreSQL)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ciutat_id")
    private Long ciutatId;
    
    // ═══════════════════════════════════════════════════════════════════
    // PROPIETATS SIMPLES
    // ═══════════════════════════════════════════════════════════════════
    
    // @Column: Especifica el nom de la columna i restriccions
    // - nullable = false: Camp obligatori (no pot ser NULL)
    @Column(name = "nom", nullable = false)
    private String nom;
    
    @Column(name = "pais")
    private String pais;
    
    @Column(name = "poblacio")
    private Integer poblacio;
    
    // ═══════════════════════════════════════════════════════════════════
    // RELACIÓ ONE-TO-MANY
    // ═══════════════════════════════════════════════════════════════════
    
    // @OneToMany: Una ciutat té molts ciutadans
    // 
    // PARÀMETRES IMPORTANTS:
    // - mappedBy = "ciutat": Indica que l'altre costat de la relació (Ciutada)
    //   és el propietari i té un camp anomenat "ciutat".
    //   Aquest costat és el INVERS de la relació.
    // 
    // - cascade = CascadeType.ALL: Propaga totes les operacions
    //   (persist, merge, remove, refresh, detach) als ciutadans relacionats.
    //   Si es guarda/esborra una ciutat, es guarden/esborren els seus ciutadans.
    // 
    // - fetch = FetchType.EAGER: Carrega els ciutadans SEMPRE que es carrega la ciutat.
    //   Equivalent a lazy="false" del XML original.
    //   Això elimina qualsevol problema de LazyInitializationException.
    // 
    // - orphanRemoval = true: Esborra automàticament els ciutadans que ja no
    //   estiguin en la col·lecció. Si es treu un ciutadà del Set, s'esborra de la BD.
    @OneToMany(mappedBy = "ciutat", cascade = CascadeType.ALL, 
               fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<Ciutada> ciutadans = new HashSet<>();
    
    // ═══════════════════════════════════════════════════════════════════
    // UUID PER EQUALS/HASHCODE
    // ═══════════════════════════════════════════════════════════════════
    
    // UUID: Identificador únic generat al crear l'objecte.
    // IMPORTANT: Necessari per equals/hashCode quan ciutatId encara és null
    // (abans de persistir l'objecte a la base de dades).
    // 
    // PER QUÈ UUID?
    // - ciutatId és null fins que s'insereix a la BD
    // - Si usem ciutatId per equals/hashCode, dos objectes nous serien "iguals"
    // - Amb UUID, cada objecte té un identificador únic des del moment de creació
    @Column(name = "uuid", nullable = false, updatable = false, unique = true)
    private String uuid = UUID.randomUUID().toString();
    
    // ═══════════════════════════════════════════════════════════════════
    // CONSTRUCTORS
    // ═══════════════════════════════════════════════════════════════════
    
    // Constructor buit (obligatori per JPA)
    public Ciutat() {}
    
    // Constructor amb paràmetres
    public Ciutat(String nom, String pais, Integer poblacio) {
        this.nom = nom;
        this.pais = pais;
        this.poblacio = poblacio;
    }
    
    // ═══════════════════════════════════════════════════════════════════
    // GETTERS I SETTERS
    // ═══════════════════════════════════════════════════════════════════
    
    public Long getCiutatId() { return ciutatId; }
    public void setCiutatId(Long ciutatId) { this.ciutatId = ciutatId; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }
    
    public Integer getPoblacio() { return poblacio; }
    public void setPoblacio(Integer poblacio) { this.poblacio = poblacio; }
    
    public Set<Ciutada> getCiutadans() { return ciutadans; }
    
    // SETTER ESPECIAL per setCiutadans:
    // Aquest mètode no només assigna el Set, sinó que manté la coherència
    // bidireccional cridant als mètodes helper addCiutada i removeCiutada.
    public void setCiutadans(Set<Ciutada> ciutadans) {
        this.ciutadans.clear();
        if (ciutadans != null) {
            ciutadans.forEach(this::addCiutada);
        }
    }
    
    // ═══════════════════════════════════════════════════════════════════
    // MÈTODES HELPER PER MANTENIR COHERÈNCIA BIDIRECCIONAL
    // ═══════════════════════════════════════════════════════════════════
    
    /**
     * Afegeix un ciutadà a aquesta ciutat.
     * 
     * IMPORTANT: Aquest mètode manté la COHERÈNCIA BIDIRECCIONAL.
     * Quan afegeixes un ciutadà a una ciutat, també s'actualitza
     * la referència inversa (ciutada.setCiutat(this)).
     * 
     * Sense això, la relació estaria inconsistent:
     * - ciutat.getCiutadans() contindria el ciutadà
     * - Però ciutada.getCiutat() seria null o una altra ciutat
     * 
     * USO:
     * ciutat.addCiutada(nouCiutada);  // Millor que ciutat.getCiutadans().add()
     */
    public void addCiutada(Ciutada ciutada) {
        ciutadans.add(ciutada);
        ciutada.setCiutat(this);
    }
    
    /**
     * Elimina un ciutadà d'aquesta ciutat.
     * 
     * IMPORTANT: Aquest mètode manté la COHERÈNCIA BIDIRECCIONAL.
     * Quan elimines un ciutadà d'una ciutat, també es trenca
     * la referència inversa (ciutada.setCiutat(null)).
     * 
     * Amb orphanRemoval=true, aquest ciutadà s'esborrarà de la BD
     * al fer commit de la transacció.
     * 
     * USO:
     * ciutat.removeCiutada(ciutada);  // Millor que ciutat.getCiutadans().remove()
     */
    public void removeCiutada(Ciutada ciutada) {
        ciutadans.remove(ciutada);
        ciutada.setCiutat(null);
    }
    
    // ═══════════════════════════════════════════════════════════════════
    // TOSTRING
    // ═══════════════════════════════════════════════════════════════════
    
    @Override
    public String toString() {
        // Utilitzem Stream API per formatar la llista de ciutadans
        String llistaCiutadans = "Buit";
        if (ciutadans != null && !ciutadans.isEmpty()) {
            llistaCiutadans = ciutadans.stream()
                .map(c -> c.getNom() + " " + c.getCognom())
                .collect(Collectors.joining(" | "));
        }
        
        return String.format("Ciutat [ID=%d, Nom=%s, País=%s, Població=%d, Ciutadans: [%s]]",
            ciutatId, nom, pais, poblacio, llistaCiutadans);
    }
    
    // ═══════════════════════════════════════════════════════════════════
    // EQUALS I HASHCODE BASATS EN UUID
    // ═══════════════════════════════════════════════════════════════════
    
    // EQUALS amb instanceof (NO getClass()):
    // IMPORTANT: Hibernate crea PROXIES (subclasses) per lazy loading.
    // Utilitzar getClass() fallaria perquè Proxy.class != Ciutat.class.
    // Per això usem instanceof que funciona amb proxies.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ciutat)) return false;
        Ciutat ciutat = (Ciutat) o;
        return Objects.equals(uuid, ciutat.uuid);
    }
    
    // HASHCODE basat en UUID:
    // Garanteix consistència abans i després de persistir l'entitat.
    // Dos objectes diferents tindran sempre hashCodes diferents (UUID únic).
    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}