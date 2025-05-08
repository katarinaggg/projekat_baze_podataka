package savetovaliste.view.psihoterapeut.frameovi;

import com.toedter.calendar.JDateChooser;
import savetovaliste.dao.CenaDao;
import savetovaliste.dao.SeansaDao;
import savetovaliste.dao.TestDao;
import savetovaliste.model.Seansa;
import savetovaliste.model.Test;
import savetovaliste.view.psihoterapeut.paneli.BuduceSeansePanel;
import savetovaliste.view.psihoterapeut.paneli.OdrzaneSeansePanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OdrziSeansuFrame extends JFrame {

    private JDateChooser dateChooser;
    private JSpinner timeSpinner;
    private JTextArea beleškeArea;
    private JTextField trajanjeField;
    private JCheckBox placanjeCheck;

    private JComboBox<Test> testCombo;
    private JTextField rezultatField;
    private DefaultTableModel testModel;
    private JTable testTabela;

    private BuduceSeansePanel buduceSeansePanel;
    private OdrzaneSeansePanel odrzaneSeansePanel;

    private Seansa seansa;

    private List<Test> izabraniTestovi = new ArrayList<>();
    private List<String> rezultati = new ArrayList<>();

    public OdrziSeansuFrame(Seansa seansa, BuduceSeansePanel buduceSeansePanel, OdrzaneSeansePanel odrzaneSeansePanel) {
        this.seansa = seansa;
        this.buduceSeansePanel = buduceSeansePanel;
        this.odrzaneSeansePanel = odrzaneSeansePanel;

        setTitle("Unos detalja o seansi");
        setSize(450, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;

        add(new JLabel("Datum:"), gbc);
        gbc.gridx = 1;
        dateChooser = new JDateChooser();
        dateChooser.setDate(seansa.getDatum());
        dateChooser.setEnabled(false);
        add(dateChooser, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Vreme:"), gbc);
        gbc.gridx = 1;
        SpinnerDateModel timeModel = new SpinnerDateModel();
        timeSpinner = new JSpinner(timeModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setValue(seansa.getVreme());
        timeSpinner.setEnabled(false);
        add(timeSpinner, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Trajanje (npr. 1.00):"), gbc);
        gbc.gridx = 1;
        trajanjeField = new JTextField();
        add(trajanjeField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Beleške:"), gbc);
        gbc.gridx = 1;
        beleškeArea = new JTextArea(4, 20);
        beleškeArea.setLineWrap(true);
        beleškeArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(beleškeArea);
        add(scrollPane, gbc);

        gbc.gridx = 0; gbc.gridy++;
        placanjeCheck = new JCheckBox("Plaćanje");
        add(placanjeCheck, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Izaberi test:"), gbc);
        gbc.gridx = 1;
        testCombo = new JComboBox<>(new TestDao().ucitajSveTestove().toArray(new Test[0]));
        add(testCombo, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Rezultat testa:"), gbc);
        gbc.gridx = 1;
        rezultatField = new JTextField();
        add(rezultatField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        JButton dodajTestBtn = new JButton("Dodaj test");
        add(dodajTestBtn, gbc);

        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        testModel = new DefaultTableModel(new Object[]{"Test", "Rezultat"}, 0);
        testTabela = new JTable(testModel);
        JScrollPane testScroll = new JScrollPane(testTabela);
        testScroll.setPreferredSize(new Dimension(350, 100));
        add(testScroll, gbc);

        dodajTestBtn.addActionListener(e -> {
            Test izabran = (Test) testCombo.getSelectedItem();
            String rez = rezultatField.getText();
            if (izabran != null && !rez.isEmpty()) {
                izabraniTestovi.add(izabran);
                rezultati.add(rez);
                testModel.addRow(new Object[]{izabran.getNaziv(), rez});
                rezultatField.setText("");
            }
        });

        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        JButton sacuvajBtn = new JButton("Sačuvaj seansu");
        add(sacuvajBtn, gbc);

        sacuvajBtn.addActionListener(e -> {
            String trajanjeStr = trajanjeField.getText();
            String beleske = beleškeArea.getText();
            boolean placanje = placanjeCheck.isSelected();

            if (trajanjeStr.isEmpty() || beleske.isEmpty()){
                JOptionPane.showMessageDialog(this, "Molimo popunite sva polja.", "Greška", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                BigDecimal trajanje = new BigDecimal(trajanjeStr);
                int cenaId = new CenaDao().getTrenutnaCenaId();
                if (cenaId == 0) {
                    JOptionPane.showMessageDialog(this, "Nema informacije o ceni.", "Greška", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                seansa.setTrajanje(trajanje);
                seansa.setBeleske(beleske);
                seansa.setPlacanje(placanje);
                seansa.setCenaId(cenaId);

                boolean uspeh = new SeansaDao().oznaciSeansuKaoOdrzanu(seansa);
                if (uspeh) {
                    for (int i = 0; i < izabraniTestovi.size(); i++) {
                        new SeansaDao().dodajRezultatZaTest(seansa.getSeansaId(), izabraniTestovi.get(i).getTestId(), rezultati.get(i));
                    }
                    buduceSeansePanel.osveziTabelu();
                    odrzaneSeansePanel.osveziTabelu();
                    JOptionPane.showMessageDialog(this, "Seansa je uspešno uneta.");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Greška pri čuvanju.", "Greška", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Greška prilikom obrade.", "Greška", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }
}
