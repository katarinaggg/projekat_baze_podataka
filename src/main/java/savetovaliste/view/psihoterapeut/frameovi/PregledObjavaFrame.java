package savetovaliste.view.psihoterapeut.frameovi;

import savetovaliste.dao.SeansaDao;
import savetovaliste.model.Objava;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class PregledObjavaFrame extends JFrame {

    public PregledObjavaFrame(int seansaId) {
        setTitle("Pregled objava za seansu #" + seansaId);
        setSize(650, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JLabel naslov = new JLabel("Objave za seansu", SwingConstants.CENTER);
        naslov.setFont(new Font("SansSerif", Font.BOLD, 26));
        naslov.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(naslov, BorderLayout.NORTH);

        // Centralni panel koji će ići u scroll pane
        JPanel sadrzajPanel = new JPanel();
        sadrzajPanel.setLayout(new BoxLayout(sadrzajPanel, BoxLayout.Y_AXIS));
        sadrzajPanel.setBackground(Color.WHITE);
        sadrzajPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        List<Objava> objave = new SeansaDao().getObjaveZaSeansu(seansaId);
        if (objave.isEmpty()) {
            JLabel nema = new JLabel("Za ovu seansu ne postoji nijedna objava.");
            nema.setFont(new Font("SansSerif", Font.ITALIC, 16));
            nema.setAlignmentX(Component.CENTER_ALIGNMENT);
            sadrzajPanel.add(nema);
        } else {
            for (Objava o : objave) {
                JPanel objavaPanel = napraviPanelZaObjavu(o);
                sadrzajPanel.add(objavaPanel);
                sadrzajPanel.add(Box.createRigidArea(new Dimension(0, 15))); // razmak između objava
            }
        }

        JScrollPane scrollPane = new JScrollPane(sadrzajPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

    }

    private JPanel napraviPanelZaObjavu(Objava o) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy.");

        panel.add(kreirajRed("Datum objave", formater.format(o.getDatumObjave())));
        panel.add(kreirajRed("Primalac", o.getPrimalac()));
        panel.add(kreirajRed("Razlog", o.getRazlog()));

        return panel;
    }

    private JPanel kreirajRed(String labela, String vrednost) {
        JPanel red = new JPanel(new BorderLayout());
        red.setBackground(new Color(245, 245, 245));
        red.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JLabel l = new JLabel(labela + ": ");
        l.setFont(new Font("SansSerif", Font.BOLD, 16));

        JLabel v = new JLabel(vrednost);
        v.setFont(new Font("SansSerif", Font.PLAIN, 16));

        red.add(l, BorderLayout.WEST);
        red.add(v, BorderLayout.CENTER);
        return red;
    }
}
