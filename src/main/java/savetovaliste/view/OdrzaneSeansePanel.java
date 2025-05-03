package savetovaliste.view;

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

        model = new DefaultTableModel(new Object[]{
                "Datum", "Vreme", "Klijent", "Beleške", "Trajanje", "Plaćeno"
        }, 0);

        tabela = new JTable(model);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        SeansaDao dao = new SeansaDao();
        List<Seansa> seanse = dao.vratiOdrzaneSeanse(psihoterapeut.getPsihoterapeutId());

        for (Seansa s : seanse) {
            model.addRow(new Object[]{
                    s.getDatum(), s.getVreme(), s.getKlijentImePrezime(),
                    s.getBeleske(), s.getTrajanje(), s.isPlacanje() ? "Da" : "Ne"
            });
        }
    }
}
