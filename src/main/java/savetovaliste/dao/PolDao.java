package savetovaliste.dao;

import savetovaliste.model.Pol;
import savetovaliste.utility.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PolDao {

    public List<Pol> getSviPolovi() {
        List<Pol> polovi = new ArrayList<>();
        String sql = "SELECT * FROM pol";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Pol pol = new Pol(rs.getString("pol_id"), rs.getString("pol"));
                polovi.add(pol);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return polovi;
    }
}
