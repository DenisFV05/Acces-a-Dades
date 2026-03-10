package cat.iesesteveterradas.fites;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Implementa codi que compleixi el següent:
 * 
 * - Llegeix un fitxer de text d'entrada amb codificació UTF-8.
 * - Inverteix el text de cada línia utilitzant el mètode 'giraText'.
 * - Escriu les línies invertides en un fitxer de sortida amb codificació UTF-8.
 * - El fitxer de sortida es crea o sobrescriu si ja existeix.
 * - Si hi ha un error en llegir o escriure els fitxers, l'excepció es mostra a la consola.
 */
public class Exercici1 {

    private String filePathIn;
    private String filePathOut;


    public static void main(String args[]) {
        Exercici1 exercici = new Exercici1();
        // Configurar els fitxers d'entrada i sortida
        exercici.configuraRutaFitxerEntrada(System.getProperty("user.dir") + "/data/exercici1/Exercici1Entrada.txt");
        exercici.configuraRutaFitxerSortida(System.getProperty("user.dir") + "/data/exercici1/Exercici1Solucio.txt");
        // Executar la lògica principal
        exercici.executa();
    }    

    // Processa el fitxer d'entrada i genera el fitxer de sortida.
    public void executa() {
        File fitxerEntrada = new File(filePathIn);
        File fitxerSortida = new File(filePathOut);

        // Crear el directori si no existeix
        fitxerSortida.getParentFile().mkdirs();

        try (
            Scanner scanner = new Scanner(fitxerEntrada, StandardCharsets.UTF_8);
            FileWriter writer = new FileWriter(fitxerSortida, StandardCharsets.UTF_8)
        ) {
            while (scanner.hasNextLine()) {
                String linia = scanner.nextLine();
                String liniaInvertida = giraText(linia);
                writer.write(liniaInvertida + System.lineSeparator());
            }

            System.out.println("Fitxer processat correctament. Resultat a: " + filePathOut);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Mètode per invertir el text d'una línia
    public static String giraText(String text) {
        return new StringBuilder(text).reverse().toString();
    }


    /****************************************************************************/
    /*                          NO CAL MODIFICAR                                */
    /****************************************************************************/
    // Mètode per configurar el fitxer d'entrada
    public void configuraRutaFitxerEntrada(String filePathIn) {
        this.filePathIn = filePathIn;
    }

    // Mètode per configurar el fitxer de sortida
    public void configuraRutaFitxerSortida(String filePathOut) {
        this.filePathOut = filePathOut;
    }
}
