package savetovaliste.view.psihoterapeut.frameovi;

import savetovaliste.dao.SeansaDao;
import savetovaliste.model.Objava;
import savetovaliste.model.Seansa;
import savetovaliste.model.TestRezultatDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class SeansaDetaljiFrame extends JFrame {

    public SeansaDetaljiFrame(Seansa seansa) {
        setTitle("Detalji seanse");
        setSize(800, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Color pozadina = new Color(227, 199, 163);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(pozadina);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        Font boldFont = new Font("SansSerif", Font.BOLD, 16);
        Font plainFont = new Font("SansSerif", Font.PLAIN, 16);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy.");
        SimpleDateFormat vremeFormat = new SimpleDateFormat("HH:mm");

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(pozadina);

        infoPanel.add(kreirajRed("Datum:", dateFormat.format(seansa.getDatum()), boldFont, plainFont, pozadina));
        infoPanel.add(kreirajRed("Vreme:", vremeFormat.format(seansa.getVreme()), boldFont, plainFont, pozadina));
        infoPanel.add(kreirajRed("Klijent:", seansa.getKlijentImePrezime(), boldFont, plainFont, pozadina));
        infoPanel.add(kreirajRed("Trajanje:", seansa.getTrajanje() + " h", boldFont, plainFont, pozadina));
        infoPanel.add(kreirajRed("Plaćeno:", seansa.getPlacanje() ? "Da" : "Ne", boldFont, plainFont, pozadina));
        infoPanel.add(kreirajRed("Zločin:", seansa.getZlocin() ? "Da" : "Ne", boldFont, plainFont, pozadina));
        infoPanel.add(kreirajRed("Na sudu:", seansa.getNaSudu() ? "Da" : "Ne", boldFont, plainFont, pozadina));

        panel.add(infoPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel beleskeLabel = new JLabel("Beleške:");
        beleskeLabel.setFont(boldFont);
        beleskeLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        beleskeLabel.setForeground(Color.BLACK);
        JPanel beleskeNaslovPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        beleskeNaslovPanel.setBackground(pozadina);
        beleskeNaslovPanel.add(beleskeLabel);
        panel.add(beleskeNaslovPanel);

        JTextArea textArea = new JTextArea(seansa.getBeleske());
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setFont(plainFont);
        JScrollPane beleskeScroll = new JScrollPane(textArea);
        beleskeScroll.setPreferredSize(new Dimension(700, 100));
        panel.add(beleskeScroll);

        List<TestRezultatDTO> testovi = new SeansaDao().vratiTestoveZaSeansu(seansa.getSeansaId());
        if (!testovi.isEmpty()) {
            JLabel testoviLabel = new JLabel("Testovi urađeni tokom seanse:");
            testoviLabel.setFont(boldFont);
            testoviLabel.setForeground(Color.BLACK);
            JPanel naslovPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            naslovPanel.setBackground(pozadina);
            naslovPanel.add(testoviLabel);
            panel.add(naslovPanel);

            String[] kolone = {"Naziv testa", "Cena", "Rezultat"};
            DefaultTableModel model = new DefaultTableModel(kolone, 0);
            for (TestRezultatDTO t : testovi) {
                model.addRow(new Object[]{
                        t.getNaziv(), t.getCena() + " RSD", t.getRezultat()
                });
            }
            JTable tabela = new JTable(model);
            tabela.setFont(plainFont);
            tabela.setRowHeight(24);
            tabela.setShowGrid(true);
            tabela.setGridColor(Color.DARK_GRAY);
            JScrollPane scrollPane = new JScrollPane(tabela);
            scrollPane.setPreferredSize(new Dimension(700, 100));
            panel.add(scrollPane);
        } else{
            JPanel porukaTestPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            porukaTestPanel.setBackground(pozadina);
            JLabel nemaTestovaLabel = new JLabel("Klijent nije radio nijedan test tokom ove seanse.");
            nemaTestovaLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
            nemaTestovaLabel.setForeground(new Color(153, 0, 0));
            nemaTestovaLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
            porukaTestPanel.add(nemaTestovaLabel);
            panel.add(porukaTestPanel);
        }

        List<Objava> objave = new SeansaDao().getObjaveZaSeansu(seansa.getSeansaId());
        if (objave.isEmpty()) {
            JPanel porukaObjavaPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            porukaObjavaPanel.setBackground(pozadina);
            JLabel nemaObjavaLabel = new JLabel("Podaci sa ove seanse nisu objavljeni trećim licima.");
            nemaObjavaLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
            nemaObjavaLabel.setForeground(new Color(153, 0, 0));
            nemaObjavaLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
            porukaObjavaPanel.add(nemaObjavaLabel);
            panel.add(porukaObjavaPanel);

        } else {
            JLabel objaveLabel = new JLabel("Objave podataka sa ove seanse:");
            objaveLabel.setFont(boldFont);
            objaveLabel.setForeground(Color.BLACK);
            JPanel naslovPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            naslovPanel.setBackground(pozadina);
            naslovPanel.add(objaveLabel);
            panel.add(naslovPanel);

            String[] kolone = {"Datum objave", "Primalac", "Razlog"};
            DefaultTableModel objaveModel = new DefaultTableModel(kolone, 0);
            for (Objava o : objave) {
                objaveModel.addRow(new Object[]{
                        dateFormat.format(o.getDatumObjave()), o.getPrimalac(), o.getRazlog()
                });
            }
            JTable objavaTabela = new JTable(objaveModel);
            objavaTabela.setFont(plainFont);
            objavaTabela.setRowHeight(24);
            objavaTabela.setShowGrid(true);
            objavaTabela.setGridColor(Color.DARK_GRAY);
            JScrollPane scrollObjave = new JScrollPane(objavaTabela);
            scrollObjave.setPreferredSize(new Dimension(700, 100));
            panel.add(scrollObjave);
        }

        JScrollPane mainScroll = new JScrollPane(panel);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);
        setContentPane(mainScroll);
    }

    private JPanel kreirajRed(String labelaText, String vrednostText, Font labelFont, Font valueFont, Color pozadina) {
        JPanel red = new JPanel(new BorderLayout());
        red.setBackground(pozadina);
        red.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));

        JLabel labela = new JLabel(labelaText);
        labela.setFont(labelFont);
        labela.setPreferredSize(new Dimension(150, 25));

        JLabel vrednost = new JLabel(vrednostText);
        vrednost.setFont(valueFont);
        vrednost.setHorizontalAlignment(SwingConstants.LEFT);

        red.add(labela, BorderLayout.WEST);
        red.add(vrednost, BorderLayout.CENTER);
        return red;
    }
}
