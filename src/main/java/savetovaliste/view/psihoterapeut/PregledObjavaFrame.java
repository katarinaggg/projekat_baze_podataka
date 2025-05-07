package savetovaliste.view.psihoterapeut;

import savetovaliste.dao.SeansaDao;
import savetovaliste.model.Objava;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PregledObjavaFrame extends JFrame {
    public PregledObjavaFrame(int seansaId) {
        setTitle("Pregled objava za seansu #" + seansaId);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        String[] kolone = {"Datum objave", "Primalac", "Razlog"};
        DefaultTableModel model = new DefaultTableModel(kolone, 0);
        JTable tabela = new JTable(model);

        List<Objava> objave = new SeansaDao().getObjaveZaSeansu(seansaId);
        for (Objava o : objave) {
            model.addRow(new Object[]{
                    o.getDatumObjave(),
                    o.getPrimalac(),
                    o.getRazlog()
            });
        }

        JScrollPane scroll = new JScrollPane(tabela);
        add(scroll, BorderLayout.CENTER);
        setVisible(true);
    }
}
