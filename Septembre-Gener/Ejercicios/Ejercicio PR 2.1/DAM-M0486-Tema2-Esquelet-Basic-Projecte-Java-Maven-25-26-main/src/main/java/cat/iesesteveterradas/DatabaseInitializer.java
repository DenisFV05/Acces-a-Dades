package cat.iesesteveterradas;

import cat.iesesteveterradas.utils.UtilsSQLite;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseInitializer {

    public static void initDatabase(Connection conn) throws SQLException {
        String createFaction = """
            CREATE TABLE IF NOT EXISTS Faccio (
                id INTEGER PRIMARY KEY,
                nom VARCHAR(15),
                resum VARCHAR(500)
            );
        """;

        String createCharacter = """
            CREATE TABLE IF NOT EXISTS Personatge (
                id INTEGER PRIMARY KEY,
                nom VARCHAR(15),
                atac REAL,
                defensa REAL,
                idFaccio INTEGER,
                FOREIGN KEY (idFaccio) REFERENCES Faccio(id)
            );
        """;

        UtilsSQLite.queryUpdate(conn, createFaction);
        UtilsSQLite.queryUpdate(conn, createCharacter);

        insertIfEmpty(conn);
    }

    private static void insertIfEmpty(Connection conn) throws SQLException {
        ResultSet rs = UtilsSQLite.querySelect(conn, "SELECT COUNT(*) AS total FROM Faccio");
        if (rs.next() && rs.getInt("total") > 0) return;

        // Inserir dades base
        UtilsSQLite.queryUpdate(conn,
            "INSERT INTO Faccio VALUES (1,'Cavallers','Facció europea.');");
        UtilsSQLite.queryUpdate(conn,
            "INSERT INTO Faccio VALUES (2,'Vikings','Facció nòrdica.');");

        UtilsSQLite.queryUpdate(conn,
            "INSERT INTO Personatge VALUES (1,'Warden',80,70,1)");
        UtilsSQLite.queryUpdate(conn,
            "INSERT INTO Personatge VALUES (2,'Peace',90,40,1)");
        UtilsSQLite.queryUpdate(conn,
            "INSERT INTO Personatge VALUES (3,'Raider',95,60,2)");
        UtilsSQLite.queryUpdate(conn,
            "INSERT INTO Personatge VALUES (4,'Warlord',60,95,2)");
    }
}
