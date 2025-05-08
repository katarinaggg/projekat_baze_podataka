package savetovaliste.view.psihoterapeut.frameovi;

import savetovaliste.model.Dugovanje;
import savetovaliste.model.PsihoterapeutKandidat;
import savetovaliste.dao.PlacanjeDao;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class DugovanjaFrame extends JFrame {

    public DugovanjaFrame(int klijentId, PsihoterapeutKandidat psihoterapeut) {
        setTitle("Dugovanja klijenta");
        setSize(750, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JLabel naslov = new JLabel("Pregled dugovanja", SwingConstants.CENTER);
        naslov.setFont(new Font("SansSerif", Font.BOLD, 24));
        naslov.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(naslov, BorderLayout.NORTH);

        JPanel sadrzajPanel = new JPanel();
        sadrzajPanel.setLayout(new BoxLayout(sadrzajPanel, BoxLayout.Y_AXIS));
        sadrzajPanel.setBackground(Color.WHITE);
        sadrzajPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        List<Dugovanje> dugovanja = new PlacanjeDao()
                .vratiDugovanjaZaKlijentaIpsiho(klijentId, psihoterapeut.getPsihoterapeutId());

        if (dugovanja.isEmpty()) {
            JLabel nema = new JLabel("Klijent nema nijedno dugovanje.");
            nema.setFont(new Font("SansSerif", Font.ITALIC, 16));
            nema.setAlignmentX(Component.CENTER_ALIGNMENT);
            nema.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
            sadrzajPanel.add(nema);
        } else {
            for (Dugovanje d : dugovanja) {
                JPanel panel = napraviPanelZaDugovanje(d);
                sadrzajPanel.add(panel);
                sadrzajPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }

        JScrollPane scrollPane = new JScrollPane(sadrzajPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel napraviPanelZaDugovanje(Dugovanje d) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(255, 240, 240));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 100, 100)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        panel.setMaximumSize(new Dimension(600, Integer.MAX_VALUE)); // fiksiramo širinu

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy.");

        panel.add(kreirajRed("Datum seanse", sdf.format(d.getDatum())));
        panel.add(kreirajRed("Dugovani iznos", d.getIznos() + " " + d.getValuta()));
        panel.add(kreirajRed("Opis dugovanja", "<html>" + d.getOpis() + "</html>"));

        return panel;
    }

    private JPanel kreirajRed(String labela, String vrednost) {
        JPanel red = new JPanel(new BorderLayout());
        red.setBackground(new Color(255, 240, 240));
        red.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JLabel l = new JLabel(labela + ": ");
        l.setFont(new Font("SansSerif", Font.BOLD, 15));

        JLabel v = new JLabel(vrednost);
        v.setFont(new Font("SansSerif", Font.PLAIN, 15));
        v.setAlignmentX(Component.LEFT_ALIGNMENT);

        red.add(l, BorderLayout.WEST);
        red.add(v, BorderLayout.CENTER);
        return red;
    }
}
