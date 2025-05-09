package savetovaliste.view.psihoterapeut.paneli;

import savetovaliste.dao.SeansaDao;
import savetovaliste.model.PsihoterapeutKandidat;
import savetovaliste.model.Seansa;
import savetovaliste.view.psihoterapeut.frameovi.OdrziSeansuFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class BuduceSeansePanel extends JPanel {

    private JTable tabela;
    private DefaultTableModel model;
    private PsihoterapeutKandidat psihoterapeut;

    public BuduceSeansePanel(PsihoterapeutKandidat psihoterapeut, OdrzaneSeansePanel odrzaneSeansePanel) {
        this.psihoterapeut = psihoterapeut;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel naslov = new JLabel("Termini budućih seansi", SwingConstants.CENTER);
        naslov.setFont(new Font("Arial", Font.BOLD, 22));
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
        tabela.setFont(new Font("SansSerif", Font.PLAIN, 16));
        tabela.setRowHeight(28);
        tabela.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));

        JScrollPane scrollPane = new JScrollPane(tabela);
        add(scrollPane, BorderLayout.CENTER);

        // SAKRIVANJE KOLONE "Seansa ID"
        tabela.getColumnModel().getColumn(3).setMinWidth(0);
        tabela.getColumnModel().getColumn(3).setMaxWidth(0);
        tabela.getColumnModel().getColumn(3).setWidth(0);

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

            // Čitamo ID iz sakrivene kolone
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
        model.setRowCount(0);

        SeansaDao dao = new SeansaDao();
        List<Seansa> lista = dao.vratiBuduceSeanse(psihoterapeut.getPsihoterapeutId());

        SimpleDateFormat datumFormater = new SimpleDateFormat("dd.MM.yyyy.");
        SimpleDateFormat vremeFormatter = new SimpleDateFormat("HH:mm");

        for (Seansa s : lista) {
            model.addRow(new Object[]{
                    datumFormater.format(s.getDatum()),
                    vremeFormatter.format(s.getVreme()),
                    s.getKlijentImePrezime(),
                    s.getSeansaId()
            });
        }
    }
}
