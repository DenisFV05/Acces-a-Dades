package com.project.pr14;

import jakarta.json.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.project.objectes.Llibre;

/**
 * Classe principal que gestiona la lectura i el processament de fitxers JSON per obtenir dades de llibres.
 */
public class PR14GestioLlibreriaJakartaMain {

    private final File dataFile;

    /**
     * Constructor de la classe PR14GestioLlibreriaJSONPMain.
     *
     * @param dataFile Fitxer on es troben els llibres.
     */
    public PR14GestioLlibreriaJakartaMain(File dataFile) {
        this.dataFile = dataFile;
    }

    public static void main(String[] args) {
        File dataFile = new File(System.getProperty("user.dir"), "data/pr14" + File.separator + "llibres_input.json");
        PR14GestioLlibreriaJakartaMain app = new PR14GestioLlibreriaJakartaMain(dataFile);
        app.processarFitxer();
    }

    /**
     * Processa el fitxer JSON per carregar, modificar, afegir, esborrar i guardar les dades dels llibres.
     */
    public void processarFitxer() {
        List<Llibre> llibres = carregarLlibres();
        if (llibres != null) {
            modificarAnyPublicacio(llibres, 1, 1995);
            afegirNouLlibre(llibres, new Llibre(4, "Històries de la ciutat", "Miquel Soler", 2022));
            esborrarLlibre(llibres, 2);
            guardarLlibres(llibres);
        }
    }

    /**
     * Carrega els llibres des del fitxer JSON.
     *
     * @return Llista de llibres o null si hi ha hagut un error en la lectura.
     */
    public List<Llibre> carregarLlibres() {
        List<Llibre> llibres = new ArrayList<>();
        try (InputStream fis = new FileInputStream(dataFile);
            JsonReader reader = Json.createReader(fis)) { // Segun he encontrado, en try with resources esto se puede hacer
            
            JsonArray jsonArray = reader.readArray();
            for (JsonValue valor : jsonArray) {
                JsonObject obj = valor.asJsonObject();
                int id = obj.getInt("id");
                String titol = obj.getString("titol");
                String autor = obj.getString("autor");
                int any = obj.getInt("any");
                llibres.add(new Llibre(id, titol, autor, any)); 
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return llibres;
    }

    /**
     * Modifica l'any de publicació d'un llibre amb un id específic.
     *
     * @param llibres Llista de llibres.
     * @param id Identificador del llibre a modificar.
     * @param nouAny Nou any de publicació.
     */
    public void modificarAnyPublicacio(List<Llibre> llibres, int id, int nouAny) {
        for (Llibre l : llibres) {
            if (l.getId() == id) {
                l.setAny(nouAny);
                break;
            }
        }
    }

    /**
     * Afegeix un nou llibre a la llista de llibres.
     *
     * @param llibres Llista de llibres.
     * @param nouLlibre Nou llibre a afegir.
     */
    public void afegirNouLlibre(List<Llibre> llibres, Llibre nouLlibre) {
        llibres.add(nouLlibre);
    }

    /**
     * Esborra un llibre amb un id específic de la llista de llibres.
     *
     * @param llibres Llista de llibres.
     * @param id Identificador del llibre a esborrar.
     */
    public void esborrarLlibre(List<Llibre> llibres, int id) {
        llibres.removeIf(l -> l.getId() == id);
    }

    /**
     * Guarda la llista de llibres en un fitxer nou.
     *
     * @param llibres Llista de llibres a guardar.
     */
    public void guardarLlibres(List<Llibre> llibres) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Llibre l : llibres) {
            JsonObject obj = Json.createObjectBuilder() // He buscado y esta sintaxis se puede hacer
                .add("id", l.getId())
                .add("titol", l.getTitol())
                .add("autor", l.getAutor())
                .add("any", l.getAny())
                .build();
            arrayBuilder.add(obj);
        }
        File outputFile = new File(dataFile.getParent(), "llibres_output_jakarta.json");
        try (OutputStream fos = new FileOutputStream(outputFile);
            JsonWriter writer = Json.createWriter(fos)
        ) {
            writer.writeArray(arrayBuilder.build());
            System.out.println("Llibres guardats correctament a " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}