package savetovaliste.view.psihoterapeut;

import com.toedter.calendar.JDateChooser;
import savetovaliste.dao.CenaDao;
import savetovaliste.dao.SeansaDao;
import savetovaliste.model.Seansa;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class OdrziSeansuFrame extends JFrame {

    private JDateChooser dateChooser;
    private JSpinner timeSpinner;
    private JTextArea beleškeArea;
    private JTextField trajanjeField;
    private JCheckBox placanjeCheck;
    private JCheckBox zlocinCheck;
    private JCheckBox naSuduCheck;

    // treba nam panel da ga osvezimo kada odrzimo neku seansu
    // ona se vise nece prikazivati na tom panelu buducih
    private BuduceSeansePanel buduceSeansePanel;

    // treba nam i panel s odrzanim seansama da bi
    // ga osvezili kada dodamo ovu novu
    private OdrzaneSeansePanel odrzaneSeansePanel;

    private Seansa seansa;


    public OdrziSeansuFrame(Seansa seansa, BuduceSeansePanel buduceSeansePanel, OdrzaneSeansePanel odrzaneSeansePanel) {
        this.seansa = seansa;
        this.buduceSeansePanel = buduceSeansePanel;
        this.odrzaneSeansePanel = odrzaneSeansePanel;

        setTitle("Unos detalja o seansi");
        setSize(500, 500);
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
        beleškeArea = new JTextArea(5, 20);
        beleškeArea.setLineWrap(true);
        beleškeArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(beleškeArea);
        add(scrollPane, gbc);

        gbc.gridx = 0; gbc.gridy++;
        placanjeCheck = new JCheckBox("Plaćanje");
        add(placanjeCheck, gbc);
        gbc.gridx = 1;
        zlocinCheck = new JCheckBox("Zločin");
        add(zlocinCheck, gbc);

        gbc.gridx = 0; gbc.gridy++;
        naSuduCheck = new JCheckBox("Na sudu");
        add(naSuduCheck, gbc);

        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        JButton sacuvajBtn = new JButton("Sačuvaj seansu");
        add(sacuvajBtn, gbc);

        sacuvajBtn.addActionListener(e -> {
            String trajanjeStr = trajanjeField.getText();
            String beleske = beleškeArea.getText();
            boolean placanje = placanjeCheck.isSelected();
            boolean zlocin = zlocinCheck.isSelected();
            boolean naSudu = naSuduCheck.isSelected();

            if (trajanjeStr.isEmpty() || beleske.isEmpty()){
                JOptionPane.showMessageDialog(
                        OdrziSeansuFrame.this,
                        "Molimo popunite sva polja.",
                        "Greška",
                        JOptionPane.ERROR_MESSAGE
                );
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
                seansa.setZlocin(zlocin);
                seansa.setNaSudu(naSudu);
                seansa.setCenaId(cenaId);

                boolean uspeh = new SeansaDao().oznaciSeansuKaoOdrzanu(seansa);
                if (uspeh) {
                    // iz ove izbacujemo
                    buduceSeansePanel.osveziTabelu();
                    // u ovu ubacujemo
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
