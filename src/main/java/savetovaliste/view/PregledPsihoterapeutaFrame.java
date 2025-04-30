package savetovaliste.view;
import savetovaliste.model.PsihoterapeutKandidat;
import savetovaliste.dao.PsihoKandDao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PregledPsihoterapeutaFrame extends JFrame {

    // ovo nam je main frame aplikacije

    private JTable table;
    private DefaultTableModel tableModel;

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
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

        // Puni tabelu podacima
        ucitajPodatke();

        // Scroll
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        ImageIcon prijavaIkona = new ImageIcon("src/main/resources/login.png");
        ImageIcon registracijaIkona = new ImageIcon("src/main/resources/signup.png");

        JButton prijavaBtn = new JButton("Prijava", prijavaIkona);
        prijavaBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        prijavaBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        prijavaBtn.addActionListener(e -> {
           new PrijavaFrame().setVisible(true);
        });

        JButton registracijaBtn = new JButton("Registracija", registracijaIkona);
        registracijaBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        registracijaBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        registracijaBtn.addActionListener(e -> {
            new RegistracijaFrame().setVisible(true);
        });


        JPanel dugmiciPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
        dugmiciPanel.setBackground(new Color(172, 230, 180));

        dugmiciPanel.add(prijavaBtn);
        dugmiciPanel.add(registracijaBtn);

        add(dugmiciPanel, BorderLayout.SOUTH);
    }

    private void ucitajPodatke() {
        PsihoKandDao repo = new PsihoKandDao();
        List<PsihoterapeutKandidat> kandidati = repo.getAllKandidati();

        for (PsihoterapeutKandidat k : kandidati) {
            Object[] red = {
                    k.getIme(),
                    k.getPrezime(),
                    k.getDatumRodjenja(),
                    k.getTelefon(),
                    k.getEmail(),
                    k.getFakultetNaziv(),
                    k.getStepenNaziv()
            };
            tableModel.addRow(red);
        }
    }
}