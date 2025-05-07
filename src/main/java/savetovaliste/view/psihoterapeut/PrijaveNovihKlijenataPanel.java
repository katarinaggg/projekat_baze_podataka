package savetovaliste.view.psihoterapeut;

import savetovaliste.dao.SeansaDao;
import savetovaliste.model.Klijent;
import savetovaliste.model.PsihoterapeutKandidat;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;


public class PrijaveNovihKlijenataPanel extends JPanel {

    private JTable tabela;
    private DefaultTableModel model;

    private PsihoterapeutKandidat psihoterapeut;

    public PrijaveNovihKlijenataPanel(PsihoterapeutKandidat psihoterapeut) {
        this.psihoterapeut = psihoterapeut;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel naslov = new JLabel("Pregled prijava novih klijenata", SwingConstants.CENTER);
        naslov.setFont(new Font("Arial", Font.BOLD, 20));
        naslov.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(naslov, BorderLayout.NORTH);

        model = new DefaultTableModel(new Object[]{
                "Ime", "Prezime", "Pol", "Datum rođenja", "Telefon", "Email", "Opis problema", "Prvi put"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tabela);
        add(scrollPane, BorderLayout.CENTER);

        popuniTabelu(psihoterapeut);

    }

    private void popuniTabelu(PsihoterapeutKandidat psihoterapeut) {
        model.setRowCount(0); // očisti prethodne redove

        SeansaDao seansaDao = new SeansaDao();
        List<Klijent> klijenti = seansaDao.vratiNoveKLijente(psihoterapeut.getPsihoterapeutId());

        for (Klijent k : klijenti) {
            model.addRow(new Object[]{
                    k.getIme(),
                    k.getPrezime(),
                    k.getPol(),
                    k.getDatumRodjenja(),
                    k.getTelefon(),
                    k.getEmail(),
                    k.getOpis(),
                    k.isPrviPut() ? "Da" : "Ne"

            });
        }
    }


}
