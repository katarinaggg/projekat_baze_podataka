package savetovaliste.utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    // singleton na neki nacin
    // imamo samo jednu konekciju na bazu
    // i uvek koristimo nju

    // podaci za konekciju se NE nalaze u kodu, vec se citaju:
    //  1) iz fajla db.properties u root folderu projekta (nije na GitHub-u, nalazi se u .gitignore)
    //  2) ili iz environment varijabli DB_URL, DB_USER, DB_PASSWORD (imaju prednost nad fajlom)
    // sablon za fajl je db.properties.example
    private static final String CONFIG_FILE = "db.properties";
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/savetovaliste";

    private static final String url;
    private static final String korisnik;
    private static final String lozinka;

    static {
        Properties podesavanja = ucitajPodesavanja();
        url = vrednost("DB_URL", podesavanja.getProperty("db.url", DEFAULT_URL));
        korisnik = vrednost("DB_USER", podesavanja.getProperty("db.user"));
        lozinka = vrednost("DB_PASSWORD", podesavanja.getProperty("db.password"));
    }

    // polje u kojem cuvamo konekciju
    // static - samo 1 konekcija
    private static Connection connection;

    // privatni konstruktor da ne moze niko da pravi novu instancu
    private DatabaseConnection() {
    }

    private static Properties ucitajPodesavanja() {
        Properties p = new Properties();
        Path putanja = Path.of(CONFIG_FILE);
        if (Files.exists(putanja)) {
            try (InputStream in = new FileInputStream(putanja.toFile())) {
                p.load(in);
            } catch (IOException e) {
                throw new RuntimeException("Neuspesno citanje fajla " + CONFIG_FILE, e);
            }
        }
        return p;
    }

    // environment varijabla ima prednost, ako nije postavljena koristi se vrednost iz fajla
    private static String vrednost(String envNaziv, String izFajla) {
        String izOkruzenja = System.getenv(envNaziv);
        if (izOkruzenja != null && !izOkruzenja.isBlank()) {
            return izOkruzenja;
        }
        return izFajla;
    }

    public static Connection getConnection() {
        if (korisnik == null || korisnik.isBlank() || lozinka == null) {
            throw new IllegalStateException(
                    "Nisu podeseni podaci za konekciju sa bazom. Kopirajte db.properties.example u "
                    + CONFIG_FILE + " (u root folderu projekta) i upisite svoje MySQL podatke, "
                    + "ili postavite environment varijable DB_URL, DB_USER i DB_PASSWORD.");
        }
        try {
            // samo prvi put kreira novu instancu
            // a svaki sledeci put samo vracamo tu postojecu
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(url, korisnik, lozinka);
                //System.out.println("Uspesno povezivanje sa bazom!");
            }
        } catch (SQLException e) {
            //System.out.println("Neuspesno povezivanje sa bazom!");
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
