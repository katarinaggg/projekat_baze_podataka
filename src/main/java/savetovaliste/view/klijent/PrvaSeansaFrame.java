package savetovaliste.view.klijent;

import savetovaliste.dao.SeansaDao;
import savetovaliste.model.Klijent;
import savetovaliste.model.PsihoterapeutKandidat;
import savetovaliste.model.Seansa;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PrvaSeansaFrame extends JFrame {

    private JComboBox<String> timeComboBox;
    private JDateChooser dateChooser;

    public PrvaSeansaFrame(Klijent klijent, PsihoterapeutKandidat terapeut) {
        setTitle("Unos prve seanse");
        setSize(450, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Info labela za korisnika
        JTextArea infoLabel = new JTextArea("Radno vreme savetovališta je od 09:00 do 20:00");
        infoLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        infoLabel.setEditable(false);
        infoLabel.setOpaque(false);
        infoLabel.setFocusable(false);
        infoLabel.setLineWrap(true);
        infoLabel.setWrapStyleWord(true);
        add(infoLabel, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        Font font = new Font("SansSerif", Font.PLAIN, 14);

        // Datum
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd.MM.yyyy");
        dateChooser.setDate(new Date());

        // ComboBox za vreme
        timeComboBox = new JComboBox<>();
        popuniVremeCombo(new Date());

        // Dodaj listener za promenu datuma
        dateChooser.getDateEditor().addPropertyChangeListener("date", evt -> {
            Date izabraniDatum = dateChooser.getDate();
            if (izabraniDatum != null) {
                popuniVremeCombo(izabraniDatum);
            }
        });

        // Trajanje
        JTextField trajanjeField = new JTextField("1.0");
        trajanjeField.setFont(font);
        trajanjeField.setEditable(false);

        // Beleške
        JTextField beleskeField = new JTextField("Prva besplatna seansa");
        beleskeField.setFont(font);
        beleskeField.setEditable(false);

        panel.add(new JLabel("Datum seanse:")); panel.add(dateChooser);
        panel.add(new JLabel("Vreme seanse:")); panel.add(timeComboBox);
        panel.add(new JLabel("Trajanje (h):")); panel.add(trajanjeField);
        panel.add(new JLabel("Beleške:")); panel.add(beleskeField);

        JButton btnSacuvaj = new JButton("Sačuvaj seansu");
        btnSacuvaj.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnSacuvaj.addActionListener(e -> {
            try {
                Date datum = dateChooser.getDate();
                String vremeStr = (String) timeComboBox.getSelectedItem();

                if (datum == null || vremeStr == null) {
                    JOptionPane.showMessageDialog(this, "Morate izabrati datum i vreme.", "Greška", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                LocalTime lt = LocalTime.parse(vremeStr, DateTimeFormatter.ofPattern("HH:mm"));
                Time vreme = Time.valueOf(lt);

                Seansa seansa = new Seansa();
                seansa.setDatum(datum);
                seansa.setVreme(vreme);
                seansa.setTrajanje(new BigDecimal(trajanjeField.getText()));
                seansa.setPlacanje(false); // besplatna
                seansa.setOdrzana(true);   // odmah označena kao održana
                seansa.setKlijentId(klijent.getId());
                seansa.setPsihoterapeutId(terapeut.getPsihoterapeutId());
                seansa.setBeleske(beleskeField.getText());
                seansa.setCenaId(null); // jer je besplatna

                SeansaDao dao = new SeansaDao();
                if (dao.postojiZakazanaSeansa(terapeut.getPsihoterapeutId(), datum, vreme)) {
                    JOptionPane.showMessageDialog(this, "Izabrani termin je već zauzet za ovog terapeuta!", "Zauzet termin", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                boolean ok = dao.unesiSeansuProcedura(seansa);

                if (ok) {
                    JOptionPane.showMessageDialog(this, "Prva seansa uspešno uneta!", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Greška pri unosu seanse.", "Greška", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Došlo je do greške: " + ex.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(panel, BorderLayout.CENTER);
        JPanel bottom = new JPanel();
        bottom.add(btnSacuvaj);
        add(bottom, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void popuniVremeCombo(Date datum) {
        timeComboBox.removeAllItems();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int currentHour = cal.get(Calendar.HOUR_OF_DAY);

        boolean danas = isIstiDan(datum, new Date());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        for (int i = 9; i <= 20; i++) {
            if (!danas || i > currentHour) {
                timeComboBox.addItem(LocalTime.of(i, 0).format(formatter));
            }
        }
    }

    private boolean isIstiDan(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
                c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);
    }
}
