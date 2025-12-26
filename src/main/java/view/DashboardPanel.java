package view;

import service.*;
import model.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DashboardPanel extends JPanel {
    private ProduitService produitService;
    private StockService stockService;
    private CategorieService categorieService;
    private FournisseurService fournisseurService;
    
    public DashboardPanel() {
        produitService = new ProduitService();
        stockService = new StockService();
        categorieService = new CategorieService();
        fournisseurService = new FournisseurService();
        
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Tableau de Bord", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Récupérer les données
        List<Produit> produits = produitService.findAll();
        List<Stock> stocksAlerte = stockService.getStocksEnAlerte();
        List<Categorie> categories = categorieService.findAll();
        List<Fournisseur> fournisseurs = fournisseurService.findAll();
        
        // Calculer la valeur du stock
        double valeurStock = 0;
        for (Produit p : produits) {
            valeurStock += p.getPrixAchat() * p.getQuantiteStock();
        }
        
        // Ajouter les cartes
        statsPanel.add(createStatCard("Produits", 
            String.valueOf(produits.size()), 
            new Color(70, 130, 180), 
            "Total produits enregistrés"));
        
        statsPanel.add(createStatCard("Valeur Stock", 
            String.format("%,.2f DH", valeurStock), 
            new Color(34, 139, 34), 
            "Valeur totale du stock"));
        
        statsPanel.add(createStatCard("Alertes", 
            String.valueOf(stocksAlerte.size()), 
            new Color(220, 20, 60), 
            "Produits en alerte"));
        
        statsPanel.add(createStatCard("Catégories", 
            String.valueOf(categories.size()), 
            new Color(255, 140, 0), 
            "Nombre de catégories"));
        
        statsPanel.add(createStatCard("Fournisseurs", 
            String.valueOf(fournisseurs.size()), 
            new Color(186, 85, 211), 
            "Fournisseurs actifs"));
        
        statsPanel.add(createStatCard("Stock Min", 
            "0", 
            new Color(0, 191, 255), 
            "À réapprovisionner"));
        
        add(statsPanel, BorderLayout.CENTER);
    }
    
    private JPanel createStatCard(String title, String value, Color color, String description) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker(), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setBackground(new Color(245, 245, 245));
        
        // En-tête
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(color.darker());
        
        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        descriptionLabel.setForeground(Color.GRAY);
        
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(descriptionLabel, BorderLayout.SOUTH);
        
        // Valeur
        JLabel valueLabel = new JLabel(value, JLabel.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 32));
        valueLabel.setForeground(color);
        
        card.add(headerPanel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
}