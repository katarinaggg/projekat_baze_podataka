package savetovaliste.view.psihoterapeut;

import savetovaliste.dao.SeansaDao;
import savetovaliste.model.PsihoterapeutKandidat;
import savetovaliste.model.Seansa;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OdrzaneSeansePanel extends JPanel {

    PsihoterapeutKandidat psihoterapeut;
    private JTable tabela;
    private DefaultTableModel model;

    public OdrzaneSeansePanel(PsihoterapeutKandidat psihoterapeut) {
        this.psihoterapeut = psihoterapeut;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel naslov = new JLabel("Održane seanse", SwingConstants.CENTER);
        naslov.setFont(new Font("Arial", Font.BOLD, 20));
        naslov.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(naslov, BorderLayout.NORTH);

        model = new DefaultTableModel(new Object[]{
                "Datum", "Vreme", "Klijent", "Beleške", "Trajanje", "Plaćeno"
        }, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(model);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);

        JButton btnObjave = new JButton("Prikaži objave");
        btnObjave.setFont(new Font("SansSerif", Font.PLAIN, 14));

        btnObjave.addActionListener(e -> {
            int selectedRow = tabela.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Morate izabrati seansu iz tabele.");
                return;
            }

            SeansaDao dao = new SeansaDao();
            List<Seansa> lista = dao.vratiOdrzaneSeanse(psihoterapeut.getPsihoterapeutId());
            Seansa selektovana = lista.get(selectedRow);

            new PregledObjavaFrame(selektovana.getSeansaId());
        });

        bottomPanel.add(btnObjave);
        add(bottomPanel, BorderLayout.SOUTH);


        tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = tabela.getSelectedRow();
                    if (selectedRow != -1) {
                        SeansaDao dao = new SeansaDao();
                        List<Seansa> lista = dao.vratiOdrzaneSeanse(psihoterapeut.getPsihoterapeutId());
                        Seansa selektovanaSeansa = lista.get(selectedRow);

                        // otvori novi detaljni prozor
                        SeansaDetaljiFrame detalji = new SeansaDetaljiFrame(selektovanaSeansa);
                        detalji.setVisible(true);
                    }
                }
            }
        });
        osveziTabelu();
    }

    public void osveziTabelu() {
        model.setRowCount(0); // očisti prethodne redove

        SeansaDao dao = new SeansaDao();
        List<Seansa> lista = dao.vratiOdrzaneSeanse(psihoterapeut.getPsihoterapeutId());

        for (Seansa s : lista) {
            model.addRow(new Object[]{
                    s.getDatum(), s.getVreme(), s.getKlijentImePrezime(),
                    s.getBeleske(), s.getTrajanje(), s.getPlacanje() ? "Da" : "Ne"
            });
        }
    }
}