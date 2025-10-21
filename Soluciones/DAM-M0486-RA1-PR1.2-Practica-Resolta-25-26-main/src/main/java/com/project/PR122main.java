package com.project;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import com.project.excepcions.IOFitxerExcepcio;
import com.project.objectes.PR122persona;

public class PR122main {
    private static String filePath = System.getProperty("user.dir") + "/data/PR122persones.dat";

    public static void main(String[] args) {
        List<PR122persona> persones = new ArrayList<>();
        persones.add(new PR122persona("Maria", "López", 36));
        persones.add(new PR122persona("Gustavo", "Ponts", 63));
        persones.add(new PR122persona("Irene", "Sales", 54));

        try {
            serialitzarPersones(persones);
            List<PR122persona> deserialitzades = deserialitzarPersones();
            deserialitzades.forEach(System.out::println);  // Mostra la informació per pantalla
        } catch (IOFitxerExcepcio e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // Mètode per serialitzar la llista de persones
    public static void serialitzarPersones(List<PR122persona> persones) throws IOFitxerExcepcio {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(persones);
        } catch (IOException e) {
            throw new IOFitxerExcepcio("Error en serialitzar les persones a l'arxiu", e);
        }
    }

    // Mètode per deserialitzar la llista de persones
    public static List<PR122persona> deserialitzarPersones() throws IOFitxerExcepcio {
        try (FileInputStream fis = new FileInputStream(filePath);
            ObjectInputStream ois = new ObjectInputStream(fis)) {
            
            Object deserialitzat = ois.readObject(); // Llegeix l'objecte des del fitxer
            
            // Comprovem que és una llista
            if (deserialitzat instanceof List<?>) {
                List<?> tempList = (List<?>) deserialitzat;
                List<PR122persona> persones = new ArrayList<>();
                
                // Iterem per cada element per assegurar-nos que és del tipus PR122persona
                for (Object obj : tempList) {
                    if (obj instanceof PR122persona) {
                        persones.add((PR122persona) obj);
                    } else {
                        throw new IOFitxerExcepcio("L'objecte deserialitzat conté un element que no és de tipus PR122persona.");
                    }
                }
                return persones;  // Retornem la llista segura de PR122persona
            } else {
                throw new IOFitxerExcepcio("L'objecte deserialitzat no és una llista.");
            }
            
        } catch (FileNotFoundException e) {
            throw new IOFitxerExcepcio("Fitxer no trobat: " + filePath, e);
        } catch (IOException | ClassNotFoundException e) {
            throw new IOFitxerExcepcio("Error en deserialitzar les persones de l'arxiu", e);
        }
    }


    // Getter i Setter per a filePath (opcional)
    public static String getFilePath() {
        return filePath;
    }

    public static void setFilePath(String newFilePath) {
        filePath = newFilePath;
    }
}
