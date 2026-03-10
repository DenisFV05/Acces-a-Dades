package com.project;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class EscripturaFileChannel {

    public static void main(String[] args) {
        String camiBaseStr = System.getProperty("user.dir") + "/data/esports-nio";
        Path rutaFitxer = Paths.get(camiBaseStr, "equips-channel.dat");

        try {
            Files.createDirectories(rutaFitxer.getParent());
            escriureEquips(rutaFitxer);
            System.out.println("Fitxer d'equips creat correctament a: " + rutaFitxer.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error en l'operació d'escriptura amb FileChannel: " + e.getMessage());
        }
    }

    /**
     * Escriu les dades de dos equips a un fitxer utilitzant FileChannel i ByteBuffer.
     * @param rutaFitxer La ruta del fitxer on s'escriuran les dades.
     * @throws IOException Si ocorre un error d'escriptura.
     */
    public static void escriureEquips(Path rutaFitxer) throws IOException {
        try (FileChannel channel = FileChannel.open(rutaFitxer,
                StandardOpenOption.WRITE,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {

            ByteBuffer buffer = ByteBuffer.allocate(128); // Buffer de 128 bytes

            // Dades de l'Equip 1: KOI
            Equip equip1 = new Equip(true, 5, 150000.75, "KOI");
            escriureEquipAlCanal(channel, buffer, equip1);

            // Dades de l'Equip 2: G2 Esports
            Equip equip2 = new Equip(true, 5, 9500000.50, "G2 Esports");
            escriureEquipAlCanal(channel, buffer, equip2);
        }
    }
    
    private static void escriureEquipAlCanal(FileChannel channel, ByteBuffer buffer, Equip equip) throws IOException {
        // 1. Neteja el buffer per a la nova escriptura
        buffer.clear();

        // 2. Posa les dades de l'equip al buffer
        buffer.put(equip.actiu() ? (byte) 1 : (byte) 0);
        buffer.putInt(equip.jugadors());
        buffer.putDouble(equip.premis());

        byte[] nomBytes = equip.nom().getBytes(StandardCharsets.UTF_8);
        buffer.putInt(nomBytes.length); // Guardem la longitud de l'string
        buffer.put(nomBytes);           // Guardem l'string

        // 3. Prepara el buffer per ser escrit al canal
        buffer.flip();

        // 4. Escriu el contingut del buffer al canal
        channel.write(buffer);
    }
}