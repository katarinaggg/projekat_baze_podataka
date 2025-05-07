package savetovaliste.view.psihoterapeut;

import savetovaliste.dao.SeansaDao;
import savetovaliste.model.Objava;
import savetovaliste.model.Seansa;
import savetovaliste.model.TestRezultatDTO;

import java.text.SimpleDateFormat;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SeansaDetaljiFrame extends JFrame {

    public SeansaDetaljiFrame(Seansa seansa) {
        setTitle("Detalji seanse");
        setSize(500, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Font boldFont = new Font("SansSerif", Font.PLAIN, 16);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        JLabel datumLabel = new JLabel("Datum: " + dateFormat.format(seansa.getDatum()));
        datumLabel.setFont(boldFont);
        panel.add(datumLabel);

        SimpleDateFormat vremeFormat = new SimpleDateFormat("HH:mm");
        JLabel vremeLabel = new JLabel("Vreme: " + vremeFormat.format(seansa.getVreme()));
        vremeLabel.setFont(boldFont);
        panel.add(vremeLabel);

        JLabel klijentLabel = new JLabel("Klijent: " + seansa.getKlijentImePrezime());
        klijentLabel.setFont(boldFont);
        panel.add(klijentLabel);

        JLabel trajanjeLabel = new JLabel("Trajanje: " + seansa.getTrajanje() + " h");
        trajanjeLabel.setFont(boldFont);
        panel.add(trajanjeLabel);

        JLabel placanjeLabel = new JLabel("Plaćeno: " + (seansa.getPlacanje() ? "Da" : "Ne"));
        placanjeLabel.setFont(boldFont);
        panel.add(placanjeLabel);

        JLabel zlocinLabel = new JLabel("Zločin: " + (seansa.getZlocin() ? "Da" : "Ne"));
        zlocinLabel.setFont(boldFont);
        panel.add(zlocinLabel);

        JLabel sudLabel = new JLabel("Na sudu: " + (seansa.getNaSudu() ? "Da" : "Ne"));
        sudLabel.setFont(boldFont);
        panel.add(sudLabel);

        // Beleške
        JLabel beleskeLabel = new JLabel("Beleške:");
        beleskeLabel.setFont(boldFont);
        panel.add(beleskeLabel);

        JTextArea textArea = new JTextArea(seansa.getBeleske());
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        panel.add(new JScrollPane(textArea));

// Testovi (ako ih ima)
        List<TestRezultatDTO> testovi = new SeansaDao().vratiTestoveZaSeansu(seansa.getSeansaId());

        if (!testovi.isEmpty()) {
            JLabel testoviLabel = new JLabel("Testovi urađeni tokom seanse:");
            testoviLabel.setFont(boldFont);
            testoviLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
            panel.add(testoviLabel);

            String[] kolone = {"Naziv testa", "Cena", "Rezultat"};
            DefaultTableModel model = new DefaultTableModel(kolone, 0);

            for (TestRezultatDTO t : testovi) {
                model.addRow(new Object[]{
                        t.getNaziv(), t.getCena() + " RSD", t.getRezultat()
                });
            }

            JTable tabela = new JTable(model);
            tabela.setFillsViewportHeight(true);
            tabela.setRowHeight(24);

            JScrollPane scrollPane = new JScrollPane(tabela);
            scrollPane.setPreferredSize(new Dimension(400, 100));
            panel.add(scrollPane);

        }

        // --- Pregled objava seanse ---
        List<Objava> objave = new SeansaDao().getObjaveZaSeansu(seansa.getSeansaId());

        if (objave.isEmpty()) {
            JLabel nemaObjavaLabel = new JLabel("Podaci sa ove seanse nisu objavljeni trećim licima.");
            nemaObjavaLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
            nemaObjavaLabel.setForeground(Color.GRAY);
            nemaObjavaLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
            panel.add(nemaObjavaLabel);
        } else {
            JLabel objaveLabel = new JLabel("Objave podataka sa ove seanse:");
            objaveLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
            objaveLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
            panel.add(objaveLabel);

            String[] kolone = {"Datum objave", "Primalac", "Razlog"};
            DefaultTableModel objaveModel = new DefaultTableModel(kolone, 0);
            for (savetovaliste.model.Objava o : objave) {
                objaveModel.addRow(new Object[]{
                        o.getDatumObjave(), o.getPrimalac(), o.getRazlog()
                });
            }

            JTable objavaTabela = new JTable(objaveModel);
            objavaTabela.setFillsViewportHeight(true);
            objavaTabela.setRowHeight(24);

            JScrollPane scrollObjave = new JScrollPane(objavaTabela);
            scrollObjave.setPreferredSize(new Dimension(400, 100));
            panel.add(scrollObjave);
        }



        add(panel);
    }
}
