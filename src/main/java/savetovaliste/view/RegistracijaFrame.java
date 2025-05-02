package savetovaliste.view;

import com.toedter.calendar.JDateChooser;
import savetovaliste.dao.*;
import savetovaliste.model.Fakultet;
import savetovaliste.model.OblastPsihoterapije;
import savetovaliste.model.PsihoterapeutKandidat;
import savetovaliste.model.Univerzitet;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.util.List;

public class RegistracijaFrame extends JFrame{
    private JTextField tfIme = new JTextField(15);
    private JTextField tfPrezime = new JTextField(15);
    private JTextField tfJmbg = new JTextField(15);
    private JTextField tfTelefon = new JTextField(15);
    private JTextField tfEmail = new JTextField(15);
    private JTextField tfPrebivaliste = new JTextField(15);
    private JDateChooser datumRodjenjaChooser = new JDateChooser();
    private JPasswordField tfLozinka = new JPasswordField(32);
    private JComboBox<Univerzitet> cbUniverziteti = new JComboBox<>();
    private JComboBox<Fakultet> cbFakulteti = new JComboBox<>();
    private JComboBox<String> cbStepeni = new JComboBox<>();
    private JDateChooser datumSertifikacijeChooser = new JDateChooser();
    private JComboBox<OblastPsihoterapije> cbOblasti = new JComboBox<>();

    private String pocetniJmbg;

    private PregledPsihoterapeutaFrame parentFrame;

    public RegistracijaFrame(PregledPsihoterapeutaFrame parentFrame, String pocetniJmbg) {
        this.parentFrame = parentFrame;
        this.pocetniJmbg = pocetniJmbg;
        setTitle("Registracija psihoterapeuta");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 550);
        setLocationRelativeTo(null); // centriraj prozor na ekranu

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); // margine oko svakog elementa
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int y = 0; // redni broj reda

        gbc.gridx = 0; gbc.gridy = y;
        add(new JLabel("Ime:"), gbc);
        gbc.gridx = 1;
        add(tfIme, gbc);

        gbc.gridx = 0; gbc.gridy = ++y;
        add(new JLabel("Prezime:"), gbc);
        gbc.gridx = 1;
        add(tfPrezime, gbc);

        gbc.gridx = 0; gbc.gridy = ++y;
        add(new JLabel("JMBG:"), gbc);
        gbc.gridx = 1;
        add(tfJmbg, gbc);

        gbc.gridx = 0; gbc.gridy = ++y;
        add(new JLabel("Telefon:"), gbc);
        gbc.gridx = 1;
        add(tfTelefon, gbc);

        gbc.gridx = 0; gbc.gridy = ++y;
        add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        add(tfEmail, gbc);

        gbc.gridx = 0; gbc.gridy = ++y;
        add(new JLabel("Prebivalište:"), gbc);
        gbc.gridx = 1;
        add(tfPrebivaliste, gbc);

        gbc.gridx = 0; gbc.gridy = ++y;
        add(new JLabel("Datum rođenja:"), gbc);
        gbc.gridx = 1;
        datumRodjenjaChooser.setDateFormatString("yyyy-MM-dd");
        add(datumRodjenjaChooser, gbc);

        gbc.gridx = 0; gbc.gridy = ++y;
        add(new JLabel("Lozinka:"), gbc);
        gbc.gridx = 1;
        add(tfLozinka, gbc);

        gbc.gridx = 0; gbc.gridy = ++y;
        add(new JLabel("Univerzitet:"), gbc);
        gbc.gridx = 1;
        cbUniverziteti.addActionListener(e -> {
            Univerzitet izabrani = (Univerzitet) cbUniverziteti.getSelectedItem();
            if (izabrani != null) {
                ucitajFakultetePoUniverzitetu(izabrani.getId());
            }
        });

        add(cbUniverziteti, gbc);

        gbc.gridx = 0; gbc.gridy = ++y;
        add(new JLabel("Fakultet:"), gbc);
        gbc.gridx = 1;
        add(cbFakulteti, gbc);

        gbc.gridx = 0; gbc.gridy = ++y;
        add(new JLabel("Stepen studija:"), gbc);
        gbc.gridx = 1;
        add(cbStepeni, gbc);

        gbc.gridx = 0; gbc.gridy = ++y;
        add(new JLabel("Datum sertifikacije:"), gbc);
        gbc.gridx = 1;
        datumSertifikacijeChooser.setDateFormatString("yyyy-MM-dd");
        add(datumSertifikacijeChooser, gbc);

        gbc.gridx = 0; gbc.gridy = ++y;
        add(new JLabel("Oblast psihoterapije:"), gbc);
        gbc.gridx = 1;
        add(cbOblasti, gbc);



        // Dugme na sredini
        JButton btnRegistruj = new JButton("Registruj");
        gbc.gridx = 0; gbc.gridy = ++y;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        add(btnRegistruj, gbc);

        // Poveži dugme sa akcijom
        btnRegistruj.addActionListener(e -> registruj());

        ucitajUniverzitete();
        ucitajFakultete();
        ucitajStepene();
        ucitajOblastiPsihoterapije();

        popuniAkoPostojiSupervizija(); // ← ovo dodaj

        setVisible(true);
    }


    private void ucitajUniverzitete() {
        cbUniverziteti.removeAllItems();
        cbUniverziteti.addItem(new Univerzitet(0,"-- Univerziteti --"));
        List<Univerzitet> univerziteti = new UniverzitetDao().getSviUniverziteti();
        for (Univerzitet u : univerziteti) {
            cbUniverziteti.addItem(u);
        }
    }



    private void ucitajFakultete() {
        cbFakulteti.removeAllItems();
        cbFakulteti.addItem(new Fakultet(0, "-- Fakulteti --"));

        List<Fakultet> fakulteti = new FakultetDao().getSviFakulteti();
        for (Fakultet f : fakulteti) {
            cbFakulteti.addItem(f);  // ubacuješ OBJEKAT
        }
    }

    private void ucitajFakultetePoUniverzitetu(int univerzitetId) {
        cbFakulteti.removeAllItems();
        cbFakulteti.addItem(new Fakultet(0, "-- izaberite fakultet --"));
        List<Fakultet> fakulteti = new FakultetDao().getFakultetiPoUniverzitetu(univerzitetId);
        for (Fakultet f : fakulteti) {
            cbFakulteti.addItem(f);
        }
    }

    private void ucitajStepene() {
        cbStepeni.removeAllItems();
        List<String> stepeni = new StepenDao().getSviStepeni();
        for (String s : stepeni) {
            cbStepeni.addItem(s);
        }
    }

    private void ucitajOblastiPsihoterapije() {
        cbOblasti.removeAllItems();
        List<OblastPsihoterapije> oblasti = new OblastPsihoterapijeDao().getSveOblasti(); // napravi DAO ako nemaš
        for (OblastPsihoterapije o : oblasti) {
            cbOblasti.addItem(o);
        }
    }



    private void registruj() {
        try {
            // Preuzmi sve unose
            String ime = tfIme.getText().trim();
            String prezime = tfPrezime.getText().trim();
            String jmbgText = tfJmbg.getText().trim();
            String telefon = tfTelefon.getText().trim();
            String email = tfEmail.getText().trim();
            String prebivaliste = tfPrebivaliste.getText().trim();
            String lozinka = new String(tfLozinka.getPassword()).trim();
            java.util.Date izabraniDatum = datumRodjenjaChooser.getDate();

            //  Validacije unosa
            if (ime.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Unesite ime.");
                return;
            }

            if (prezime.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Unesite prezime.");
                return;
            }

            if (jmbgText.isEmpty() || !jmbgText.matches("\\d{13}")) {
                JOptionPane.showMessageDialog(this, "Unesite ispravan JMBG.");
                return;
            }

            if (telefon.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Unesite telefon.");
                return;
            }

            if (email.isEmpty() || !email.contains("@")) {
                JOptionPane.showMessageDialog(this, "Unesite validan e-mail.");
                return;
            }

            if (prebivaliste.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Unesite prebivalište.");
                return;
            }

            if (izabraniDatum == null) {
                JOptionPane.showMessageDialog(this, "Izaberite datum rođenja.");
                return;
            }

            if (lozinka.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Unesite lozinku.");
                return;
            }

            if (cbFakulteti.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Molimo izaberite fakultet.");
                return;
            }

            if (cbStepeni.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Izaberite stepen studija.");
                return;
            }

            // Provera da li je već registrovan
            PsihoKandDao rep = new PsihoKandDao();
            if (rep.daLiJeRegistrovan(jmbgText, email)) {
                JOptionPane.showMessageDialog(this, "Korisnik sa ovim JMBG-om ili email adresom je već registrovan.");
                return;
            }


            // Ako je sve validno, nastavi s unosom
            PsihoterapeutKandidat k = new PsihoterapeutKandidat();
            k.setIme(ime);
            k.setPrezime(prezime);
            k.setJmbg(Long.parseLong(jmbgText));
            k.setTelefon(telefon);
            k.setEmail(email);
            k.setPrebivaliste(prebivaliste);
            k.setDatumRodjenja(new java.sql.Date(izabraniDatum.getTime()));
            k.setLozinka(lozinka);

            Fakultet izabraniFakultet = (Fakultet) cbFakulteti.getSelectedItem();
            if (izabraniFakultet == null || izabraniFakultet.getFakultetId() == 0) {
                JOptionPane.showMessageDialog(this, "Molimo izaberite fakultet.");
                return;
            }
            k.setFakultetId(izabraniFakultet.getFakultetId());

            String stepenId = cbStepeni.getSelectedItem().toString();
            k.setStepenId(stepenId);

            PsihoKandDao psiho = new PsihoKandDao();

            if (psiho.registrujKandidata(k)) {
                psiho.postaviRegistrovan(jmbgText);

                int id = psiho.getIdPoJmbg(Long.parseLong(jmbgText));  // Dodaj ovu metodu u DAO ako ne postoji


                JOptionPane.showMessageDialog(this, "Uspešna registracija!");


                java.util.Date datumSert = datumSertifikacijeChooser.getDate();

                if (datumSert == null) {
                    JOptionPane.showMessageDialog(this, "Morate izabrati datum sertifikacije.");
                    return;
                }

                java.sql.Date sqlDatumSert = new java.sql.Date(datumSert.getTime());



                OblastPsihoterapije izabranaOblast = (OblastPsihoterapije) cbOblasti.getSelectedItem();

                if (datumSert == null || izabranaOblast == null) {
                    JOptionPane.showMessageDialog(this, "Morate uneti datum sertifikacije i oblast.");
                    return;
                }

                psiho.dodajSertifikat(
                        id,
                        izabranaOblast.getOblastPsihoteraoijeId(),
                        sqlDatumSert
                );


                //  Osveži tabelu u glavnom prozoru
                if (parentFrame != null) {
                    parentFrame.osveziTabelu();
                }

                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Greška pri registraciji.");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Greška prilikom registracije.");
            ex.printStackTrace();
        }
    }

    private void popuniAkoPostojiSupervizija() {
        if (pocetniJmbg == null) return;

        tfJmbg.setText(pocetniJmbg);
        tfJmbg.setEditable(false);

        PsihoterapeutKandidat gotovKandidat = new PsihoKandDao().getKandidatAkoImaZavrsenuSuperviziju(pocetniJmbg);

        if (gotovKandidat != null) {
            tfIme.setText(gotovKandidat.getIme());
            tfPrezime.setText(gotovKandidat.getPrezime());
            tfTelefon.setText(gotovKandidat.getTelefon());
            tfEmail.setText(gotovKandidat.getEmail());
            tfPrebivaliste.setText(gotovKandidat.getPrebivaliste());
            datumRodjenjaChooser.setDate(gotovKandidat.getDatumRodjenja());

            int fakultetId = gotovKandidat.getFakultetId();
            int univerzitetId = new FakultetDao().getUniverzitetIdZaFakultet(fakultetId);

            // Postavi univerzitet (poziva se automatski ucitajFakultetePoUniverzitetu)
            for (int i = 0; i < cbUniverziteti.getItemCount(); i++) {
                Univerzitet u = cbUniverziteti.getItemAt(i);
                if (u.getId() == univerzitetId) {
                    cbUniverziteti.setSelectedIndex(i);
                    break;
                }
            }

            // fakultet
            for (int i = 0; i < cbFakulteti.getItemCount(); i++) {
                Fakultet f = cbFakulteti.getItemAt(i);
                if (f.getFakultetId() == gotovKandidat.getFakultetId()) {
                    cbFakulteti.setSelectedIndex(i);
                    break;
                }
            }

            cbStepeni.setSelectedItem(gotovKandidat.getStepenId());
        }
    }


}
