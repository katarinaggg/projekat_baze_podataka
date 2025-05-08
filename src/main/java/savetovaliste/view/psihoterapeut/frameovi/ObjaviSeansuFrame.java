package savetovaliste.view.psihoterapeut.frameovi;

import com.toedter.calendar.JDateChooser;
import savetovaliste.dao.SeansaDao;
import savetovaliste.model.Seansa;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class ObjaviSeansuFrame extends JFrame {
    private Seansa seansa;
    private JDateChooser datumChooser;
    private JComboBox<String> primalacCombo;
    private JTextArea razlogArea;
    private JCheckBox zlocinCheck;
    private JCheckBox naSuduCheck;

    public ObjaviSeansuFrame(Seansa seansa) {
        this.seansa = seansa;

        setTitle("Objava podataka sa seanse");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;

        add(new JLabel("Datum objave:"), gbc);
        gbc.gridx = 1;
        datumChooser = new JDateChooser();
        datumChooser.setDate(new Date());
        add(datumChooser, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Primalac:"), gbc);
        gbc.gridx = 1;
        primalacCombo = new JComboBox<>(new String[]{
                "Policija", "Sud", "Klijent", "Porodica", "Mentor psihoterapeuta", "Centar za socijalni rad", "Poslodavac", "Škola"
        });
        primalacCombo.setEditable(true); // da može ručno da se unese
        add(primalacCombo, gbc);

        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        add(new JLabel("Razlog objave:"), gbc);

        gbc.gridy++;
        razlogArea = new JTextArea(5, 20);
        razlogArea.setLineWrap(true);
        razlogArea.setWrapStyleWord(true);
        JScrollPane razlogScroll = new JScrollPane(razlogArea);
        add(razlogScroll, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        zlocinCheck = new JCheckBox("Zločin");
        add(zlocinCheck, gbc);
        gbc.gridx = 1;
        naSuduCheck = new JCheckBox("Na sudu");
        add(naSuduCheck, gbc);

        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        JButton sacuvajBtn = new JButton("Sačuvaj objavu");
        add(sacuvajBtn, gbc);

        sacuvajBtn.addActionListener(e -> {
            try {
                Date datum = datumChooser.getDate();
                String primalac = (String) primalacCombo.getSelectedItem();
                String razlog = razlogArea.getText().trim();
                boolean zlocin = zlocinCheck.isSelected();
                boolean naSudu = naSuduCheck.isSelected();

                if (datum == null || primalac.isEmpty() || razlog.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Popunite sva polja.", "Greška", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean uspeh = new SeansaDao().objaviSeansu(
                        seansa.getSeansaId(), new java.sql.Date(datum.getTime()), primalac, razlog, zlocin, naSudu
                );

                if (uspeh) {
                    JOptionPane.showMessageDialog(this, "Objava uspešno sačuvana.");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Greška pri čuvanju objave.", "Greška", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Greška prilikom obrade.", "Greška", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }
}
