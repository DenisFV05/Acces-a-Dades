package cat.iesesteveterradas;

import cat.iesesteveterradas.utils.UtilsSQLite;
import java.sql.Connection;
import java.util.Scanner;

public class PR210Honor {

    public static void main(String[] args) {
        String dbPath = "forhonor.db"; // Se puede cambiar la ruta
        Connection conn = null;

        try {
            conn = UtilsSQLite.connect(dbPath);

            // Inicializar BD si no existe
            DatabaseInitializer.initDatabase(conn);

            HonorService service = new HonorService(conn);
            Scanner sc = new Scanner(System.in);

            int op;

            do {
                System.out.println("\n=== MENÚ FOR HONOR ===");
                System.out.println("1. Mostrar taula");
                System.out.println("2. Personatges per facció");
                System.out.println("3. Millor atacant");
                System.out.println("4. Millor defensor");
                System.out.println("0. Sortir");
                System.out.print("Tria opció: ");

                op = sc.nextInt();

                switch (op) {
                    case 1 -> service.mostrarTaula(sc);
                    case 2 -> service.mostrarPersonatgesPerFaccio(sc);
                    case 3 -> service.millorAtacant(sc);
                    case 4 -> service.millorDefensor(sc);
                }

            } while (op != 0);

            sc.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            UtilsSQLite.disconnect(conn);
        }
    }
}
