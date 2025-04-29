package savetovaliste.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // singleton na neki nacin
    // imamo samo jednu konekciju na bazu
    // i uvek koristimo nju

    // informacije o nasoj bazi:
    private static final String url = "jdbc:mysql://localhost:3306/savetovaliste";
    private static final String korisnik = "root";
    private static final String lozinka = "sifrazabaze";

    // polje u kojem cuvamo konekciju
    // static - samo 1 konekcija
    private static Connection connection;

    // privatni konstruktor da ne moze niko da pravi novu instancu
    private DatabaseConnection() {
    }

    public static Connection getConnection() {
        try {
            // samo prvi put kreira novu instancu
            // a svaki sledeci put samo vracamo tu postojecu
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(url, korisnik, lozinka);
                System.out.println("Uspesno povezivanje sa bazom!");
            }
        } catch (SQLException e) {
            System.out.println("Neuspesno povezivanje sa bazom!");
            throw new RuntimeException(e);
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
