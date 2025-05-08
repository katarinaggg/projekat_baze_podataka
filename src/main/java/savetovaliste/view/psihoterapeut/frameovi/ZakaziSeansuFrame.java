package savetovaliste.view.psihoterapeut.frameovi;

import com.toedter.calendar.JDateChooser;
import savetovaliste.dao.SeansaDao;
import savetovaliste.model.Klijent;
import savetovaliste.model.PsihoterapeutKandidat;
import savetovaliste.view.psihoterapeut.paneli.BuduceSeansePanel;

import javax.swing.*;
import java.awt.*;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ZakaziSeansuFrame extends JFrame {

    private JDateChooser dateChooser;
    private JSpinner timeSpinner;

    Klijent klijent;
    PsihoterapeutKandidat psihoterapeut;
    BuduceSeansePanel buduceSeansePanel;

    public ZakaziSeansuFrame(Klijent klijent, PsihoterapeutKandidat psihoterapeut, BuduceSeansePanel buduceSeansePanel) throws HeadlessException {
        this.klijent = klijent;
        this.psihoterapeut = psihoterapeut;
        this.buduceSeansePanel = buduceSeansePanel;

        setTitle("Zakaži seansu");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
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
        timeSpinner.setEditor(new JSpinner.DateEditor(timeSpinner, "HH:mm"));
        add(timeSpinner, gbc);

        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        JButton zakaziBtn = new JButton("Zakaži");
        zakaziBtn.setPreferredSize(new Dimension(150, 30));
        add(zakaziBtn, gbc);

        zakaziBtn.addActionListener(e -> {
            Date datum = dateChooser.getDate();
            String vremeStr = new SimpleDateFormat("HH:mm:ss").format((Date) timeSpinner.getValue());

            if (datum == null || vremeStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Unesite datum i vreme.", "Greška", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Time vreme = Time.valueOf(vremeStr);
                SeansaDao seansaDao = new SeansaDao();

                // moramo prvo da proverimo dal vec postoji zakazana seansa u ovom izabranom terminu
                if(seansaDao.postojiZakazanaSeansa(psihoterapeut.getPsihoterapeutId(), datum, vreme)) {
                    JOptionPane.showMessageDialog(this, "Već postoji zakazana seansa u ovom terminu.", "Greška", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean uspeh = seansaDao.zakaziSeansu(klijent.getId(), psihoterapeut.getPsihoterapeutId(), datum, vreme);
                if (uspeh) {
                    JOptionPane.showMessageDialog(this, "Seansa uspešno zakazana.");
                    buduceSeansePanel.osveziTabelu();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Greška prilikom zakazivanja.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Greška pri obradi vremena.", "Greška", JOptionPane.ERROR_MESSAGE);
            }
        });

    }
}
