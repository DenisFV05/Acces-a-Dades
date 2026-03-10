package com.project;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LecturaRandomAccessFile {

    public static void main(String[] args) {
        // Definim la ruta al fitxer que volem llegir
        String camiBaseStr = System.getProperty("user.dir") + "/data/esports";
        Path rutaFitxer = Paths.get(camiBaseStr, "equips.dat");

        // Comprovem primer si el fitxer existeix
        if (!Files.exists(rutaFitxer)) {
            System.err.println("Error: El fitxer " + rutaFitxer.toAbsolutePath() + " no existeix.");
            System.err.println("Si us plau, executa primer RandomAccessFileWriter per crear-lo.");
            return;
        }

        try {
            // Cridem el mètode que llegeix les dades i ens retorna una llista
            List<Equip> equips = llegirDadesEquips(rutaFitxer);

            // Ara, el main s'encarrega de presentar les dades
            System.out.println("S'han llegit " + equips.size() + " equips del fitxer:");
            System.out.println("------------------------------------");
            for (int i = 0; i < equips.size(); i++) {
                Equip equip = equips.get(i);
                System.out.printf("DADES DE L'EQUIP %d:%n", i + 1);
                System.out.printf("  Nom: %s%n", equip.nom());
                System.out.printf("  Actiu: %b%n", equip.actiu());
                System.out.printf("  Jugadors: %d%n", equip.jugadors());
                System.out.printf("  Premis Acumulats: %,.2f €%n", equip.premis());
                System.out.println("------------------------------------");
            }

        } catch (IOException e) {
            System.err.println("Hi ha hagut un error en llegir el fitxer: " + e.getMessage());
        }
    }

    /**
     * Llegeix un fitxer de dades binari i retorna una llista d'objectes Equip.
     * @param rutaFitxer El Path del fitxer a llegir.
     * @return Una llista amb tots els equips llegits.
     * @throws IOException Si ocorre un error de lectura.
     */
    public static List<Equip> llegirDadesEquips(Path rutaFitxer) throws IOException {
        List<Equip> llistaEquips = new ArrayList<>();

        try (RandomAccessFile raf = new RandomAccessFile(rutaFitxer.toFile(), "r")) {
            // El bucle continua mentre el punter no hagi arribat al final del fitxer
            while (raf.getFilePointer() < raf.length()) {
                // Llegim les dades en el mateix ordre que les vam escriure
                boolean actiu = raf.readBoolean();
                int jugadors = raf.readInt();
                double premis = raf.readDouble();
                String nom = raf.readUTF();

                // Creem un nou objecte Equip i l'afegim a la llista
                llistaEquips.add(new Equip(actiu, jugadors, premis, nom));
            }
        }
        return llistaEquips;
    }
}