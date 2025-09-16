package com.project;

import com.project.estructuresdades.Objecte;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

// Aquesta classe llegeix una llista d'objectes serialitzables d'un fitxer binari
public class LecturaLlistes {

    public static void main(String[] args) {
        String basePath = System.getProperty("user.dir") + "/data/";
        String filePath = basePath + "ArxiuLlistes.dat";

        // Crear la carpeta 'data' si no existeix
        crearDirectori(basePath);

        // Llegir la llista d'objectes des del fitxer i mostrar-la
        llegirILlistarObjectes(filePath);
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

    // Mètode que llegeix una llista d'objectes des d'un fitxer binari i la mostra
    public static void llegirILlistarObjectes(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            Objecte[] objectesArray = (Objecte[]) ois.readObject();
            ArrayList<Objecte> llistaObjectes = new ArrayList<>(Arrays.asList(objectesArray));

            // Mostrar cada objecte de la llista
            for (Objecte obj : llistaObjectes) {
                System.out.println(obj);
            }

        } catch (ClassNotFoundException | IOException e) {
            System.out.println("Error en la lectura del fitxer: " + filePath);
            e.printStackTrace();
        }
    }
}
