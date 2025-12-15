package com.project;

import jakarta.persistence.*;

@Entity
@Table(name = "CIUTADA")
public class Ciutada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CIUTADA_ID")
    private Long id;

    private String nom;
    private String cognom;
    private int edat;

    @ManyToOne
    @JoinColumn(name = "CIUTAT_ID")
    private Ciutat ciutat;

    public Ciutada() {}

    public Ciutada(String nom, String cognom, int edat) {
        this.nom = nom;
        this.cognom = cognom;
        this.edat = edat;
    }

    // getters y setters
    public Long getId() { return id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getCognom() { return cognom; }
    public void setCognom(String cognom) { this.cognom = cognom; }
    public int getEdat() { return edat; }
    public void setEdat(int edat) { this.edat = edat; }

    public Ciutat getCiutat() { return ciutat; }
    public void setCiutat(Ciutat ciutat) { this.ciutat = ciutat; }

    @Override
    public String toString() {
        return nom + " " + cognom + " (" + edat + " anys)";
    }
}
