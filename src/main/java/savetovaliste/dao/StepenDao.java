package savetovaliste.dao;

import savetovaliste.utility.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StepenDao {
    public List<String> getSviStepeni() {
        List<String> lista = new ArrayList<>();
        String query = "SELECT stepen_id FROM stepen ORDER BY stepen_id ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                lista.add(rs.getString("stepen_id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}
