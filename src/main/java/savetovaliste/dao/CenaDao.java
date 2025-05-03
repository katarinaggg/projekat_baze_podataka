package savetovaliste.dao;

import savetovaliste.utility.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CenaDao {
    public int getTrenutnaCenaId() {
        int cenaId = 0;
        String sql = "SELECT cena_id " +
                     "FROM cena_seanse " +
                     "WHERE datum_od = (SELECT MAX(datum_od) FROM cena_seanse);";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                cenaId =  rs.getInt("cena_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cenaId;
    }
}
