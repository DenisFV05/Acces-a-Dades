package com.project;

import com.project.estructuresdades.Objecte;
import com.project.utilitats.UtilitatsFitxers;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class EscripturaLlistes {

    // Mètode per escriure la llista d'objectes serialitzables en un fitxer binari
    public static void escriureLlistaObjectes(String camiFitxer, List<Objecte> llista) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(camiFitxer);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(llista.toArray(new Objecte[0]));  // Escriu la llista com un array d'Objecte
        }
    }

    public static void main(String[] args) {
        String camiBase = System.getProperty("user.dir") + "/data/";
        String camiFitxer = camiBase + "ArxiuLlistes.dat";

        try {
            // Utilitzar la classe utilitat per crear la carpeta si no existeix
            UtilitatsFitxers.crearCarpetaSiNoExisteix(camiBase);

            // Crear i omplir la llista d'objectes
            List<Objecte> llista = new ArrayList<>();
            llista.add(new Objecte("Escriptori", "Estudiar"));
            llista.add(new Objecte("Telèfon", "Perdre el temps"));

            // Escriure la llista d'objectes en el fitxer binari
            escriureLlistaObjectes(camiFitxer, llista);

            System.out.println("Llest");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
