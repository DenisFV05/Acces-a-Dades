package com.project;

import java.io.IOException;
import java.nio.file.*;

public class PR111Files {

    public static void main(String[] args) {
        String camiFitxer = System.getProperty("user.dir") + "/data/pr111";
        gestionarArxius(camiFitxer);
    }

    public static void gestionarArxius(String camiFitxer) {
        // 1. Crear carpeta System.getProperty("user.dir") + "/data/pr111/myFiles"
        Path dir = Paths.get(camiFitxer, "myFiles");

        try {
            Files.createDirectories(dir);
        // 2. Crear file1.txt file2.txt
            Path file1 = dir.resolve("file1.txt");
            Path file2 = dir.resolve("file2.txt");

            if (!Files.exists(file1)) {
                Files.createFile(file1);
            }
            if (!Files.exists(file2)) {
                Files.createFile(file2);
            }
            
        // 3. Rename file2.txt to renamedFile.txt
            Path renamedFile = dir.resolve("renamedFile.txt");
            if (Files.exists(file2)) {
                Files.move(file2, renamedFile, StandardCopyOption.REPLACE_EXISTING); // Lo he buscado, basicamente lo reemplaza
            }
        // 4. List arxius dins de myFiles
            System.out.println("Els arxius de la carpeta són:");
            // p es cada file, getFilenName coge el nombre, y se imprime con un guion delante
            Files.list(dir).forEach(p -> System.out.println(" - " + p.getFileName())); // Funcion que he buscado lambda
        // 5. Delete file1.txt
            if (Files.exists(file1)) {
                Files.delete(file1);
            }
        // 6. List de nuevo
            System.out.println("Els arxius de la carpeta són:");
            // p es cada file, getFilenName coge el nombre, y se imprime con un guion delante
            Files.list(dir).forEach(p -> System.out.println(" - " + p.getFileName())); // Funcion que he buscado lambda


        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
