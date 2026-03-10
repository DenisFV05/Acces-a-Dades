package cat.iesesteveterradas;

import cat.iesesteveterradas.utils.UtilsSQLite;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CharacterDAO {
    private final Connection conn;

    public CharacterDAO(Connection conn) { this.conn = conn; }

    public List<CharacterFH> getByFaction(int idFaction) throws Exception {
        List<CharacterFH> list = new ArrayList<>();
        ResultSet rs = UtilsSQLite.querySelect(conn,
            "SELECT * FROM Personatge WHERE idFaccio=" + idFaction);
        while (rs.next()) {
            list.add(new CharacterFH(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getDouble("atac"),
                rs.getDouble("defensa"),
                rs.getInt("idFaccio")
            ));
        }
        rs.close();
        return list;
    }

    public CharacterFH getBestAttack(int idFaction) throws Exception {
        ResultSet rs = UtilsSQLite.querySelect(conn,
            "SELECT * FROM Personatge WHERE idFaccio=" + idFaction + " ORDER BY atac DESC LIMIT 1");
        if (rs.next()) {
            return new CharacterFH(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getDouble("atac"),
                rs.getDouble("defensa"),
                rs.getInt("idFaccio")
            );
        }
        return null;
    }

    public CharacterFH getBestDefense(int idFaction) throws Exception {
        ResultSet rs = UtilsSQLite.querySelect(conn,
            "SELECT * FROM Personatge WHERE idFaccio=" + idFaction + " ORDER BY defensa DESC LIMIT 1");
        if (rs.next()) {
            return new CharacterFH(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getDouble("atac"),
                rs.getDouble("defensa"),
                rs.getInt("idFaccio")
            );
        }
        return null;
    }
}
