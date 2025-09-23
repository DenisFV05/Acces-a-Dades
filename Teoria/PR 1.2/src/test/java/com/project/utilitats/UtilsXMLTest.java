package com.project.utilitats;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;

class UtilsXMLTest {

    @Test
    void testLlegirIEscriureXML() throws ParserConfigurationException {
        // Preparació
        String camiBase = System.getProperty("java.io.tmpdir") + "/testData/";
        String camiFitxer = camiBase + "ArxiuTest.xml";

        try {
            // Crear document XML per a la prova
            Document doc = crearDocumentXMLDeProva();

            // Escriure el document XML en un fitxer
            UtilsXML.escriure(camiFitxer, doc);

            // Llegir el document XML del fitxer
            Document docLlegit = UtilsXML.llegir(camiFitxer);
            assertNotNull(docLlegit, "El document llegit no hauria de ser nul");

            // Comprovar que l'arrel és correcte
            Element arrel = docLlegit.getDocumentElement();
            assertEquals("arrel", arrel.getNodeName(), "L'element arrel hauria de ser 'arrel'");

        } finally {
            // Neteja: esborrar el fitxer després del test
            new File(camiFitxer).delete();
        }
    }

    @Test
    void testObtenirTextPerXPath() throws ParserConfigurationException {
        // Preparació
        String camiBase = System.getProperty("java.io.tmpdir") + "/testData/";
        String camiFitxer = camiBase + "ArxiuTest.xml";

        try {
            // Crear document XML per a la prova
            Document doc = crearDocumentXMLDeProva();
            UtilsXML.escriure(camiFitxer, doc);

            // Llegir el document
            Document docLlegit = UtilsXML.llegir(camiFitxer);

            // Utilitzar XPath per obtenir text
            String textElement = UtilsXML.obtenirTextPerXPath(docLlegit, "/arrel/element");
            assertEquals("Valor element", textElement, "El contingut hauria de ser 'Valor element'");

        } finally {
            // Neteja: esborrar el fitxer després del test
            new File(camiFitxer).delete();
        }
    }

    @Test
    void testObtenirPrimerFillPerNom() {
        try {
            // Crear document XML per a la prova
            Document doc = crearDocumentXMLDeProva();
            Element arrel = doc.getDocumentElement();

            // Obtenir el primer fill amb un nom donat
            Element element = UtilsXML.obtenirPrimerFillPerNom(arrel, "element");
            assertNotNull(element, "L'element no hauria de ser nul");
            assertEquals("element", element.getNodeName(), "El nom hauria de ser 'element'");

        } catch (Exception e) {
            fail("No hauria de fallar amb excepció: " + e.getMessage());
        }
    }

    // Mètode auxiliar per crear un document XML senzill
    private Document crearDocumentXMLDeProva() throws ParserConfigurationException {
        // Crear un document XML senzill per a la prova
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.newDocument();

        // Crear elements XML
        Element arrel = doc.createElement("arrel");
        doc.appendChild(arrel);

        Element element = doc.createElement("element");
        element.setTextContent("Valor element");
        arrel.appendChild(element);

        return doc;
    }
}
