package cat.iesesteveterradas;

public class Faction {
    private int id;
    private String nom;
    private String resum;

    public Faction(int id, String nom, String resum) {
        this.id = id;
        this.nom = nom;
        this.resum = resum;
    }

    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getResum() { return resum; }

    @Override
    public String toString() {
        return id + " | " + nom + " | " + resum;
    }
}
