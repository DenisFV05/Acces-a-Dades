package com.project.pr13;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.project.pr13.format.PersonaFormatter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * Classe principal que gestiona la lectura i el processament de fitxers XML per obtenir dades de persones.
 */
public class PR130Main {

    private final File dataDir;

    /**
     * Constructor de la classe PR130Main.
     * 
     * @param dataDir Directori on es troben els fitxers de dades.
     */
    public PR130Main(File dataDir) {
        this.dataDir = dataDir;
    }

    /**
     * Mètode principal que inicia l'execució del programa.
     */
    public static void main(String[] args) {
        String userDir = System.getProperty("user.dir");
        File dataDir = new File(userDir, "data" + File.separator + "pr13");

        PR130Main app = new PR130Main(dataDir);
        app.processarFitxerXML("persones.xml");
    }

    /**
     * Processa un fitxer XML per obtenir la informació de les persones i imprimir-la.
     */
    public void processarFitxerXML(String filename) {
        File inputFile = new File(dataDir, filename);
        Document doc = parseXML(inputFile);
        if (doc != null) {
            NodeList persones = doc.getElementsByTagName("persona");
            imprimirCapçaleres();
            imprimirDadesPersones(persones);
        }
    }

    /**
     * Llegeix un fitxer XML i el converteix en un objecte Document.
     */
    public static Document parseXML(File inputFile) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputFile);
            doc.getDocumentElement().normalize();
            return doc;
        } catch (Exception e) {
            System.err.println("Error llegint el fitxer XML: " + e.getMessage());
            return null;
        }
    }

    /**
     * Imprimeix les capçaleres de la taula.
     */
    private void imprimirCapçaleres() {
        System.out.println(PersonaFormatter.getCapçaleres());
    }

    /**
     * Imprimeix totes les persones llegides del fitxer XML.
     */
    private void imprimirDadesPersones(NodeList persones) {
        for (int i = 0; i < persones.getLength(); i++) {
            Element persona = (Element) persones.item(i);

            String nom = persona.getElementsByTagName("nom").item(0).getTextContent();
            String cognom = persona.getElementsByTagName("cognom").item(0).getTextContent();
            String edat = persona.getElementsByTagName("edat").item(0).getTextContent();
            String ciutat = persona.getElementsByTagName("ciutat").item(0).getTextContent();

            System.out.println(PersonaFormatter.formatarPersona(nom, cognom, edat, ciutat));
        }
    }
}
