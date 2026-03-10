package com.project.pr13;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.project.pr13.format.AsciiTablePrinter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Classe principal que permet gestionar un fitxer XML de cursos amb opcions per llistar, afegir i eliminar alumnes, 
 * així com mostrar informació dels cursos i mòduls.
 */
public class PR132Main {

    private final Path xmlFilePath;
    private static final Scanner scanner = new Scanner(System.in);

    public PR132Main(Path xmlFilePath) {
        this.xmlFilePath = xmlFilePath;
    }

    public static void main(String[] args) {
        String userDir = System.getProperty("user.dir");
        Path xmlFilePath = Path.of(userDir, "data", "pr13", "cursos.xml");

        PR132Main app = new PR132Main(xmlFilePath);
        app.executar();
    }

    public void executar() {
        boolean exit = false;
        while (!exit) {
            mostrarMenu();
            System.out.print("Escull una opció: ");
            int opcio = scanner.nextInt();
            scanner.nextLine(); // Netegem buffer
            exit = processarOpcio(opcio);
        }
    }

    public boolean processarOpcio(int opcio) {
        String cursId, nomAlumne;
        switch (opcio) {
            case 1:
                imprimirTaulaCursos(llistarCursos());
                return false;
            case 2:
                System.out.print("Introdueix l'ID del curs per veure els seus mòduls: ");
                cursId = scanner.nextLine();
                imprimirTaulaModuls(mostrarModuls(cursId));
                return false;
            case 3:
                System.out.print("Introdueix l'ID del curs per veure la llista d'alumnes: ");
                cursId = scanner.nextLine();
                imprimirLlistaAlumnes(llistarAlumnes(cursId));
                return false;
            case 4:
                System.out.print("Introdueix l'ID del curs on vols afegir l'alumne: ");
                cursId = scanner.nextLine();
                System.out.print("Introdueix el nom complet de l'alumne a afegir: ");
                nomAlumne = scanner.nextLine();
                afegirAlumne(cursId, nomAlumne);
                return false;
            case 5:
                System.out.print("Introdueix l'ID del curs on vols eliminar l'alumne: ");
                cursId = scanner.nextLine();
                System.out.print("Introdueix el nom complet de l'alumne a eliminar: ");
                nomAlumne = scanner.nextLine();
                eliminarAlumne(cursId, nomAlumne);
                return false;
            case 6:
                System.out.println("Sortint del programa...");
                return true;
            default:
                System.out.println("Opció no reconeguda. Si us plau, prova de nou.");
                return false;
        }
    }

    private void mostrarMenu() {
        System.out.println("\nMENÚ PRINCIPAL");
        System.out.println("1. Llistar IDs de cursos i tutors");
        System.out.println("2. Mostrar IDs i títols dels mòduls d'un curs");
        System.out.println("3. Llistar alumnes d’un curs");
        System.out.println("4. Afegir un alumne a un curs");
        System.out.println("5. Eliminar un alumne d'un curs");
        System.out.println("6. Sortir");
    }

    public List<List<String>> llistarCursos() {
        List<List<String>> llistaCursos = new ArrayList<>();
        try {
            Document doc = carregarDocumentXML(xmlFilePath);
            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList nodes = (NodeList) xpath.evaluate("/cursos/curs", doc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++) {
                Element curs = (Element) nodes.item(i);
                String id = curs.getAttribute("id");
                String tutor = xpath.evaluate("tutor", curs);
                NodeList alumnesNodes = (NodeList) xpath.evaluate("alumnes/alumne", curs, XPathConstants.NODESET);
                String totalAlumnes = String.valueOf(alumnesNodes.getLength());
                llistaCursos.add(List.of(id, tutor, totalAlumnes));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return llistaCursos;
    }

    public void imprimirTaulaCursos(List<List<String>> cursos) {
        List<String> capçaleres = List.of("ID", "Tutor", "Total Alumnes");
        AsciiTablePrinter.imprimirTaula(capçaleres, cursos);
    }

    public List<List<String>> mostrarModuls(String idCurs) {
        List<List<String>> moduls = new ArrayList<>();
        try {
            Document doc = carregarDocumentXML(xmlFilePath);
            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList nodes = (NodeList) xpath.evaluate("/cursos/curs[@id='" + idCurs + "']/moduls/modul", doc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++) {
                Element modul = (Element) nodes.item(i);
                String id = modul.getAttribute("id");
                String titol = xpath.evaluate("titol", modul);
                moduls.add(List.of(id, titol));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return moduls;
    }

    public void imprimirTaulaModuls(List<List<String>> moduls) {
        List<String> capçaleres = List.of("ID Mòdul", "Títol");
        AsciiTablePrinter.imprimirTaula(capçaleres, moduls);
    }

    public List<String> llistarAlumnes(String idCurs) {
        List<String> alumnes = new ArrayList<>();
        try {
            Document doc = carregarDocumentXML(xmlFilePath);
            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList nodes = (NodeList) xpath.evaluate("/cursos/curs[@id='" + idCurs + "']/alumnes/alumne", doc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++) {
                alumnes.add(nodes.item(i).getTextContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return alumnes;
    }

    public void imprimirLlistaAlumnes(List<String> alumnes) {
        System.out.println("Alumnes:");
        alumnes.forEach(alumne -> System.out.println("- " + alumne));
    }

    public void afegirAlumne(String idCurs, String nomAlumne) {
        try {
            Document doc = carregarDocumentXML(xmlFilePath);
            XPath xpath = XPathFactory.newInstance().newXPath();
            Node alumnesNode = (Node) xpath.evaluate("/cursos/curs[@id='" + idCurs + "']/alumnes", doc, XPathConstants.NODE);
            if (alumnesNode != null) {
                Element nouAlumne = doc.createElement("alumne");
                nouAlumne.setTextContent(nomAlumne);
                alumnesNode.appendChild(nouAlumne);
                guardarDocumentXML(doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminarAlumne(String idCurs, String nomAlumne) {
        try {
            Document doc = carregarDocumentXML(xmlFilePath);
            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList nodes = (NodeList) xpath.evaluate("/cursos/curs[@id='" + idCurs + "']/alumnes/alumne[text()='" + nomAlumne + "']", doc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++) {
                Node alumne = nodes.item(i);
                alumne.getParentNode().removeChild(alumne);
            }
            guardarDocumentXML(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Document carregarDocumentXML(Path pathToXml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(pathToXml.toFile());
        } catch (Exception e) {
            throw new RuntimeException("Error en carregar el document XML.", e);
        }
    }

    private void guardarDocumentXML(Document document) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(xmlFilePath.toFile());
            transformer.transform(source, result);
            System.out.println("El fitxer XML ha estat guardat amb èxit.");
        } catch (TransformerException e) {
            System.out.println("Error en guardar el fitxer XML.");
            e.printStackTrace();
        }
    }
}
