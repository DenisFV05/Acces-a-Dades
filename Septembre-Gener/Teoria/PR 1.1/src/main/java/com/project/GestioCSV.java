package com.project;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.project.utilitats.UtilsCSV;

// En aquest exemple es fa servir
// UtilsCSV per llegir i modificar
// les dades d'un CSV

public class GestioCSV {
    public static void main(String[] args) {
        String camiBase = System.getProperty("user.dir") + "/data/";
        String nomFitxer = "GestioCSV.csv";
        String camiFitxer = camiBase + nomFitxer;

        // Crear la carpeta 'data' si no existeix
        File carpeta = new File(camiBase);
        if (!carpeta.exists()) {
            if (!carpeta.mkdirs()) {
                System.out.println("Error en la creació de la carpeta 'data'");
                return;
            }
        }

        // Llegir el fitxer CSV
        List<String> csv = UtilsCSV.llegir(camiFitxer);

        // Obtenir les columnes
        String[] columnes = UtilsCSV.obtenirClaus(csv);
        System.out.println("Les columnes són: " + Arrays.toString(columnes));

        // Obtenir la posició de la columna "titol"
        int posicioColumna = UtilsCSV.obtenirPosicioColumna(csv, "titol");
        System.out.println("La columna \"titol\" està a la posició " + posicioColumna);

        // Obtenir les dades de la columna "titol"
        String[] dadesTitol = UtilsCSV.obtenirDadesColumna(csv, "titol");
        dadesTitol = Arrays.copyOfRange(dadesTitol, 1, dadesTitol.length); // Treu el primer element, que és el nom de la columna
        System.out.println("Els títols de l'arxiu són: " + Arrays.toString(dadesTitol));

        // Obtenir el número de línia de "Titanic"
        int numLiniaTitanic = UtilsCSV.obtenirNumLinia(csv, "titol", "Titanic");
        System.out.println("La fila on està \"Titanic\" és la " + numLiniaTitanic);

        // Obtenir el número de línia de "Avatar"
        int numLiniaAvatar = UtilsCSV.obtenirNumLinia(csv, "id", "123");
        System.out.println("La fila on està \"Avatar\" és la " + numLiniaAvatar);

        // Obtenir el text de la línia corresponent a "Avatar"
        String liniaAvatar = csv.get(numLiniaAvatar);
        System.out.println("El text de la fila \"d'Avatar\" és \"" + liniaAvatar + "\"");

        // Convertir la línia a un array de dades
        String[] arrAvatar = UtilsCSV.obtenirArrayLinia(liniaAvatar);
        System.out.println("L'array de dades de la fila \"d'Avatar\" és " + Arrays.toString(arrAvatar));

        // Actualitzar l'any de la pel·lícula "Avatar"
        int posAny = UtilsCSV.obtenirPosicioColumna(csv, "any");
        int anyAntic = Integer.parseInt(arrAvatar[posAny]);
        int anyNou = (int) ((Math.random() * (2020 - 1999)) + 1999);
        UtilsCSV.actualitzarLinia(csv, numLiniaAvatar, "any", Integer.toString(anyNou));

        // Escriure els canvis al fitxer CSV
        UtilsCSV.escriure(camiFitxer, csv);
        System.out.println("S'ha canviat l'any \"d'Avatar\", era " + anyAntic + " i s'ha posat " + anyNou);

        // Llistar les dades del CSV
        System.out.println("\nDades del CSV:");
        UtilsCSV.llistar(csv);
    }
}
