package com.project.utilitats;

import java.io.File;
import java.io.IOException;
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
import org.xml.sax.SAXException;

public class UtilsXML {

    // Llegeix un fitxer XML i el retorna com un Document W3C
    public static Document llegir(String camiFitxer) {
        Document doc = null;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            File fitxer = new File(camiFitxer);
            doc = dBuilder.parse(fitxer);
            doc.getDocumentElement().normalize();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    // Desa un Document XML en un fitxer
    public static void escriure(String cami, Document doc) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            eliminarEspaisInnecessaris(doc);
            DOMSource source = new DOMSource(doc);
            StreamResult resultat = new StreamResult(new File(cami));
            transformer.transform(source, resultat);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    // Elimina salts de línia innecessaris d'un Document XML (per millorar-ne la llegibilitat)
    public static void eliminarEspaisInnecessaris(Node node) {
        NodeList fills = node.getChildNodes();
        for (int i = 0; i < fills.getLength(); ++i) {
            Node fill = fills.item(i);
            if (fill.getNodeType() == Node.TEXT_NODE) {
                fill.setTextContent(fill.getTextContent().trim());
            }
            eliminarEspaisInnecessaris(fill);
        }
    }

    // Retorna els nodes d'una expressió XPath
    public static NodeList obtenirLlistaNodes(Document doc, String expressio) {
        NodeList llista = null;
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            llista = (NodeList) xPath.compile(expressio).evaluate(doc, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return llista;
    }

    // Retorna el primer element d'una llista de nodes
    public static Element obtenirPrimerElement(NodeList llista) {
        Element resultat = null;
        Node primer = llista.item(0);
        if (primer != null && primer.getNodeType() == Node.ELEMENT_NODE) {
            resultat = (Element) primer;
        } else {
            for (int i = 0; i < llista.getLength(); i++) {
                Node node = llista.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    resultat = (Element) node;
                    break;
                }
            }
        }
        return resultat;
    }

    // Retorna el primer element d'una expressió XPath
    public static Element obtenirPrimerElementPerXpath(Document doc, String expressio) {
        NodeList llista = obtenirLlistaNodes(doc, expressio);
        return obtenirPrimerElement(llista);
    }

    // Retorna el contingut de text d'una expressió XPath
    public static String obtenirTextPerXPath(Document doc, String expressio) {
        NodeList llista = obtenirLlistaNodes(doc, expressio);
        Element element = obtenirPrimerElement(llista);
        return element != null ? element.getTextContent() : null;
    }

    // Retorna el primer fill amb un nom donat d'un element pare
    public static Element obtenirPrimerFillPerNom(Element pare, String nomFill) {
        NodeList llista = pare.getElementsByTagName(nomFill);
        return obtenirPrimerElement(llista);
    }
}
