package com.project.pr13;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

/**
 * Classe principal que crea un document XML amb informació de llibres i el guarda en un fitxer.
 */
public class PR131Main {

    private File dataDir;

    public PR131Main(File dataDir) {
        this.dataDir = dataDir;
    }

    public File getDataDir() {
        return dataDir;
    }

    public void setDataDir(File dataDir) {
        this.dataDir = dataDir;
    }

    public static void main(String[] args) {
        String userDir = System.getProperty("user.dir");
        File dataDir = new File(userDir, "data" + File.separator + "pr13");

        PR131Main app = new PR131Main(dataDir);
        app.processarFitxerXML("biblioteca.xml");
    }

    public void processarFitxerXML(String filename) {
        if (comprovarIDirCrearDirectori(dataDir)) {
            Document doc = construirDocument();
            File fitxerSortida = new File(dataDir, filename);
            guardarDocument(doc, fitxerSortida);
        }
    }

    private boolean comprovarIDirCrearDirectori(File directori) {
        if (!directori.exists()) {
            return directori.mkdirs();
        }
        return true;
    }

    private static Document construirDocument() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            // Crear elemento raíz <biblioteca>
            Element biblioteca = doc.createElement("biblioteca");
            doc.appendChild(biblioteca);

            // Crear elemento <llibre> con atributo id="001"
            Element llibre = doc.createElement("llibre");
            llibre.setAttribute("id", "001");
            biblioteca.appendChild(llibre);

            // Agregar elementos del libro
            Element titol = doc.createElement("titol");
            titol.setTextContent("El viatge dels venturons");
            llibre.appendChild(titol);

            Element autor = doc.createElement("autor");
            autor.setTextContent("Joan Pla");
            llibre.appendChild(autor);

            Element anyPublicacio = doc.createElement("anyPublicacio");
            anyPublicacio.setTextContent("1998");
            llibre.appendChild(anyPublicacio);

            Element editorial = doc.createElement("editorial");
            editorial.setTextContent("Edicions Mar");
            llibre.appendChild(editorial);

            Element genere = doc.createElement("genere");
            genere.setTextContent("Aventura");
            llibre.appendChild(genere);

            Element pagines = doc.createElement("pagines");
            pagines.setTextContent("320");
            llibre.appendChild(pagines);

            Element disponible = doc.createElement("disponible");
            disponible.setTextContent("true");
            llibre.appendChild(disponible);

            return doc;
        } catch (Exception e) {
            System.err.println("Error construint el document XML: " + e.getMessage());
            return null;
        }
    }

    private static void guardarDocument(Document doc, File fitxerSortida) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            // Formatear el XML con sangría
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(fitxerSortida);

            transformer.transform(source, result);
            System.out.println("XML creat correctament a: " + fitxerSortida.getAbsolutePath());

        } catch (TransformerException e) {
            System.err.println("Error guardant el fitxer XML: " + e.getMessage());
        }
    }
}
