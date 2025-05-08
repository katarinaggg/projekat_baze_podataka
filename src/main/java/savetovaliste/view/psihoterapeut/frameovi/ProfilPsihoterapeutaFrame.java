package savetovaliste.view.psihoterapeut.frameovi;

import savetovaliste.model.PsihoterapeutKandidat;
import savetovaliste.view.psihoterapeut.paneli.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ProfilPsihoterapeutaFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel desniPanel;
    private PsihoterapeutKandidat psihoterapeut;

    private JFrame roditelj;


    public ProfilPsihoterapeutaFrame(PsihoterapeutKandidat psihoterapeut, JFrame roditelj) {
        this.psihoterapeut = psihoterapeut;
        this.roditelj = roditelj;

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
        JButton btnNoviKlijenti = new JButton("Novi klijenti");
        JButton btnSviKlijenti = new JButton("Svi klijenti");
        JButton btnOdrzaneSeanse = new JButton("Odrzane seanse");
        JButton btnBuduceSeanse = new JButton("Termini buducih seansi");
        JButton btnPLacanja = new JButton("Placanja");

        btnProfil.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnNoviKlijenti.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSviKlijenti.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnOdrzaneSeanse.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnBuduceSeanse.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnPLacanja.setAlignmentX(Component.CENTER_ALIGNMENT);

        ImageIcon logoIkonica = new ImageIcon("src/main/resources/logo.png");
        Image logoSlika = logoIkonica.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        JLabel logoLabela = new JLabel(new ImageIcon(logoSlika));
        logoLabela.setAlignmentX(Component.CENTER_ALIGNMENT);

        leviPanel.add(logoLabela);
        leviPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        leviPanel.add(btnProfil);
        leviPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        leviPanel.add(btnNoviKlijenti);
        leviPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        leviPanel.add(btnSviKlijenti);
        leviPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        leviPanel.add(btnOdrzaneSeanse);
        leviPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        leviPanel.add(btnBuduceSeanse);
        leviPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        leviPanel.add(btnPLacanja);

        JButton btnOdjava = new JButton("Odjavi se");
        btnOdjava.setAlignmentX(Component.CENTER_ALIGNMENT);
        leviPanel.add(Box.createVerticalGlue()); // Gura dugme na dno
        leviPanel.add(btnOdjava);

        btnOdjava.addActionListener(e -> {
            int izbor = JOptionPane.showConfirmDialog(
                    this,
                    "Da li ste sigurni da želite da se odjavite?",
                    "Potvrda odjave",
                    JOptionPane.YES_NO_OPTION
            );
            if (izbor == JOptionPane.YES_OPTION) {
                dispose();
                if (roditelj != null) {
                    roditelj.setVisible(true);
                }
            }
        });


        add(leviPanel, BorderLayout.WEST);

        // Desna strana sa panelima
        desniPanel = new JPanel();
        cardLayout = new CardLayout();
        desniPanel.setLayout(cardLayout);

        OdrzaneSeansePanel odrzaneSeansePanel = new OdrzaneSeansePanel(psihoterapeut);
        BuduceSeansePanel buduceSeansePanel= new BuduceSeansePanel(psihoterapeut, odrzaneSeansePanel);

        desniPanel.add(new ProfilPanel(psihoterapeut), "Profil");
        desniPanel.add(new PrijaveNovihKlijenataPanel(psihoterapeut), "Prijave novih klijenata");
        desniPanel.add(new SviKlijentiPanel(psihoterapeut, buduceSeansePanel), "Svi klijenti");
        desniPanel.add(odrzaneSeansePanel, "Odrzane seanse");
        desniPanel.add(buduceSeansePanel, "Termini buducih seansi");
        desniPanel.add(new PlacanjaPanel(psihoterapeut), "Placanja");

        add(desniPanel, BorderLayout.CENTER);

        // Dugmići za menjanje panela
        btnProfil.addActionListener(e -> cardLayout.show(desniPanel, "Profil"));
        btnNoviKlijenti.addActionListener(e -> cardLayout.show(desniPanel, "Prijave novih klijenata"));
        btnSviKlijenti.addActionListener(e -> cardLayout.show(desniPanel, "Svi klijenti"));
        btnOdrzaneSeanse.addActionListener(e -> cardLayout.show(desniPanel, "Odrzane seanse"));
        btnBuduceSeanse.addActionListener(e -> cardLayout.show(desniPanel, "Termini buducih seansi"));
        btnPLacanja.addActionListener(e -> cardLayout.show(desniPanel, "Placanja"));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (roditelj != null) {
                    roditelj.setVisible(true);
                }
            }
        });

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

}