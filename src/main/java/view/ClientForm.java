package view;

import model.Client;
import service.ClientService;

import javax.swing.*;
import java.awt.*;

public class ClientForm extends JDialog {
    private Client client;
    private ClientService clientService;
    
    private JTextField codeField;
    private JTextField nomField;
    private JComboBox<String> typeCombo;
    private JTextField telephoneField;
    private JTextField emailField;
    private JTextArea adresseArea;
    private JTextField villeField;
    private JTextField creditField;
    
    public ClientForm(JFrame parent, Client client) {
        super(parent, client == null ? "Nouveau Client" : "Modifier Client", true);
        this.client = client;
        this.clientService = new ClientService();
        
        setSize(600, 500);
        setLocationRelativeTo(parent);
        initComponents();
        
        if (client != null) {
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
        
        // Type
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        typeCombo = new JComboBox<>(new String[]{"PARTICULIER", "ENTREPRISE"});
        formPanel.add(typeCombo, gbc);
        
        // Téléphone
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Téléphone:"), gbc);
        gbc.gridx = 1;
        telephoneField = new JTextField(15);
        formPanel.add(telephoneField, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(20);
        formPanel.add(emailField, gbc);
        
        // Adresse
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Adresse:"), gbc);
        gbc.gridx = 1;
        adresseArea = new JTextArea(3, 20);
        adresseArea.setLineWrap(true);
        adresseArea.setWrapStyleWord(true);
        JScrollPane adresseScroll = new JScrollPane(adresseArea);
        formPanel.add(adresseScroll, gbc);
        
        // Ville
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Ville:"), gbc);
        gbc.gridx = 1;
        villeField = new JTextField(15);
        formPanel.add(villeField, gbc);
        
        // Crédit
        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(new JLabel("Crédit (DH):"), gbc);
        gbc.gridx = 1;
        creditField = new JTextField(10);
        creditField.setText("0.00");
        formPanel.add(creditField, gbc);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton sauvegarderBtn = new JButton("Sauvegarder");
        JButton annulerBtn = new JButton("Annuler");
        
        sauvegarderBtn.addActionListener(e -> sauvegarderClient());
        annulerBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(sauvegarderBtn);
        buttonPanel.add(annulerBtn);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void chargerDonnees() {
        if (client != null) {
            codeField.setText(client.getCode());
            nomField.setText(client.getNom());
            typeCombo.setSelectedItem(client.getType());
            telephoneField.setText(client.getTelephone());
            emailField.setText(client.getEmail());
            adresseArea.setText(client.getAdresse() != null ? client.getAdresse() : "");
            villeField.setText(client.getVille());
            creditField.setText(String.format("%.2f", client.getCredit()));
        }
    }
    
    private void sauvegarderClient() {
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
        
        // Validation email
        String email = emailField.getText().trim();
        if (!email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this,
                "Format d'email invalide",
                "Erreur", JOptionPane.ERROR_MESSAGE);
            emailField.requestFocus();
            return;
        }
        
        // Validation crédit
        double credit = 0.0;
        try {
            credit = Double.parseDouble(creditField.getText().trim());
            if (credit < 0) {
                JOptionPane.showMessageDialog(this,
                    "Le crédit ne peut pas être négatif",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                creditField.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Veuillez entrer un montant valide pour le crédit",
                "Erreur", JOptionPane.ERROR_MESSAGE);
            creditField.requestFocus();
            return;
        }
        
        try {
            // Créer ou mettre à jour le client
            Client clientToSave;
            if (this.client == null) {
                clientToSave = new Client();
            } else {
                clientToSave = this.client;
            }
            
            clientToSave.setCode(codeField.getText().trim().toUpperCase());
            clientToSave.setNom(nomField.getText().trim());
            clientToSave.setType((String) typeCombo.getSelectedItem());
            clientToSave.setTelephone(telephoneField.getText().trim());
            clientToSave.setEmail(email);
            clientToSave.setAdresse(adresseArea.getText().trim());
            clientToSave.setVille(villeField.getText().trim());
            clientToSave.setCredit(credit);
            
            // Sauvegarder
            boolean success;
            if (this.client == null) {
                success = clientService.create(clientToSave);
            } else {
                success = clientService.update(clientToSave);
            }
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Client sauvegardé avec succès!",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la sauvegarde du client\nLe code existe peut-être déjà",
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