package cat.iesesteveterradas.fites;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class Exercici0Test {

    private Exercici0 exercici;
    private String filePath;

    @BeforeEach
    public void setUp() {
        exercici = new Exercici0();
        filePath = System.getProperty("user.dir") + "/data/exercici0/Exercici0.dat";
        exercici.configuraRutaFitxer(filePath);
    }

    @Test
    public void testComprovaExistenciaFitxer() {
        File fitxer = new File(filePath);
        boolean expected = fitxer.exists();
        boolean actual = exercici.comprovaExistenciaFitxer();
        assertEquals(expected, actual, "El mètode comprovaExistenciaFitxer ha de retornar si el fitxer existeix.");
    }

    @Test
    public void testDeterminaSiEsOcult() {
        File fitxer = new File(filePath);
        boolean expected = fitxer.isHidden();
        boolean actual = exercici.determinaSiEsOcult();
        assertEquals(expected, actual, "El mètode determinaSiEsOcult ha de retornar si el fitxer és ocult.");
    }

    @Test
    public void testObtenirDataUltimaModificacio() {
        File fitxer = new File(filePath);
        Date expected = new Date(fitxer.lastModified());
        Date actual = exercici.obtenirDataUltimaModificacio();
        assertEquals(expected, actual, "El mètode obtenirDataUltimaModificacio ha de retornar la data de l'última modificació.");
    }

    @Test
    public void testVerificarEsPotModificar() {
        File fitxer = new File(filePath);
        boolean expected = fitxer.canWrite();
        boolean actual = exercici.verificarEsPotModificar();
        assertEquals(expected, actual, "El mètode verificarEsPotModificar ha de retornar si el fitxer es pot modificar.");
    }

    @Test
    public void testLlistarArxiusDirectori() {
        File directoriBase = new File(filePath).getParentFile();
        ArrayList<String> expected = new ArrayList<>();
        if (directoriBase.exists() && directoriBase.isDirectory()) {
            File[] fitxers = directoriBase.listFiles();
            if (fitxers != null) {
                for (File f : fitxers) {
                    expected.add(f.getName());
                }
            }
        } else {
            expected = null;
        }
        ArrayList<String> actual = exercici.llistarArxiusDirectori();
        assertEquals(expected, actual, "El mètode llistarArxiusDirectori ha de retornar la llista d'arxius del directori base.");
    }
}
