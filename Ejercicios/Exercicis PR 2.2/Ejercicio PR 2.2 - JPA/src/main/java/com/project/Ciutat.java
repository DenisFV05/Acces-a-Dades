package com.project;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "CIUTAT")
public class Ciutat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CIUTAT_ID")
    private Long id;

    private String nom;
    private String pais;
    private int poblacio;

    @OneToMany(mappedBy = "ciutat", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Ciutada> ciutadans = new HashSet<>();

    public Ciutat() {}

    public Ciutat(String nom, String pais, int poblacio) {
        this.nom = nom;
        this.pais = pais;
        this.poblacio = poblacio;
    }

    // getters y setters
    public Long getId() { return id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }
    public int getPoblacio() { return poblacio; }
    public void setPoblacio(int poblacio) { this.poblacio = poblacio; }

    public Set<Ciutada> getCiutadans() { return ciutadans; }

    public void addCiutada(Ciutada c) {
        ciutadans.add(c);
        c.setCiutat(this);
    }

    @Override
    public String toString() {
        return nom + " (" + pais + "), Població: " + poblacio +
               ", Ciutadans: " + ciutadans.size();
    }
}
