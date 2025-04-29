package savetovaliste.view;
import savetovaliste.model.PsihoterapeutKandidat;
import savetovaliste.repository.PsihoKandRep;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PregledPsihoterapeutaFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public PregledPsihoterapeutaFrame() {
        setTitle("Pregled psihoterapeuta Savetovališta");
        setSize(1200, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Definišemo kolone
        String[] kolone = {"ID", "Ime", "Prezime","jmbg", "Datum rođenja", "Telefon", "Prebivalište", "Email", "Fakultet ID", "Stepen ID"};
        tableModel = new DefaultTableModel(kolone, 0);
        table = new JTable(tableModel);

        // Puni tabelu podacima
        ucitajPodatke();

        // Scroll
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void ucitajPodatke() {
        PsihoKandRep repo = new PsihoKandRep();
        List<PsihoterapeutKandidat> kandidati = repo.getAllKandidati();

        for (PsihoterapeutKandidat k : kandidati) {
            Object[] red = {
                    k.getPsihoterapeutId(),
                    k.getIme(),
                    k.getPrezime(),
                    k.getJmbg(),
                    k.getDatumRodjenja(),
                    k.getTelefon(),
                    k.getPrebivaliste(),
                    k.getEmail(),
                    k.getFakultetNaziv(),
                    k.getStepenId()
            };
            tableModel.addRow(red);
        }
    }
}