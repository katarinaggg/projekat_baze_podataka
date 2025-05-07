package savetovaliste.view.psihoterapeut;

import savetovaliste.dao.SeansaDao;
import savetovaliste.model.PsihoterapeutKandidat;
import savetovaliste.model.Seansa;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BuduceSeansePanel extends JPanel {

    private JTable tabela;
    private DefaultTableModel model;

    PsihoterapeutKandidat psihoterapeut;

    public BuduceSeansePanel(PsihoterapeutKandidat psihoterapeut, OdrzaneSeansePanel odrzaneSeansePanel) {
        this.psihoterapeut = psihoterapeut;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel naslov = new JLabel("Termini budućih seansi", SwingConstants.CENTER);
        naslov.setFont(new Font("Arial", Font.BOLD, 20));
        naslov.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(naslov, BorderLayout.NORTH);

        model = new DefaultTableModel(new Object[]{
                "Datum", "Vreme", "Klijent", "Seansa ID"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(model);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JButton unesiBtn = new JButton("Unesi seansu");
        unesiBtn.setPreferredSize(new Dimension(200, 40));
        unesiBtn.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel dugmePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        dugmePanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        dugmePanel.add(unesiBtn);
        add(dugmePanel, BorderLayout.SOUTH);

        unesiBtn.addActionListener(e -> {
            int selectedRow = tabela.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Morate selektovati seansu.", "Greška", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int seansaId = (int) model.getValueAt(selectedRow, 3);
            SeansaDao dao = new SeansaDao();
            Seansa s = dao.nadjiSeansuPoId(seansaId);

            if (s != null) {
                new OdrziSeansuFrame(s, this, odrzaneSeansePanel).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Greška pri pronalaženju seanse.");
            }
        });

        osveziTabelu();
    }

    public void osveziTabelu() {
        model.setRowCount(0); // očisti prethodne redove

        SeansaDao dao = new SeansaDao();
        List<Seansa> lista = dao.vratiBuduceSeanse(psihoterapeut.getPsihoterapeutId());

        for (Seansa s : lista) {
            model.addRow(new Object[]{
                    s.getDatum(),
                    s.getVreme(),
                    s.getKlijentImePrezime(),
                    s.getSeansaId()
            });
        }
    }

}
