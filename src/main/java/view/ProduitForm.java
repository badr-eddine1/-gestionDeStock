package view;

import model.Produit;
import service.ProduitService;
import service.CategorieService;
import service.FournisseurService;
import model.Categorie;
import model.Fournisseur;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ProduitForm extends JDialog {
    private Produit produit;
    private ProduitService produitService;
    private CategorieService categorieService;
    private FournisseurService fournisseurService;
    
    private JTextField referenceField;
    private JTextField nomField;
    private JTextArea descriptionArea;
    private JTextField prixAchatField;
    private JTextField prixVenteField;
    private JComboBox<String> categorieCombo;
    private JComboBox<String> fournisseurCombo;
    private JTextField seuilAlerteField;
    private JTextField uniteMesureField;
    
    // Pour stocker les IDs correspondants aux noms
    private Map<String, Integer> categorieMap = new HashMap<>();
    private Map<String, Integer> fournisseurMap = new HashMap<>();
    
    public ProduitForm(JFrame parent, Produit produit) {
        super(parent, produit == null ? "Nouveau Produit" : "Modifier Produit", true);
        this.produit = produit;
        this.produitService = new ProduitService();
        this.categorieService = new CategorieService();
        this.fournisseurService = new FournisseurService();
        
        setSize(600, 500);
        setLocationRelativeTo(parent);
        initComponents();
        
        if (produit != null) {
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
        
        // Reference
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Reference*:"), gbc);
        gbc.gridx = 1;
        referenceField = new JTextField(20);
        formPanel.add(referenceField, gbc);
        
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
        descriptionArea = new JTextArea(3, 20);
        formPanel.add(new JScrollPane(descriptionArea), gbc);
        
        // Prix Achat
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Prix d'achat*:"), gbc);
        gbc.gridx = 1;
        prixAchatField = new JTextField(10);
        formPanel.add(prixAchatField, gbc);
        
        // Prix Vente
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Prix de vente*:"), gbc);
        gbc.gridx = 1;
        prixVenteField = new JTextField(10);
        formPanel.add(prixVenteField, gbc);
        
        // Catégorie
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Catégorie:"), gbc);
        gbc.gridx = 1;
        categorieCombo = new JComboBox<>();
        chargerCategories();
        formPanel.add(categorieCombo, gbc);
        
        // Fournisseur
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Fournisseur:"), gbc);
        gbc.gridx = 1;
        fournisseurCombo = new JComboBox<>();
        chargerFournisseurs();
        formPanel.add(fournisseurCombo, gbc);
        
        // Seuil d'alerte
        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(new JLabel("Seuil d'alerte:"), gbc);
        gbc.gridx = 1;
        seuilAlerteField = new JTextField(5);
        seuilAlerteField.setText("10");
        formPanel.add(seuilAlerteField, gbc);
        
        // Unité de mesure
        gbc.gridx = 0; gbc.gridy = 8;
        formPanel.add(new JLabel("Unité de mesure:"), gbc);
        gbc.gridx = 1;
        uniteMesureField = new JTextField(10);
        uniteMesureField.setText("Pièce");
        formPanel.add(uniteMesureField, gbc);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton sauvegarderBtn = new JButton("Sauvegarder");
        JButton annulerBtn = new JButton("Annuler");
        
        sauvegarderBtn.addActionListener(e -> sauvegarderProduit());
        annulerBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(sauvegarderBtn);
        buttonPanel.add(annulerBtn);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void chargerCategories() {
        categorieCombo.removeAllItems();
        categorieMap.clear();
        
        // Ajouter une option vide
        categorieCombo.addItem("-- Sélectionner --");
        categorieMap.put("-- Sélectionner --", 0);
        
        for (Categorie categorie : categorieService.findAll()) {
            categorieCombo.addItem(categorie.getNom());
            categorieMap.put(categorie.getNom(), categorie.getId());
        }
    }
    
    private void chargerFournisseurs() {
        fournisseurCombo.removeAllItems();
        fournisseurMap.clear();
        
        // Ajouter une option vide
        fournisseurCombo.addItem("-- Sélectionner --");
        fournisseurMap.put("-- Sélectionner --", 0);
        
        for (Fournisseur fournisseur : fournisseurService.findAll()) {
            fournisseurCombo.addItem(fournisseur.getNom());
            fournisseurMap.put(fournisseur.getNom(), fournisseur.getId());
        }
    }
    
    private void chargerDonnees() {
        if (produit != null) {
            referenceField.setText(produit.getReference());
            nomField.setText(produit.getNom());
            descriptionArea.setText(produit.getDescription() != null ? produit.getDescription() : "");
            prixAchatField.setText(String.valueOf(produit.getPrixAchat()));
            prixVenteField.setText(String.valueOf(produit.getPrixVente()));
            seuilAlerteField.setText(String.valueOf(produit.getSeuilAlerte()));
            uniteMesureField.setText(produit.getUniteMesure());
            
            // Sélectionner la catégorie
            if (produit.getCategorieNom() != null) {
                categorieCombo.setSelectedItem(produit.getCategorieNom());
            }
            
            // Sélectionner le fournisseur
            if (produit.getFournisseurNom() != null) {
                fournisseurCombo.setSelectedItem(produit.getFournisseurNom());
            }
        }
    }
    
    private void sauvegarderProduit() {
        // Validation des champs obligatoires
        if (referenceField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "La référence est obligatoire",
                "Erreur", JOptionPane.ERROR_MESSAGE);
            referenceField.requestFocus();
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
            // Validation des prix
            double prixAchat = Double.parseDouble(prixAchatField.getText());
            double prixVente = Double.parseDouble(prixVenteField.getText());
            
            if (prixAchat <= 0 || prixVente <= 0) {
                JOptionPane.showMessageDialog(this,
                    "Les prix doivent être positifs",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (prixVente < prixAchat) {
                JOptionPane.showMessageDialog(this,
                    "Le prix de vente doit être supérieur au prix d'achat",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Validation du seuil d'alerte
            int seuilAlerte = Integer.parseInt(seuilAlerteField.getText());
            if (seuilAlerte < 0) {
                JOptionPane.showMessageDialog(this,
                    "Le seuil d'alerte ne peut pas être négatif",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Créer ou mettre à jour le produit
            Produit produitToSave;
            if (this.produit == null) {
                produitToSave = new Produit();
            } else {
                produitToSave = this.produit;
            }
            
            produitToSave.setReference(referenceField.getText().trim());
            produitToSave.setNom(nomField.getText().trim());
            produitToSave.setDescription(descriptionArea.getText().trim());
            produitToSave.setPrixAchat(prixAchat);
            produitToSave.setPrixVente(prixVente);
            produitToSave.setSeuilAlerte(seuilAlerte);
            produitToSave.setUniteMesure(uniteMesureField.getText().trim());
            
            // Récupérer l'ID de la catégorie sélectionnée
            String selectedCategorie = (String) categorieCombo.getSelectedItem();
            if (selectedCategorie != null && !selectedCategorie.equals("-- Sélectionner --")) {
                produitToSave.setCategorieId(categorieMap.get(selectedCategorie));
            } else {
                produitToSave.setCategorieId(0);
            }
            
            // Récupérer l'ID du fournisseur sélectionné
            String selectedFournisseur = (String) fournisseurCombo.getSelectedItem();
            if (selectedFournisseur != null && !selectedFournisseur.equals("-- Sélectionner --")) {
                produitToSave.setFournisseurId(fournisseurMap.get(selectedFournisseur));
            } else {
                produitToSave.setFournisseurId(0);
            }
            
            // Sauvegarder dans la base de données
            boolean success;
            if (this.produit == null) {
                success = produitService.create(produitToSave);
            } else {
                success = produitService.update(produitToSave);
            }
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Produit sauvegardé avec succès!",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                
                // Fermer le formulaire
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la sauvegarde du produit",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Veuillez entrer des valeurs numériques valides pour les prix et le seuil",
                "Erreur de format", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erreur: " + e.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}