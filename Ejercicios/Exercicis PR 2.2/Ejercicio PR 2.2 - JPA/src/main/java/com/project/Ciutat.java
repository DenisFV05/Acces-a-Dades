package com.project;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "ciutat")
public class Ciutat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ciutatId;

    private String nom;
    private String pais;
    private int poblacio;

    @OneToMany(mappedBy = "ciutat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Ciutada> ciutadans;

    public Ciutat() { }

    public Ciutat(String nom, String pais, int poblacio) {
        this.nom = nom;
        this.pais = pais;
        this.poblacio = poblacio;
    }

    // Getters y Setters
    public long getCiutatId() { return ciutatId; }
    public void setCiutatId(long ciutatId) { this.ciutatId = ciutatId; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public int getPoblacio() { return poblacio; }
    public void setPoblacio(int poblacio) { this.poblacio = poblacio; }

    public Set<Ciutada> getCiutadans() { return ciutadans; }
    public void setCiutadans(Set<Ciutada> ciutadans) { this.ciutadans = ciutadans; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(nom).append(" (").append(pais).append("), Població: ").append(poblacio).append(", Ciutadans: ");
        if (ciutadans != null && !ciutadans.isEmpty()) {
            for (Ciutada c : ciutadans) {
                sb.append(c.getNom()).append(" ").append(c.getCognom()).append(" | ");
            }
            sb.setLength(sb.length() - 3); // eliminar el último " | "
        } else {
            sb.append("[]");
        }
        return sb.toString();
    }
}
