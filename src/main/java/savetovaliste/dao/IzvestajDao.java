package savetovaliste.dao;

import java.math.BigDecimal;
import java.sql.*;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import savetovaliste.utility.DatabaseConnection;

public class IzvestajDao {

    public int brojOdrzanihSeansi(int psihoterapeutId) {
        String sql = "SELECT COUNT(*) FROM seansa WHERE psihoterapeut_kandidat_id = ? AND odrzana = true";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, psihoterapeutId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public BigDecimal[] ukupnaZaradaSeanseITestovi(int psihoterapeutId) {
        BigDecimal zaradaSeanse = BigDecimal.ZERO;
        BigDecimal zaradaTestovi = BigDecimal.ZERO;

        String sql = """
                SELECT p.svrha, SUM(p.iznos) AS ukupno
                FROM placanje p
                JOIN seansa s ON p.seansa_id = s.seansa_id
                WHERE s.psihoterapeut_kandidat_id = ? AND p.valuta_id = 'RSD'
                GROUP BY p.svrha
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, psihoterapeutId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String svrha = rs.getString("svrha");
                    BigDecimal ukupno = rs.getBigDecimal("ukupno");
                    if (svrha.startsWith("SEANSA")) {
                        zaradaSeanse = zaradaSeanse.add(ukupno);
                    } else if (svrha.equals("TEST")) {
                        zaradaTestovi = zaradaTestovi.add(ukupno);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BigDecimal[]{zaradaSeanse, zaradaTestovi};
    }

    public Date najranijaSeansa(int psihoterapeutId) {
        String sql = "SELECT MIN(dan) FROM seansa WHERE psihoterapeut_kandidat_id = ? AND odrzana = true";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, psihoterapeutId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDate(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String klijentSaNajviseSeansi(int psihoterapeutId) {
        String sql = """
                SELECT k.ime, k.prezime, COUNT(*) AS broj
                FROM seansa s
                JOIN klijent k ON s.klijent_id = k.klijent_id
                WHERE s.psihoterapeut_kandidat_id = ? AND s.odrzana = true
                GROUP BY k.klijent_id
                ORDER BY broj DESC
                LIMIT 1
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, psihoterapeutId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("ime") + " " + rs.getString("prezime");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "N/A";
    }

    public String godinaSaNajvisePrihoda(int psihoterapeutId) {
        String sql = """
                SELECT YEAR(p.datum_uplate) AS godina, SUM(p.iznos) AS ukupno
                FROM placanje p
                JOIN seansa s ON p.seansa_id = s.seansa_id
                WHERE s.psihoterapeut_kandidat_id = ? AND p.valuta_id = 'RSD'
                GROUP BY YEAR(p.datum_uplate)
                ORDER BY ukupno DESC
                LIMIT 1
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, psihoterapeutId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return String.valueOf(rs.getInt("godina"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "N/A";
    }

    public String prosecnoVremePocetkaSeansi(int psihoterapeutId) {
        String sql = """
        SELECT AVG(TIME_TO_SEC(vreme)) AS proseks
        FROM seansa
        WHERE psihoterapeut_kandidat_id = ? AND odrzana = true
    """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, psihoterapeutId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double proseks = rs.getDouble("proseks");
                    if (proseks > 0) {
                        // Zaokruži na najbliži ceo sat u sekundama
                        int roundedSeconds = ((int) Math.round(proseks / 3600.0)) * 3600;

                        // Konvertuj u hh:mm
                        int hours = (roundedSeconds / 3600) % 24;
                        return String.format("%02d:00h", hours);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "N/A";
    }


}
