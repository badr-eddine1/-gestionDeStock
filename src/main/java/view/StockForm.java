package view;

import model.Stock;
import model.Produit;
import service.StockService;
import service.ProduitService;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class StockForm extends JDialog {
    private Stock stock;
    private StockService stockService;
    private ProduitService produitService;
    
    private JComboBox<String> produitCombo;
    private JTextField quantiteField;
    private JTextField quantiteMinField;
    private JTextField quantiteMaxField;
    private JTextField emplacementField;
    
    private Map<String, Integer> produitMap = new HashMap<>();
    
    public StockForm(JFrame parent, Stock stock) {
        super(parent, stock == null ? "Nouveau Stock" : "Modifier Stock", true);
        this.stock = stock;
        this.stockService = new StockService();
        this.produitService = new ProduitService();
        
        setSize(500, 400);
        setLocationRelativeTo(parent);
        initComponents();
        
        if (stock != null) {
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
        
        // Produit
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Produit*:"), gbc);
        gbc.gridx = 1;
        produitCombo = new JComboBox<>();
        chargerProduits();
        formPanel.add(produitCombo, gbc);
        
        // Quantité
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Quantité*:"), gbc);
        gbc.gridx = 1;
        quantiteField = new JTextField(10);
        quantiteField.setText("0");
        formPanel.add(quantiteField, gbc);
        
        // Quantité Min
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Quantité Min:"), gbc);
        gbc.gridx = 1;
        quantiteMinField = new JTextField(10);
        quantiteMinField.setText("0");
        formPanel.add(quantiteMinField, gbc);
        
        // Quantité Max
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Quantité Max:"), gbc);
        gbc.gridx = 1;
        quantiteMaxField = new JTextField(10);
        quantiteMaxField.setText("1000");
        formPanel.add(quantiteMaxField, gbc);
        
        // Emplacement
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Emplacement:"), gbc);
        gbc.gridx = 1;
        emplacementField = new JTextField(20);
        emplacementField.setText("Entrepôt A");
        formPanel.add(emplacementField, gbc);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton sauvegarderBtn = new JButton("Sauvegarder");
        JButton annulerBtn = new JButton("Annuler");
        
        sauvegarderBtn.addActionListener(e -> sauvegarderStock());
        annulerBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(sauvegarderBtn);
        buttonPanel.add(annulerBtn);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void chargerProduits() {
        produitCombo.removeAllItems();
        produitMap.clear();
        
        // Ajouter une option vide
        produitCombo.addItem("-- Sélectionner --");
        produitMap.put("-- Sélectionner --", 0);
        
        for (Produit produit : produitService.findAll()) {
            String display = produit.getReference() + " - " + produit.getNom();
            produitCombo.addItem(display);
            produitMap.put(display, produit.getId());
        }
    }
    
    private void chargerDonnees() {
        if (stock != null) {
            quantiteField.setText(String.valueOf(stock.getQuantite()));
            quantiteMinField.setText(String.valueOf(stock.getQuantiteMin()));
            quantiteMaxField.setText(String.valueOf(stock.getQuantiteMax()));
            emplacementField.setText(stock.getEmplacement());
            
            // Sélectionner le produit dans le combo
            for (String key : produitMap.keySet()) {
                if (produitMap.get(key) == stock.getProduitId()) {
                    produitCombo.setSelectedItem(key);
                    break;
                }
            }
        }
    }
    
    private void sauvegarderStock() {
        // Validation
        if (produitCombo.getSelectedItem() == null || 
            produitCombo.getSelectedItem().equals("-- Sélectionner --")) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner un produit",
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Validation des valeurs
            int quantite = Integer.parseInt(quantiteField.getText());
            int quantiteMin = Integer.parseInt(quantiteMinField.getText());
            int quantiteMax = Integer.parseInt(quantiteMaxField.getText());
            
            if (quantite < 0 || quantiteMin < 0 || quantiteMax < 0) {
                JOptionPane.showMessageDialog(this,
                    "Les valeurs ne peuvent pas être négatives",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (quantiteMin >= quantiteMax) {
                JOptionPane.showMessageDialog(this,
                    "La quantité min doit être inférieure à la quantité max",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Créer ou mettre à jour le stock
            Stock stockToSave;
            if (this.stock == null) {
                stockToSave = new Stock();
            } else {
                stockToSave = this.stock;
            }
            
            // Récupérer l'ID du produit
            String selectedProduit = (String) produitCombo.getSelectedItem();
            int produitId = produitMap.get(selectedProduit);
            
            stockToSave.setProduitId(produitId);
            stockToSave.setQuantite(quantite);
            stockToSave.setQuantiteMin(quantiteMin);
            stockToSave.setQuantiteMax(quantiteMax);
            stockToSave.setEmplacement(emplacementField.getText().trim());
            
            // Sauvegarder
            boolean success;
            if (this.stock == null) {
                success = stockService.create(stockToSave);
            } else {
                success = stockService.update(stockToSave);
            }
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Stock sauvegardé avec succès!",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la sauvegarde du stock",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Veuillez entrer des valeurs numériques valides",
                "Erreur de format", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erreur: " + e.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}