
package tikape.runko.database;

import java.sql.*;
import java.util.*;
import tikape.runko.domain.Kysymys;


public class KysymysDao implements Dao<Kysymys, Integer>{
    private Database database;

    public KysymysDao(Database database) {
        this.database = database;
    }

    @Override
    public Kysymys findOne(Integer key) throws SQLException {
        Connection c = database.getConnection();
        PreparedStatement s = c.prepareStatement("SELECT * FROM Kysymys WHERE id = ?");
        s.setInt(1, key);
        ResultSet r = s.executeQuery();
        
        if (!r.next()) {
            return null;
        }
        
        Kysymys k = new Kysymys(r.getInt("id"), r.getString("kurssi"), r.getString("aihe"), r.getString("kysymysteksti"));
        return k;
    }

    @Override
    public List<Kysymys> findAll() throws SQLException {
        List<Kysymys> kysymykset = new ArrayList<>();
        try (Connection c = database.getConnection();
                ResultSet r = c.prepareStatement("SELECT * FROM Kysymys").executeQuery()) {
            while (r.next()) {
                kysymykset.add(new Kysymys(r.getInt("id"), r.getString("kurssi"), r.getString("aihe"), r.getString("kysymysteksti")));
            }
        }
        return kysymykset;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection c = database.getConnection();
        PreparedStatement s = c.prepareStatement("DELETE FROM Kysymys WHERE id = ?");
        s.setInt(1, key);
        s.executeUpdate();
        
        s.close();
        c.close();
    }

    @Override
    public Kysymys save(Kysymys object) throws SQLException {
        /*Kysymys byName = findByName(object.getKysymysteksti());
        if (byName != null) {
            return byName;
        }*/
        
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Kysymys (kurssi, aihe, kysymysteksti) VALUES (?, ?, ?)");
            stmt.setString(1, object.getKurssi());
            stmt.setString(2, object.getAihe());
            stmt.setString(3, object.getKysymysteksti());
            stmt.executeUpdate();
        }

        return findByName(object.getKysymysteksti());
    }
    
    private Kysymys findByName(String kysymys) throws SQLException {
        try (Connection c = database.getConnection()) {
            PreparedStatement stmt = c.prepareStatement("SELECT * FROM Kysymys WHERE kysymysteksti = ?");
            stmt.setString(1, "kysymysteksti");

            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }

            return new Kysymys(result.getInt("id"), result.getString("kurssi"), result.getString("aihe"), result.getString("kysymysteksti"));
        }
    }
    
}
