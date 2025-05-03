package savetovaliste.view;

import com.toedter.calendar.JDateChooser;
import savetovaliste.dao.CenaDao;
import savetovaliste.dao.SeansaDao;
import savetovaliste.model.Klijent;
import savetovaliste.model.PsihoterapeutKandidat;
import savetovaliste.model.Seansa;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NovaSeansaFrame extends JFrame {

    private JDateChooser dateChooser;
    private JSpinner timeSpinner;
    private JTextArea beleškeArea;
    private JTextField trajanjeField;
    private JCheckBox placanjeCheck;
    private JCheckBox zlocinCheck;
    private JCheckBox naSuduCheck;

    private Klijent klijent;
    private PsihoterapeutKandidat psihoterapeutKandidat;

    public NovaSeansaFrame(Klijent klijent, PsihoterapeutKandidat psihoterapeutKandidat) {
        this.klijent = klijent;
        this.psihoterapeutKandidat = psihoterapeutKandidat;

        setTitle("Nova seansa");
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
        dateChooser.setDateFormatString("dd.MM.yyyy");
        add(dateChooser, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Vreme:"), gbc);
        gbc.gridx = 1;
        SpinnerDateModel timeModel = new SpinnerDateModel();
        timeSpinner = new JSpinner(timeModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
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
            Date datum = dateChooser.getDate();
            String vreme = new SimpleDateFormat("HH:mm:ss").format((Date) timeSpinner.getValue());
            String trajanjeStr = trajanjeField.getText();
            String beleske = beleškeArea.getText();
            boolean placanje = placanjeCheck.isSelected();
            boolean zlocin = zlocinCheck.isSelected();
            boolean naSudu = naSuduCheck.isSelected();

            if (datum==null || vreme.isEmpty() || trajanjeStr.isEmpty() || beleske.isEmpty()){
                JOptionPane.showMessageDialog(
                        NovaSeansaFrame.this,
                        "Molimo popunite sva polja.",
                        "Greška",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            int cenaId = new CenaDao().getTrenutnaCenaId();
            if(cenaId == 0){
                JOptionPane.showMessageDialog(NovaSeansaFrame.this, "Ne postoji podatak o ceni seanse", "Greska", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try{
                Seansa seansa = new Seansa(datum, Time.valueOf(vreme), beleske, new BigDecimal(trajanjeStr),placanje,zlocin,naSudu, klijent.getId(), cenaId,psihoterapeutKandidat.getPsihoterapeutId());
                boolean uspeh = new SeansaDao().unesiSeansuProcedura(seansa);
                if (uspeh) {
                    JOptionPane.showMessageDialog(this, "Seansa uspešno sačuvana!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Greška prilikom čuvanja seanse.");
                }

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

        });

        setVisible(true);
    }
}
