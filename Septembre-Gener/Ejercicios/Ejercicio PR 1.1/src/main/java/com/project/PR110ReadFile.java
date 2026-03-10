package com.project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PR110ReadFile {

    public static void main(String[] args) {
        String camiFitxer = System.getProperty("user.dir") + "/data/GestioTasques.java";
        llegirIMostrarFitxer(camiFitxer);  // Només cridem a la funció amb la ruta del fitxer
    }

    // Funció que llegeix el fitxer i mostra les línies amb numeració
    public static void llegirIMostrarFitxer(String camiFitxer) {
        int numLinia = 1; // Numero a la izq 
        try (BufferedReader br = new BufferedReader(new FileReader(camiFitxer))) { //Bufer que lee
            String linia; // String vacio

            while ((linia = br.readLine()) != null) { //Mientras haya lineas
                System.out.println(numLinia + ": " + linia); //Mostrar la linea como se pide
                numLinia++; //Siguiente linea

            }
        } catch (IOException e) {
            System.out.println("Error!");
        }

    }
}
