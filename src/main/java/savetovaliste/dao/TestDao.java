package savetovaliste.dao;

import savetovaliste.model.Test;
import savetovaliste.utility.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TestDao {

    public List<Test> ucitajSveTestove() {
        List<Test> testovi = new ArrayList<Test>();

        String sql = "SELECT test_id, naziv, cena, test_oblast_id FROM test";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Test t = new Test(
                        rs.getInt("test_id"),
                        rs.getString("naziv"),
                        rs.getDouble("cena"),
                        rs.getInt("test_oblast_id")
                );
                testovi.add(t);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return testovi;
    }
}
