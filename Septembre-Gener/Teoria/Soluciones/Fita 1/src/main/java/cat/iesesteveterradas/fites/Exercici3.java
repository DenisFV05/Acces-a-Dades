package cat.iesesteveterradas.fites;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonWriter;
import jakarta.json.JsonWriterFactory;
import jakarta.json.stream.JsonGenerator;


/**
 * Implementa el codi que realitzi el següent:
 * 
 * - Genera documents XML i JSON a partir de la informació sobre llenguatges de programació continguda en una llista.
 * - Guarda l'estructura XML i JSON generada en fitxers a les rutes especificades amb els noms 'Exercici3.xml' i 'Exercici3.json'.
 * - Per crear el fitxer JSON cal usar Jakarta.
 * - Gestió d'errors: si hi ha algun problema en escriure o generar els fitxers, mostra l'excepció a la consola.
 
 Documents esperats (el contingut és important, el format bonic no es te en compte):

 <?xml version="1.0" encoding="UTF-8" standalone="no"?>
<llista>
    <llenguatge dificultat="alta" extensio=".c">
        <nom>C</nom>
        <any>1972</any>
    </llenguatge>
    <llenguatge dificultat="mitjana" extensio=".java">
        <nom>Java</nom>
        <any>1995</any>
    </llenguatge>
    <llenguatge dificultat="baixa" extensio=".js">
        <nom>Javascript</nom>
        <any>1995</any>
    </llenguatge>
    <llenguatge dificultat="baixa" extensio=".m">
        <nom>Objective C</nom>
        <any>1984</any>
    </llenguatge>
    <llenguatge dificultat="mitjana" extensio=".dart">
        <nom>Dart</nom>
        <any>2011</any>
    </llenguatge>
</llista>


[
    {
        "nom": "C",
        "any": "1972",
        "extensio": ".c",
        "dificultat": "alta"
    },
    {
        "nom": "Java",
        "any": "1995",
        "extensio": ".java",
        "dificultat": "mitjana"
    },
    {
        "nom": "Javascript",
        "any": "1995",
        "extensio": ".js",
        "dificultat": "baixa"
    },
    {
        "nom": "Objective C",
        "any": "1984",
        "extensio": ".m",
        "dificultat": "baixa"
    },
    {
        "nom": "Dart",
        "any": "2011",
        "extensio": ".dart",
        "dificultat": "mitjana"
    }
]
*/

public class Exercici3 {
    private String filePathXML;
    private String filePathJson;

    public static void main(String[] args) {
        String basePath = System.getProperty("user.dir") + "/data/exercici3/";
        String filePathXML = basePath + "Exercici3.xml";
        String filePathJson = basePath + "Exercici3.json";

        Exercici3 exercici = new Exercici3();
        exercici.configuraPathFitxerSortidaXML(filePathXML);
        exercici.configuraPathFitxerSortidaJson(filePathJson);
        exercici.executa();
    }

    // Mètode que executa la lògica de la classe
    public void executa() {
        ArrayList<String[]> llista = new ArrayList<>();
        llista.add(new String[]{"C", "1972", ".c", "alta"});
        llista.add(new String[]{"Java", "1995", ".java", "mitjana"});
        llista.add(new String[]{"Javascript", "1995", ".js", "baixa"});
        llista.add(new String[]{"Objective C", "1984", ".m", "baixa"});
        llista.add(new String[]{"Dart", "2011", ".dart", "mitjana"});

        // Crear i guardar el document XML
        Document doc = crearDocumentXML(llista);
        guardarXML(filePathXML, doc);

        // Crear i guardar el document JSON
        guardarJSON(filePathJson, llista);
    }

    // Mètode que crea el document XML a partir de la llista proporcionada
    private Document crearDocumentXML(ArrayList<String[]> llista) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.newDocument();

            Element root = doc.createElement("llista");
            doc.appendChild(root);
            for (String[] dades : llista) {
                Element llenguatge = doc.createElement("llenguatge");
                llenguatge.setAttribute("dificultat", dades[3]);
                llenguatge.setAttribute("extensio", dades[2]);

                Element nom = doc.createElement("nom");
                nom.setTextContent(dades[0]);
                Element any = doc.createElement("any");
                any.setTextContent(dades[1]);

                llenguatge.appendChild(nom);
                llenguatge.appendChild(any);

                root.appendChild(llenguatge);
            }

            return doc;

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }
    }


    // Escriu un Document en un fitxer XML
    public static void guardarXML(String path, Document doc) {
        try {
            // Crear el directori si no existeix
            File fitxer = new File(path);
            fitxer.getParentFile().mkdirs();

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "no");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(fitxer);
            transformer.transform(source, result);

            System.out.println("Fitxer XML creat correctament: " + path);

        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }


    // Escriu la llista en un fitxer JSON utilitzant Jakarta
    public void guardarJSON(String path, ArrayList<String[]> llista) {
        try {
            // Crear el directori si no existeix
            File fitxer = new File(path);
            fitxer.getParentFile().mkdirs();

            // Crear el JSON
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

            for (String[] dades : llista) {
                JsonObjectBuilder obj = Json.createObjectBuilder()
                        .add("nom", dades[0])
                        .add("any", dades[1])
                        .add("extensio", dades[2])
                        .add("dificultat", dades[3]);
                arrayBuilder.add(obj);
            }

            // Per que surti més bonic 
            Map<String, Object> config = new HashMap<>();
            config.put(JsonGenerator.PRETTY_PRINTING, true);
            JsonWriterFactory writerFactory = Json.createWriterFactory(config);

            // Escriure el fitxer JSON
            try (FileWriter fw = new FileWriter(fitxer);
                JsonWriter jsonWriter = writerFactory.createWriter(fw)) {
                jsonWriter.writeArray(arrayBuilder.build());
            }

            System.out.println("Fitxer JSON creat correctament: " + path);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /****************************************************************************/
    /*                              NO CAL TOCAR                                */
    /****************************************************************************/
    // Retorna els nodes d'una expressió XPath
    public static NodeList getNodeList(Document doc, String expression) {
        NodeList llista = null;
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            llista = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return llista;
    }

    // Setter per definir el path XML
    public void configuraPathFitxerSortidaXML(String filePath) {
        this.filePathXML = filePath;
    }

    // Setter per definir el path JSON
    public void configuraPathFitxerSortidaJson(String filePath) {
        this.filePathJson = filePath;
    }

    // Getter per obtenir el path XML
    public String obtenirRutaFitxerSortidaXML() {
        return this.filePathXML;
    }

    // Getter per obtenir el path JSON
    public String obtenirRutaFitxerSortidaJson() {
        return this.filePathJson;
    }
}
