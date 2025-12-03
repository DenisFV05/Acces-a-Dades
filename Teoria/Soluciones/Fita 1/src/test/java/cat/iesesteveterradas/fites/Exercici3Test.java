package cat.iesesteveterradas.fites;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

public class Exercici3Test {

    private Exercici3 exercici;

    @TempDir
    Path tempDir;

    @BeforeEach
    public void setUp() {
        exercici = new Exercici3();
        String tempFilePathXML = tempDir.resolve("Exercici3Test.xml").toString();
        String tempFilePathJson = tempDir.resolve("Exercici3Test.json").toString();
        exercici.configuraPathFitxerSortidaXML(tempFilePathXML);
        exercici.configuraPathFitxerSortidaJson(tempFilePathJson);
    }

    @Test
    public void testExecuta() {
        // Executa la lògica de la classe
        exercici.executa();

        // Comprovar que el fitxer XML s'ha creat
        File fitxerXML = new File(exercici.obtenirRutaFitxerSortidaXML());
        assertTrue(fitxerXML.exists(), "El fitxer XML de sortida no existeix.");

        // Comprovar que el fitxer JSON s'ha creat
        File fitxerJson = new File(exercici.obtenirRutaFitxerSortidaJson());
        assertTrue(fitxerJson.exists(), "El fitxer JSON de sortida no existeix.");

        // Carregar el fitxer XML generat per validar-ne el contingut
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fitxerXML);
            doc.getDocumentElement().normalize();

            // Comprovar l'estructura del document XML
            NodeList llista = doc.getElementsByTagName("llenguatge");
            assertEquals(5, llista.getLength(), "Hi hauria d'haver 5 elements llenguatge en el fitxer XML.");

            // Comprovar el primer llenguatge
            NodeList noms = Exercici3.getNodeList(doc, "//llenguatge/nom");
            assertEquals("C", noms.item(0).getTextContent(), "El primer llenguatge en el fitxer XML hauria de ser C.");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error en analitzar el fitxer XML.");
        }

        // Carregar el fitxer JSON generat per validar-ne el contingut
        try {
            String jsonContent = Files.readString(Path.of(exercici.obtenirRutaFitxerSortidaJson()));
            JSONArray jsonArray = new JSONArray(jsonContent);

            // Comprovar que el JSON té 5 elements
            assertEquals(5, jsonArray.length(), "Hi hauria d'haver 5 elements en el fitxer JSON.");

            // Comprovar el primer element
            JSONObject primerElement = jsonArray.getJSONObject(0);
            assertEquals("C", primerElement.getString("nom"), "El primer llenguatge en el fitxer JSON hauria de ser C.");
            assertEquals("1972", primerElement.getString("any"), "L'any del primer llenguatge en el fitxer JSON hauria de ser 1972.");
            assertEquals(".c", primerElement.getString("extensio"), "L'extensió del primer llenguatge en el fitxer JSON hauria de ser .c.");
            assertEquals("alta", primerElement.getString("dificultat"), "La dificultat del primer llenguatge en el fitxer JSON hauria de ser alta.");
        } catch (IOException e) {
            e.printStackTrace();
            fail("Error en llegir o analitzar el fitxer JSON.");
        }
    }
}

