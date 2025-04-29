package savetovaliste.repository;

import java.sql.*;
import java.util.*;
import savetovaliste.model.Fakultet;
import savetovaliste.utility.DatabaseConnection;

public class FakultetRep {

    public List<Fakultet> getSviFakulteti() {
        List<Fakultet> lista = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT fakultet_id, naziv FROM fakultet ORDER BY naziv")) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Fakultet f = new Fakultet();
                f.setFakultetId(rs.getInt("fakultet_id"));
                f.setNaziv(rs.getString("naziv"));
                lista.add(f);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

}
