package cat.iesesteveterradas;

import java.util.List;
import java.util.Scanner;

public class HonorService {

    private final FactionDAO factionDAO;
    private final CharacterDAO characterDAO;

    public HonorService(CharacterDAO characterDAO, FactionDAO factionDAO) {
        this.characterDAO = characterDAO;
        this.factionDAO = factionDAO;
    }

    public HonorService(java.sql.Connection conn) {
        this.factionDAO = new FactionDAO(conn);
        this.characterDAO = new CharacterDAO(conn);
    }

    public void mostrarTaula(Scanner sc) throws Exception {
        System.out.print("Quina taula vols veure (Faccio/Personatge)? ");
        String t = sc.next();

        if (t.equalsIgnoreCase("Faccio")) {
            factionDAO.getAll().forEach(System.out::println);
        } else {
            System.out.println("Personatges de totes les faccions:");
            List<CharacterFH> lista1 = characterDAO.getByFaction(1);
            List<CharacterFH> lista2 = characterDAO.getByFaction(2);
            lista1.forEach(System.out::println);
            lista2.forEach(System.out::println);
        }
    }

    public void mostrarPersonatgesPerFaccio(Scanner sc) throws Exception {
        System.out.print("Introdueix ID de la facció: ");
        int id = sc.nextInt();
        Faction f = factionDAO.getById(id);
        if (f == null) {
            System.out.println("Facció inexistent.");
            return;
        }
        System.out.println("Personatges de la facció " + f.getNom() + ":");
        characterDAO.getByFaction(id).forEach(System.out::println);
    }

    public void millorAtacant(Scanner sc) throws Exception {
        System.out.print("ID facció: ");
        int id = sc.nextInt();
        Faction f = factionDAO.getById(id);
        CharacterFH c = characterDAO.getBestAttack(id);
        if (c != null) System.out.println("Millor atacant de " + f.getNom() + ": " + c);
    }

    public void millorDefensor(Scanner sc) throws Exception {
        System.out.print("ID facció: ");
        int id = sc.nextInt();
        Faction f = factionDAO.getById(id);
        CharacterFH c = characterDAO.getBestDefense(id);
        if (c != null) System.out.println("Millor defensor de " + f.getNom() + ": " + c);
    }
}
