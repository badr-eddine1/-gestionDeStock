package view;

import model.Categorie;
import service.CategorieService;

import javax.swing.*;
import java.awt.*;

public class CategorieForm extends JDialog {
    private Categorie categorie;
    private CategorieService categorieService;
    
    private JTextField codeField;
    private JTextField nomField;
    private JTextArea descriptionArea;
    
    public CategorieForm(JFrame parent, Categorie categorie) {
        super(parent, categorie == null ? "Nouvelle Catégorie" : "Modifier Catégorie", true);
        this.categorie = categorie;
        this.categorieService = new CategorieService();
        
        setSize(500, 400);
        setLocationRelativeTo(parent);
        initComponents();
        
        if (categorie != null) {
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
        
        // Description
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        formPanel.add(scrollPane, gbc);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton sauvegarderBtn = new JButton("Sauvegarder");
        JButton annulerBtn = new JButton("Annuler");
        
        sauvegarderBtn.addActionListener(e -> sauvegarderCategorie());
        annulerBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(sauvegarderBtn);
        buttonPanel.add(annulerBtn);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void chargerDonnees() {
        if (categorie != null) {
            codeField.setText(categorie.getCode());
            nomField.setText(categorie.getNom());
            descriptionArea.setText(categorie.getDescription() != null ? categorie.getDescription() : "");
        }
    }
    
    private void sauvegarderCategorie() {
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
        
        try {
            // Créer ou mettre à jour la catégorie
            Categorie categorieToSave;
            if (this.categorie == null) {
                categorieToSave = new Categorie();
            } else {
                categorieToSave = this.categorie;
            }
            
            categorieToSave.setCode(codeField.getText().trim().toUpperCase());
            categorieToSave.setNom(nomField.getText().trim());
            categorieToSave.setDescription(descriptionArea.getText().trim());
            
            // Sauvegarder
            boolean success;
            if (this.categorie == null) {
                success = categorieService.create(categorieToSave);
            } else {
                success = categorieService.update(categorieToSave);
            }
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Catégorie sauvegardée avec succès!",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la sauvegarde de la catégorie\nLe code existe peut-être déjà",
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