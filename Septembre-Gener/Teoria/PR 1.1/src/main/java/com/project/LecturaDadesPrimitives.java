package com.project;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

// Aquesta classe llegeix dades primitives d'un fitxer binari i les mostra
public class LecturaDadesPrimitives {

    public static void main(String[] args) {
        String basePath = System.getProperty("user.dir") + "/data/";
        String filePath = basePath + "ArxiuDadesPrimitives.dat";

        // Crear la carpeta 'data' si no existeix
        try {
            Files.createDirectories(Path.of(basePath));
        } catch (IOException e) {
            System.out.println("Error en la creació de la carpeta: " + basePath);
            e.printStackTrace();
            return;
        }

        // Llegir el fitxer i mostrar el contingut
        llegirIFerMostra(filePath);
    }

    // Mètode que llegeix les dades d'un fitxer binari i les mostra
    public static void llegirIFerMostra(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath);
             DataInputStream dis = new DataInputStream(fis)) {

            // Llegir dades primitives
            String cad = dis.readUTF();
            int num = dis.readInt();
            boolean bool = dis.readBoolean();
            char chr = dis.readChar();
            double dou = dis.readDouble();

            // Mostrar les dades
            System.out.println("String > " + cad);
            System.out.println("Enter > " + num);
            System.out.println("Booleà > " + bool);
            System.out.println("Caràcter > " + chr);
            System.out.println("Double > " + dou);

        } catch (IOException e) {
            System.out.println("Error en la lectura del fitxer: " + filePath);
            e.printStackTrace();
        }
    }
}
