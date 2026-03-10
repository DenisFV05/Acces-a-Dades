package com.project;

import com.project.estructuresdades.Objecte;

import java.io.*; 
import java.nio.file.Files;
import java.nio.file.Path;

// Aquesta classe llegeix dos objectes serialitzables d'un fitxer binari
public class LecturaObjectes {

    public static void main(String[] args) {
        String basePath = System.getProperty("user.dir") + "/data/";
        String filePath = basePath + "ArxiuObjectes.dat";

        // Crear la carpeta 'data' si no existeix
        crearDirectori(basePath);

        // Llegir els objectes del fitxer i mostrar-los
        llegirIMostrarObjectes(filePath);
    }

    // Mètode per crear un directori si no existeix
    public static void crearDirectori(String basePath) {
        try {
            Files.createDirectories(Path.of(basePath));
        } catch (IOException e) {
            System.out.println("Error en la creació de la carpeta: " + basePath);
            e.printStackTrace();
        }
    }

    // Mètode que llegeix objectes d'un fitxer binari i els mostra
    public static void llegirIMostrarObjectes(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            // Llegir dos objectes
            Objecte obj0 = (Objecte) ois.readObject();
            Objecte obj1 = (Objecte) ois.readObject();

            // Mostrar els objectes
            System.out.println(obj0);
            System.out.println(obj1);

        } catch (ClassNotFoundException | IOException e) {
            System.out.println("Error en la lectura del fitxer: " + filePath);
            e.printStackTrace();
        }
    }
}
