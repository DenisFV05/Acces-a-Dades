package com.project;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class EscripturaRandomAccessFile {

    public static void main(String[] args) {
        // Definim el directori base i el nom del nostre fitxer binari
        String camiBaseStr = System.getProperty("user.dir") + "/data/esports";
        Path camiBase = Paths.get(camiBaseStr);
        Path rutaFitxer = camiBase.resolve("equips.dat"); // .dat és una extensió comuna per a fitxers de dades

        try {
            // Assegurem que el directori base existeix
            Files.createDirectories(camiBase);
            System.out.println("Directori base assegurat: " + camiBase);

            // Cridem el mètode que escriu les dades
            escriureDadesEquips(rutaFitxer);

        } catch (IOException e) {
            System.err.println("Hi ha hagut un error en l'operació de fitxers: " + e.getMessage());
        }
    }

    /**
     * Escriu les dades de dos equips d'eSports a un fitxer utilitzant RandomAccessFile.
     * Per a cada equip, desa: actiu (boolean), jugadors (int), premis (double) i nom (UTF).
     *
     * @param rutaFitxer El Path del fitxer on s'escriuran les dades.
     */
    public static void escriureDadesEquips(Path rutaFitxer) {

        // Fem servir un bloc try-with-resources per assegurar que el fitxer es tanca automàticament
        try (RandomAccessFile raf = new RandomAccessFile(rutaFitxer.toFile(), "rw")) {

            // --- DADES DE L'EQUIP 1: KOI ---
            boolean actiu1 = true;
            int jugadors1 = 5;
            double premis1 = 150000.75;
            String nom1 = "KOI";

            System.out.println("Escrivint dades per a l'equip: " + nom1);
            raf.writeBoolean(actiu1);   // 1 byte
            raf.writeInt(jugadors1);    // 4 bytes
            raf.writeDouble(premis1);   // 8 bytes
            raf.writeUTF(nom1);         // Longitud variable (2 bytes per la longitud + bytes del text)

            System.out.println("Dades de " + nom1 + " escrites correctament.");
            System.out.println("Posició actual del punter: " + raf.getFilePointer() + " bytes.");

            // --- DADES DE L'EQUIP 2: G2 Esports ---
            boolean actiu2 = true;
            int jugadors2 = 5;
            double premis2 = 9500000.50;
            String nom2 = "G2 Esports";

            System.out.println("\nEscrivint dades per a l'equip: " + nom2);
            raf.writeBoolean(actiu2);
            raf.writeInt(jugadors2);
            raf.writeDouble(premis2);
            raf.writeUTF(nom2);

            System.out.println("Dades de " + nom2 + " escrites correctament.");

            System.out.println("\nProcés finalitzat. Fitxer creat a: " + rutaFitxer.toAbsolutePath());
            System.out.println("Mida total del fitxer: " + raf.length() + " bytes.");

        } catch (IOException e) {
            System.err.println("No s'ha pogut escriure al fitxer d'accés aleatori: " + e.getMessage());
        }
    }
}