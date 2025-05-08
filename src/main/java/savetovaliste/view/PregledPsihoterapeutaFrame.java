package savetovaliste.view;
import savetovaliste.model.PsihoterapeutKandidat;
import savetovaliste.dao.PsihoKandDao;
import savetovaliste.view.klijent.UpitnikFrame;
import savetovaliste.view.psihoterapeut.paneli.SviKlijentiPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class PregledPsihoterapeutaFrame extends JFrame {

    // ovo nam je main frame aplikacije

    private JTable table;
    private DefaultTableModel tableModel;
    private SviKlijentiPanel sviKlijentiPanel;


    public PregledPsihoterapeutaFrame() {
        setTitle("Savetovalište 'Novi početak'");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel gornjiPanel = new JPanel();
        gornjiPanel.setLayout(new BoxLayout(gornjiPanel, BoxLayout.Y_AXIS));
        gornjiPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        gornjiPanel.setBackground(new Color(172, 230, 180));


        ImageIcon ikonica = new ImageIcon("src/main/resources/logo.png");
        Image slika = ikonica.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(slika));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


        JLabel naslovLabel = new JLabel("Dobrodošli u savetovalište 'Novi početak'");
        naslovLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        naslovLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel("Naši psihoterapeuti:");
        label.setFont(new Font("SansSerif", Font.PLAIN, 16));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        gornjiPanel.add(logoLabel);
        gornjiPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        gornjiPanel.add(naslovLabel);
        gornjiPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        gornjiPanel.add(label);

        add(gornjiPanel, BorderLayout.NORTH);


        // Definišemo kolone
        String[] kolone = {"Ime", "Prezime", "Datum rođenja", "Telefon", "Email", "Fakultet", "Stepen"};
        tableModel = new DefaultTableModel(kolone, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(22);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

        table.getColumnModel().getColumn(2).setPreferredWidth(90);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(200);
        table.getColumnModel().getColumn(5).setPreferredWidth(180);
        table.getColumnModel().getColumn(6).setPreferredWidth(75);

        // Puni tabelu podacima
        ucitajPodatke();

        // Scroll
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        ImageIcon prijavaIkona = new ImageIcon("src/main/resources/login.png");
        ImageIcon registracijaIkona = new ImageIcon("src/main/resources/signup.png");
        ImageIcon obrisiIkona = new ImageIcon("src/main/resources/delete.png");
        ImageIcon odaberiPsihIkona = new ImageIcon("src/main/resources/odaberiPsih.png");

        JButton prijavaBtn = new JButton("Prijava", prijavaIkona);
        prijavaBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        prijavaBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        prijavaBtn.addActionListener(e -> {
           new PrijavaFrame(this).setVisible(true);
        });

        JButton registracijaBtn = new JButton("Registracija", registracijaIkona);
        registracijaBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        registracijaBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        registracijaBtn.addActionListener(e -> {
            String jmbgInput = JOptionPane.showInputDialog(this, "Unesite svoj JMBG:");
            if (jmbgInput != null && jmbgInput.matches("\\d{13}")) {
                new RegistracijaFrame(this, jmbgInput).setVisible(true);
            } else if (jmbgInput != null) {
                JOptionPane.showMessageDialog(this, "Unesite validan JMBG (13 cifara).");
            }
        });

        JButton izaberiPsihoterapeutaBtn = new JButton("Pošalji upitnik", odaberiPsihIkona);
        izaberiPsihoterapeutaBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        izaberiPsihoterapeutaBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        izaberiPsihoterapeutaBtn.addActionListener(e -> {
            int red = table.getSelectedRow();
            if (red == -1) {
                JOptionPane.showMessageDialog(this, "Selektujte psihoterapeuta iz tabele.");
                return;
            }

            String email = tableModel.getValueAt(red, 4).toString();
            PsihoKandDao dao = new PsihoKandDao();
            PsihoterapeutKandidat kandidat = dao.nadjiPoEmailu(email);

            if (kandidat != null) {
                new UpitnikFrame(kandidat); // otvaraš upitnik za selektovanog terapeuta
            } else {
                JOptionPane.showMessageDialog(this, "Greška: nije moguće naći terapeuta po mejlu.");
            }
        });

        /*
        JButton obrisiBtn = new JButton("Obriši kandidata", obrisiIkona);
        obrisiBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        obrisiBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        obrisiBtn.addActionListener(e -> obrisiSelektovanogKandidata());
        */

        JPanel dugmiciPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
        dugmiciPanel.setBackground(new Color(172, 230, 180));

        Dimension dugmeDimenzije = new Dimension(130, 80); // širina, visina
        prijavaBtn.setPreferredSize(dugmeDimenzije);
        registracijaBtn.setPreferredSize(dugmeDimenzije);
        //obrisiBtn.setPreferredSize(dugmeDimenzije);
        izaberiPsihoterapeutaBtn.setPreferredSize(dugmeDimenzije);

        dugmiciPanel.add(prijavaBtn);
        dugmiciPanel.add(registracijaBtn);
        //dugmiciPanel.add(obrisiBtn);
        dugmiciPanel.add(izaberiPsihoterapeutaBtn);

        add(dugmiciPanel, BorderLayout.SOUTH);
    }

    private void ucitajPodatke() {
        PsihoKandDao repo = new PsihoKandDao();
        List<PsihoterapeutKandidat> kandidati = repo.getAllKandidati();

        SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy.");

        for (PsihoterapeutKandidat k : kandidati) {
            Object[] red = {
                    k.getIme(),
                    k.getPrezime(),
                    formater.format(k.getDatumRodjenja()),
                    k.getTelefon(),
                    k.getEmail(),
                    k.getFakultetNaziv(),
                    k.getStepenNaziv()
            };
            tableModel.addRow(red);
        }
    }

    public void osveziTabelu() {
        tableModel.setRowCount(0); // briši sve redove
        ucitajPodatke();           // ponovo učitaj
    }

    private void obrisiSelektovanogKandidata() {
        int selektovaniRed = table.getSelectedRow();
        if (selektovaniRed == -1) {
            JOptionPane.showMessageDialog(this, "Molimo selektujte kandidata za brisanje.");
            return;
        }

        String email = tableModel.getValueAt(selektovaniRed, 4).toString(); // 4 = kolona za email
        int potvrda = JOptionPane.showConfirmDialog(this, "Da li ste sigurni da želite da obrišete kandidata?", "Potvrda", JOptionPane.YES_NO_OPTION);

        if (potvrda == JOptionPane.YES_OPTION) {
            PsihoKandDao dao = new PsihoKandDao();
            dao.obrisiKandidataPoEmailu(email);
            JOptionPane.showMessageDialog(this, "Kandidat je obrisan.");
            osveziTabelu();
        }
    }


}