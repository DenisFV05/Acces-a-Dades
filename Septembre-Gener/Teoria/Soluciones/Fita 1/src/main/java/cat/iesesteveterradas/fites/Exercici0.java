package cat.iesesteveterradas.fites;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Objectius:
 *   Implementa els mètodes: comprovaExistenciaFitxer, determinaSiEsOcult, obtenirDataUltimaModificacio, verificarEsPotModificar, llistarArxiusDirectori 
 * 
 *   La funcionalitat que s'espera que realitzin és:
 * 
 * - Comprovar si `filePath` existeix.
 * - Determinar si és ocult.
 * - Obtenir la data de la darrera modificació.
 * - Verificar si es pot modificar.
 * - Llistar els arxius del directori pare de `filePath`.
 */

public class Exercici0 {

    private String filePath;

    public static void main(String args[]) {
        Exercici0 exercici = new Exercici0();
        // Configurar la ruta del fitxer
        exercici.configuraRutaFitxer(System.getProperty("user.dir") + "/data/exercici0/Exercici0.dat");
        // Executar la lògica principal
        exercici.executa();
    }

    // Mètode que conté la lògica principal
    public void executa() {
        boolean existeix = comprovaExistenciaFitxer();
        System.out.println("El fitxer existeix: " + existeix);

        if (existeix) {
            boolean ocult = determinaSiEsOcult();
            System.out.println("El fitxer és ocult: " + ocult);

            Date modificat = obtenirDataUltimaModificacio();
            DateFormat formatData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String strModificat = formatData.format(modificat);
            System.out.println("Última modificació: " + strModificat);

            boolean esPotModificar = verificarEsPotModificar();
            System.out.println("El fitxer es pot modificar: " + esPotModificar);
        } else {
            System.out.println("El fitxer no existeix per determinar si és ocult, mostrar la data de l'última modificació o verificar si es pot modificar.");
        }

        ArrayList<String> llistaArxius = llistarArxiusDirectori();
        if (llistaArxius != null) {
            System.out.println("La llista de fitxers d'aquesta carpeta és: " + llistaArxius);
        } else {
            System.out.println("El directori base no existeix o no és un directori.");
        }
    }

    // Mètode per comprovar l'existència del fitxer
    public Boolean comprovaExistenciaFitxer() {
        Path fitxer = Paths.get(filePath);
        if (Files.exists(fitxer)) {
            return true;
        }
        return false;
    }

    // Mètode per determinar si el fitxer és ocult
    public Boolean determinaSiEsOcult() {
        Path fitxer = Paths.get(filePath);
        try {
            if (Files.isHidden(fitxer)) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; 
    }


    // Mètode per obtenir la data de l'última modificació
    public Date obtenirDataUltimaModificacio() {
        Path fitxer = Paths.get(filePath);
        try {
            // Segons internet, es necesita FileTime, i després el return ha d'utilitzar Millis, per què sino no es pot fer el return bé
            FileTime fileTime = Files.getLastModifiedTime(fitxer);
            return new Date(fileTime.toMillis());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Mètode per verificar si el fitxer es pot modificar
    public Boolean verificarEsPotModificar() {
        Path fitxer = Paths.get(filePath);
        if (Files.isWritable(fitxer)) {
            return true;
        }
        return false;
    }


    // Mètode per llistar els fitxers del directori base
    public ArrayList<String> llistarArxiusDirectori() {
        ArrayList<String> llista = new ArrayList<>(); // Hem de crear-lo igualment

        try {
            Path fitxer = Paths.get(filePath);
            Path directoriPare = fitxer.getParent();// Obtenim el directori PARE 
            if (directoriPare != null && Files.isDirectory(directoriPare)) {
                File dir = directoriPare.toFile(); // De path a file, important
                File[] arxius = dir.listFiles();

                if (arxius != null) {
                    for (File f : arxius) {
                        llista.add(f.getName()); // Volem el nom nomès
                    }
                }
                return llista;
            } else {
                return null; // No és un directori vàlid
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    /****************************************************************************/
    /*                          NO CAL MODIFICAR                                */
    /****************************************************************************/
    // Mètode per configurar la ruta del fitxer
    public void configuraRutaFitxer(String filePath) {
        this.filePath = filePath;
    }
}
