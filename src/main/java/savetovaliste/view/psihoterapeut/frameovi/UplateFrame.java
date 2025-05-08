package savetovaliste.view.psihoterapeut.frameovi;

import savetovaliste.dao.PlacanjeDao;
import savetovaliste.model.Placanje;
import savetovaliste.model.PsihoterapeutKandidat;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class UplateFrame extends JFrame {

    private JTable tabela;
    private DefaultTableModel model;

    public UplateFrame(int klijentId, PsihoterapeutKandidat psihoterapeut) {
        setTitle("Pregled uplata klijenta");
        setSize(950, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel naslov = new JLabel("Sve uplate klijenta", SwingConstants.CENTER);
        naslov.setFont(new Font("Arial", Font.BOLD, 20));
        naslov.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(naslov, BorderLayout.NORTH);

        List<Placanje> uplate = new PlacanjeDao()
                .vratiUplateZaKlijentaIPsihoterapeuta(klijentId, psihoterapeut.getPsihoterapeutId());

        if (uplate.isEmpty()) {
            JPanel sadrzajPanel = new JPanel();
            sadrzajPanel.setLayout(new BoxLayout(sadrzajPanel, BoxLayout.Y_AXIS));
            sadrzajPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

            JLabel nema = new JLabel("Klijent nema nijednu uplatu.");
            nema.setFont(new Font("SansSerif", Font.ITALIC, 16));
            nema.setAlignmentX(Component.CENTER_ALIGNMENT);
            sadrzajPanel.add(nema);

            add(sadrzajPanel, BorderLayout.CENTER);
        } else {
            model = new DefaultTableModel(new Object[]{
                    "Datum uplate", "Iznos", "Svrha", "Napomena", "Rata", "Provizija 5%", "Valuta", "Način plaćanja"
            }, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            tabela = new JTable(model);
            tabela.setFont(new Font("SansSerif", Font.PLAIN, 14));
            tabela.setRowHeight(24);
            tabela.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

            tabela.getColumnModel().getColumn(2).setPreferredWidth(200);
            tabela.getColumnModel().getColumn(3).setPreferredWidth(220);
            tabela.getColumnModel().getColumn(4).setPreferredWidth(40);
            tabela.getColumnModel().getColumn(5).setPreferredWidth(40);

            JScrollPane scrollPane = new JScrollPane(tabela);
            add(scrollPane, BorderLayout.CENTER);

            popuniTabelu(uplate);
        }

        setVisible(true);
    }

    private void popuniTabelu(List<Placanje> uplate) {
        model.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy.");

        for (Placanje p : uplate) {
            model.addRow(new Object[]{
                    sdf.format(p.getDatumUplate()),
                    p.getIznos(),
                    p.getSvrha(),
                    p.getNapomena() != null ? p.getNapomena() : "/",
                    p.getRata() != null ? p.getRata() : "/",
                    p.isProvizija5Posto() ? "Da" : "Ne",
                    p.getValutaId(),
                    p.getNacinPlacanjaNaziv()
            });
        }
    }
}
