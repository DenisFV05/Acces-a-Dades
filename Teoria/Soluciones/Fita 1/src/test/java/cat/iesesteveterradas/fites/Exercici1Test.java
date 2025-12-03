package cat.iesesteveterradas.fites;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Exercici1Test {

    private Exercici1 exercici;
    private File fileEntrada;
    private File fileSortida;

    @TempDir
    Path tempDir;

    @BeforeEach
    public void setUp() {
        exercici = new Exercici1();
        try {
            // Crear fitxer d'entrada al directori temporal
            fileEntrada = new File(tempDir.toFile(), "entradaTest.txt");
            try (FileWriter writer = new FileWriter(fileEntrada, StandardCharsets.UTF_8)) {
                writer.write("Aquesta és la primera línia.\n");
                writer.write("Aquesta és la segona línia.\n");
            }

            // Establir el fitxer d'entrada i sortida en Exercici1
            fileSortida = new File(tempDir.toFile(), "sortidaTest.txt");
            exercici.configuraRutaFitxerEntrada(fileEntrada.getAbsolutePath());
            exercici.configuraRutaFitxerSortida(fileSortida.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            fail("No s'ha pogut configurar el fitxer d'entrada per al test.");
        }
    }

    @Test
    public void testExecuta() {
        exercici.executa();

        // Comprova que el fitxer de sortida existeix
        assertTrue(fileSortida.exists(), "El fitxer de sortida hauria de ser creat.");

        // Llegeix el contingut del fitxer de sortida
        try {
            List<String> liniesSortida = Files.readAllLines(fileSortida.toPath(), StandardCharsets.UTF_8);
            assertEquals(2, liniesSortida.size(), "El fitxer de sortida hauria de tenir dues línies.");

            // Comprova que cada línia de sortida és la inversa de cada línia d'entrada
            assertEquals(Exercici1.giraText("Aquesta és la primera línia."), liniesSortida.get(0), 
                         "La primera línia del fitxer de sortida hauria de ser la inversa de la primera línia del fitxer d'entrada.");
            assertEquals(Exercici1.giraText("Aquesta és la segona línia."), liniesSortida.get(1), 
                         "La segona línia del fitxer de sortida hauria de ser la inversa de la segona línia del fitxer d'entrada.");
        } catch (IOException e) {
            fail("No s'ha pogut llegir el fitxer de sortida.");
        }
    }

    @Test
    public void testGiraText() {
        String text = "Hola món";
        String esperat = "nóm aloH";
        String actual = Exercici1.giraText(text);
        assertEquals(esperat, actual, "El mètode giraText hauria d'invertir el text correctament.");
    }
}
