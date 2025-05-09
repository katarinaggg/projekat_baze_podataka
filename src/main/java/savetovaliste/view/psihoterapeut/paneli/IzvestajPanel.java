package savetovaliste.view.psihoterapeut.paneli;

import savetovaliste.dao.IzvestajDao;
import savetovaliste.model.PsihoterapeutKandidat;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IzvestajPanel extends JPanel {

    int psihoterapeutId;

    public IzvestajPanel(PsihoterapeutKandidat psihoterapeut) {

        this.psihoterapeutId = psihoterapeut.getPsihoterapeutId();

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        setBackground(Color.WHITE);

        JLabel naslov = new JLabel("Izveštaj o radu", SwingConstants.CENTER);
        naslov.setFont(new Font("SansSerif", Font.BOLD, 26));
        naslov.setForeground(new Color(50, 50, 50));
        naslov.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(naslov, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        IzvestajDao dao = new IzvestajDao();

        int brojSeansi = dao.brojOdrzanihSeansi(psihoterapeutId);
        BigDecimal[] zarade = dao.ukupnaZaradaSeanseITestovi(psihoterapeutId);
        Date najranija = dao.najranijaSeansa(psihoterapeutId);
        String klijentNajvise = dao.klijentSaNajviseSeansi(psihoterapeutId);
        String najboljaGodina = dao.godinaSaNajvisePrihoda(psihoterapeutId);
        String prosekVreme = dao.prosecnoVremePocetkaSeansi(psihoterapeutId);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy.");

        infoPanel.add(kreirajInfoRed("Ukupan broj održanih seansi", String.valueOf(brojSeansi)));
        infoPanel.add(kreirajInfoRed("Datum prve odrzane seanse", najranija != null ? sdf.format(najranija) : "N/A"));
        infoPanel.add(kreirajInfoRed("Prosečno vreme početka seansi", prosekVreme));
        infoPanel.add(kreirajInfoRed("Klijent sa najviše seansi", klijentNajvise));
        infoPanel.add(kreirajInfoRed("Ukupan prihod od seansi (RSD)", zarade[0].toPlainString()));
        infoPanel.add(kreirajInfoRed("Ukupan prihod od testova (RSD)", zarade[1].toPlainString()));
        infoPanel.add(kreirajInfoRed("Godina sa najviše prihoda", najboljaGodina));

        add(infoPanel, BorderLayout.CENTER);
    }

    private JPanel kreirajInfoRed(String labelaTekst, String vrednostTekst) {
        JPanel red = new JPanel(new BorderLayout());
        red.setBackground(Color.WHITE);
        red.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));

        JPanel sadrzaj = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sadrzaj.setBackground(Color.WHITE);

        JLabel labela = new JLabel(labelaTekst + ": ");
        labela.setFont(new Font("SansSerif", Font.BOLD, 16));

        JLabel vrednost = new JLabel(vrednostTekst);
        vrednost.setFont(new Font("SansSerif", Font.PLAIN, 16));

        sadrzaj.add(labela);
        sadrzaj.add(vrednost);
        red.add(sadrzaj, BorderLayout.CENTER);

        return red;
    }
}
