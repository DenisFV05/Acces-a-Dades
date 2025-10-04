package com.project;

import com.project.excepcions.IOFitxerExcepcio;
import com.project.utilitats.UtilsCSV;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PR123mainTreballadors {
    private String filePath = System.getProperty("user.dir") + "/data/PR123treballadors.csv";
    private Scanner scanner = new Scanner(System.in);

    // Getter i Setter
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    // Mètode main
    public static void main(String[] args) {
        PR123mainTreballadors programa = new PR123mainTreballadors();
        programa.iniciar();
    }

    // Iniciar programa interactiu
    public void iniciar() {
        boolean sortir = false;

        while (!sortir) {
            try {
                mostrarMenu();
                int opcio = Integer.parseInt(scanner.nextLine());

                switch (opcio) {
                    case 1 -> mostrarTreballadors();
                    case 2 -> modificarTreballadorInteractiu();
                    case 3 -> {
                        System.out.println("Sortint...");
                        sortir = true;
                    }
                    default -> System.out.println("Opció no vàlida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Si us plau, introdueix un número vàlid.");
            } catch (IOFitxerExcepcio e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    // Menú
    private void mostrarMenu() {
        System.out.println("\nMenú de Gestió de Treballadors");
        System.out.println("1. Mostra tots els treballadors");
        System.out.println("2. Modificar dades d'un treballador");
        System.out.println("3. Sortir");
        System.out.print("Selecciona una opció: ");
    }

    // Mostrar treballadors
    public void mostrarTreballadors() throws IOFitxerExcepcio {
        List<String> linies = llegirFitxerCSV();
        System.out.println("\n--- Llista de treballadors ---");
        for (String linia : linies) {
            System.out.println(linia);
        }
    }

    // Modificar treballador interactiu
    public void modificarTreballadorInteractiu() throws IOFitxerExcepcio {
        System.out.print("\nIntrodueix l'ID del treballador que vols modificar: ");
        String id = scanner.nextLine();

        System.out.print("Quina dada vols modificar (Nom, Cognom, Departament, Salari)? ");
        String columna = scanner.nextLine();

        System.out.print("Introdueix el nou valor per a " + columna + ": ");
        String nouValor = scanner.nextLine();

        System.out.print("\nVols guardar els canvis al fitxer? (s/n): ");
        String resposta = scanner.nextLine();
        boolean guardar = resposta.equalsIgnoreCase("s");

        modificarTreballador(id, columna, nouValor, guardar);

        if (guardar) System.out.println("Canvis guardats correctament.");
        else System.out.println("Canvis descartats.");
    }

    // Modificar treballador (versió per tests i usuaris)
    public void modificarTreballador(String id, String columna, String nouValor, boolean guardarDirectament) throws IOFitxerExcepcio {
        List<String> linies = llegirFitxerCSV();

        if (linies.isEmpty()) {
            throw new IOFitxerExcepcio("El fitxer està buit o no conté dades.");
        }

        String[] capcalera = linies.get(0).split(",");
        int indexColumna = -1;

        for (int i = 0; i < capcalera.length; i++) {
            if (capcalera[i].equalsIgnoreCase(columna)) {
                indexColumna = i;
                break;
            }
        }

        if (indexColumna == -1) {
            throw new IOFitxerExcepcio("No s'ha trobat la columna: " + columna);
        }

        boolean trobat = false;
        List<String> novesLinies = new ArrayList<>();
        novesLinies.add(linies.get(0));

        for (int i = 1; i < linies.size(); i++) {
            String[] camps = linies.get(i).split(",");
            if (camps[0].equals(id)) {
                camps[indexColumna] = nouValor;
                trobat = true;
            }
            novesLinies.add(String.join(",", camps));
        }

        if (!trobat) {
            throw new IOFitxerExcepcio("No s'ha trobat cap treballador amb l'ID " + id);
        }

        System.out.println("\n--- Nova taula de treballadors ---");
        novesLinies.forEach(System.out::println);

        if (guardarDirectament) {
            escriureFitxerCSV(novesLinies);
        }
    }

    // Versió simplificada per tests: llama a la de arriba pasando true
    public void modificarTreballador(String id, String columna, String nouValor) throws IOFitxerExcepcio {
        modificarTreballador(id, columna, nouValor, true);
    }

    // Llegir fitxer CSV
    private List<String> llegirFitxerCSV() throws IOFitxerExcepcio {
        List<String> treballadorsCSV = UtilsCSV.llegir(filePath);
        if (treballadorsCSV == null) {
            throw new IOFitxerExcepcio("Error en llegir el fitxer.");
        }
        return treballadorsCSV;
    }

    // Escriure fitxer CSV
    private void escriureFitxerCSV(List<String> treballadorsCSV) throws IOFitxerExcepcio {
        try {
            UtilsCSV.escriure(filePath, treballadorsCSV);
        } catch (Exception e) {
            throw new IOFitxerExcepcio("Error en escriure el fitxer.", e);
        }
    }
}
