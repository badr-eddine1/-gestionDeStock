package view;

import connexion.MyConnexion;
import model.Utilisateur;
import service.UtilisateurService;

import java.awt.*;
import javax.swing.*;

public class LoginForm extends JFrame {
    private JPanel mainPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JLabel userLabel;
    private JTextField usernameField;
    private JLabel passLabel;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;
    private JButton signupButton;
    private JLabel welcomeLabel;
    private JLabel iconLabel;
    
    private static Utilisateur utilisateurConnecte; // Variable statique pour stocker l'utilisateur connecté

    public LoginForm() {
        initComponents();
        setupFrame();
    }

    private void setupFrame() {
        setTitle("Connexion - Gestion de Stock");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(245, 247, 250));
    }

    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(255, 255, 255));

        // Panel gauche (graphique/branding)
        leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                // Dégradé de fond
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(66, 133, 244),
                    0, getHeight(), new Color(52, 168, 83)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                // Motifs décoratifs
                g2d.setColor(new Color(255, 255, 255, 30));
                g2d.setStroke(new BasicStroke(2));
                // Cercles décoratifs
                g2d.drawOval(50, 50, 80, 80);
                g2d.drawOval(getWidth() - 130, getHeight() - 130, 80, 80);
                g2d.drawOval(getWidth()/2 - 40, getHeight()/2 - 40, 80, 80);
                g2d.dispose();
            }
        };
        leftPanel.setPreferredSize(new Dimension(350, 500));
        leftPanel.setLayout(new BorderLayout());

        // Contenu du panel gauche
        JPanel leftContent = new JPanel(new BorderLayout());
        leftContent.setOpaque(false);
        leftContent.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        welcomeLabel = new JLabel("<html><div style='text-align:center'>"
                + "<h1 style='color:white; font-size:32px; margin-bottom:10px;'>Bienvenue</h1>"
                + "<p style='color: rgba(255,255,255,0.8); font-size:16px;'>"
                + "Système de Gestion de Stock<br>Connectez-vous pour accéder au tableau de bord</p>"
                + "</div></html>");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Icône moderne
        iconLabel = new JLabel("🔐") {
            @Override
            public void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Fond circulaire
                g2d.setColor(new Color(255, 255, 255, 30));
                g2d.fillOval(0, 0, 100, 100);
                super.paintComponent(g);
                g2d.dispose();
            }
        };
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setPreferredSize(new Dimension(100, 100));
        
        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        iconPanel.setOpaque(false);
        iconPanel.add(iconLabel);
        
        leftContent.add(welcomeLabel, BorderLayout.NORTH);
        leftContent.add(iconPanel, BorderLayout.CENTER);
        
        // Copyright en bas
        JLabel copyrightLabel = new JLabel("© 2024 StockMaster Pro");
        copyrightLabel.setForeground(new Color(255, 255, 255, 180));
        copyrightLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        copyrightLabel.setHorizontalAlignment(SwingConstants.CENTER);
        leftContent.add(copyrightLabel, BorderLayout.SOUTH);
        
        leftPanel.add(leftContent, BorderLayout.CENTER);
        mainPanel.add(leftPanel, BorderLayout.WEST);

        // Panel droit (formulaire)
        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // En-tête du formulaire
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        titleLabel = new JLabel("Connexion");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(33, 33, 33));
        subtitleLabel = new JLabel("Accédez à votre espace personnel");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(117, 117, 117));
        
        JPanel titlesPanel = new JPanel(new BorderLayout(0, 5));
        titlesPanel.setOpaque(false);
        titlesPanel.add(titleLabel, BorderLayout.NORTH);
        titlesPanel.add(subtitleLabel, BorderLayout.SOUTH);
        headerPanel.add(titlesPanel, BorderLayout.NORTH);
        rightPanel.add(headerPanel, BorderLayout.NORTH);

        // Formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 30, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 0, 0, 0);

        // Champ utilisateur
        userLabel = new JLabel("Nom d'utilisateur");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        userLabel.setForeground(new Color(66, 66, 66));
        usernameField = createStyledTextField();
        usernameField.setPreferredSize(new Dimension(300, 45));
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(userLabel, gbc);
        gbc.gridy = 1;
        formPanel.add(usernameField, gbc);

        // Champ mot de passe
        passLabel = new JLabel("Mot de passe");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        passLabel.setForeground(new Color(66, 66, 66));
        passwordField = createStyledPasswordField();
        passwordField.setPreferredSize(new Dimension(300, 45));
        gbc.gridy = 2;
        formPanel.add(passLabel, gbc);
        gbc.gridy = 3;
        formPanel.add(passwordField, gbc);

        // Boutons
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        loginButton = createStyledButton("Se connecter", new Color(66, 133, 244));
        cancelButton = createStyledButton("Annuler", new Color(117, 117, 117));
        signupButton = new JButton("Créer un compte");
        
        // Style du bouton créer compte
        signupButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        signupButton.setForeground(new Color(66, 133, 244));
        signupButton.setBorderPainted(false);
        signupButton.setContentAreaFilled(false);
        signupButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signupButton.setFocusPainted(false);
        signupButton.addActionListener(e -> new SignUpForm().setVisible(true));
        
        loginButton.addActionListener(e -> handleLogin());
        cancelButton.addActionListener(e -> System.exit(0));
        
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(loginButton, gbc);
        gbc.gridy = 1;
        buttonPanel.add(cancelButton, gbc);
        gbc.gridy = 2;
        buttonPanel.add(signupButton, gbc);
        
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.insets = new Insets(30, 0, 0, 0);
        formPanel.add(buttonPanel, gbc);
        rightPanel.add(formPanel, BorderLayout.CENTER);

        // Lien "Mot de passe oublié"
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setOpaque(false);
        JLabel forgotLabel = new JLabel("Mot de passe oublié ?");
        forgotLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        forgotLabel.setForeground(new Color(66, 133, 244));
        forgotLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Effet survol pour le lien
        forgotLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                forgotLabel.setForeground(new Color(33, 111, 219));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                forgotLabel.setForeground(new Color(66, 133, 244));
            }
        });
        
        footerPanel.add(forgotLabel);
        rightPanel.add(footerPanel, BorderLayout.SOUTH);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        add(mainPanel);
        
        // Entrée = connexion
        getRootPane().setDefaultButton(loginButton);
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
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                g2d.dispose();
            }
        };
        field.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setOpaque(false);
        
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
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                g2d.dispose();
            }
        };
        field.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setOpaque(false);
        
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

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Fond avec effet survol
                if (getModel().isRollover()) {
                    g2d.setColor(bgColor.darker());
                } else if (getModel().isPressed()) {
                    g2d.setColor(bgColor.darker().darker());
                } else {
                    g2d.setColor(bgColor);
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
                g2d.dispose();
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(300, 45));
        return button;
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Veuillez remplir tous les champs",
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        UtilisateurService utilisateurService = new UtilisateurService();
        Utilisateur utilisateur = utilisateurService.authentifier(username, password);
        
        if (utilisateur != null) {
            // Stocker l'utilisateur connecté
            utilisateurConnecte = utilisateur;
            
            // Afficher un message de bienvenue avec le rôle
            String roleMessage = "";
            switch (utilisateur.getRole()) {
                case "ADMIN":
                    roleMessage = "Administrateur";
                    break;
                case "MANAGER":
                    roleMessage = "Manager";
                    break;
                case "VENDEUR":
                    roleMessage = "Vendeur";
                    break;
                default:
                    roleMessage = utilisateur.getRole();
            }
            
            JOptionPane.showMessageDialog(this,
                "Connexion réussie!\n" +
                "Bienvenue " + utilisateur.getNomComplet() + "\n" +
                "Rôle: " + roleMessage,
                "Succès", JOptionPane.INFORMATION_MESSAGE);
            
            dispose();
            SwingUtilities.invokeLater(() -> new MainFrame(utilisateur).setVisible(true));
        } else {
            JOptionPane.showMessageDialog(this,
                "Nom d'utilisateur ou mot de passe incorrect",
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Méthode statique pour obtenir l'utilisateur connecté depuis n'importe où
    public static Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }
    
    // Méthode pour déconnecter l'utilisateur
    public static void logout() {
        utilisateurConnecte = null;
    }

    public static void main(String[] args) {
        // Appliquer un look and feel moderne
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // Personaliser les couleurs des fenêtres de dialogue
            UIManager.put("OptionPane.background", Color.WHITE);
            UIManager.put("Panel.background", Color.WHITE);
            UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 14));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            if (MyConnexion.testerConnexion()) {
                new LoginForm().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null,
                    "<html><div style='text-align:center;'>"
                    + "<b>Erreur de connexion à la base de données</b><br>"
                    + "Veuillez vérifier votre connexion et réessayer"
                    + "</div></html>",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}