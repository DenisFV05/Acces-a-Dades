package com.project;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Entitat JPA que representa un ciutadà/ciutadana.
 * 
 * ANOTACIONS JPA:
 * Aquesta classe utilitza anotacions de Jakarta Persistence (JPA) per definir
 * el mapatge amb la base de dades. Hibernate llegeix aquestes anotacions
 * per saber com persistir els objectes.
 */
@Entity  // Marca aquesta classe com una entitat JPA gestionada
@Table(name = "ciutadans")  // Especifica el nom de la taula a la base de dades
public class Ciutada implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // ═══════════════════════════════════════════════════════════════════
    // CLAU PRIMÀRIA
    // ═══════════════════════════════════════════════════════════════════
    
    // @Id: Marca aquest camp com la clau primària de l'entitat
    // @GeneratedValue: Indica que el valor es genera automàticament
    // - strategy = IDENTITY: Utilitza el mecanisme natiu de la BD (AUTO_INCREMENT en MySQL/SQLite)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ciutada_id")
    private Long ciutadaId;
    
    // ═══════════════════════════════════════════════════════════════════
    // PROPIETATS SIMPLES
    // ═══════════════════════════════════════════════════════════════════
    
    // @Column: Especifica el nom de la columna i restriccions
    // - nullable = false: Camp obligatori (no pot ser NULL)
    @Column(name = "nom", nullable = false)
    private String nom;
    
    @Column(name = "cognom")
    private String cognom;
    
    @Column(name = "edat")
    private Integer edat;
    
    // ═══════════════════════════════════════════════════════════════════
    // RELACIÓ MANY-TO-ONE
    // ═══════════════════════════════════════════════════════════════════
    
    // @ManyToOne: Molts ciutadans pertanyen a una ciutat
    // - Aquest és el costat PROPIETARI de la relació (té el @JoinColumn)
    // - FetchType.EAGER: Carrega la ciutat SEMPRE (equivalent a lazy="false" del XML)
    // @JoinColumn: Especifica la columna de clau forana (Foreign Key)
    // - name = "ciutat_id": nom de la columna FK a la taula ciutadans
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ciutat_id")
    private Ciutat ciutat;
    
    // ═══════════════════════════════════════════════════════════════════
    // UUID PER EQUALS/HASHCODE
    // ═══════════════════════════════════════════════════════════════════
    
    // UUID: Identificador únic generat al crear l'objecte.
    // IMPORTANT: Necessari per equals/hashCode quan ciutadaId encara és null
    // (abans de persistir l'objecte a la base de dades).
    // 
    // PER QUÈ UUID?
    // - ciutadaId és null fins que s'insereix a la BD
    // - Si usem ciutadaId per equals/hashCode, dos objectes nous serien "iguals"
    // - Amb UUID, cada objecte té un identificador únic des del moment de creació
    @Column(name = "uuid", nullable = false, updatable = false, unique = true)
    private String uuid = UUID.randomUUID().toString();
    
    // ═══════════════════════════════════════════════════════════════════
    // CONSTRUCTORS
    // ═══════════════════════════════════════════════════════════════════
    
    // Constructor buit (obligatori per JPA)
    public Ciutada() {}
    
    // Constructor amb paràmetres
    public Ciutada(String nom, String cognom, Integer edat) {
        this.nom = nom;
        this.cognom = cognom;
        this.edat = edat;
    }
    
    // ═══════════════════════════════════════════════════════════════════
    // GETTERS I SETTERS
    // ═══════════════════════════════════════════════════════════════════
    
    public Long getCiutadaId() { return ciutadaId; }
    public void setCiutadaId(Long ciutadaId) { this.ciutadaId = ciutadaId; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getCognom() { return cognom; }
    public void setCognom(String cognom) { this.cognom = cognom; }
    
    public Integer getEdat() { return edat; }
    public void setEdat(Integer edat) { this.edat = edat; }
    
    public Ciutat getCiutat() { return ciutat; }
    public void setCiutat(Ciutat ciutat) { this.ciutat = ciutat; }
    
    // ═══════════════════════════════════════════════════════════════════
    // TOSTRING
    // ═══════════════════════════════════════════════════════════════════
    
    @Override
    public String toString() {
        return ciutadaId + ": " + nom + " " + cognom + " (" + edat + " anys)";
    }
    
    // ═══════════════════════════════════════════════════════════════════
    // EQUALS I HASHCODE BASATS EN UUID
    // ═══════════════════════════════════════════════════════════════════
    
    // EQUALS amb instanceof (NO getClass()):
    // IMPORTANT: Hibernate crea PROXIES (subclasses) per lazy loading.
    // Utilitzar getClass() fallaria perquè Proxy.class != Ciutada.class.
    // Per això usem instanceof que funciona amb proxies.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ciutada)) return false;
        Ciutada ciutada = (Ciutada) o;
        return Objects.equals(uuid, ciutada.uuid);
    }
    
    // HASHCODE basat en UUID:
    // Garanteix consistència abans i després de persistir l'entitat.
    // Dos objectes diferents tindran sempre hashCodes diferents (UUID únic).
    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}