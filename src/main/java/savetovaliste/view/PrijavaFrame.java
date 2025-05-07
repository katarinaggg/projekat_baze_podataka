package savetovaliste.view;

import savetovaliste.dao.PsihoKandDao;
import savetovaliste.model.PsihoterapeutKandidat;
import savetovaliste.view.psihoterapeut.ProfilPsihoterapeutaFrame;

import javax.swing.*;
import java.awt.*;

public class PrijavaFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField lozinkaField;
    private JButton prijavaBtn;

    private JFrame roditelj;


    public PrijavaFrame(JFrame roditelj) {
        this.roditelj = roditelj;

        setTitle("Prijava psihoterapeuta");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Koristimo GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Prazan razmak oko svih komponenti
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel emailLabel = new JLabel("Email:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(emailLabel, gbc);

        emailField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        JLabel lozinkaLabel = new JLabel("Lozinka:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(lozinkaLabel, gbc);

        lozinkaField = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(lozinkaField, gbc);

        prijavaBtn = new JButton("Prijavi se");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(prijavaBtn, gbc);

        add(panel);
        getRootPane().setDefaultButton(prijavaBtn);

        prijavaBtn.addActionListener(e -> prijava());
    }

    private void prijava() {
        String email = emailField.getText();
        String lozinka = new String(lozinkaField.getPassword()).trim();

        if (email.isEmpty() || lozinka.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Niste popunili sva polja!", "Greška", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PsihoKandDao psihoKandDao = new PsihoKandDao();

        // ovo bi trebalo da vrati korisnika ako je registrovan i ako je tacna lozinka
        PsihoterapeutKandidat psihoterapeut = psihoKandDao.login(email, lozinka);

        if(psihoterapeut==null){
            JOptionPane.showMessageDialog(this,"Došlo je do greške\nProverite da li ste registrovani\ni da li ste uneli tačne podatke","Greška",JOptionPane.ERROR_MESSAGE);
        }
        else {
            ProfilPsihoterapeutaFrame profil = new ProfilPsihoterapeutaFrame(psihoterapeut, roditelj);
            profil.setVisible(true);
            roditelj.setVisible(false); // sakrij glavni prozor
            dispose(); // zatvori prijavu
        }
    }
}
