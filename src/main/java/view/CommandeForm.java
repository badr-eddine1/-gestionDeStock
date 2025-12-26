package view;

import model.Commande;
import model.LigneCommande;
import model.Produit;
import model.Client;
import model.Fournisseur;
import service.CommandeService;
import service.ProduitService;
import service.ClientService;
import service.FournisseurService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandeForm extends JDialog {
    private Commande commande;
    private CommandeService commandeService;
    private ProduitService produitService;
    private ClientService clientService;
    private FournisseurService fournisseurService;
    
    private JTextField numeroField;
    private JComboBox<String> typeCombo;
    private JComboBox<String> clientCombo;
    private JComboBox<String> fournisseurCombo;
    private JTextField dateCommandeField;
    private JTextField dateLivraisonField;
    private JComboBox<String> statutCombo;
    private JComboBox<String> modePaiementCombo;
    private JTextField tvaField;
    private JTextArea remarquesArea;
    
    // Table des lignes de commande
    private DefaultTableModel lignesModel;
    private JTable lignesTable;
    private Map<String, Integer> produitsMap = new HashMap<>(); // Key: "code - nom", Value: produitId
    private Map<String, Integer> clientsMap = new HashMap<>();  // Key: "code - nom", Value: clientId
    private Map<String, Integer> fournisseursMap = new HashMap<>(); // Key: "code - nom", Value: fournisseurId
    
    // Champs pour ajouter une ligne
    private JComboBox<String> produitCombo;
    private JTextField quantiteField;
    private JTextField prixUnitaireField;
    private JTextField remiseField;
    
    // Totaux
    private JLabel totalHtLabel;
    private JLabel totalTtcLabel;
    
    public CommandeForm(JFrame parent, Commande commande) {
        super(parent, commande == null ? "Nouvelle Commande" : "Modifier Commande", true);
        this.commande = commande;
        this.commandeService = new CommandeService();
        this.produitService = new ProduitService();
        this.clientService = new ClientService();
        this.fournisseurService = new FournisseurService();
        
        setSize(1000, 700);
        setLocationRelativeTo(parent);
        initComponents();
        
        if (commande != null) {
            chargerDonnees();
        } else {
            // Générer un numéro de commande par défaut
            numeroField.setText("CMD-" + System.currentTimeMillis() / 1000);
            dateCommandeField.setText(LocalDate.now().toString());
        }
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel supérieur: informations de la commande
        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Numéro
        gbc.gridx = 0; gbc.gridy = 0;
        infoPanel.add(new JLabel("Numéro*:"), gbc);
        gbc.gridx = 1;
        numeroField = new JTextField(15);
        infoPanel.add(numeroField, gbc);
        
        // Type
        gbc.gridx = 2; gbc.gridy = 0;
        infoPanel.add(new JLabel("Type*:"), gbc);
        gbc.gridx = 3;
        typeCombo = new JComboBox<>(new String[]{"ACHAT", "VENTE"});
        typeCombo.addActionListener(e -> chargerComboSelonType());
        infoPanel.add(typeCombo, gbc);
        
        // Client (initialement caché, affiché pour VENTE)
        gbc.gridx = 0; gbc.gridy = 1;
        infoPanel.add(new JLabel("Client:"), gbc);
        gbc.gridx = 1;
        clientCombo = new JComboBox<>();
        chargerClients();
        infoPanel.add(clientCombo, gbc);
        
        // Fournisseur (initialement caché, affiché pour ACHAT)
        gbc.gridx = 2; gbc.gridy = 1;
        infoPanel.add(new JLabel("Fournisseur:"), gbc);
        gbc.gridx = 3;
        fournisseurCombo = new JComboBox<>();
        chargerFournisseurs();
        infoPanel.add(fournisseurCombo, gbc);
        
        // Date commande
        gbc.gridx = 0; gbc.gridy = 2;
        infoPanel.add(new JLabel("Date commande*:"), gbc);
        gbc.gridx = 1;
        dateCommandeField = new JTextField(10);
        dateCommandeField.setText(LocalDate.now().toString());
        infoPanel.add(dateCommandeField, gbc);
        
        // Date livraison
        gbc.gridx = 2; gbc.gridy = 2;
        infoPanel.add(new JLabel("Date livraison:"), gbc);
        gbc.gridx = 3;
        dateLivraisonField = new JTextField(10);
        infoPanel.add(dateLivraisonField, gbc);
        
        // Statut
        gbc.gridx = 0; gbc.gridy = 3;
        infoPanel.add(new JLabel("Statut:"), gbc);
        gbc.gridx = 1;
        statutCombo = new JComboBox<>(new String[]{"EN_ATTENTE", "CONFIRME", "LIVRE", "ANNULE"});
        infoPanel.add(statutCombo, gbc);
        
        // Mode paiement
        gbc.gridx = 2; gbc.gridy = 3;
        infoPanel.add(new JLabel("Mode paiement:"), gbc);
        gbc.gridx = 3;
        modePaiementCombo = new JComboBox<>(new String[]{"ESPECES", "CHEQUE", "VIREMENT", "CREDIT"});
        infoPanel.add(modePaiementCombo, gbc);
        
        // TVA
        gbc.gridx = 0; gbc.gridy = 4;
        infoPanel.add(new JLabel("TVA (%):"), gbc);
        gbc.gridx = 1;
        tvaField = new JTextField(5);
        tvaField.setText("20.00");
        infoPanel.add(tvaField, gbc);
        
        // Remarques
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        infoPanel.add(new JLabel("Remarques:"), gbc);
        gbc.gridy = 6;
        remarquesArea = new JTextArea(3, 40);
        remarquesArea.setLineWrap(true);
        remarquesArea.setWrapStyleWord(true);
        infoPanel.add(new JScrollPane(remarquesArea), gbc);
        
        mainPanel.add(infoPanel, BorderLayout.NORTH);
        
        // Panel central: lignes de commande
        JPanel lignesPanel = new JPanel(new BorderLayout());
        lignesPanel.setBorder(BorderFactory.createTitledBorder("Lignes de commande"));
        
        // Table des lignes
        String[] colonnes = {"Produit", "Quantité", "Prix unitaire", "Remise %", "Total ligne"};
        lignesModel = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        lignesTable = new JTable(lignesModel);
        lignesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollLignes = new JScrollPane(lignesTable);
        lignesPanel.add(scrollLignes, BorderLayout.CENTER);
        
        // Panel pour ajouter une ligne
        JPanel ajouterLignePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ajouterLignePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        produitCombo = new JComboBox<>();
        chargerProduits();
        
        quantiteField = new JTextField(5);
        quantiteField.setText("1");
        
        prixUnitaireField = new JTextField(8);
        prixUnitaireField.setText("0.00");
        
        remiseField = new JTextField(5);
        remiseField.setText("0.00");
        
        JButton ajouterLigneBtn = new JButton("Ajouter ligne");
        JButton supprimerLigneBtn = new JButton("Supprimer ligne");
        
        ajouterLigneBtn.addActionListener(e -> ajouterLigne());
        supprimerLigneBtn.addActionListener(e -> supprimerLigne());
        
        // Écouteur pour charger le prix du produit sélectionné
        produitCombo.addActionListener(e -> {
            String selected = (String) produitCombo.getSelectedItem();
            if (selected != null && !selected.equals("-- Sélectionner --")) {
                // Trouver le produit
                Integer produitId = produitsMap.get(selected);
                if (produitId != null) {
                    Produit produit = produitService.findById(produitId);
                    if (produit != null) {
                        if ("ACHAT".equals(typeCombo.getSelectedItem())) {
                            // Prix d'achat pour les commandes d'achat
                            prixUnitaireField.setText(String.format("%.2f", produit.getPrixAchat()));
                        } else {
                            // Prix de vente pour les commandes de vente
                            prixUnitaireField.setText(String.format("%.2f", produit.getPrixVente()));
                        }
                    }
                }
            }
        });
        
        ajouterLignePanel.add(new JLabel("Produit:"));
        ajouterLignePanel.add(produitCombo);
        ajouterLignePanel.add(new JLabel("Quantité:"));
        ajouterLignePanel.add(quantiteField);
        ajouterLignePanel.add(new JLabel("Prix unitaire:"));
        ajouterLignePanel.add(prixUnitaireField);
        ajouterLignePanel.add(new JLabel("Remise %:"));
        ajouterLignePanel.add(remiseField);
        ajouterLignePanel.add(ajouterLigneBtn);
        ajouterLignePanel.add(supprimerLigneBtn);
        
        lignesPanel.add(ajouterLignePanel, BorderLayout.SOUTH);
        
        mainPanel.add(lignesPanel, BorderLayout.CENTER);
        
        // Panel inférieur: totaux et boutons
        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        // Panel des totaux
        JPanel totauxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalHtLabel = new JLabel("Total HT: 0.00 DH");
        totalTtcLabel = new JLabel("Total TTC: 0.00 DH");
        totalHtLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalTtcLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        totauxPanel.add(totalHtLabel);
        totauxPanel.add(Box.createHorizontalStrut(20));
        totauxPanel.add(totalTtcLabel);
        
        // Panel des boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton calculerBtn = new JButton("Calculer totaux");
        JButton sauvegarderBtn = new JButton("Sauvegarder");
        JButton annulerBtn = new JButton("Annuler");
        
        calculerBtn.addActionListener(e -> calculerTotaux());
        sauvegarderBtn.addActionListener(e -> sauvegarderCommande());
        annulerBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(calculerBtn);
        buttonPanel.add(sauvegarderBtn);
        buttonPanel.add(annulerBtn);
        
        bottomPanel.add(totauxPanel, BorderLayout.WEST);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Initialiser l'affichage selon le type
        chargerComboSelonType();
    }
    
    private void chargerProduits() {
        produitCombo.removeAllItems();
        produitsMap.clear();
        
        produitCombo.addItem("-- Sélectionner --");
        
        for (Produit produit : produitService.findAll()) {
            String display = produit.getReference() + " - " + produit.getNom() + 
                           " (Stock: " + produit.getQuantiteStock() + ")";
            produitCombo.addItem(display);
            produitsMap.put(display, produit.getId());
        }
    }
    
    private void chargerClients() {
        clientCombo.removeAllItems();
        clientsMap.clear();
        
        clientCombo.addItem("-- Sélectionner --");
        
        for (Client client : clientService.findAll()) {
            String display = client.getCode() + " - " + client.getNom();
            clientCombo.addItem(display);
            clientsMap.put(display, client.getId());
        }
    }
    
    private void chargerFournisseurs() {
        fournisseurCombo.removeAllItems();
        fournisseursMap.clear();
        
        fournisseurCombo.addItem("-- Sélectionner --");
        
        for (Fournisseur fournisseur : fournisseurService.findAll()) {
            String display = fournisseur.getCode() + " - " + fournisseur.getNom();
            fournisseurCombo.addItem(display);
            fournisseursMap.put(display, fournisseur.getId());
        }
    }
    
    private void chargerComboSelonType() {
        String type = (String) typeCombo.getSelectedItem();
        if ("ACHAT".equals(type)) {
            clientCombo.setEnabled(false);
            clientCombo.setSelectedIndex(0);
            fournisseurCombo.setEnabled(true);
        } else if ("VENTE".equals(type)) {
            clientCombo.setEnabled(true);
            fournisseurCombo.setEnabled(false);
            fournisseurCombo.setSelectedIndex(0);
        }
    }
    
    private void ajouterLigne() {
        String selectedProduit = (String) produitCombo.getSelectedItem();
        if (selectedProduit == null || selectedProduit.equals("-- Sélectionner --")) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner un produit",
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            int quantite = Integer.parseInt(quantiteField.getText().trim());
            double prixUnitaire = Double.parseDouble(prixUnitaireField.getText().trim());
            double remise = Double.parseDouble(remiseField.getText().trim());
            
            if (quantite <= 0) {
                JOptionPane.showMessageDialog(this,
                    "La quantité doit être positive",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (prixUnitaire <= 0) {
                JOptionPane.showMessageDialog(this,
                    "Le prix unitaire doit être positif",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (remise < 0 || remise > 100) {
                JOptionPane.showMessageDialog(this,
                    "La remise doit être entre 0 et 100%",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Vérifier le stock pour les ventes
            if ("VENTE".equals(typeCombo.getSelectedItem())) {
                Integer produitId = produitsMap.get(selectedProduit);
                if (produitId != null) {
                    Produit produit = produitService.findById(produitId);
                    if (produit != null && quantite > produit.getQuantiteStock()) {
                        int choix = JOptionPane.showConfirmDialog(this,
                            "Stock insuffisant!\n" +
                            "Stock disponible: " + produit.getQuantiteStock() + "\n" +
                            "Quantité demandée: " + quantite + "\n\n" +
                            "Voulez-vous quand même ajouter cette ligne?",
                            "Stock insuffisant",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);
                        
                        if (choix != JOptionPane.YES_OPTION) {
                            return;
                        }
                    }
                }
            }
            
            // Extraire juste le nom du produit pour l'affichage dans la table
            String produitNom = selectedProduit;
            if (selectedProduit.contains(" - ")) {
                String[] parts = selectedProduit.split(" - ");
                if (parts.length >= 2) {
                    produitNom = parts[1];
                    if (produitNom.contains(" (Stock:")) {
                        produitNom = produitNom.substring(0, produitNom.indexOf(" (Stock:")).trim();
                    }
                }
            }
            
            // Calculer le total de la ligne
            double totalLigne = quantite * prixUnitaire * (1 - remise / 100);
            
            // Ajouter à la table
            lignesModel.addRow(new Object[]{
                produitNom, // Juste le nom
                quantite,
                String.format("%.2f", prixUnitaire), // Sans "DH"
                String.format("%.2f", remise),      // Sans "%"
                String.format("%.2f", totalLigne)   // Sans "DH"
            });
            
            // Réinitialiser les champs
            produitCombo.setSelectedIndex(0);
            quantiteField.setText("1");
            prixUnitaireField.setText("0.00");
            remiseField.setText("0.00");
            
            // Calculer les totaux
            calculerTotaux();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Veuillez entrer des valeurs numériques valides",
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void supprimerLigne() {
        int selectedRow = lignesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner une ligne à supprimer",
                "Aucune sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        lignesModel.removeRow(selectedRow);
        calculerTotaux();
    }
    
    private void calculerTotaux() {
        double totalHt = 0.0;
        
        for (int i = 0; i < lignesModel.getRowCount(); i++) {
            try {
                Object totalObj = lignesModel.getValueAt(i, 4);
                if (totalObj != null) {
                    String totalStr = totalObj.toString().replace(",", ".").trim();
                    totalHt += Double.parseDouble(totalStr);
                }
            } catch (NumberFormatException e) {
                // Ignorer les erreurs
            }
        }
        
        double tva = 20.0; // Valeur par défaut
        try {
            tva = Double.parseDouble(tvaField.getText().trim());
        } catch (NumberFormatException e) {
            // Garder la valeur par défaut
        }
        
        double totalTtc = totalHt * (1 + tva / 100);
        
        totalHtLabel.setText(String.format("Total HT: %.2f DH", totalHt));
        totalTtcLabel.setText(String.format("Total TTC: %.2f DH", totalTtc));
    }
    
    private void chargerDonnees() {
        if (commande != null) {
            numeroField.setText(commande.getNumero());
            typeCombo.setSelectedItem(commande.getType());
            dateCommandeField.setText(commande.getDateCommande().toString());
            
            if (commande.getDateLivraison() != null) {
                dateLivraisonField.setText(commande.getDateLivraison().toString());
            }
            
            statutCombo.setSelectedItem(commande.getStatut());
            modePaiementCombo.setSelectedItem(commande.getModePaiement());
            tvaField.setText(String.format("%.2f", commande.getTva()));
            remarquesArea.setText(commande.getRemarques() != null ? commande.getRemarques() : "");
            
            // Sélectionner client/fournisseur
            chargerComboSelonType();
            
            if ("ACHAT".equals(commande.getType()) && commande.getFournisseurId() != null && commande.getFournisseurId() > 0) {
                for (Map.Entry<String, Integer> entry : fournisseursMap.entrySet()) {
                    if (entry.getValue().equals(commande.getFournisseurId())) {
                        fournisseurCombo.setSelectedItem(entry.getKey());
                        break;
                    }
                }
            } else if ("VENTE".equals(commande.getType()) && commande.getClientId() != null && commande.getClientId() > 0) {
                for (Map.Entry<String, Integer> entry : clientsMap.entrySet()) {
                    if (entry.getValue().equals(commande.getClientId())) {
                        clientCombo.setSelectedItem(entry.getKey());
                        break;
                    }
                }
            }
            
            // Charger les lignes
            if (commande.getLignes() != null) {
                lignesModel.setRowCount(0); // Vider la table d'abord
                
                for (LigneCommande ligne : commande.getLignes()) {
                    Produit produit = produitService.findById(ligne.getProduitId());
                    if (produit != null) {
                        double totalLigne = ligne.getQuantite() * ligne.getPrixUnitaire() * (1 - ligne.getRemise() / 100);
                        
                        lignesModel.addRow(new Object[]{
                            produit.getNom(),
                            ligne.getQuantite(),
                            String.format("%.2f", ligne.getPrixUnitaire()),
                            String.format("%.2f", ligne.getRemise()),
                            String.format("%.2f", totalLigne)
                        });
                    }
                }
            }
            
            calculerTotaux();
        }
    }
    
    private void sauvegarderCommande() {
        // Validation
        if (numeroField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Le numéro de commande est obligatoire",
                "Erreur", JOptionPane.ERROR_MESSAGE);
            numeroField.requestFocus();
            return;
        }
        
        if (lignesModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                "La commande doit contenir au moins une ligne",
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Créer ou mettre à jour la commande
            Commande commandeToSave;
            if (this.commande == null) {
                commandeToSave = new Commande();
            } else {
                commandeToSave = this.commande;
            }
            
            commandeToSave.setNumero(numeroField.getText().trim());
            commandeToSave.setType((String) typeCombo.getSelectedItem());
            
            // Validation date commande
            try {
                commandeToSave.setDateCommande(LocalDate.parse(dateCommandeField.getText().trim()));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Format de date invalide pour la date de commande (AAAA-MM-JJ)",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                dateCommandeField.requestFocus();
                return;
            }
            
            // Date livraison (optionnelle)
            if (!dateLivraisonField.getText().trim().isEmpty()) {
                try {
                    commandeToSave.setDateLivraison(LocalDate.parse(dateLivraisonField.getText().trim()));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this,
                        "Format de date invalide pour la date de livraison (AAAA-MM-JJ)",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                    dateLivraisonField.requestFocus();
                    return;
                }
            } else {
                commandeToSave.setDateLivraison(null);
            }
            
            commandeToSave.setStatut((String) statutCombo.getSelectedItem());
            commandeToSave.setModePaiement((String) modePaiementCombo.getSelectedItem());
            
            // Validation TVA
            try {
                commandeToSave.setTva(Double.parseDouble(tvaField.getText().trim()));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                    "Veuillez entrer une valeur numérique pour la TVA",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                tvaField.requestFocus();
                return;
            }
            
            commandeToSave.setRemarques(remarquesArea.getText().trim());
            
            // Déterminer client/fournisseur
            String type = (String) typeCombo.getSelectedItem();
            if ("ACHAT".equals(type)) {
                String selectedFournisseur = (String) fournisseurCombo.getSelectedItem();
                if (selectedFournisseur != null && !selectedFournisseur.equals("-- Sélectionner --")) {
                    Integer fournisseurId = fournisseursMap.get(selectedFournisseur);
                    if (fournisseurId != null && fournisseurId > 0) {
                        commandeToSave.setFournisseurId(fournisseurId);
                    } else {
                        commandeToSave.setFournisseurId(null);
                    }
                } else {
                    commandeToSave.setFournisseurId(null);
                }
                commandeToSave.setClientId(null);
                
            } else if ("VENTE".equals(type)) {
                String selectedClient = (String) clientCombo.getSelectedItem();
                if (selectedClient != null && !selectedClient.equals("-- Sélectionner --")) {
                    Integer clientId = clientsMap.get(selectedClient);
                    if (clientId != null && clientId > 0) {
                        commandeToSave.setClientId(clientId);
                    } else {
                        commandeToSave.setClientId(null);
                    }
                } else {
                    commandeToSave.setClientId(null);
                }
                commandeToSave.setFournisseurId(null);
            }
            
            // Créer les lignes de commande
            List<LigneCommande> lignes = new ArrayList<>();
            for (int i = 0; i < lignesModel.getRowCount(); i++) {
                String produitNomAffiche = (String) lignesModel.getValueAt(i, 0);
                
                // Extraire quantité
                Object quantiteObj = lignesModel.getValueAt(i, 1);
                int quantite = Integer.parseInt(quantiteObj.toString());
                
                // Extraire prix unitaire
                Object prixObj = lignesModel.getValueAt(i, 2);
                String prixStr = prixObj.toString().replace(",", ".").trim();
                double prixUnitaire = Double.parseDouble(prixStr);
                
                // Extraire remise
                Object remiseObj = lignesModel.getValueAt(i, 3);
                String remiseStr = remiseObj.toString().replace(",", ".").trim();
                double remise = Double.parseDouble(remiseStr);
                
                // Trouver l'ID du produit
                Integer produitId = null;
                
                // Chercher dans la map des produits
                for (Map.Entry<String, Integer> entry : produitsMap.entrySet()) {
                    String display = entry.getKey();
                    if (display.contains(" - ")) {
                        String[] parts = display.split(" - ");
                        if (parts.length >= 2) {
                            String nomDansDisplay = parts[1];
                            if (nomDansDisplay.contains(" (Stock:")) {
                                nomDansDisplay = nomDansDisplay.substring(0, nomDansDisplay.indexOf(" (Stock:")).trim();
                            }
                            
                            if (nomDansDisplay.equalsIgnoreCase(produitNomAffiche.trim())) {
                                produitId = entry.getValue();
                                break;
                            }
                        }
                    }
                }
                
                // Si toujours pas trouvé, chercher dans la base de données
                if (produitId == null) {
                    List<Produit> produits = produitService.findAll();
                    for (Produit p : produits) {
                        if (p.getNom().equalsIgnoreCase(produitNomAffiche.trim())) {
                            produitId = p.getId();
                            break;
                        }
                    }
                }
                
                if (produitId == null || produitId == 0) {
                    JOptionPane.showMessageDialog(this,
                        "Produit '" + produitNomAffiche + "' introuvable. Veuillez réessayer.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Créer la ligne de commande
                LigneCommande ligne = new LigneCommande(produitId, quantite, prixUnitaire);
                ligne.setRemise(remise);
                lignes.add(ligne);
            }
            
            commandeToSave.setLignes(lignes);
            commandeToSave.calculerTotaux();
            
            // Sauvegarder
            boolean success;
            if (this.commande == null) {
                success = commandeService.create(commandeToSave);
            } else {
                success = commandeService.update(commandeToSave);
            }
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Commande sauvegardée avec succès!",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la sauvegarde de la commande\n" +
                    "Vérifiez que le numéro de commande est unique",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors de la sauvegarde: " + e.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}