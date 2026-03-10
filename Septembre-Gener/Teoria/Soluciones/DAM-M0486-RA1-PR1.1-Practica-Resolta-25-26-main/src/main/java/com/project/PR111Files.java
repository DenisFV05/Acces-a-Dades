package com.project;

import java.io.File;
import java.io.IOException;

public class PR111Files {

    public static void main(String[] args) {
        String camiFitxer = System.getProperty("user.dir") + "/data/pr111";
        gestionarArxius(camiFitxer);
    }

    public static void gestionarArxius(String camiFitxer) {
        try {
            // Crear la carpeta "myFiles" dins de camiFitxer
            File carpeta = new File(camiFitxer, "myFiles");
            if (!carpeta.exists()) {
                carpeta.mkdirs(); // Crear tots els directoris necessaris
            }

            // Crear "file1.txt" i "file2.txt"
            File file1 = new File(carpeta, "file1.txt");
            File file2 = new File(carpeta, "file2.txt");
            file1.createNewFile();
            file2.createNewFile();

            // Renombrar "file2.txt" a "renamedFile.txt"
            File renamedFile = new File(carpeta, "renamedFile.txt");
            file2.renameTo(renamedFile);

            // Mostrar el llistat dels arxius de la carpeta
            mostrarLlistatArxius(carpeta);

            // Eliminar "file1.txt"
            file1.delete();

            // Tornar a mostrar el llistat dels arxius de la carpeta
            mostrarLlistatArxius(carpeta);

        } catch (IOException e) {
            System.out.println("Error gestionant els arxius: " + e.getMessage());
        }
    }

    // Mètode per mostrar els arxius dins d'una carpeta
    public static void mostrarLlistatArxius(File carpeta) {
        String[] arxius = carpeta.list();
        if (arxius != null && arxius.length > 0) {
            System.out.println("Els arxius de la carpeta són:");
            for (String arxiu : arxius) {
                System.out.println("- " + arxiu);
            }
        } else {
            System.out.println("La carpeta no conté cap arxiu.");
        }
    }
}
