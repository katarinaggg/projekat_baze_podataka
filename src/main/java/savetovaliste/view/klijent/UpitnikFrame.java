package savetovaliste.view.klijent;

import com.toedter.calendar.JDateChooser;
import savetovaliste.dao.KlijentDao;
import savetovaliste.dao.PolDao;
import savetovaliste.dao.SeansaDao;
import savetovaliste.model.Klijent;
import savetovaliste.model.Pol;
import savetovaliste.model.PsihoterapeutKandidat;
import savetovaliste.model.Seansa;
import savetovaliste.view.klijent.PrvaSeansaFrame;

import javax.swing.*;
import java.awt.*;

public class UpitnikFrame extends JFrame {

    public UpitnikFrame(PsihoterapeutKandidat kandidat) {
        setTitle("Upitnik - Prijava klijenta");
        setSize(570, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 50, 10, 10); // razmak između komponenti
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        Font labelFont = new Font("SansSerif", Font.BOLD, 15);

        JLabel lblIme = new JLabel("Ime:");
        lblIme.setFont(labelFont);
        panel.add(lblIme, gbc);
        gbc.gridx = 1;
        JTextField tfIme = new JTextField();
        panel.add(tfIme, gbc);

        gbc.gridy++; gbc.gridx = 0;
        JLabel lblPrezime = new JLabel("Prezime:");
        lblPrezime.setFont(labelFont);
        panel.add(lblPrezime, gbc);
        gbc.gridx = 1;
        JTextField tfPrezime = new JTextField();
        panel.add(tfPrezime, gbc);

        gbc.gridy++; gbc.gridx = 0;
        JLabel lblDatum = new JLabel("Datum rođenja:");
        lblDatum.setFont(labelFont);
        panel.add(lblDatum, gbc);
        gbc.gridx = 1;
        JDateChooser datumChooser = new JDateChooser();
        datumChooser.setDateFormatString("dd. MM. yyyy");
        panel.add(datumChooser, gbc);

        gbc.gridy++; gbc.gridx = 0;
        JLabel lblPol = new JLabel("Pol:");
        lblPol.setFont(labelFont);
        panel.add(lblPol, gbc);
        gbc.gridx = 1;
        JComboBox<Pol> cbPol = new JComboBox<>();
        PolDao polDao = new PolDao();
        for (Pol pol : polDao.getSviPolovi()) {
            cbPol.addItem(pol); // Prikazuje naziv, pamti ID
        }
        panel.add(cbPol, gbc);

        gbc.gridy++; gbc.gridx = 0;
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(labelFont);
        panel.add(lblEmail, gbc);
        gbc.gridx = 1;
        JTextField tfEmail = new JTextField();
        panel.add(tfEmail, gbc);

        gbc.gridy++; gbc.gridx = 0;
        JLabel lblTelefon = new JLabel("Telefon:");
        lblTelefon.setFont(labelFont);
        panel.add(lblTelefon, gbc);
        gbc.gridx = 1;
        JTextField tfTelefon = new JTextField();
        panel.add(tfTelefon, gbc);

        gbc.gridy++; gbc.gridx = 1;
        JCheckBox cbIsaoRanije = new JCheckBox("Već je išao kod psihoterapeuta");
        panel.add(cbIsaoRanije, gbc);

        gbc.gridy++; gbc.gridx = 0;
        JLabel lblProblem = new JLabel("Opis problema:");
        lblProblem.setFont(labelFont);
        panel.add(lblProblem, gbc);

        gbc.gridx = 1;
        gbc.weighty = 1.0; // Omogućava vertikalno širenje
        gbc.fill = GridBagConstraints.BOTH; // Širi komponentu i horizontalno i vertikalno
        JTextArea taProblem = new JTextArea(8, 25);
        taProblem.setLineWrap(true);
        taProblem.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(taProblem);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        panel.add(scroll, gbc);

        // Resetuj weighty i fill posle textarea, da ne utiče na druge komponente
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;


        // Dugme
        JButton btnPosalji = new JButton("Pošalji prijavu") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color boja;
                if (getModel().isPressed()) {
                    boja = new Color(24, 116, 205); // Kliknuto
                } else if (getModel().isRollover()) {
                    boja = new Color(65, 150, 255); // Hover
                } else {
                    boja = new Color(30, 144, 255); // Default
                }

                g2.setColor(boja);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // PRVO nacrtaj pozadinu
                g2.dispose();

                super.paintComponent(g); // TEK ONDA crtaj tekst
            }


            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(30, 144, 255));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
            }
        };

        btnPosalji.setRolloverEnabled(true);
        btnPosalji.setOpaque(false);
        btnPosalji.setContentAreaFilled(false); // Ne koristi default background
        btnPosalji.setBorderPainted(false);     // Ne koristi default border
        btnPosalji.setFocusPainted(false);
        btnPosalji.setForeground(Color.WHITE);
        btnPosalji.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnPosalji.setPreferredSize(new Dimension(200, 40));
        btnPosalji.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


//        btnPosalji.addActionListener(e -> {
//            // Provera da li su sva polja popunjena
//            if (tfIme.getText().trim().isEmpty() ||
//                    tfPrezime.getText().trim().isEmpty() ||
//                    datumChooser.getDate() == null ||
//                    tfEmail.getText().trim().isEmpty() ||
//                    tfTelefon.getText().trim().isEmpty() ||
//                    taProblem.getText().trim().isEmpty()) {
//
//                JOptionPane.showMessageDialog(this, "Sva polja moraju biti popunjena!", "Greška", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//
//            // Provera formatiranja e-mail adrese (opciono)
//            if (!tfEmail.getText().trim().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
//                JOptionPane.showMessageDialog(this, "Unesite ispravnu email adresu!", "Greška", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//
//            KlijentDao dao = new KlijentDao();
//
//            String unetiEmail = tfEmail.getText().trim();
//            if (dao.nadjiKlijentaPoEmailu(unetiEmail) != null) {
//                JOptionPane.showMessageDialog(this, "Klijent sa ovom email adresom već postoji!", "Greška", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//
//            Klijent klijent = new Klijent();
//            klijent.setIme(tfIme.getText().trim());
//            klijent.setPrezime(tfPrezime.getText().trim());
//            klijent.setDatumRodjenja(datumChooser.getDate());
//            klijent.setEmail(unetiEmail);
//            klijent.setTelefon(tfTelefon.getText().trim());
//            // Ako je čekirano da je ranije išao, znači da nije prvi put, pa je logika obrnuta:
//            klijent.setPrviPut(!cbIsaoRanije.isSelected());
//            klijent.setOpis(taProblem.getText().trim());
//            Pol izabraniPol = (Pol) cbPol.getSelectedItem();
//            klijent.setPolId(izabraniPol.getId());  // 'M' ili 'Z'
//            klijent.setPol(izabraniPol.getNaziv()); // 'Muški' ili 'Ženski'
//
//            boolean uspesno = dao.dodajKlijenta(klijent);
//
//            if (uspesno) {
//                // odmah pronađi klijenta da dobiješ njegov ID
//                Klijent noviKlijent = dao.nadjiKlijentaPoEmailu(klijent.getEmail());
//
//                Seansa prvaSeansa = new Seansa();
//                prvaSeansa.setDatum(new java.util.Date());
//                prvaSeansa.setVreme(new java.sql.Time(System.currentTimeMillis()));
//                prvaSeansa.setTrajanje(new java.math.BigDecimal("1.0"));
//                prvaSeansa.setPlacanje(false);
//                prvaSeansa.setOdrzana(true);
//                prvaSeansa.setBeleske("Prva besplatna seansa");
//                prvaSeansa.setKlijentId(noviKlijent.getId());
//                prvaSeansa.setPsihoterapeutId(kandidat.getPsihoterapeutId()); // koristiš kandidata koji je već prosleđen
//                prvaSeansa.setCenaId(null); // jer je besplatna
//
//                boolean uspehSeansa = new SeansaDao().unesiSeansuProcedura(prvaSeansa);
//
//                if (uspehSeansa) {
//                    JOptionPane.showMessageDialog(this, "Prijava uspešna.", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
//                    new PrvaSeansaFrame(noviKlijent, kandidat).setVisible(true);
//                    this.dispose();
//                } else {
//                    JOptionPane.showMessageDialog(this, "Klijent dodat, ali došlo je do greške pri zakazivanju prve seanse.", "Greška", JOptionPane.ERROR_MESSAGE);
//                }
//            } else {
//                JOptionPane.showMessageDialog(this, "Došlo je do greške pri dodavanju klijenta.", "Greška", JOptionPane.ERROR_MESSAGE);
//            }
//
//        });

        btnPosalji.addActionListener(e -> {
            // Provera da li su sva polja popunjena
            if (tfIme.getText().trim().isEmpty() ||
                    tfPrezime.getText().trim().isEmpty() ||
                    datumChooser.getDate() == null ||
                    tfEmail.getText().trim().isEmpty() ||
                    tfTelefon.getText().trim().isEmpty() ||
                    taProblem.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(this, "Sva polja moraju biti popunjena!", "Greška", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Provera formatiranja e-mail adrese (opciono)
            if (!tfEmail.getText().trim().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                JOptionPane.showMessageDialog(this, "Unesite ispravnu email adresu!", "Greška", JOptionPane.ERROR_MESSAGE);
                return;
            }

            KlijentDao dao = new KlijentDao();

            String unetiEmail = tfEmail.getText().trim();
            if (dao.nadjiKlijentaPoEmailu(unetiEmail) != null) {
                JOptionPane.showMessageDialog(this, "Klijent sa ovom email adresom već postoji!", "Greška", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Klijent klijent = new Klijent();
            klijent.setIme(tfIme.getText().trim());
            klijent.setPrezime(tfPrezime.getText().trim());
            klijent.setDatumRodjenja(datumChooser.getDate());
            klijent.setEmail(unetiEmail);
            klijent.setTelefon(tfTelefon.getText().trim());
            klijent.setPrviPut(!cbIsaoRanije.isSelected());
            klijent.setOpis(taProblem.getText().trim());
            Pol izabraniPol = (Pol) cbPol.getSelectedItem();
            klijent.setPolId(izabraniPol.getId());
            klijent.setPol(izabraniPol.getNaziv());

            boolean uspesno = dao.dodajKlijenta(klijent);

            if (uspesno) {
                Klijent noviKlijent = dao.nadjiKlijentaPoEmailu(klijent.getEmail());
                JOptionPane.showMessageDialog(this, "Prijava uspešna. Unesite detalje prve seanse.", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
                new PrvaSeansaFrame(noviKlijent, kandidat);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Došlo je do greške pri dodavanju klijenta.", "Greška", JOptionPane.ERROR_MESSAGE);
            }
        });



        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        bottomPanel.add(btnPosalji);

        add(panel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        panel.setBackground(new Color(187, 229, 252));
        bottomPanel.setBackground(new Color(187, 229, 252));
        getContentPane().setBackground(new Color(187, 229, 252));

        setVisible(true);
    }


}
