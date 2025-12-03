package com.project.pr14;

import com.project.objectes.Llibre;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Classe principal que gestiona la lectura i el processament de fitxers JSON per obtenir dades de llibres.
 * Utilitza la llibreria org.json.
 */
public class PR14GestioLlibreriaOrgJsonMain {

    private final File dataFile;

    /**
     * Constructor de la classe PR14GestioLlibreriaOrgJsonMain.
     *
     * @param dataFile Fitxer on es troben els llibres.
     */
    public PR14GestioLlibreriaOrgJsonMain(File dataFile) {
        this.dataFile = dataFile;
    }

    public static void main(String[] args) {
        File dataFile = new File(System.getProperty("user.dir"), "data/pr14" + File.separator + "llibres_input.json");
        PR14GestioLlibreriaOrgJsonMain app = new PR14GestioLlibreriaOrgJsonMain(dataFile);
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
     * Carrega els llibres des del fitxer JSON utilitzant la llibreria org.json.
     *
     * @return Llista de llibres o null si hi ha hagut un error en la lectura.
     */
    public List<Llibre> carregarLlibres() {
        List<Llibre> llibres = new ArrayList<>();
        try {
            String content = new String(Files.readAllBytes(Paths.get(dataFile.getPath())));
            JSONArray jsonArray = new JSONArray(content);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String titol = jsonObject.getString("titol");
                String autor = jsonObject.getString("autor");
                int any = jsonObject.getInt("any");
                llibres.add(new Llibre(id, titol, autor, any));
            }
        } catch (IOException e) {
            e.printStackTrace();
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
        for (Llibre llibre : llibres) {
            if (llibre.getId() == id) {
                llibre.setAny(nouAny);
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
        Iterator<Llibre> iterator = llibres.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getId() == id) {
                iterator.remove();
            }
        }
    }

    /**
     * Guarda la llista de llibres en un fitxer nou utilitzant la llibreria org.json.
     *
     * @param llibres Llista de llibres a guardar.
     */
    public void guardarLlibres(List<Llibre> llibres) {
        JSONArray jsonArray = new JSONArray();
        for (Llibre llibre : llibres) {
            JSONObject llibreJson = new JSONObject();
            llibreJson.put("id", llibre.getId());
            llibreJson.put("titol", llibre.getTitol());
            llibreJson.put("autor", llibre.getAutor());
            llibreJson.put("any", llibre.getAny());
            jsonArray.put(llibreJson);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(dataFile.getParent(), "llibres_output_json_org.json"))) {
            writer.write(jsonArray.toString(4)); // Format JSON amb indentació
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
