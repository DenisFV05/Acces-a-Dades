package com.project.estructuresdades;

import java.io.Serializable;

public class Objecte implements Serializable {

    // Afegim un número de sèrie (serialVersionUID)
    private static final long serialVersionUID = 1L;

    private String nom;
    private String utilitat;

    // Constructor públic per crear objectes
    public Objecte(String nom, String utilitat) {
        this.nom = nom;
        this.utilitat = utilitat;
    }

    // Getters per accedir a les propietats
    public String getNom() {
        return nom;
    }

    public String getUtilitat() {
        return utilitat;
    }

    @Override
    public String toString() {
        return "Nom: " + this.nom + ", Utilitat: " + this.utilitat;
    }
}
