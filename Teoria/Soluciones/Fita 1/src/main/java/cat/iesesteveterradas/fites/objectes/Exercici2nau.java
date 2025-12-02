package cat.iesesteveterradas.fites.objectes;

import java.io.Serializable;
import java.util.Objects;

/**
 * Estem parlant de serialitzar objectes.
 * Cal que s'asseguris que aquesta classe implementa
 * el necessari.
 */

public class Exercici2nau implements Serializable {
    private static final long serialVersionUID = 1L;

    String nom;
    String pais;
    int any;

    public Exercici2nau (String nom, String pais, int any) {
        this.nom = nom;
        this.pais = pais;
        this.any = any;
    }

    @Override
    public String toString () {
        return this.nom + ", " + this.pais + " " + this.any;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exercici2nau)) return false;
        Exercici2nau that = (Exercici2nau) o;
        return any == that.any &&
                Objects.equals(nom, that.nom) &&
                Objects.equals(pais, that.pais);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom, pais, any);
    }


    // Posem tots el getters y setters, fan falta pel exercici 2
    public static long getSerialversionuid() {
        return serialVersionUID;
    }



    public String getNom() {
        return nom;
    }



    public void setNom(String nom) {
        this.nom = nom;
    }



    public String getPais() {
        return pais;
    }



    public void setPais(String pais) {
        this.pais = pais;
    }



    public int getAny() {
        return any;
    }



    public void setAny(int any) {
        this.any = any;
    }
}