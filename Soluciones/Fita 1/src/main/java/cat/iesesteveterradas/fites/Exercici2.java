package cat.iesesteveterradas.fites;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import cat.iesesteveterradas.fites.objectes.Exercici2nau;

/**
 * Implementa el codi que realitzi el següent:
 * 
 * - Serialitza aquesta llista en un fitxer binari anomenat 'Exercici2.dat' al directori de treball especificat.
 * - Després d'una pausa d'1 segon, deserialitza la llista del fitxer binari i la imprimeix per pantalla amb el format "Nom, País Any".
 * - Gestió d'errors: si hi ha algun problema en escriure o llegir el fitxer (ex. fitxer no trobat), mostra l'excepció a la consola amb e.printStackTrace()
 */
public class Exercici2 {
    private String filePath;

    public static void main(String[] args) {
        String basePath = System.getProperty("user.dir") + "/data/exercici2/";
        String filePath = basePath + "Exercici2.dat";

        Exercici2 exercici = new Exercici2();
        exercici.configurarRutaFitxerSortida(filePath);
        exercici.executa();
    }

    // Mètode que executa la lògica de la classe
    public void executa() {
        ArrayList<Exercici2nau> llista0 = new ArrayList<>();
        llista0.add(new Exercici2nau("Vostok", "USSR", 1961));
        llista0.add(new Exercici2nau("Mercury", "US", 1961));
        llista0.add(new Exercici2nau("Gemini", "US", 1965));
        llista0.add(new Exercici2nau("Soyuz", "Russia", 1967));
        llista0.add(new Exercici2nau("Apollo", "US", 1968));
        llista0.add(new Exercici2nau("Shuttle", "US", 1981));
        llista0.add(new Exercici2nau("Shenzhou", "China", 2003));
        llista0.add(new Exercici2nau("Crew Dragon", "US", 2020));

        serialitzaLlista(filePath, llista0);

        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) { 
            e.printStackTrace(); 
        }

        ArrayList<Exercici2nau> llista1 = deserialitzaLlista(filePath);
        imprimeixLlista(llista1);
    }

    // Mètode per serialitzar la llista a un fitxer
    public void serialitzaLlista(String filePath, ArrayList<Exercici2nau> llista) {
        try {
            File fitxer = new File(filePath);
            fitxer.getParentFile().mkdirs();

            FileOutputStream fos = new FileOutputStream(fitxer);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(llista);

            // Tancar 
            oos.close();
            fos.close();

            System.out.println("Llista serialitzada correctament al fitxer: " + filePath);

        } catch (IOException e) {
            e.printStackTrace(); 
        }
    }


    // Mètode per deserialitzar la llista del fitxer
    public ArrayList<Exercici2nau> deserialitzaLlista(String filePath) {
        ArrayList<Exercici2nau> llista = null;

        try {
            // Obrir el fitxer per llegir objectes
            FileInputStream fis = new FileInputStream(filePath);
            ObjectInputStream ois = new ObjectInputStream(fis);

            llista = (ArrayList<Exercici2nau>) ois.readObject();

            // Tancar 
            ois.close();
            fis.close();

            System.out.println("Llista deserialitzada correctament del fitxer: " + filePath);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return llista;
    }

    // Mètode per imprimir la llista
    public void imprimeixLlista(ArrayList<Exercici2nau> llista) {
        if (llista == null || llista.isEmpty()) {
            System.out.println("La llista està buida o no s'ha carregat correctament.");
            return;
        }

        for (Exercici2nau nau : llista) {
            System.out.println(nau.getNom() + ", " + nau.getPais() + " " + nau.getAny()); // Utilitzem els getters que hem definit al objecte
        }
    }



    /****************************************************************************/
    /*                          NO CAL MODIFICAR                                */
    /****************************************************************************/       
    // Setter per definir el path
    public void configurarRutaFitxerSortida(String filePath) {
        this.filePath = filePath;
    }

    // Getter per obtenir el path
    public String obtenirRutaFitxerSortida() {
        return this.filePath;
    }
}
