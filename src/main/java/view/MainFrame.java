package view;

import service.*;
import model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JLabel statusLabel;
    
    private ProduitService produitService;
    private StockService stockService;
    private CategorieService categorieService;
    private FournisseurService fournisseurService;
    private ClientService clientService;
    private CommandeService commandeService;
    
    public MainFrame() {
        produitService = new ProduitService();
        stockService = new StockService();
        categorieService = new CategorieService();
        fournisseurService = new FournisseurService();
        clientService = new ClientService();
        commandeService = new CommandeService();
        
        setTitle("Gestion de Stock - Système de gestion");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initUI();
        setVisible(true);
    }
    
    private void initUI() {
        setLayout(new BorderLayout());
        
        createMenuBar();
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        mainPanel.add(createDashboardPanel(), "dashboard");
        mainPanel.add(createProduitPanel(), "produits");
        mainPanel.add(createStockPanel(), "stock");
        mainPanel.add(createCategoriePanel(), "categories");
        mainPanel.add(createFournisseurPanel(), "fournisseurs");
        mainPanel.add(createClientPanel(), "clients");
        mainPanel.add(createCommandePanel(), "commandes");
        
        add(mainPanel, BorderLayout.CENTER);
        
        statusLabel = new JLabel("Système de gestion de stock - Prêt");
        statusLabel.setBorder(BorderFactory.createEtchedBorder());
        add(statusLabel, BorderLayout.SOUTH);
        
        cardLayout.show(mainPanel, "dashboard");
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // Menu Fichier
        JMenu fichierMenu = new JMenu("Fichier");
        JMenuItem dashboardItem = new JMenuItem("Tableau de bord");
        JMenuItem quitterItem = new JMenuItem("Quitter");
        
        dashboardItem.addActionListener(e -> cardLayout.show(mainPanel, "dashboard"));
        quitterItem.addActionListener(e -> System.exit(0));
        
        fichierMenu.add(dashboardItem);
        fichierMenu.addSeparator();
        fichierMenu.add(quitterItem);
        
        // Menu Gestion
        JMenu gestionMenu = new JMenu("Gestion");
        JMenuItem produitItem = new JMenuItem("Produits");
        JMenuItem stockItem = new JMenuItem("Stock");
        JMenuItem categorieItem = new JMenuItem("Catégories");
        JMenuItem fournisseurItem = new JMenuItem("Fournisseurs");
        JMenuItem clientItem = new JMenuItem("Clients");
        JMenuItem commandeItem = new JMenuItem("Commandes");
        
        produitItem.addActionListener(e -> {
            cardLayout.show(mainPanel, "produits");
            // Rafraîchir les produits quand on change d'onglet
            JPanel produitPanel = (JPanel) mainPanel.getComponent(1);
            refreshProduitPanel(produitPanel);
        });
        
        stockItem.addActionListener(e -> {
            cardLayout.show(mainPanel, "stock");
            // Rafraîchir le stock quand on change d'onglet
            JPanel stockPanel = (JPanel) mainPanel.getComponent(2);
            refreshStockPanel(stockPanel);
        });
        
        categorieItem.addActionListener(e -> cardLayout.show(mainPanel, "categories"));
        fournisseurItem.addActionListener(e -> cardLayout.show(mainPanel, "fournisseurs"));
        clientItem.addActionListener(e -> cardLayout.show(mainPanel, "clients"));
        commandeItem.addActionListener(e -> cardLayout.show(mainPanel, "commandes"));
        
        gestionMenu.add(produitItem);
        gestionMenu.add(stockItem);
        gestionMenu.addSeparator();
        gestionMenu.add(categorieItem);
        gestionMenu.add(fournisseurItem);
        gestionMenu.add(clientItem);
        gestionMenu.add(commandeItem);
        
        menuBar.add(fichierMenu);
        menuBar.add(gestionMenu);
        
        setJMenuBar(menuBar);
    }
    
    private JPanel createDashboardPanel() {
        JPanel dashboardPanel = new JPanel(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Tableau de Bord - Gestion de Stock", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        dashboardPanel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Récupérer les données
        List<Produit> produits = produitService.findAll();
        List<Stock> stocksAlerte = stockService.getStocksEnAlerte();
        List<Categorie> categories = categorieService.findAll();
        List<Fournisseur> fournisseurs = fournisseurService.findAll();
        List<Client> clients = clientService.findAll();
        List<Commande> commandes = commandeService.findAll();
        
        // Calculer la valeur du stock
        double valeurStock = 0;
        for (Produit p : produits) {
            valeurStock += p.getPrixAchat() * p.getQuantiteStock();
        }
        
        // Ajouter les cartes de statistiques
        statsPanel.add(createStatCard("Produits", 
            String.valueOf(produits.size()), 
            Color.BLUE, 
            "Total produits enregistrés"));
        
        statsPanel.add(createStatCard("Valeur Stock", 
            String.format("%,.2f DH", valeurStock), 
            Color.GREEN, 
            "Valeur totale du stock"));
        
        statsPanel.add(createStatCard("Alertes Stock", 
            String.valueOf(stocksAlerte.size()), 
            Color.RED, 
            "Produits en alerte"));
        
        statsPanel.add(createStatCard("Catégories", 
            String.valueOf(categories.size()), 
            Color.ORANGE, 
            "Total catégories"));
        
        statsPanel.add(createStatCard("Fournisseurs", 
            String.valueOf(fournisseurs.size()), 
            Color.MAGENTA, 
            "Fournisseurs actifs"));
        
        statsPanel.add(createStatCard("Clients", 
            String.valueOf(clients.size()), 
            Color.CYAN, 
            "Clients enregistrés"));
        
        dashboardPanel.add(statsPanel, BorderLayout.CENTER);
        
        return dashboardPanel;
    }
    
    private JPanel createStatCard(String title, String value, Color color, String subtitle) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setBackground(new Color(250, 250, 250));
        
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        titleLabel.setForeground(Color.DARK_GRAY);
        
        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        subtitleLabel.setForeground(Color.GRAY);
        
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        JLabel valueLabel = new JLabel(value, JLabel.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 28));
        valueLabel.setForeground(color);
        
        card.add(topPanel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createProduitPanel() {
        JPanel produitPanel = new JPanel(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Gestion des Produits", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        produitPanel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton ajouterBtn = new JButton("Nouveau Produit");
        JButton modifierBtn = new JButton("Modifier");
        JButton supprimerBtn = new JButton("Supprimer");
        JButton rafraichirBtn = new JButton("Rafraîchir");
        JButton alerteBtn = new JButton("Voir Alertes");
        
        JTextField rechercheField = new JTextField(20);
        JButton rechercherBtn = new JButton("Rechercher");
        
        topPanel.add(ajouterBtn);
        topPanel.add(modifierBtn);
        topPanel.add(supprimerBtn);
        topPanel.add(rafraichirBtn);
        topPanel.add(alerteBtn);
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(new JLabel("Rechercher:"));
        topPanel.add(rechercheField);
        topPanel.add(rechercherBtn);
        
        produitPanel.add(topPanel, BorderLayout.NORTH);
        
        // Modèle de table
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        String[] columns = {"ID", "Reference", "Nom", "Catégorie", "Prix Achat", "Prix Vente", "Stock", "Statut"};
        for (String col : columns) {
            model.addColumn(col);
        }
        
        JTable table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(25);
        
        // Ajouter un écouteur pour charger les données quand le panneau devient visible
        produitPanel.addHierarchyListener(e -> {
            if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
                if (produitPanel.isShowing()) {
                    chargerDonneesProduits(model, "");
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        produitPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Ajouter les écouteurs d'événements
        ajouterBtn.addActionListener(e -> {
            ouvrirFormulaireProduit(null, table, model);
        });
        
        modifierBtn.addActionListener(e -> {
            modifierProduit(table, model);
        });
        
        supprimerBtn.addActionListener(e -> {
            supprimerProduit(table, model);
        });
        
        rafraichirBtn.addActionListener(e -> {
            chargerDonneesProduits(model, "");
            rechercheField.setText("");
            if (statusLabel != null) {
                statusLabel.setText("Liste des produits rafraîchie");
            }
        });
        
        alerteBtn.addActionListener(e -> {
            afficherProduitsEnAlerte();
        });
        
        rechercherBtn.addActionListener(e -> {
            chargerDonneesProduits(model, rechercheField.getText());
        });
        
        // Double-clic pour modifier
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    modifierProduit(table, model);
                }
            }
        });
        
        return produitPanel;
    }
    
    private void chargerDonneesProduits(DefaultTableModel model, String recherche) {
        model.setRowCount(0);
        
        List<Produit> produits;
        if (recherche == null || recherche.trim().isEmpty()) {
            produits = produitService.findAll();
        } else {
            produits = produitService.rechercherParNom(recherche);
        }
        
        for (Produit p : produits) {
            model.addRow(new Object[]{
                p.getId(),
                p.getReference(),
                p.getNom(),
                p.getCategorieNom() != null ? p.getCategorieNom() : "N/A",
                String.format("%.2f DH", p.getPrixAchat()),
                String.format("%.2f DH", p.getPrixVente()),
                p.getQuantiteStock(),
                p.getStatutStock()
            });
        }
        
        if (statusLabel != null) {
            statusLabel.setText("Produits chargés: " + produits.size());
        }
    }
    
    private void ouvrirFormulaireProduit(Produit produit, JTable table, DefaultTableModel model) {
        ProduitForm form = new ProduitForm(this, produit);
        form.setVisible(true);
        
        if (!form.isVisible()) {
            chargerDonneesProduits(model, "");
            if (statusLabel != null) {
                statusLabel.setText("Produit sauvegardé - Liste mise à jour");
            }
        }
    }
    
    private void modifierProduit(JTable table, DefaultTableModel model) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner un produit à modifier",
                "Aucune sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int produitId = (int) table.getValueAt(selectedRow, 0);
        Produit produit = produitService.findById(produitId);
        
        if (produit == null) {
            JOptionPane.showMessageDialog(this,
                "Produit non trouvé",
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        ProduitForm form = new ProduitForm(this, produit);
        form.setVisible(true);
        
        if (!form.isVisible()) {
            chargerDonneesProduits(model, "");
        }
    }
    
    private void supprimerProduit(JTable table, DefaultTableModel model) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner un produit à supprimer",
                "Aucune sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Êtes-vous sûr de vouloir supprimer ce produit?\nCette action est irréversible.",
            "Confirmation de suppression",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            int produitId = (int) table.getValueAt(selectedRow, 0);
            boolean success = produitService.deleteById(produitId);
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Produit supprimé avec succès",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                chargerDonneesProduits(model, "");
            } else {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la suppression du produit\nLe produit est peut-être utilisé dans des commandes.",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void afficherProduitsEnAlerte() {
        List<Produit> alertes = produitService.getProduitsEnAlerte();
        if (alertes.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Aucun produit en alerte de stock",
                "Information", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder message = new StringBuilder();
            message.append("<html><body><h3>Produits en alerte de stock:</h3><ul>");
            
            for (Produit p : alertes) {
                message.append("<li><b>").append(p.getNom())
                       .append("</b> - Stock: ").append(p.getQuantiteStock())
                       .append(" (Seuil: ").append(p.getSeuilAlerte())
                       .append(") - ").append(p.getReference())
                       .append("</li>");
            }
            
            message.append("</ul><p><b>Total: ").append(alertes.size()).append(" produits</b></p></body></html>");
            
            JTextPane textPane = new JTextPane();
            textPane.setContentType("text/html");
            textPane.setText(message.toString());
            textPane.setEditable(false);
            
            JScrollPane scrollPane = new JScrollPane(textPane);
            scrollPane.setPreferredSize(new Dimension(500, 300));
            
            JOptionPane.showMessageDialog(this,
                scrollPane,
                "Alertes Stock",
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void refreshProduitPanel(JPanel produitPanel) {
        // Trouver la table dans le panneau
        for (Component comp : produitPanel.getComponents()) {
            if (comp instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) comp;
                JViewport viewport = scrollPane.getViewport();
                if (viewport.getView() instanceof JTable) {
                    JTable table = (JTable) viewport.getView();
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    chargerDonneesProduits(model, "");
                    break;
                }
            }
        }
    }
    
private JPanel createStockPanel() {
    JPanel stockPanel = new JPanel(new BorderLayout());
    
    JLabel titleLabel = new JLabel("Gestion du Stock", JLabel.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
    titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    stockPanel.add(titleLabel, BorderLayout.NORTH);
    
    JTabbedPane tabbedPane = new JTabbedPane();
    
    // Onglet État des stocks
    JPanel etatPanel = new JPanel(new BorderLayout());
    String[] colonnesEtat = {"ID", "Produit", "Quantité", "Min", "Max", "Emplacement", "Statut"};
    DefaultTableModel etatModel = new DefaultTableModel(colonnesEtat, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    
    JTable tableEtat = new JTable(etatModel);
    tableEtat.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    
    // Ajouter un écouteur pour charger les données quand le panneau devient visible
    etatPanel.addHierarchyListener(e -> {
        if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
            if (etatPanel.isShowing()) {
                refreshStockTable(tableEtat);
            }
        }
    });
    
    JScrollPane scrollEtat = new JScrollPane(tableEtat);
    scrollEtat.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    etatPanel.add(scrollEtat, BorderLayout.CENTER);
    
    // Panel de boutons pour l'état
    JPanel etatButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
    etatButtons.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
    
    JButton nouveauStockBtn = new JButton("Nouveau Stock");
    JButton modifierStockBtn = new JButton("Modifier Stock");
    JButton supprimerStockBtn = new JButton("Supprimer");
    JButton rafraichirStockBtn = new JButton("Rafraîchir");
    
    nouveauStockBtn.addActionListener(e -> {
        StockForm form = new StockForm(this, null);
        form.setVisible(true);
        if (!form.isVisible()) {
            refreshStockTable(tableEtat);
        }
    });
    
    modifierStockBtn.addActionListener(e -> {
        int selectedRow = tableEtat.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner un stock à modifier",
                "Aucune sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int stockId = (int) tableEtat.getValueAt(selectedRow, 0);
        Stock stock = stockService.findById(stockId);
        
        if (stock != null) {
            StockForm form = new StockForm(this, stock);
            form.setVisible(true);
            if (!form.isVisible()) {
                refreshStockTable(tableEtat);
            }
        }
    });
    
    supprimerStockBtn.addActionListener(e -> {
        supprimerStock(tableEtat);
    });
    
    rafraichirStockBtn.addActionListener(e -> {
        refreshStockTable(tableEtat);
        if (statusLabel != null) {
            statusLabel.setText("Tableau des stocks rafraîchi");
        }
    });
    
    etatButtons.add(nouveauStockBtn);
    etatButtons.add(modifierStockBtn);
    etatButtons.add(supprimerStockBtn);
    etatButtons.add(rafraichirStockBtn);
    
    etatPanel.add(etatButtons, BorderLayout.SOUTH);
    tabbedPane.addTab("État des stocks", etatPanel);
    
    // Onglet Alertes
    JPanel alertesPanel = new JPanel(new BorderLayout());
    String[] colonnesAlertes = {"Produit", "Quantité", "Quantité Min", "Déficit", "Statut"};
    DefaultTableModel alertesModel = new DefaultTableModel(colonnesAlertes, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    
    JTable tableAlertes = new JTable(alertesModel);
    
    // Ajouter un écouteur pour charger les données quand le panneau devient visible
    alertesPanel.addHierarchyListener(e -> {
        if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
            if (alertesPanel.isShowing()) {
                chargerAlertesStock(tableAlertes);
            }
        }
    });
    
    JScrollPane scrollAlertes = new JScrollPane(tableAlertes);
    scrollAlertes.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    alertesPanel.add(scrollAlertes, BorderLayout.CENTER);
    
    // Bouton pour rafraîchir les alertes
    JPanel alerteButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
    alerteButtons.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
    JButton rafraichirAlertesBtn = new JButton("Rafraîchir Alertes");
    
    rafraichirAlertesBtn.addActionListener(e -> {
        chargerAlertesStock(tableAlertes);
        if (statusLabel != null) {
            statusLabel.setText("Alertes rafraîchies");
        }
    });
    
    alerteButtons.add(rafraichirAlertesBtn);
    alertesPanel.add(alerteButtons, BorderLayout.SOUTH);
    
    tabbedPane.addTab("Alertes", alertesPanel);
    
    // Onglet Mouvements - SIMPLIFIÉ (affichage seulement)
    JPanel mouvementsPanel = new JPanel(new BorderLayout());
    JLabel infoLabel = new JLabel(
        "<html><div style='text-align: center; padding: 20px;'>" +
        "<h3>Journal des mouvements de stock</h3>" +
        "<p>Les mouvements de stock sont gérés automatiquement à travers :</p>" +
        "<ul style='text-align: left; margin-left: 50px;'>" +
        "<li>Les commandes d'achat (entrées)</li>" +
        "<li>Les commandes de vente (sorties)</li>" +
        "<li>La modification directe des stocks</li>" +
        "</ul>" +
        "<p>Tous les mouvements sont enregistrés dans la table <i>mouvement_stock</i>.</p>" +
        "</div></html>",
        JLabel.CENTER
    );
    mouvementsPanel.add(infoLabel, BorderLayout.CENTER);
    tabbedPane.addTab("Mouvements", mouvementsPanel);
    
    stockPanel.add(tabbedPane, BorderLayout.CENTER);
    
    // SUPPRIMÉ: Panel des actions rapides (entrée/sortie/ajustement)
    // Il n'y a plus de panel d'actions en bas
    
    return stockPanel;
}
    
    private void refreshStockTable(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        
        List<Stock> stocks = stockService.findAll();
        for (Stock s : stocks) {
            model.addRow(new Object[]{
                s.getId(),
                s.getProduitNom(),
                s.getQuantite(),
                s.getQuantiteMin(),
                s.getQuantiteMax(),
                s.getEmplacement(),
                s.getStatut()
            });
        }
        
        if (statusLabel != null) {
            statusLabel.setText("Stocks chargés: " + stocks.size());
        }
    }
    
    private void chargerAlertesStock(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        
        List<Stock> alertes = stockService.getStocksEnAlerte();
        for (Stock s : alertes) {
            int deficit = s.getQuantiteMin() - s.getQuantite();
            String statut = deficit > 0 ? "CRITIQUE" : "ALERTE";
            model.addRow(new Object[]{
                s.getProduitNom(),
                s.getQuantite(),
                s.getQuantiteMin(),
                deficit > 0 ? deficit : 0,
                statut
            });
        }
        
        if (statusLabel != null) {
            if (alertes.isEmpty()) {
                statusLabel.setText("Aucune alerte de stock");
            } else {
                statusLabel.setText("Alertes stock: " + alertes.size() + " produits en alerte");
            }
        }
    }
    
    private void supprimerStock(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner un stock à supprimer",
                "Aucune sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Êtes-vous sûr de vouloir supprimer ce stock?\nCette action est irréversible.",
            "Confirmation de suppression",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            int stockId = (int) table.getValueAt(selectedRow, 0);
            boolean success = stockService.deleteById(stockId);
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Stock supprimé avec succès",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                refreshStockTable(table);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la suppression du stock",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void refreshStockPanel(JPanel stockPanel) {
        if (stockPanel != null) {
            Component[] comps = stockPanel.getComponents();
            for (Component comp : comps) {
                if (comp instanceof JTabbedPane) {
                    JTabbedPane tabbedPane = (JTabbedPane) comp;
                    for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                        Component tabComp = tabbedPane.getComponentAt(i);
                        if (tabComp instanceof JPanel) {
                            JPanel tabPanel = (JPanel) tabComp;
                            for (Component tabInnerComp : tabPanel.getComponents()) {
                                if (tabInnerComp instanceof JScrollPane) {
                                    JScrollPane scrollPane = (JScrollPane) tabInnerComp;
                                    JViewport viewport = scrollPane.getViewport();
                                    if (viewport.getView() instanceof JTable) {
                                        JTable table = (JTable) viewport.getView();
                                        if (i == 0) { // État des stocks
                                            refreshStockTable(table);
                                        } else if (i == 1) { // Alertes
                                            chargerAlertesStock(table);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
                }
            }
        }
    }
    
private JPanel createCategoriePanel() {
    JPanel categoriePanel = new JPanel(new BorderLayout());
    
    JLabel titleLabel = new JLabel("Gestion des Catégories", JLabel.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
    titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    categoriePanel.add(titleLabel, BorderLayout.NORTH);
    
    // Table des catégories
    String[] colonnes = {"ID", "Code", "Nom", "Description", "Nombre Produits"};
    DefaultTableModel model = new DefaultTableModel(colonnes, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    
    JTable table = new JTable(model);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.setRowHeight(25);
    
    // Charger les données
    chargerCategories(table);
    
    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    categoriePanel.add(scrollPane, BorderLayout.CENTER);
    
    // Panel des boutons
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
    
    JButton ajouterBtn = new JButton("Nouvelle Catégorie");
    JButton modifierBtn = new JButton("Modifier");
    JButton supprimerBtn = new JButton("Supprimer");
    JButton rafraichirBtn = new JButton("Rafraîchir");
    
    ajouterBtn.addActionListener(e -> {
        CategorieForm form = new CategorieForm(this, null);
        form.setVisible(true);
        if (!form.isVisible()) {
            chargerCategories(table);
        }
    });
    
    modifierBtn.addActionListener(e -> {
        modifierCategorie(table);
    });
    
    supprimerBtn.addActionListener(e -> {
        supprimerCategorie(table);
    });
    
    rafraichirBtn.addActionListener(e -> {
        chargerCategories(table);
        if (statusLabel != null) {
            statusLabel.setText("Catégories rafraîchies");
        }
    });
    
    // Double-clic pour modifier
    table.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            if (evt.getClickCount() == 2) {
                modifierCategorie(table);
            }
        }
    });
    
    buttonPanel.add(ajouterBtn);
    buttonPanel.add(modifierBtn);
    buttonPanel.add(supprimerBtn);
    buttonPanel.add(rafraichirBtn);
    
    categoriePanel.add(buttonPanel, BorderLayout.SOUTH);
    
    return categoriePanel;
}

private void chargerCategories(JTable table) {
    DefaultTableModel model = (DefaultTableModel) table.getModel();
    model.setRowCount(0);
    
    List<Categorie> categories = categorieService.findAll();
    for (Categorie c : categories) {
        String description = c.getDescription();
        String descriptionAffichee = "";
        if (description != null && !description.isEmpty()) {
            if (description.length() > 50) {
                descriptionAffichee = description.substring(0, 50) + "...";
            } else {
                descriptionAffichee = description;
            }
        }
        
        model.addRow(new Object[]{
            c.getId(),
            c.getCode(),
            c.getNom(),
            descriptionAffichee,
            c.getNombreProduits()
        });
    }
    
    if (statusLabel != null) {
        statusLabel.setText("Catégories chargées: " + categories.size());
    }
}

private void modifierCategorie(JTable table) {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this,
            "Veuillez sélectionner une catégorie à modifier",
            "Aucune sélection", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    int categorieId = (int) table.getValueAt(selectedRow, 0);
    Categorie categorie = categorieService.findById(categorieId);
    
    if (categorie == null) {
        JOptionPane.showMessageDialog(this,
            "Catégorie non trouvée",
            "Erreur", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    CategorieForm form = new CategorieForm(this, categorie);
    form.setVisible(true);
    
    if (!form.isVisible()) {
        chargerCategories(table);
    }
}

private void supprimerCategorie(JTable table) {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this,
            "Veuillez sélectionner une catégorie à supprimer",
            "Aucune sélection", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    int categorieId = (int) table.getValueAt(selectedRow, 0);
    String nomCategorie = (String) table.getValueAt(selectedRow, 2);
    int nbProduits = (int) table.getValueAt(selectedRow, 4);
    
    // Vérifier si la catégorie contient des produits
    if (nbProduits > 0) {
        JOptionPane.showMessageDialog(this,
            "Impossible de supprimer cette catégorie!\n" +
            "Elle contient " + nbProduits + " produit(s).\n" +
            "Veuillez d'abord supprimer ou réaffecter les produits.",
            "Erreur", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    int confirm = JOptionPane.showConfirmDialog(this,
        "Êtes-vous sûr de vouloir supprimer la catégorie :\n" +
        nomCategorie + " ?\n\n" +
        "Cette action est irréversible.",
        "Confirmation de suppression",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE);
    
    if (confirm == JOptionPane.YES_OPTION) {
        Categorie categorie = new Categorie();
        categorie.setId(categorieId);
        
        boolean success = categorieService.delete(categorie);
        
        if (success) {
            JOptionPane.showMessageDialog(this,
                "Catégorie supprimée avec succès",
                "Succès", JOptionPane.INFORMATION_MESSAGE);
            chargerCategories(table);
        } else {
            JOptionPane.showMessageDialog(this,
                "Erreur lors de la suppression de la catégorie",
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
    
 
    
private JPanel createFournisseurPanel() {
    JPanel fournisseurPanel = new JPanel(new BorderLayout());
    
    JLabel titleLabel = new JLabel("Gestion des Fournisseurs", JLabel.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
    titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    fournisseurPanel.add(titleLabel, BorderLayout.NORTH);
    
    // Table des fournisseurs
    String[] colonnes = {"ID", "Code", "Nom", "Téléphone", "Email", "Ville", "Pays"};
    DefaultTableModel model = new DefaultTableModel(colonnes, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    
    JTable table = new JTable(model);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.setRowHeight(25);
    
    // Charger les données
    chargerFournisseurs(table);
    
    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    fournisseurPanel.add(scrollPane, BorderLayout.CENTER);
    
    // Panel des boutons
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
    
    JButton ajouterBtn = new JButton("Nouveau Fournisseur");
    JButton modifierBtn = new JButton("Modifier");
    JButton supprimerBtn = new JButton("Supprimer");
    JButton rafraichirBtn = new JButton("Rafraîchir");
    JButton rechercherBtn = new JButton("Rechercher");
    JTextField rechercheField = new JTextField(15);
    
    ajouterBtn.addActionListener(e -> {
        FournisseurForm form = new FournisseurForm(this, null);
        form.setVisible(true);
        if (!form.isVisible()) {
            chargerFournisseurs(table);
        }
    });
    
    modifierBtn.addActionListener(e -> {
        modifierFournisseur(table);
    });
    
    supprimerBtn.addActionListener(e -> {
        supprimerFournisseur(table);
    });
    
    rafraichirBtn.addActionListener(e -> {
        chargerFournisseurs(table);
        rechercheField.setText("");
        if (statusLabel != null) {
            statusLabel.setText("Fournisseurs rafraîchis");
        }
    });
    
    rechercherBtn.addActionListener(e -> {
        rechercherFournisseurs(table, rechercheField.getText());
    });
    
    // Double-clic pour modifier
    table.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            if (evt.getClickCount() == 2) {
                modifierFournisseur(table);
            }
        }
    });
    
    // Panel de recherche
    JPanel recherchePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    recherchePanel.add(new JLabel("Rechercher (nom/code):"));
    recherchePanel.add(rechercheField);
    recherchePanel.add(rechercherBtn);
    
    buttonPanel.add(ajouterBtn);
    buttonPanel.add(modifierBtn);
    buttonPanel.add(supprimerBtn);
    buttonPanel.add(rafraichirBtn);
    buttonPanel.add(Box.createHorizontalStrut(20));
    buttonPanel.add(recherchePanel);
    
    fournisseurPanel.add(buttonPanel, BorderLayout.SOUTH);
    
    return fournisseurPanel;
}

private void chargerFournisseurs(JTable table) {
    chargerFournisseurs(table, "");
}

private void chargerFournisseurs(JTable table, String recherche) {
    DefaultTableModel model = (DefaultTableModel) table.getModel();
    model.setRowCount(0);
    
    List<Fournisseur> fournisseurs;
    if (recherche == null || recherche.trim().isEmpty()) {
        fournisseurs = fournisseurService.findAll();
    } else {
        // Pour l'instant, on filtre manuellement
        // Vous pourriez ajouter une méthode de recherche dans FournisseurService
        fournisseurs = new ArrayList<>();
        List<Fournisseur> allFournisseurs = fournisseurService.findAll();
        String rechercheLower = recherche.toLowerCase();
        
        for (Fournisseur f : allFournisseurs) {
            if (f.getNom().toLowerCase().contains(rechercheLower) ||
                f.getCode().toLowerCase().contains(rechercheLower) ||
                (f.getEmail() != null && f.getEmail().toLowerCase().contains(rechercheLower)) ||
                (f.getVille() != null && f.getVille().toLowerCase().contains(rechercheLower))) {
                fournisseurs.add(f);
            }
        }
    }
    
    for (Fournisseur f : fournisseurs) {
        model.addRow(new Object[]{
            f.getId(),
            f.getCode(),
            f.getNom(),
            f.getTelephone(),
            f.getEmail(),
            f.getVille(),
            f.getPays()
        });
    }
    
    if (statusLabel != null) {
        statusLabel.setText("Fournisseurs chargés: " + fournisseurs.size());
    }
}

private void rechercherFournisseurs(JTable table, String recherche) {
    chargerFournisseurs(table, recherche);
}

private void modifierFournisseur(JTable table) {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this,
            "Veuillez sélectionner un fournisseur à modifier",
            "Aucune sélection", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    int fournisseurId = (int) table.getValueAt(selectedRow, 0);
    Fournisseur fournisseur = fournisseurService.findById(fournisseurId);
    
    if (fournisseur == null) {
        JOptionPane.showMessageDialog(this,
            "Fournisseur non trouvé",
            "Erreur", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    FournisseurForm form = new FournisseurForm(this, fournisseur);
    form.setVisible(true);
    
    if (!form.isVisible()) {
        chargerFournisseurs(table);
    }
}

private void supprimerFournisseur(JTable table) {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this,
            "Veuillez sélectionner un fournisseur à supprimer",
            "Aucune sélection", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    int fournisseurId = (int) table.getValueAt(selectedRow, 0);
    String nomFournisseur = (String) table.getValueAt(selectedRow, 2);
    String codeFournisseur = (String) table.getValueAt(selectedRow, 1);
    
    // Vérifier si le fournisseur est utilisé dans des produits
    // Vous devriez ajouter une méthode dans FournisseurService pour vérifier cela
    // Pour l'instant, on demande juste confirmation
    
    int confirm = JOptionPane.showConfirmDialog(this,
        "Êtes-vous sûr de vouloir supprimer le fournisseur :\n" +
        codeFournisseur + " - " + nomFournisseur + " ?\n\n" +
        "Attention: Cette action est irréversible.\n" +
        "Si ce fournisseur est utilisé dans des produits,\n" +
        "ces produits n'auront plus de fournisseur associé.",
        "Confirmation de suppression",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE);
    
    if (confirm == JOptionPane.YES_OPTION) {
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setId(fournisseurId);
        
        boolean success = fournisseurService.delete(fournisseur);
        
        if (success) {
            JOptionPane.showMessageDialog(this,
                "Fournisseur supprimé avec succès",
                "Succès", JOptionPane.INFORMATION_MESSAGE);
            chargerFournisseurs(table);
        } else {
            JOptionPane.showMessageDialog(this,
                "Erreur lors de la suppression du fournisseur\n" +
                "Il est peut-être utilisé dans des produits ou commandes",
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
    
private JPanel createClientPanel() {
    JPanel clientPanel = new JPanel(new BorderLayout());
    
    JLabel titleLabel = new JLabel("Gestion des Clients", JLabel.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
    titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    clientPanel.add(titleLabel, BorderLayout.NORTH);
    
    // Table des clients
    String[] colonnes = {"ID", "Code", "Nom", "Type", "Téléphone", "Email", "Ville", "Crédit (DH)"};
    DefaultTableModel model = new DefaultTableModel(colonnes, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    
    JTable table = new JTable(model);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.setRowHeight(25);
    
    // Charger les données
    chargerClients(table);
    
    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    clientPanel.add(scrollPane, BorderLayout.CENTER);
    
    // Panel des boutons
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
    
    JButton ajouterBtn = new JButton("Nouveau Client");
    JButton modifierBtn = new JButton("Modifier");
    JButton supprimerBtn = new JButton("Supprimer");
    JButton rafraichirBtn = new JButton("Rafraîchir");
    JButton rechercherBtn = new JButton("Rechercher");
    JTextField rechercheField = new JTextField(15);
    
    ajouterBtn.addActionListener(e -> {
        ClientForm form = new ClientForm(this, null);
        form.setVisible(true);
        if (!form.isVisible()) {
            chargerClients(table);
        }
    });
    
    modifierBtn.addActionListener(e -> {
        modifierClient(table);
    });
    
    supprimerBtn.addActionListener(e -> {
        supprimerClient(table);
    });
    
    rafraichirBtn.addActionListener(e -> {
        chargerClients(table);
        rechercheField.setText("");
        if (statusLabel != null) {
            statusLabel.setText("Clients rafraîchis");
        }
    });
    
    rechercherBtn.addActionListener(e -> {
        rechercherClients(table, rechercheField.getText());
    });
    
    // Double-clic pour modifier
    table.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            if (evt.getClickCount() == 2) {
                modifierClient(table);
            }
        }
    });
    
    // Panel de recherche
    JPanel recherchePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    recherchePanel.add(new JLabel("Rechercher (nom/code):"));
    recherchePanel.add(rechercheField);
    recherchePanel.add(rechercherBtn);
    
    buttonPanel.add(ajouterBtn);
    buttonPanel.add(modifierBtn);
    buttonPanel.add(supprimerBtn);
    buttonPanel.add(rafraichirBtn);
    buttonPanel.add(Box.createHorizontalStrut(20));
    buttonPanel.add(recherchePanel);
    
    clientPanel.add(buttonPanel, BorderLayout.SOUTH);
    
    return clientPanel;
}

private void chargerClients(JTable table) {
    chargerClients(table, "");
}

private void chargerClients(JTable table, String recherche) {
    DefaultTableModel model = (DefaultTableModel) table.getModel();
    model.setRowCount(0);
    
    List<Client> clients;
    if (recherche == null || recherche.trim().isEmpty()) {
        clients = clientService.findAll();
    } else {
        // Filtrage manuelle (à améliorer avec une méthode de recherche dans ClientService)
        clients = new ArrayList<>();
        List<Client> allClients = clientService.findAll();
        String rechercheLower = recherche.toLowerCase();
        
        for (Client c : allClients) {
            if (c.getNom().toLowerCase().contains(rechercheLower) ||
                c.getCode().toLowerCase().contains(rechercheLower) ||
                (c.getEmail() != null && c.getEmail().toLowerCase().contains(rechercheLower)) ||
                (c.getVille() != null && c.getVille().toLowerCase().contains(rechercheLower)) ||
                c.getType().toLowerCase().contains(rechercheLower)) {
                clients.add(c);
            }
        }
    }
    
    for (Client c : clients) {
        model.addRow(new Object[]{
            c.getId(),
            c.getCode(),
            c.getNom(),
            c.getType(),
            c.getTelephone(),
            c.getEmail(),
            c.getVille(),
            String.format("%.2f", c.getCredit())
        });
    }
    
    if (statusLabel != null) {
        statusLabel.setText("Clients chargés: " + clients.size());
    }
}

private void rechercherClients(JTable table, String recherche) {
    chargerClients(table, recherche);
}

private void modifierClient(JTable table) {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this,
            "Veuillez sélectionner un client à modifier",
            "Aucune sélection", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    int clientId = (int) table.getValueAt(selectedRow, 0);
    Client client = clientService.findById(clientId);
    
    if (client == null) {
        JOptionPane.showMessageDialog(this,
            "Client non trouvé",
            "Erreur", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    ClientForm form = new ClientForm(this, client);
    form.setVisible(true);
    
    if (!form.isVisible()) {
        chargerClients(table);
    }
}

private void supprimerClient(JTable table) {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this,
            "Veuillez sélectionner un client à supprimer",
            "Aucune sélection", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    int clientId = (int) table.getValueAt(selectedRow, 0);
    String nomClient = (String) table.getValueAt(selectedRow, 2);
    String codeClient = (String) table.getValueAt(selectedRow, 1);
    double credit = Double.parseDouble(((String) table.getValueAt(selectedRow, 7)).replace(",", "."));
    
    // Vérifier si le client a un crédit impayé
    if (credit > 0) {
        int choix = JOptionPane.showConfirmDialog(this,
            "Ce client a un crédit impayé de " + String.format("%.2f", credit) + " DH.\n" +
            "Voulez-vous vraiment le supprimer ?\n\n" +
            "Attention: Cette action est irréversible.",
            "Crédit impayé détecté",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (choix != JOptionPane.YES_OPTION) {
            return;
        }
    }
    
    int confirm = JOptionPane.showConfirmDialog(this,
        "Êtes-vous sûr de vouloir supprimer le client :\n" +
        codeClient + " - " + nomClient + " ?\n\n" +
        "Attention: Cette action est irréversible.\n" +
        "Si ce client a des commandes en cours,\n" +
        "elles seront conservées mais sans client associé.",
        "Confirmation de suppression",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE);
    
    if (confirm == JOptionPane.YES_OPTION) {
        Client client = new Client();
        client.setId(clientId);
        
        boolean success = clientService.delete(client);
        
        if (success) {
            JOptionPane.showMessageDialog(this,
                "Client supprimé avec succès",
                "Succès", JOptionPane.INFORMATION_MESSAGE);
            chargerClients(table);
        } else {
            JOptionPane.showMessageDialog(this,
                "Erreur lors de la suppression du client\n" +
                "Il a peut-être des commandes en cours",
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
    
private JPanel createCommandePanel() {
    JPanel commandePanel = new JPanel(new BorderLayout());
    
    JLabel titleLabel = new JLabel("Gestion des Commandes", JLabel.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
    titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    commandePanel.add(titleLabel, BorderLayout.NORTH);
    
    // Table des commandes
    String[] colonnes = {"ID", "Numéro", "Type", "Client/Fournisseur", "Date", "Statut", "Total TTC"};
    DefaultTableModel model = new DefaultTableModel(colonnes, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    
    JTable table = new JTable(model);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.setRowHeight(25);
    
    // Charger les données
    chargerCommandes(table);
    
    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    commandePanel.add(scrollPane, BorderLayout.CENTER);
    
    // Panel des boutons
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
    
    JButton nouvelleBtn = new JButton("Nouvelle Commande");
    JButton detailBtn = new JButton("Voir Détails");
    JButton modifierBtn = new JButton("Modifier");
    JButton supprimerBtn = new JButton("Supprimer");
    JButton rafraichirBtn = new JButton("Rafraîchir");
    JButton rechercherBtn = new JButton("Rechercher");
    JTextField rechercheField = new JTextField(15);
    
    nouvelleBtn.addActionListener(e -> {
        CommandeForm form = new CommandeForm(this, null);
        form.setVisible(true);
        if (!form.isVisible()) {
            chargerCommandes(table);
        }
    });
    
    detailBtn.addActionListener(e -> {
        voirDetailsCommande(table);
    });
    
    modifierBtn.addActionListener(e -> {
        modifierCommande(table);
    });
    
    supprimerBtn.addActionListener(e -> {
        supprimerCommande(table);
    });
    
    rafraichirBtn.addActionListener(e -> {
        chargerCommandes(table);
        rechercheField.setText("");
        if (statusLabel != null) {
            statusLabel.setText("Commandes rafraîchies");
        }
    });
    
    rechercherBtn.addActionListener(e -> {
        rechercherCommandes(table, rechercheField.getText());
    });
    
    // Double-clic pour voir les détails
    table.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            if (evt.getClickCount() == 2) {
                voirDetailsCommande(table);
            }
        }
    });
    
    // Panel de recherche
    JPanel recherchePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    recherchePanel.add(new JLabel("Rechercher (numéro/type):"));
    recherchePanel.add(rechercheField);
    recherchePanel.add(rechercherBtn);
    
    buttonPanel.add(nouvelleBtn);
    buttonPanel.add(detailBtn);
    buttonPanel.add(modifierBtn);
    buttonPanel.add(supprimerBtn);
    buttonPanel.add(rafraichirBtn);
    buttonPanel.add(Box.createHorizontalStrut(20));
    buttonPanel.add(recherchePanel);
    
    commandePanel.add(buttonPanel, BorderLayout.SOUTH);
    
    return commandePanel;
}

private void chargerCommandes(JTable table) {
    chargerCommandes(table, "");
}

private void chargerCommandes(JTable table, String recherche) {
    DefaultTableModel model = (DefaultTableModel) table.getModel();
    model.setRowCount(0);
    
    List<Commande> commandes;
    if (recherche == null || recherche.trim().isEmpty()) {
        commandes = commandeService.findAll();
    } else {
        // Filtrage manuelle
        commandes = new ArrayList<>();
        List<Commande> allCommandes = commandeService.findAll();
        String rechercheLower = recherche.toLowerCase();
        
        for (Commande c : allCommandes) {
            if (c.getNumero().toLowerCase().contains(rechercheLower) ||
                c.getType().toLowerCase().contains(rechercheLower) ||
                c.getStatut().toLowerCase().contains(rechercheLower) ||
                (c.getClientNom() != null && c.getClientNom().toLowerCase().contains(rechercheLower)) ||
                (c.getFournisseurNom() != null && c.getFournisseurNom().toLowerCase().contains(rechercheLower))) {
                commandes.add(c);
            }
        }
    }
    
    for (Commande c : commandes) {
        String nom = c.getType().equals("ACHAT") ? c.getFournisseurNom() : c.getClientNom();
        model.addRow(new Object[]{
            c.getId(),
            c.getNumero(),
            c.getType(),
            nom != null ? nom : "N/A",
            c.getDateCommande(),
            c.getStatut(),
            String.format("%.2f DH", c.getTotalTtc())
        });
    }
    
    if (statusLabel != null) {
        statusLabel.setText("Commandes chargées: " + commandes.size());
    }
}

private void rechercherCommandes(JTable table, String recherche) {
    chargerCommandes(table, recherche);
}

private void voirDetailsCommande(JTable table) {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this,
            "Veuillez sélectionner une commande",
            "Aucune sélection", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    int commandeId = (int) table.getValueAt(selectedRow, 0);
    Commande commande = commandeService.findById(commandeId);
    
    if (commande == null) {
        JOptionPane.showMessageDialog(this,
            "Commande non trouvée",
            "Erreur", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Afficher les détails dans une boîte de dialogue
    StringBuilder details = new StringBuilder();
    details.append("<html><body>");
    details.append("<h3>Détails de la commande</h3>");
    details.append("<b>Numéro:</b> ").append(commande.getNumero()).append("<br>");
    details.append("<b>Type:</b> ").append(commande.getType()).append("<br>");
    
    if ("ACHAT".equals(commande.getType())) {
        details.append("<b>Fournisseur:</b> ").append(commande.getFournisseurNom() != null ? commande.getFournisseurNom() : "N/A").append("<br>");
    } else {
        details.append("<b>Client:</b> ").append(commande.getClientNom() != null ? commande.getClientNom() : "N/A").append("<br>");
    }
    
    details.append("<b>Date commande:</b> ").append(commande.getDateCommande()).append("<br>");
    if (commande.getDateLivraison() != null) {
        details.append("<b>Date livraison:</b> ").append(commande.getDateLivraison()).append("<br>");
    }
    details.append("<b>Statut:</b> ").append(commande.getStatut()).append("<br>");
    details.append("<b>Mode paiement:</b> ").append(commande.getModePaiement()).append("<br>");
    details.append("<b>TVA:</b> ").append(String.format("%.2f%%", commande.getTva())).append("<br>");
    details.append("<b>Total HT:</b> ").append(String.format("%.2f DH", commande.getTotalHt())).append("<br>");
    details.append("<b>Total TTC:</b> ").append(String.format("%.2f DH", commande.getTotalTtc())).append("<br>");
    
    if (commande.getRemarques() != null && !commande.getRemarques().isEmpty()) {
        details.append("<b>Remarques:</b> ").append(commande.getRemarques()).append("<br>");
    }
    
    // Lignes de commande
    if (commande.getLignes() != null && !commande.getLignes().isEmpty()) {
        details.append("<h4>Lignes de commande:</h4>");
        details.append("<table border='1' style='border-collapse: collapse; width: 100%;'>");
        details.append("<tr><th>Produit</th><th>Quantité</th><th>Prix unitaire</th><th>Remise</th><th>Total</th></tr>");
        
        for (LigneCommande ligne : commande.getLignes()) {
            details.append("<tr>");
            details.append("<td>").append(ligne.getProduitNom()).append("</td>");
            details.append("<td align='center'>").append(ligne.getQuantite()).append("</td>");
            details.append("<td align='right'>").append(String.format("%.2f DH", ligne.getPrixUnitaire())).append("</td>");
            details.append("<td align='center'>").append(String.format("%.2f%%", ligne.getRemise())).append("</td>");
            details.append("<td align='right'>").append(String.format("%.2f DH", ligne.getTotalLigne())).append("</td>");
            details.append("</tr>");
        }
        
        details.append("</table>");
    }
    
    details.append("</body></html>");
    
    JTextPane textPane = new JTextPane();
    textPane.setContentType("text/html");
    textPane.setText(details.toString());
    textPane.setEditable(false);
    
    JScrollPane scrollPane = new JScrollPane(textPane);
    scrollPane.setPreferredSize(new Dimension(600, 400));
    
    JOptionPane.showMessageDialog(this,
        scrollPane,
        "Détails de la commande",
        JOptionPane.INFORMATION_MESSAGE);
}

private void modifierCommande(JTable table) {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this,
            "Veuillez sélectionner une commande à modifier",
            "Aucune sélection", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    int commandeId = (int) table.getValueAt(selectedRow, 0);
    Commande commande = commandeService.findById(commandeId);
    
    if (commande == null) {
        JOptionPane.showMessageDialog(this,
            "Commande non trouvée",
            "Erreur", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Vérifier si la commande peut être modifiée
    if ("LIVRE".equals(commande.getStatut()) || "ANNULE".equals(commande.getStatut())) {
        JOptionPane.showMessageDialog(this,
            "Cette commande ne peut pas être modifiée\n" +
            "Statut: " + commande.getStatut(),
            "Opération impossible", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    CommandeForm form = new CommandeForm(this, commande);
    form.setVisible(true);
    
    if (!form.isVisible()) {
        chargerCommandes(table);
    }
}

private void supprimerCommande(JTable table) {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this,
            "Veuillez sélectionner une commande à supprimer",
            "Aucune sélection", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    int commandeId = (int) table.getValueAt(selectedRow, 0);
    String numeroCommande = (String) table.getValueAt(selectedRow, 1);
    String statut = (String) table.getValueAt(selectedRow, 5);
    
    // Vérifier si la commande peut être supprimée
    if ("LIVRE".equals(statut)) {
        JOptionPane.showMessageDialog(this,
            "Impossible de supprimer une commande livrée\n" +
            "Veuillez l'annuler à la place",
            "Opération impossible", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    int confirm = JOptionPane.showConfirmDialog(this,
        "Êtes-vous sûr de vouloir supprimer la commande :\n" +
        numeroCommande + " ?\n\n" +
        "Attention: Cette action est irréversible.\n" +
        "Toutes les lignes de commande seront également supprimées.",
        "Confirmation de suppression",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE);
    
    if (confirm == JOptionPane.YES_OPTION) {
        Commande commande = new Commande();
        commande.setId(commandeId);
        
        boolean success = commandeService.delete(commande);
        
        if (success) {
            JOptionPane.showMessageDialog(this,
                "Commande supprimée avec succès",
                "Succès", JOptionPane.INFORMATION_MESSAGE);
            chargerCommandes(table);
        } else {
            JOptionPane.showMessageDialog(this,
                "Erreur lors de la suppression de la commande",
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
}