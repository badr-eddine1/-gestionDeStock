package view;

import model.Fournisseur;
import service.FournisseurService;

import javax.swing.*;
import java.awt.*;

public class FournisseurForm extends JDialog {
    private Fournisseur fournisseur;
    private FournisseurService fournisseurService;
    
    private JTextField codeField;
    private JTextField nomField;
    private JTextField telephoneField;
    private JTextField emailField;
    private JTextArea adresseArea;
    private JTextField villeField;
    private JTextField paysField;
    
    public FournisseurForm(JFrame parent, Fournisseur fournisseur) {
        super(parent, fournisseur == null ? "Nouveau Fournisseur" : "Modifier Fournisseur", true);
        this.fournisseur = fournisseur;
        this.fournisseurService = new FournisseurService();
        
        setSize(600, 500);
        setLocationRelativeTo(parent);
        initComponents();
        
        if (fournisseur != null) {
            chargerDonnees();
        }
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Code
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Code*:"), gbc);
        gbc.gridx = 1;
        codeField = new JTextField(20);
        formPanel.add(codeField, gbc);
        
        // Nom
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Nom*:"), gbc);
        gbc.gridx = 1;
        nomField = new JTextField(20);
        formPanel.add(nomField, gbc);
        
        // Téléphone
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Téléphone:"), gbc);
        gbc.gridx = 1;
        telephoneField = new JTextField(15);
        formPanel.add(telephoneField, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(20);
        formPanel.add(emailField, gbc);
        
        // Adresse
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Adresse:"), gbc);
        gbc.gridx = 1;
        adresseArea = new JTextArea(3, 20);
        adresseArea.setLineWrap(true);
        adresseArea.setWrapStyleWord(true);
        JScrollPane adresseScroll = new JScrollPane(adresseArea);
        formPanel.add(adresseScroll, gbc);
        
        // Ville
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Ville:"), gbc);
        gbc.gridx = 1;
        villeField = new JTextField(15);
        villeField.setText("Casablanca");
        formPanel.add(villeField, gbc);
        
        // Pays
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Pays:"), gbc);
        gbc.gridx = 1;
        paysField = new JTextField(15);
        paysField.setText("Maroc");
        formPanel.add(paysField, gbc);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton sauvegarderBtn = new JButton("Sauvegarder");
        JButton annulerBtn = new JButton("Annuler");
        
        sauvegarderBtn.addActionListener(e -> sauvegarderFournisseur());
        annulerBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(sauvegarderBtn);
        buttonPanel.add(annulerBtn);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void chargerDonnees() {
        if (fournisseur != null) {
            codeField.setText(fournisseur.getCode());
            nomField.setText(fournisseur.getNom());
            telephoneField.setText(fournisseur.getTelephone());
            emailField.setText(fournisseur.getEmail());
            adresseArea.setText(fournisseur.getAdresse() != null ? fournisseur.getAdresse() : "");
            villeField.setText(fournisseur.getVille());
            paysField.setText(fournisseur.getPays());
        }
    }
    
    private void sauvegarderFournisseur() {
        // Validation
        if (codeField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Le code est obligatoire",
                "Erreur", JOptionPane.ERROR_MESSAGE);
            codeField.requestFocus();
            return;
        }
        
        if (nomField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Le nom est obligatoire",
                "Erreur", JOptionPane.ERROR_MESSAGE);
            nomField.requestFocus();
            return;
        }
        
        // Validation email (optionnelle mais format valide si fourni)
        String email = emailField.getText().trim();
        if (!email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this,
                "Format d'email invalide",
                "Erreur", JOptionPane.ERROR_MESSAGE);
            emailField.requestFocus();
            return;
        }
        
        try {
            // Créer ou mettre à jour le fournisseur
            Fournisseur fournisseurToSave;
            if (this.fournisseur == null) {
                fournisseurToSave = new Fournisseur();
            } else {
                fournisseurToSave = this.fournisseur;
            }
            
            fournisseurToSave.setCode(codeField.getText().trim().toUpperCase());
            fournisseurToSave.setNom(nomField.getText().trim());
            fournisseurToSave.setTelephone(telephoneField.getText().trim());
            fournisseurToSave.setEmail(email);
            fournisseurToSave.setAdresse(adresseArea.getText().trim());
            fournisseurToSave.setVille(villeField.getText().trim());
            fournisseurToSave.setPays(paysField.getText().trim());
            
            // Sauvegarder
            boolean success;
            if (this.fournisseur == null) {
                success = fournisseurService.create(fournisseurToSave);
            } else {
                success = fournisseurService.update(fournisseurToSave);
            }
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Fournisseur sauvegardé avec succès!",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la sauvegarde du fournisseur\nLe code existe peut-être déjà",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erreur: " + e.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}