
package tikape.runko.database;

import java.sql.*;
import java.util.*;
import tikape.runko.domain.Kysymys;
import tikape.runko.domain.Vastaus;

public class VastausDao implements Dao<Vastaus, Integer>{
    private Database database;

    public VastausDao(Database database) {
        this.database = database;
    }

    @Override
    public Vastaus findOne(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Vastaus> findAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public List<Vastaus> findAllByKysymysIdAndBooleanFalse(Integer key) throws SQLException {
        List<Vastaus> vastaukset = new ArrayList<>();
        Connection c = database.getConnection();
        PreparedStatement s = c.prepareStatement("SELECT * FROM Vastaus WHERE kysymys_id = ? AND oikein = ?");
        s.setInt(1, key);
        s.setBoolean(2, false);
        ResultSet r = s.executeQuery();
            while (r.next()) {
                vastaukset.add(new Vastaus(r.getInt("id"), r.getInt("kysymys_id"), r.getString("vastausteksti"), r.getBoolean("oikein")));
            }
        
        return vastaukset;
    }
    
    public List<Vastaus> findAllByKysymysIdAndBooleanTrue(Integer key) throws SQLException {
        List<Vastaus> vastaukset = new ArrayList<>();
        Connection c = database.getConnection();
        PreparedStatement s = c.prepareStatement("SELECT * FROM Vastaus WHERE kysymys_id = ? AND oikein = ?");
        s.setInt(1, key);
        s.setBoolean(2, true);
        ResultSet r = s.executeQuery();
            while (r.next()) {
                vastaukset.add(new Vastaus(r.getInt("id"), r.getInt("kysymys_id"), r.getString("vastausteksti"), r.getBoolean("oikein")));
            }
        
        return vastaukset;
    }

    @Override
    public Vastaus save(Vastaus object) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Vastaus (kysymys_id, vastausteksti, oikein) VALUES (?, ?, ?)");
            stmt.setInt(1, object.getKysymys_id());
            stmt.setString(2, object.getVastausteksti());
            stmt.setBoolean(3, object.isOikein());
            stmt.executeUpdate();
        }

        return null;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection c = database.getConnection();
        PreparedStatement s = c.prepareStatement("DELETE FROM Vastaus WHERE id = ?");
        s.setInt(1, key);
        s.executeUpdate();
        
        s.close();
        c.close();
    }
    
}
