package com.project;

import com.project.estructuresdades.Objecte;
import com.project.utilitats.UtilitatsFitxers;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class EscripturaObjectes {

    public static void escriureObjectes(String camiFitxer, Objecte obj0, Objecte obj1) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(camiFitxer);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            // Escriure els objectes serialitzables al fitxer
            oos.writeObject(obj0);
            oos.writeObject(obj1);
        }
    }

    public static void main(String[] args) {
        String camiBase = System.getProperty("user.dir") + "/data/";
        String camiFitxer = camiBase + "ArxiuObjectes.dat";

        try {
            // Utilitzar la classe utilitat per crear la carpeta si no existeix
            UtilitatsFitxers.crearCarpetaSiNoExisteix(camiBase);

            // Crear dos objectes
            Objecte obj0 = new Objecte("Escriptori", "Estudiar");
            Objecte obj1 = new Objecte("Telèfon", "Perdre el temps");

            // Escriure els objectes al fitxer
            escriureObjectes(camiFitxer, obj0, obj1);

            System.out.println("Llest");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
