package savetovaliste.dao;

import java.sql.*;
import java.util.*;
import savetovaliste.model.Fakultet;
import savetovaliste.utility.DatabaseConnection;

public class FakultetDao {

    public List<Fakultet> getSviFakulteti() {
        List<Fakultet> lista = new ArrayList<>();
        String query = "SELECT fakultet_id, naziv FROM fakultet ORDER BY naziv ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                lista.add(new Fakultet(
                        rs.getInt("fakultet_id"),
                        rs.getString("naziv")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    public List<Fakultet> getFakultetiPoUniverzitetu(int univerzitetId) {
        List<Fakultet> lista = new ArrayList<>();
        String query = "SELECT fakultet_id, naziv FROM fakultet WHERE univerzitet_id = ? ORDER BY naziv ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, univerzitetId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(new Fakultet(
                        rs.getInt("fakultet_id"),
                        rs.getString("naziv")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public int getUniverzitetIdZaFakultet(int fakultetId) {
        String sql = "SELECT univerzitet_id FROM fakultet WHERE fakultet_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, fakultetId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("univerzitet_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }



}
