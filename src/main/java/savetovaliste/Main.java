package savetovaliste;

import savetovaliste.utility.DatabaseConnection;
import savetovaliste.view.PregledPsihoterapeutaFrame;
import savetovaliste.view.RegistracijaFrame;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {

        Connection con = DatabaseConnection.getConnection();
        if (con != null) {
            System.out.println("Konekcija sa bazom je uspesno ostvarena!");
        } else {
            System.out.println("Konekcija sa bazom NIJE ostvarena!");
        }

        javax.swing.SwingUtilities.invokeLater(() -> {
            RegistracijaFrame frame = new RegistracijaFrame();
            frame.setVisible(true);
        });

        javax.swing.SwingUtilities.invokeLater(() -> {
            PregledPsihoterapeutaFrame frame = new PregledPsihoterapeutaFrame();
            frame.setVisible(true);
        });

    }
}