package com.project;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class LecturaFileChannel {

    public static void main(String[] args) {
        String camiBaseStr = System.getProperty("user.dir") + "/data/esports-nio";
        Path rutaFitxer = Paths.get(camiBaseStr, "equips-channel.dat");

        if (!Files.exists(rutaFitxer)) {
            System.err.println("Error: El fitxer " + rutaFitxer.toAbsolutePath() + " no existeix.");
            return;
        }

        try {
            List<Equip> equipsLlegits = llegirEquips(rutaFitxer);

            System.out.println("S'han llegit " + equipsLlegits.size() + " equips del fitxer:");
            System.out.println("------------------------------------");
            for (Equip equip : equipsLlegits) {
                System.out.printf("  Nom: %s, Actiu: %b, Jugadors: %d, Premis: %,.2f €%n",
                        equip.nom(), equip.actiu(), equip.jugadors(), equip.premis());
            }

        } catch (IOException e) {
            System.err.println("Error en la lectura amb FileChannel: " + e.getMessage());
        }
    }

    /**
     * Llegeix un fitxer de dades d'equips i el converteix a una llista d'objectes Equip.
     * @param rutaFitxer La ruta del fitxer a llegir.
     * @return Una llista amb els equips llegits.
     * @throws IOException Si ocorre un error de lectura.
     */
    public static List<Equip> llegirEquips(Path rutaFitxer) throws IOException {
        List<Equip> equips = new ArrayList<>();
        try (FileChannel channel = FileChannel.open(rutaFitxer, StandardOpenOption.READ)) {
            ByteBuffer buffer = ByteBuffer.allocate((int) channel.size()); // Creem un buffer de la mida del fitxer

            // Llegim tot el contingut del fitxer al buffer
            channel.read(buffer);
            buffer.flip(); // Preparem el buffer per a la lectura

            while (buffer.hasRemaining()) {
                boolean actiu = buffer.get() == 1;
                int jugadors = buffer.getInt();
                double premis = buffer.getDouble();

                int longitudNom = buffer.getInt();
                byte[] nomBytes = new byte[longitudNom];
                buffer.get(nomBytes);
                String nom = new String(nomBytes, java.nio.charset.StandardCharsets.UTF_8);

                equips.add(new Equip(actiu, jugadors, premis, nom));
            }
        }
        return equips;
    }
}