package savetovaliste.dao;

import savetovaliste.model.Univerzitet;
import savetovaliste.utility.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UniverzitetDao {


    public List<Univerzitet> getSviUniverziteti() {
        List<Univerzitet> lista = new ArrayList<>();
        String query = "SELECT univerzitet_id, naziv FROM univerzitet ORDER BY naziv ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                lista.add(new Univerzitet(
                        rs.getInt("univerzitet_id"),
                        rs.getString("naziv")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}
