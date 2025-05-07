package savetovaliste.view.psihoterapeut;

import savetovaliste.model.PsihoterapeutKandidat;

import javax.swing.*;
import java.awt.*;

public class ProfilPanel extends JPanel {

    PsihoterapeutKandidat psihoterapeut;

    public ProfilPanel(PsihoterapeutKandidat psihoterapeut) {
        this.psihoterapeut = psihoterapeut;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        setBackground(Color.WHITE);

        // === GORNJI PANEL: SLIKA + NASLOV ===
        JPanel gornjiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 0));
        gornjiPanel.setBackground(Color.WHITE);

        // SLIKA
        ImageIcon originalnaSlika = new ImageIcon("src/main/resources/profil.jpg");
        Image scaledImage = originalnaSlika.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        JLabel slikaLabela = new JLabel(new ImageIcon(scaledImage));
        gornjiPanel.add(slikaLabela);

        // NASLOV
        JLabel naslov = new JLabel("Informacije o psihoterapeutu");
        naslov.setFont(new Font("SansSerif", Font.BOLD, 26));
        naslov.setForeground(new Color(50, 50, 50));
        gornjiPanel.add(naslov);

        // === INFO PANEL: PODACI ===
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); // razmak ispod naslova

        infoPanel.add(kreirajInfoRed("ID", String.valueOf(psihoterapeut.getPsihoterapeutId())));
        infoPanel.add(kreirajInfoRed("Ime", psihoterapeut.getIme()));
        infoPanel.add(kreirajInfoRed("Prezime", psihoterapeut.getPrezime()));
        infoPanel.add(kreirajInfoRed("Datum rođenja", psihoterapeut.getDatumRodjenja().toString()));
        infoPanel.add(kreirajInfoRed("Telefon", psihoterapeut.getTelefon()));
        infoPanel.add(kreirajInfoRed("Email", psihoterapeut.getEmail()));
        infoPanel.add(kreirajInfoRed("Prebivalište", psihoterapeut.getPrebivaliste()));
        infoPanel.add(kreirajInfoRed("Fakultet", psihoterapeut.getFakultetNaziv()));
        infoPanel.add(kreirajInfoRed("Stepen studija", psihoterapeut.getStepenNaziv()));
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add(kreirajInfoRed("Datum sertifikacije", psihoterapeut.getDatumSertifikacije().toString()));
        infoPanel.add(kreirajInfoRed("Oblast", psihoterapeut.getOblastNaziv()));

        // === DODAVANJE U GLAVNI PANEL ===
        add(gornjiPanel, BorderLayout.NORTH);
        add(infoPanel, BorderLayout.CENTER);
    }

    private JPanel kreirajInfoRed(String labelaTekst, String vrednostTekst) {
        JPanel red = new JPanel(new BorderLayout());
        red.setBackground(Color.WHITE);
        red.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220))); // donja linija

        JPanel sadrzaj = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sadrzaj.setBackground(Color.WHITE);

        JLabel labela = new JLabel(labelaTekst + ": ");
        labela.setFont(new Font("SansSerif", Font.BOLD, 16));

        JLabel vrednost = new JLabel(vrednostTekst);
        vrednost.setFont(new Font("SansSerif", Font.PLAIN, 16));

        sadrzaj.add(labela);
        sadrzaj.add(vrednost);
        red.add(sadrzaj, BorderLayout.CENTER);

        return red;
    }
}
