package view;

import service.UtilisateurService;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class SignUpForm extends JFrame {

    private JPanel mainPanel;
    private JScrollPane scrollPane;
    private JPanel contentPanel;
    private JPanel formPanel;
    private JPanel buttonPanel;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField nomField;
    private JTextField prenomField;
    private JTextField emailField;
    private JComboBox<String> roleComboBox;
    private JButton signupButton;
    private JButton cancelButton;
    private JLabel haveAccountLabel;
    private JButton loginButton;

    public SignUpForm() {
        initComponents();
        setupFrame();
    }

    private void setupFrame() {
        setTitle("Créer un compte - Gestion de Stock");
        setSize(550, 650); // Réduit la hauteur car on a maintenant un scroll
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(true); // Permettre le redimensionnement pour le scroll
        getContentPane().setBackground(new Color(245, 247, 250));
    }

    private void initComponents() {
        // Panel principal avec scroll
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // Créer un panel de contenu qui ira dans le scroll
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // En-tête
        JPanel headerPanel = new JPanel(new BorderLayout(0, 10));
        headerPanel.setBackground(Color.WHITE);
        
        titleLabel = new JLabel("Créer un compte");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28)); // Réduit la taille de la police
        titleLabel.setForeground(new Color(33, 33, 33));
        
        subtitleLabel = new JLabel("Rejoignez notre plateforme de gestion");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(117, 117, 117));
        
        JPanel titlesPanel = new JPanel(new BorderLayout(0, 5));
        titlesPanel.setBackground(Color.WHITE);
        titlesPanel.add(titleLabel, BorderLayout.NORTH);
        titlesPanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        headerPanel.add(titlesPanel, BorderLayout.CENTER);
        
        // Icône
        JLabel iconLabel = new JLabel("👤");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32)); // Réduit la taille de l'icône
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setPreferredSize(new Dimension(50, 50));
        
        JPanel iconPanel = new JPanel();
        iconPanel.setBackground(new Color(232, 240, 254));
        iconPanel.setPreferredSize(new Dimension(70, 70));
        iconPanel.setLayout(new GridBagLayout());
        iconPanel.setBorder(BorderFactory.createLineBorder(new Color(66, 133, 244, 50), 1));
        iconPanel.add(iconLabel);
        
        headerPanel.add(iconPanel, BorderLayout.EAST);
        
        contentPanel.add(headerPanel, BorderLayout.NORTH);

        // Formulaire avec GridBagLayout
        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 0, 0); // Réduit encore l'espacement
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // Nom d'utilisateur
        JLabel userLabel = createFormLabel("Nom d'utilisateur *");
        usernameField = createStyledTextField();
        
        gbc.gridy = 0;
        formPanel.add(userLabel, gbc);
        gbc.gridy = 1;
        formPanel.add(usernameField, gbc);

        // Mot de passe
        JLabel passLabel = createFormLabel("Mot de passe *");
        passwordField = createStyledPasswordField();
        
        gbc.gridy = 2;
        formPanel.add(passLabel, gbc);
        gbc.gridy = 3;
        formPanel.add(passwordField, gbc);

        // Nom
        JLabel nomLabel = createFormLabel("Nom");
        nomField = createStyledTextField();
        
        gbc.gridy = 4;
        formPanel.add(nomLabel, gbc);
        gbc.gridy = 5;
        formPanel.add(nomField, gbc);

        // Prénom
        JLabel prenomLabel = createFormLabel("Prénom");
        prenomField = createStyledTextField();
        
        gbc.gridy = 6;
        formPanel.add(prenomLabel, gbc);
        gbc.gridy = 7;
        formPanel.add(prenomField, gbc);

        // Email
        JLabel emailLabel = createFormLabel("Email *");
        emailField = createStyledTextField();
        
        gbc.gridy = 8;
        formPanel.add(emailLabel, gbc);
        gbc.gridy = 9;
        formPanel.add(emailField, gbc);

        // Rôle
        JLabel roleLabel = createFormLabel("Rôle *");
        roleComboBox = createStyledComboBox();
        roleComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"VENDEUR", "MANAGER", "ADMIN"}));
        roleComboBox.setSelectedItem("VENDEUR");
        
        roleComboBox.setToolTipText("VENDEUR: Vente et clients seulement\nMANAGER: Gestion complète\nADMIN: Administration système");
        
        gbc.gridy = 10;
        formPanel.add(roleLabel, gbc);
        gbc.gridy = 11;
        formPanel.add(roleComboBox, gbc);

        // Indication champs obligatoires
        JLabel requiredLabel = new JLabel("* Champs obligatoires");
        requiredLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11)); // Police plus petite
        requiredLabel.setForeground(new Color(234, 67, 53));
        
        gbc.gridy = 12;
        gbc.insets = new Insets(12, 0, 0, 0);
        formPanel.add(requiredLabel, gbc);

        // Information sur les rôles
        JTextArea roleInfo = new JTextArea();
        roleInfo.setEditable(false);
        roleInfo.setOpaque(false);
        roleInfo.setLineWrap(true);
        roleInfo.setWrapStyleWord(true);
        roleInfo.setFont(new Font("Segoe UI", Font.PLAIN, 10)); // Police plus petite
        roleInfo.setForeground(new Color(117, 117, 117));
        roleInfo.setText("VENDEUR: Gestion des ventes et clients\nMANAGER: Gestion complète du stock\nADMIN: Administration système complète");
        roleInfo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        gbc.gridy = 13;
        gbc.insets = new Insets(3, 0, 0, 0);
        formPanel.add(roleInfo, gbc);

        contentPanel.add(formPanel, BorderLayout.CENTER);

        // Panel des boutons
        buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); // Ajout de marge en haut
        
        signupButton = createPrimaryButton("S'inscrire");
        cancelButton = createSecondaryButton("Annuler");
        
        // Bouton de connexion existante
        haveAccountLabel = new JLabel("Vous avez déjà un compte ? ");
        haveAccountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        haveAccountLabel.setForeground(new Color(117, 117, 117));
        
        loginButton = new JButton("Se connecter");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        loginButton.setForeground(new Color(66, 133, 244));
        loginButton.setBorderPainted(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(e -> {
            dispose();
            new LoginForm().setVisible(true);
        });
        
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        loginPanel.setBackground(Color.WHITE);
        loginPanel.add(haveAccountLabel);
        loginPanel.add(loginButton);

        GridBagConstraints btnGbc = new GridBagConstraints();
        btnGbc.fill = GridBagConstraints.HORIZONTAL;
        btnGbc.gridwidth = 2;
        btnGbc.insets = new Insets(5, 0, 5, 0);
        btnGbc.gridx = 0;
        
        btnGbc.gridy = 0;
        buttonPanel.add(signupButton, btnGbc);
        
        btnGbc.gridy = 1;
        buttonPanel.add(cancelButton, btnGbc);
        
        btnGbc.gridy = 2;
        btnGbc.insets = new Insets(15, 0, 0, 0);
        buttonPanel.add(loginPanel, btnGbc);

        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Créer le JScrollPane et y ajouter le contentPanel
        scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null); // Enlever la bordure du scroll
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Vitesse de scroll
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        // Ajouter un écouteur pour masquer/afficher la barre de scroll
        scrollPane.getVerticalScrollBar().addAdjustmentListener(e -> {
            JScrollBar scrollBar = (JScrollBar) e.getSource();
            if (scrollBar.getMaximum() == scrollBar.getVisibleAmount()) {
                scrollBar.setVisible(false);
            } else {
                scrollBar.setVisible(true);
            }
        });

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel);

        // Actions des boutons
        signupButton.addActionListener(e -> handleSignUp());
        cancelButton.addActionListener(e -> dispose());

        // Touche Entrée = inscription
        getRootPane().setDefaultButton(signupButton);
    }

    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12)); // Police réduite
        label.setForeground(new Color(66, 66, 66));
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (hasFocus()) {
                    g2d.setColor(new Color(66, 133, 244));
                    g2d.setStroke(new BasicStroke(2));
                } else {
                    g2d.setColor(new Color(224, 224, 224));
                    g2d.setStroke(new BasicStroke(1));
                }
                
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8); // Rayon réduit
                g2d.dispose();
            }
        };
        
        field.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12)); // Padding réduit
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setOpaque(false);
        field.setPreferredSize(new Dimension(350, 38)); // Taille réduite
        
        // Effet de focus
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.repaint();
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                field.repaint();
            }
        });
        
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (hasFocus()) {
                    g2d.setColor(new Color(66, 133, 244));
                    g2d.setStroke(new BasicStroke(2));
                } else {
                    g2d.setColor(new Color(224, 224, 224));
                    g2d.setStroke(new BasicStroke(1));
                }
                
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                g2d.dispose();
            }
        };
        
        field.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setOpaque(false);
        field.setPreferredSize(new Dimension(350, 38));
        
        // Effet de focus
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.repaint();
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                field.repaint();
            }
        });
        
        return field;
    }

    private JComboBox<String> createStyledComboBox() {
        JComboBox<String> comboBox = new JComboBox<String>() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (hasFocus()) {
                    g2d.setColor(new Color(66, 133, 244));
                    g2d.setStroke(new BasicStroke(2));
                } else {
                    g2d.setColor(new Color(224, 224, 224));
                    g2d.setStroke(new BasicStroke(1));
                }
                
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                g2d.dispose();
            }
        };
        
        comboBox.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboBox.setBackground(Color.WHITE);
        comboBox.setPreferredSize(new Dimension(350, 38));
        
        // Styliser le renderer
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
                
                // Personnaliser l'affichage
                if (value != null) {
                    String role = value.toString();
                    switch (role) {
                        case "ADMIN":
                            label.setForeground(new Color(219, 68, 55));
                            break;
                        case "MANAGER":
                            label.setForeground(new Color(244, 180, 0));
                            break;
                        case "VENDEUR":
                            label.setForeground(new Color(15, 157, 88));
                            break;
                    }
                }
                
                if (isSelected) {
                    label.setBackground(new Color(232, 240, 254));
                    label.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
                }
                
                return label;
            }
        });
        
        return comboBox;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color bgColor = new Color(66, 133, 244);
                
                if (getModel().isRollover()) {
                    g2d.setColor(bgColor.darker());
                } else if (getModel().isPressed()) {
                    g2d.setColor(bgColor.darker().darker());
                } else {
                    g2d.setColor(bgColor);
                }
                
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                super.paintComponent(g);
                g2d.dispose();
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(350, 40));
        
        return button;
    }

    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color bgColor = new Color(245, 245, 245);
                Color borderColor = new Color(224, 224, 224);
                
                if (getModel().isRollover()) {
                    g2d.setColor(bgColor.darker());
                    borderColor = borderColor.darker();
                } else if (getModel().isPressed()) {
                    g2d.setColor(bgColor.darker().darker());
                    borderColor = borderColor.darker().darker();
                } else {
                    g2d.setColor(bgColor);
                }
                
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                // Bordure
                g2d.setColor(borderColor);
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                
                super.paintComponent(g);
                g2d.dispose();
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(new Color(66, 66, 66));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(350, 40));
        
        return button;
    }

    private void handleSignUp() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        String email = emailField.getText().trim();
        String role = (String) roleComboBox.getSelectedItem();

        // Validation des champs
        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            showErrorDialog("Les champs marqués d'un * sont obligatoires");
            return;
        }

        if (password.length() < 6) {
            showErrorDialog("Le mot de passe doit contenir au moins 6 caractères");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            showErrorDialog("Veuillez entrer une adresse email valide");
            return;
        }

        // Vérifier si le rôle ADMIN est sélectionné
        if ("ADMIN".equals(role)) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "⚠️ Attention: Sélection du rôle ADMIN\n\n" +
                "Le rôle ADMIN donne un accès complet au système, y compris:\n" +
                "• Gestion des utilisateurs\n" +
                "• Administration système\n" +
                "• Toutes les fonctionnalités\n\n" +
                "Êtes-vous sûr de vouloir créer un compte ADMIN?",
                "Confirmation rôle ADMIN",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (confirm != JOptionPane.YES_OPTION) {
                roleComboBox.setSelectedItem("VENDEUR");
                return;
            }
        }

        // Animation de chargement
        signupButton.setText("Inscription en cours...");
        signupButton.setEnabled(false);

        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                UtilisateurService service = new UtilisateurService();
                return service.inscrireUtilisateur(username, password, nom, prenom, email, role);
            }

            @Override
            protected void done() {
                try {
                    boolean success = get();
                    
                    if (success) {
                        String roleMessage = getRoleMessage(role);
                        
                        JOptionPane.showMessageDialog(SignUpForm.this,
                            "<html><div style='text-align:center;'>"
                            + "<b>Inscription réussie !</b><br>"
                            + "Votre compte a été créé avec succès.<br><br>"
                            + "<b>Rôle:</b> " + roleMessage + "<br><br>"
                            + "Vous pouvez maintenant vous connecter."
                            + "</div></html>",
                            "Succès", JOptionPane.INFORMATION_MESSAGE);
                        
                        dispose();
                        new LoginForm().setVisible(true);
                    } else {
                        showErrorDialog("Nom d'utilisateur ou email déjà utilisé");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showErrorDialog("Une erreur est survenue lors de l'inscription: " + e.getMessage());
                } finally {
                    signupButton.setText("S'inscrire");
                    signupButton.setEnabled(true);
                }
            }
        };
        
        worker.execute();
    }

    private String getRoleMessage(String role) {
        switch (role) {
            case "ADMIN":
                return "Administrateur (accès complet)";
            case "MANAGER":
                return "Manager (gestion complète)";
            case "VENDEUR":
                return "Vendeur (ventes et clients)";
            default:
                return role;
        }
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this,
            "<html><div style='text-align:center;'>"
            + "<b>" + message + "</b>"
            + "</div></html>",
            "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}