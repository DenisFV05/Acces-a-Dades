package com.project;

import com.project.utilitats.UtilitatsFitxers;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class EscripturaDadesPrimitives {

    // Mètode per escriure només dades primitives en un fitxer binari
    public static void escriureDadesPrimitives(String camiFitxer, String text, int numero, boolean valor, char lletra, double decimal) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(camiFitxer);
             DataOutputStream dos = new DataOutputStream(fos)) {

            // Escriure dades primitives
            dos.writeUTF(text);
            dos.writeInt(numero);
            dos.writeBoolean(valor);
            dos.writeChar(lletra);
            dos.writeDouble(decimal);

            dos.flush();
        }
    }

    public static void main(String[] args) {
        String camiBase = System.getProperty("user.dir") + "/data/";
        String camiFitxer = camiBase + "ArxiuDadesPrimitives.dat";

        try {
            // Utilitzar la classe utilitat per crear la carpeta si no existeix
            UtilitatsFitxers.crearCarpetaSiNoExisteix(camiBase);

            // Escriure només dades primitives
            escriureDadesPrimitives(camiFitxer, "Hola", 44, true, 'A', 2.46);

            System.out.println("Llest");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
