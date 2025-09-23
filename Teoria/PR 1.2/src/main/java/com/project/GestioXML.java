package com.project;

import java.io.File;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.project.utilitats.UtilsXML;

// En aquest exemple es fa servir UtilsXML per llegir i modificar les dades d'un XML

public class GestioXML {
    public static void main(String[] args) {
        String camiBase = System.getProperty("user.dir") + "/data/";
        String nomFitxer = "GestioXML.xml";
        String camiFitxer = camiBase + nomFitxer;

        // Crear la carpeta 'data' si no existeix
        File carpeta = new File(camiBase);
        if (!carpeta.exists()) {
            if (!carpeta.mkdirs()) {
                System.out.println("Error en la creació de la carpeta 'data'");
            }
        }

        System.out.println("");

        Document doc = UtilsXML.llegir(camiFitxer);

        // Exemple d'agafar un text a partir d'un XPath
        String nomId3 = UtilsXML.obtenirTextPerXPath(doc, "/menu/food[@id='id_03']/name");
        System.out.println("El nom del menjar amb (id=\"id_03\") és: " + nomId3);

        // Exemple d'agafar un element a partir d'un XPath (i llegir-ne un atribut i el seu text)
        Element elementId4Preu = UtilsXML.obtenirPrimerElementPerXpath(doc, "/menu/food[@id='id_04']/price");
        String atributMonedaId4 = elementId4Preu.getAttribute("coin");
        String textMonedaId4 = elementId4Preu.getTextContent();
        System.out.println("El nom de la moneda amb (id=\"id_04\") és \"" + atributMonedaId4 + "\" i el preu és " + textMonedaId4);

        // Exemple de llistar tots els elements d'un XPath
        System.out.println("Llista de menjars:");
        NodeList llistaMenjars = UtilsXML.obtenirLlistaNodes(doc, "/menu/food");
        imprimirLlistaMenjars(llistaMenjars);

        // Exemple de llistar tots els elements amb un filtre d'atribut
        System.out.println("Llista de menjars amb veggy igual a 'true':");
        NodeList llistaMenjarsVeggy = UtilsXML.obtenirLlistaNodes(doc, "/menu/food[@veggy='true']");
        imprimirLlistaMenjars(llistaMenjarsVeggy);

        // Exemple de llistar tots els elements amb un filtre de text
        System.out.println("Llista de menjars amb name que conté 'Belgian':");
        NodeList llistaMenjarsBelgian = UtilsXML.obtenirLlistaNodes(doc, "/menu/food/name[contains(text(), 'Belgian')]/parent::food");
        imprimirLlistaMenjars(llistaMenjarsBelgian);

        // Exemple de llistar tots els elements amb un filtre d'atribut a un fill
        System.out.println("Llista de menjars que es paguen amb 'dollar':");
        NodeList llistaMenjarsDollar = UtilsXML.obtenirLlistaNodes(doc, "/menu/food/price[@coin='dollar']/parent::food");
        imprimirLlistaMenjars(llistaMenjarsDollar);

        // Exemple de modificar un element
        Element elementId2Calories = UtilsXML.obtenirPrimerElementPerXpath(doc, "/menu/food[@id='id_02']/calories");
        int caloriesAntigues = Integer.parseInt(elementId2Calories.getTextContent());
        int caloriesNoves = (int) ((Math.random() * (999 - 500)) + 500);
        elementId2Calories.setTextContent(Integer.toString(caloriesNoves));
        UtilsXML.escriure(camiFitxer, doc);
        System.out.println("S'han canviat les calories de (id)=\"id_02\", eren " + caloriesAntigues + " i s'ha posat " + caloriesNoves);
    }

    // Mètode auxiliar per imprimir la llista de menjars
    static void imprimirLlistaMenjars(NodeList llista) {
        for (int i = 0; i < llista.getLength(); i++) {
            Node nodeMenjar = llista.item(i);
            if (nodeMenjar.getNodeType() == Node.ELEMENT_NODE) {
                // Si és de tipus "ELEMENT_NODE", es fa el cast a Element
                Element elementMenjar = (Element) nodeMenjar;
                String atributId = elementMenjar.getAttribute("id");
                Element elementNom = UtilsXML.obtenirPrimerFillPerNom(elementMenjar, "name");
                String textNom = elementNom.getTextContent();
                System.out.println(" - " + atributId + " : " + textNom);
            }
        }
    }
}
