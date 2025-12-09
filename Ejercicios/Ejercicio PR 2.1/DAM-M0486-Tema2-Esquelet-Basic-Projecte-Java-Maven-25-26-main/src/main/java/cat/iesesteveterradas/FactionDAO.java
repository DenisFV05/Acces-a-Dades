package cat.iesesteveterradas;

import cat.iesesteveterradas.utils.UtilsSQLite;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FactionDAO {
    private final Connection conn;

    public FactionDAO(Connection conn) { this.conn = conn; }

    public List<Faction> getAll() throws Exception {
        List<Faction> list = new ArrayList<>();
        ResultSet rs = UtilsSQLite.querySelect(conn, "SELECT * FROM Faccio");
        while (rs.next()) {
            list.add(new Faction(rs.getInt("id"), rs.getString("nom"), rs.getString("resum")));
        }
        rs.close();
        return list;
    }

    public Faction getById(int id) throws Exception {
        ResultSet rs = UtilsSQLite.querySelect(conn, "SELECT * FROM Faccio WHERE id=" + id);
        if (rs.next()) {
            return new Faction(rs.getInt("id"), rs.getString("nom"), rs.getString("resum"));
        }
        return null;
    }
}
