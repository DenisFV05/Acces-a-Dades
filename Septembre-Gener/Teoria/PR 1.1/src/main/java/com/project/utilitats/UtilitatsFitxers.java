package com.project.utilitats;

import java.io.File;
import java.io.IOException;

public class UtilitatsFitxers {

    // Mètode estàtic per gestionar la creació de carpetes o llençar excepció si és un fitxer
    public static void crearCarpetaSiNoExisteix(String camiFitxer) throws IOException {
        File fitxer = new File(camiFitxer);

        // Si és un fitxer i ja existeix, llençar una excepció
        if (fitxer.exists() && fitxer.isFile()) {
            throw new IOException("Error: el camí especificat és un fitxer i ja existeix.");
        }

        // Si el directori ja existeix, comprovar permisos
        if (fitxer.exists() && fitxer.isDirectory()) {
            if (!fitxer.canRead() || !fitxer.canWrite()) {
                throw new IOException("Error: el directori ja existeix però no té els permisos correctes.");
            }
        } else {
            // Si el directori no existeix, crear-lo
            if (!fitxer.mkdirs()) {
                throw new IOException("Error en la creació de la carpeta " + fitxer.getPath());
            }
        }
    }
}
