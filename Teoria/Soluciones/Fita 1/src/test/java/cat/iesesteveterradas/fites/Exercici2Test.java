package cat.iesesteveterradas.fites;

import cat.iesesteveterradas.fites.objectes.Exercici2nau;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class Exercici2Test {

    private Exercici2 exercici;

    @TempDir
    Path tempDir;

    @BeforeEach
    public void setUp() {
        exercici = new Exercici2();
        // Establim el path temporal per al fitxer de prova
        String tempFilePath = tempDir.resolve("Exercici2Test.dat").toString();
        exercici.configurarRutaFitxerSortida(tempFilePath);
    }

    @Test
    public void testExecuta() {
        // Executar la lògica de la classe
        exercici.executa();

        // Comprovar que el fitxer existeix
        File fitxerSortida = new File(exercici.obtenirRutaFitxerSortida());
        assertTrue(fitxerSortida.exists(), "El fitxer de sortida no existeix.");

        // Deserialitzar i comprovar el contingut
        ArrayList<Exercici2nau> llista = exercici.deserialitzaLlista(exercici.obtenirRutaFitxerSortida());

        assertNotNull(llista, "La llista no hauria de ser nul·la.");
        assertEquals(8, llista.size(), "La llista hauria de tenir 8 elements.");

        // Comprovació d'alguns valors
        assertEquals(new Exercici2nau("Vostok", "USSR", 1961), llista.get(0));
        assertEquals(new Exercici2nau("Crew Dragon", "US", 2020), llista.get(7));
    }
}
