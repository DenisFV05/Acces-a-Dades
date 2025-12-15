package com.project;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class Main {

    public static void main(String[] args) {

        // Creamos el directorio data si no existe
        String basePath = System.getProperty("user.dir") + "/data/";
        File dir = new File(basePath);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                System.out.println("Error creating 'data' folder");
            }
        }

        // Inicializamos JPA
        Manager.init();

        // CREATE - Ciudades
        Ciutat refCiutat1 = Manager.addCiutat("Vancouver", "Canada", 98661);
        Ciutat refCiutat2 = Manager.addCiutat("Växjö", "Suècia", 35220);
        Ciutat refCiutat3 = Manager.addCiutat("Kyoto", "Japó", 5200461);

        // CREATE - Ciudadanos (sin ciudad aún)
        Ciutada refCiutada1 = Manager.addCiutada("Tony", "Happy", 20, null);
        Ciutada refCiutada2 = Manager.addCiutada("Monica", "Mouse", 22, null);
        Ciutada refCiutada3 = Manager.addCiutada("Eirika", "Erjo", 44, null);
        Ciutada refCiutada4 = Manager.addCiutada("Ven", "Enrison", 48, null);
        Ciutada refCiutada5 = Manager.addCiutada("Akira", "Akiko", 62, null);
        Ciutada refCiutada6 = Manager.addCiutada("Masako", "Kubo", 66, null);

        // READ - Estado inicial
        System.out.println("Punt 1: Després de la creació inicial d'elements");
        Manager.listCiutats();
        Manager.listCiutadans();

        // Asignamos ciudadanos a la primera ciudad
        Set<Ciutada> ciutadansCity1 = new HashSet<>();
        ciutadansCity1.add(refCiutada1);
        ciutadansCity1.add(refCiutada2);
        ciutadansCity1.add(refCiutada3);

        Manager.updateCiutat(
                refCiutat1.getId(),
                refCiutat1.getNom(),
                refCiutat1.getPais(),
                refCiutat1.getPoblacio(),
                ciutadansCity1
        );

        // Asignamos ciudadanos a la segunda ciudad
        Set<Ciutada> ciutadansCity2 = new HashSet<>();
        ciutadansCity2.add(refCiutada4);
        ciutadansCity2.add(refCiutada5);

        Manager.updateCiutat(
                refCiutat2.getId(),
                refCiutat2.getNom(),
                refCiutat2.getPais(),
                refCiutat2.getPoblacio(),
                ciutadansCity2
        );

        // READ - Después de asignar ciudadanos
        System.out.println("Punt 2: Després d'actualitzar ciutats");
        Manager.listCiutats();
        Manager.listCiutadans();

        // UPDATE - Nombres de ciudades
        Manager.updateCiutat(
                refCiutat1.getId(),
                "Vancouver Updated",
                refCiutat1.getPais(),
                refCiutat1.getPoblacio(),
                ciutadansCity1
        );

        Manager.updateCiutat(
                refCiutat2.getId(),
                "Växjö Updated",
                refCiutat2.getPais(),
                refCiutat2.getPoblacio(),
                ciutadansCity2
        );

        // UPDATE - Nombres de ciudadanos
        Manager.updateCiutada(refCiutada1.getId(), "Tony Updated", refCiutada1.getCognom(), refCiutada1.getEdat());
        Manager.updateCiutada(refCiutada4.getId(), "Ven Updated", refCiutada4.getCognom(), refCiutada4.getEdat());

        // READ - Después de actualizar nombres
        System.out.println("Punt 3: Després d'actualització de noms");
        Manager.listCiutats();
        Manager.listCiutadans();

        // DELETE
        Manager.deleteCiutat(refCiutat3.getId());
        Manager.deleteCiutada(refCiutada6.getId());

        // READ - Después de borrado
        System.out.println("Punt 4: després d'esborrat");
        Manager.listCiutats();
        Manager.listCiutadans();

        // READ - Ciudad con ciudadanos
        System.out.println("Punt 5: Recuperació de ciutadans d'una ciutat específica");
        Ciutat ciutat = Manager.getCiutatWithCiutadans(refCiutat1.getId());

        if (ciutat != null) {
            System.out.println("Ciutadans de la ciutat '" + ciutat.getNom() + "':");
            if (ciutat.getCiutadans() != null && !ciutat.getCiutadans().isEmpty()) {
                for (Ciutada c : ciutat.getCiutadans()) {
                    System.out.println("- " + c.getNom() + " " + c.getCognom());
                }
            } else {
                System.out.println("La ciutat no té ciutadans");
            }
        } else {
            System.out.println("No s'ha trobat la ciutat");
        }

        // Cerramos JPA
        Manager.close();
    }
}
