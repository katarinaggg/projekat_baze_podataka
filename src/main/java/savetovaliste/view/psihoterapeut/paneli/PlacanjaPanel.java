package savetovaliste.view.psihoterapeut.paneli;

import savetovaliste.dao.PlacanjeDao;
import savetovaliste.dao.SeansaDao;
import savetovaliste.model.Klijent;
import savetovaliste.model.PsihoterapeutKandidat;
import savetovaliste.view.psihoterapeut.frameovi.DugovanjaFrame;
import savetovaliste.view.psihoterapeut.frameovi.UplateFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PlacanjaPanel extends JPanel {

    PsihoterapeutKandidat psihoterapeut;
    private JTable tabela;
    private DefaultTableModel model;

    public PlacanjaPanel(PsihoterapeutKandidat psihoterapeut) {
        this.psihoterapeut = psihoterapeut;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel naslov = new JLabel("Klijenti i plaćanja", SwingConstants.CENTER);
        naslov.setFont(new Font("Arial", Font.BOLD, 22));
        naslov.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(naslov, BorderLayout.NORTH);

        model = new DefaultTableModel(new Object[]{"ID", "Ime", "Prezime", "Dug"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(model) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                int modelRow = convertRowIndexToModel(row);
                boolean duguje = (boolean) model.getValueAt(modelRow, 3);

                if (isRowSelected(row)) {
                    c.setBackground(new Color(184, 207, 229));
                } else if (duguje) {
                    c.setBackground(new Color(255, 102, 102));
                } else {
                    c.setBackground(Color.WHITE);
                }
                return c;
            }
        };

        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setRowSelectionAllowed(true);
        tabela.setColumnSelectionAllowed(false);
        tabela.setCellSelectionEnabled(false);
        tabela.setFont(new Font("SansSerif", Font.PLAIN, 16));
        tabela.setRowHeight(28);
        tabela.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));

        JScrollPane scrollPane = new JScrollPane(tabela);
        add(scrollPane, BorderLayout.CENTER);

        popuniTabelu();

        JButton placanjaBtn = new JButton("Prikaži uplate");
        JButton dugovanjaBtn = new JButton("Prikaži dugovanja");

        placanjaBtn.setPreferredSize(new Dimension(200, 40));
        dugovanjaBtn.setPreferredSize(new Dimension(200, 40));
        placanjaBtn.setFont(new Font("Arial", Font.BOLD, 16));
        dugovanjaBtn.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel dugmePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        dugmePanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        dugmePanel.add(placanjaBtn);
        dugmePanel.add(Box.createHorizontalStrut(20));
        dugmePanel.add(dugovanjaBtn);
        add(dugmePanel, BorderLayout.SOUTH);

        // sakrivamo kolone "Dug" i "ID" ali ih ostavljamo u modelu
        tabela.getColumnModel().removeColumn(tabela.getColumnModel().getColumn(3)); // Dug
        tabela.getColumnModel().removeColumn(tabela.getColumnModel().getColumn(0)); // ID

        placanjaBtn.addActionListener(e -> {
            int selectedRow = tabela.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Morate izabrati klijenta iz tabele.");
                return;
            }

            int modelRow = tabela.convertRowIndexToModel(selectedRow);
            int klijentId = (int) model.getValueAt(modelRow, 0);

            new UplateFrame(klijentId, psihoterapeut).setVisible(true);
        });

        dugovanjaBtn.addActionListener(e -> {
            int selectedRow = tabela.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Morate izabrati klijenta iz tabele.");
                return;
            }

            int modelRow = tabela.convertRowIndexToModel(selectedRow);
            int klijentId = (int) model.getValueAt(modelRow, 0);

            new DugovanjaFrame(klijentId, psihoterapeut).setVisible(true);
        });
    }

    private void popuniTabelu() {
        model.setRowCount(0);
        SeansaDao seansaDao = new SeansaDao();
        PlacanjeDao placanjeDao = new PlacanjeDao();
        List<Klijent> klijenti = seansaDao.vratiSveKlijente(psihoterapeut.getPsihoterapeutId());

        for (Klijent k : klijenti) {
            boolean duguje = placanjeDao.daLiKlijentDuguje(k.getId(), psihoterapeut.getPsihoterapeutId());
            model.addRow(new Object[]{k.getId(), k.getIme(), k.getPrezime(), duguje});
        }
    }
}
