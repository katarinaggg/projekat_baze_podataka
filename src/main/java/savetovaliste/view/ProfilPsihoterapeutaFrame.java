package savetovaliste.view;

import savetovaliste.model.PsihoterapeutKandidat;

import javax.swing.*;
import java.awt.*;

public class ProfilPsihoterapeutaFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel desniPanel;
    private PsihoterapeutKandidat psihoterapeut;

    public ProfilPsihoterapeutaFrame(PsihoterapeutKandidat psihoterapeut) {
        this.psihoterapeut = psihoterapeut;

        setTitle("Profil psihoterapeuta - " + psihoterapeut.getIme() + " " + psihoterapeut.getPrezime());
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Leva strana sa dugmićima
        JPanel leviPanel = new JPanel();
        leviPanel.setLayout(new BoxLayout(leviPanel, BoxLayout.Y_AXIS));
        leviPanel.setBackground(new Color(200, 230, 201));
        leviPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JButton btnProfil = new JButton("Profil");
        JButton btnSeanse = new JButton("Seanse");
        JButton btnPlacanja = new JButton("Plaćanja");
        JButton btnKlijenti = new JButton("Klijenti");

        btnProfil.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSeanse.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnPlacanja.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnKlijenti.setAlignmentX(Component.CENTER_ALIGNMENT);

        leviPanel.add(btnProfil);
        leviPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        leviPanel.add(btnSeanse);
        leviPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        leviPanel.add(btnPlacanja);
        leviPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        leviPanel.add(btnKlijenti);

        add(leviPanel, BorderLayout.WEST);

        // Desna strana sa panelima
        desniPanel = new JPanel();
        cardLayout = new CardLayout();
        desniPanel.setLayout(cardLayout);

        JPanel profilPanel = napraviProfilPanel();
        JPanel placeholderSeanse = new JPanel();
        placeholderSeanse.add(new JLabel("Seanse - uskoro"));
        JPanel placeholderPlacanja = new JPanel();
        placeholderPlacanja.add(new JLabel("Plaćanja - uskoro"));
        JPanel placeholderKlijenti = new JPanel();
        placeholderKlijenti.add(new JLabel("Klijenti - uskoro"));

        desniPanel.add(profilPanel, "Profil");
        desniPanel.add(placeholderSeanse, "Seanse");
        desniPanel.add(placeholderPlacanja, "Placanja");
        desniPanel.add(placeholderKlijenti, "Klijenti");

        add(desniPanel, BorderLayout.CENTER);

        // Dugmići za menjanje panela
        btnProfil.addActionListener(e -> cardLayout.show(desniPanel, "Profil"));
        btnSeanse.addActionListener(e -> cardLayout.show(desniPanel, "Seanse"));
        btnPlacanja.addActionListener(e -> cardLayout.show(desniPanel, "Placanja"));
        btnKlijenti.addActionListener(e -> cardLayout.show(desniPanel, "Klijenti"));
    }

//    private JPanel napraviProfilPanel() {
//        JPanel panel = new JPanel();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
//
//        JLabel naslov = new JLabel("Informacije o psihoterapeutu");
//        naslov.setFont(new Font("SansSerif", Font.BOLD, 22));
//        naslov.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//        panel.add(naslov);
//        panel.add(Box.createRigidArea(new Dimension(0, 20)));
//
//        panel.add(new JLabel("Ime: " + psihoterapeut.getIme()));
//        panel.add(new JLabel("Prezime: " + psihoterapeut.getPrezime()));
//        panel.add(new JLabel("Datum rođenja: " + psihoterapeut.getDatumRodjenja()));
//        panel.add(new JLabel("Telefon: " + psihoterapeut.getTelefon()));
//        panel.add(new JLabel("Email: " + psihoterapeut.getEmail()));
//        panel.add(new JLabel("Fakultet: " + psihoterapeut.getFakultetNaziv()));
//        panel.add(new JLabel("Stepen studija: " + psihoterapeut.getStepenNaziv()));
//
//        return panel;
//    }

    private JPanel napraviProfilPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel naslov = new JLabel("Informacije o psihoterapeutu");
        naslov.setFont(new Font("SansSerif", Font.BOLD, 22));
        naslov.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(naslov);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        panel.add(new JLabel("ID: " + psihoterapeut.getPsihoterapeutId()));
        panel.add(new JLabel("Ime: " + psihoterapeut.getIme()));
        panel.add(new JLabel("Prezime: " + psihoterapeut.getPrezime()));
        panel.add(new JLabel("Datum rođenja: " + psihoterapeut.getDatumRodjenja()));
        panel.add(new JLabel("Telefon: " + psihoterapeut.getTelefon()));
        panel.add(new JLabel("Prebivalište: " + psihoterapeut.getPrebivaliste()));
        panel.add(new JLabel("Email: " + psihoterapeut.getEmail()));
        panel.add(new JLabel("Lozinka: " + psihoterapeut.getLozinka()));
        panel.add(new JLabel("Fakultet: " + psihoterapeut.getFakultetNaziv()));
        panel.add(new JLabel("Stepen studija: " + psihoterapeut.getStepenNaziv()));

        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(new JLabel("Sertifikat:"));
        panel.add(new JLabel("Datum sertifikacije: " + psihoterapeut.getDatumSertifikacije()));
        panel.add(new JLabel("Oblast: " + psihoterapeut.getOblastNaziv()));

        return panel;
    }
}
