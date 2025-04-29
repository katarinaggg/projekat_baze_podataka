package savetovaliste.repository;

import savetovaliste.model.Fakultet;
import savetovaliste.model.Univerzitet;
import savetovaliste.utility.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UniverzitetRep {

    public List<Univerzitet> getSviUniverziteti() {
        List<Univerzitet> lista = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT univerzitet_id, naziv FROM univerzitet")) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Univerzitet u = new Univerzitet();
                u.setId(rs.getInt("univerzitet_id"));
                u.setNaziv(rs.getString("naziv"));
                lista.add(u);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

}
