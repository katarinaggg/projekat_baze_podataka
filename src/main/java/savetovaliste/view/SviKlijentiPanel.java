package savetovaliste.view;

import savetovaliste.dao.KlijentDao;
import savetovaliste.dao.SeansaDao;
import savetovaliste.model.Klijent;
import savetovaliste.model.PsihoterapeutKandidat;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SviKlijentiPanel extends JPanel {

    PsihoterapeutKandidat psihoterapeut;
    private JTable tabela;
    private DefaultTableModel model;

    // treba nam da bi ga azurirali kada zakazemo novu seansu
    // (da bi se automatski dodala u tom panelu)
    private BuduceSeansePanel buduceSeansePanel;

    public SviKlijentiPanel(PsihoterapeutKandidat psihoterapeut, BuduceSeansePanel buduceSeansePanel) {
        this.psihoterapeut = psihoterapeut;
        this.buduceSeansePanel = buduceSeansePanel;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel naslov = new JLabel("Svi klijenti", SwingConstants.CENTER);
        naslov.setFont(new Font("Arial", Font.BOLD, 20));
        naslov.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(naslov, BorderLayout.NORTH);

        model = new DefaultTableModel(new Object[]{
                "Ime", "Prezime", "Pol", "Datum rođenja", "Telefon", "Email", "Opis problema", "Prvi put"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tabela);
        add(scrollPane, BorderLayout.CENTER);

        popuniTabelu(psihoterapeut);

        JButton zakaziSeansuBtn = new JButton("Zakaži seansu");
        zakaziSeansuBtn.addActionListener(e -> {
            int selectedRow = tabela.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Morate selektovati klijenta iz tabele.", "Greška", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String email = (String) model.getValueAt(selectedRow, 5);
            KlijentDao klijentDao = new KlijentDao();
            Klijent selektovani = klijentDao.nadjiKlijentaPoEmailu(email);

            if (selektovani != null) {
                new ZakaziSeansuFrame(selektovani, psihoterapeut, buduceSeansePanel).setVisible(true);
            }
        });

        JPanel dugmePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        dugmePanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        zakaziSeansuBtn.setPreferredSize(new Dimension(200, 40));
        zakaziSeansuBtn.setFont(new Font("Arial", Font.BOLD, 16));
        dugmePanel.add(zakaziSeansuBtn);
        add(dugmePanel, BorderLayout.SOUTH);


    }

    private void popuniTabelu(PsihoterapeutKandidat psihoterapeut) {
        model.setRowCount(0); // očisti prethodne redove

        SeansaDao seansaDao = new SeansaDao();
        List<Klijent> klijenti = seansaDao.vratiSveKlijente(psihoterapeut.getPsihoterapeutId());

        for (Klijent k : klijenti) {
            model.addRow(new Object[]{
                    k.getIme(),
                    k.getPrezime(),
                    k.getPol(),
                    k.getDatumRodjenja(),
                    k.getTelefon(),
                    k.getEmail(),
                    k.getOpis(),
                    k.isPrviPut() ? "Da" : "Ne"

            });
        }
    }
}
