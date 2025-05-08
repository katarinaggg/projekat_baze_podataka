package savetovaliste.view.psihoterapeut.paneli;

import savetovaliste.dao.SeansaDao;
import savetovaliste.model.PsihoterapeutKandidat;
import savetovaliste.model.Seansa;
import savetovaliste.view.psihoterapeut.frameovi.ObjaviSeansuFrame;
import savetovaliste.view.psihoterapeut.frameovi.PregledObjavaFrame;
import savetovaliste.view.psihoterapeut.frameovi.SeansaDetaljiFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class OdrzaneSeansePanel extends JPanel {

    private PsihoterapeutKandidat psihoterapeut;
    private JTable tabela;
    private DefaultTableModel model;
    private List<Seansa> listaSeansi;  // NOVO: lokalna lista

    public OdrzaneSeansePanel(PsihoterapeutKandidat psihoterapeut) {
        this.psihoterapeut = psihoterapeut;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel naslov = new JLabel("Održane seanse", SwingConstants.CENTER);
        naslov.setFont(new Font("Arial", Font.BOLD, 22));
        naslov.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(naslov, BorderLayout.NORTH);

        model = new DefaultTableModel(new Object[]{
                "Datum", "Vreme", "Klijent", "Trajanje"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(model);
        tabela.setFont(new Font("SansSerif", Font.PLAIN, 16));
        tabela.setRowHeight(28);
        tabela.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));

        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JButton btnObjave = new JButton("Prikaži objave");
        btnObjave.setPreferredSize(new Dimension(200, 40));
        btnObjave.setFont(new Font("Arial", Font.BOLD, 16));
        btnObjave.addActionListener(e -> {
            int selectedRow = tabela.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Morate izabrati seansu iz tabele.");
                return;
            }

            int modelRow = tabela.convertRowIndexToModel(selectedRow);
            Seansa selektovana = listaSeansi.get(modelRow);

            new PregledObjavaFrame(selektovana.getSeansaId()).setVisible(true);
        });

        JButton btnObjavi = new JButton("Objavi seansu");
        btnObjavi.setPreferredSize(new Dimension(200, 40));
        btnObjavi.setFont(new Font("Arial", Font.BOLD, 16));
        btnObjavi.addActionListener(e -> {
            int selectedRow = tabela.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Morate izabrati seansu iz tabele.");
                return;
            }

            int modelRow = tabela.convertRowIndexToModel(selectedRow);
            Seansa selektovana = listaSeansi.get(modelRow);

            new ObjaviSeansuFrame(selektovana).setVisible(true);
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        bottomPanel.add(btnObjave);
        bottomPanel.add(Box.createHorizontalStrut(20));
        bottomPanel.add(btnObjavi);
        add(bottomPanel, BorderLayout.SOUTH);

        tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = tabela.getSelectedRow();
                    if (selectedRow != -1) {
                        int modelRow = tabela.convertRowIndexToModel(selectedRow);
                        Seansa selektovanaSeansa = listaSeansi.get(modelRow);

                        new SeansaDetaljiFrame(selektovanaSeansa).setVisible(true);
                    }
                }
            }
        });

        osveziTabelu();
    }

    public void osveziTabelu() {
        model.setRowCount(0);

        SeansaDao dao = new SeansaDao();
        listaSeansi = dao.vratiOdrzaneSeanse(psihoterapeut.getPsihoterapeutId());  // NOVO: lokalna lista

        SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy.");

        for (Seansa s : listaSeansi) {
            model.addRow(new Object[]{
                    formater.format(s.getDatum()),
                    s.getVreme(),
                    s.getKlijentImePrezime(),
                    s.getTrajanje()
            });
        }
    }
}

