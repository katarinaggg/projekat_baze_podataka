package savetovaliste.dao;

import savetovaliste.model.Klijent;
import savetovaliste.utility.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class KlijentDao {

    public Klijent nadjiKlijentaPoEmailu(String email) {
        Klijent klijent = null;

        String sql = "SELECT * FROM klijent WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                klijent = new Klijent();
                klijent.setId(rs.getInt("klijent_id"));
                klijent.setIme(rs.getString("ime"));
                klijent.setPrezime(rs.getString("prezime"));
                klijent.setDatumRodjenja(rs.getDate("datum_rodjenja"));
                klijent.setTelefon(rs.getString("telefon"));
                klijent.setEmail(rs.getString("email"));
                klijent.setOpis(rs.getString("opis_problema"));
                klijent.setPrviPut(rs.getBoolean("prvi_put"));
                //klijent.setPolId(rs.getString("pol_id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return klijent;
    }
}
