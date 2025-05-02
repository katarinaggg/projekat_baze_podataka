package savetovaliste.dao;

import savetovaliste.model.OblastPsihoterapije;
import savetovaliste.utility.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OblastPsihoterapijeDao {
    public List<OblastPsihoterapije> getSveOblasti() {
        List<OblastPsihoterapije> lista = new ArrayList<>();

        String sql = "SELECT sert_oblast_id, naziv FROM oblast_psihoterapije";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                OblastPsihoterapije o = new OblastPsihoterapije();
                o.setOblastPsihoteraoijeId(rs.getInt("sert_oblast_id"));
                o.setNaziv(rs.getString("naziv"));
                lista.add(o);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

}
