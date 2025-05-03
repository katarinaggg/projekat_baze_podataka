package savetovaliste.view;

import com.toedter.calendar.JDateChooser;
import savetovaliste.model.PsihoterapeutKandidat;

import javax.swing.*;
import java.awt.*;

public class UpitnikFrame extends JFrame {
    PsihoterapeutKandidat psihoterapeut;
    public UpitnikFrame(PsihoterapeutKandidat psihoterapeut) {
        this.psihoterapeut = psihoterapeut;
        setTitle("Upitnik - Prijava klijenta");
        setSize(800, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JTextField tfIme = new JTextField();
        JTextField tfPrezime = new JTextField();
        JDateChooser datumChooser = new JDateChooser();
        datumChooser.setDateFormatString("yyyy-MM-dd");
        JComboBox<String> cbPol = new JComboBox<>(new String[]{"Muški", "Ženski"});
        JTextField tfEmail = new JTextField();
        JTextField tfTelefon = new JTextField();
        JCheckBox cbIsaoRanije = new JCheckBox("Već je išao kod psihoterapeuta");
        JTextArea taProblem = new JTextArea(5, 20);

        panel.add(new JLabel("Ime:")); panel.add(tfIme);
        panel.add(new JLabel("Prezime:")); panel.add(tfPrezime);
        panel.add(new JLabel("Datum rođenja:")); panel.add(datumChooser);
        panel.add(new JLabel("Pol:")); panel.add(cbPol);
        panel.add(new JLabel("Email:")); panel.add(tfEmail);
        panel.add(new JLabel("Telefon:")); panel.add(tfTelefon);
        panel.add(new JLabel("")); panel.add(cbIsaoRanije);
        panel.add(new JLabel("Opis problema:")); panel.add(new JScrollPane(taProblem));

        JButton btnPosalji = new JButton("Pošalji prijavu");
        btnPosalji.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Prijava uspešno poslata. Prva seansa je besplatna.", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
        });


        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(btnPosalji);
        add(panel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);


        setVisible(true);
    }
}
