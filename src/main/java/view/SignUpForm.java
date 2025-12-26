package view;

import service.UtilisateurService;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class SignUpForm extends JFrame {

    private JPanel mainPanel;
    private JPanel formPanel;
    private JPanel buttonPanel;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField nomField;
    private JTextField prenomField;
    private JTextField emailField;
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
        setSize(500, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(new Color(245, 247, 250));
    }

    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        // En-tête
        JPanel headerPanel = new JPanel(new BorderLayout(0, 10));
        headerPanel.setBackground(Color.WHITE);
        
        titleLabel = new JLabel("Créer un compte");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
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
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setPreferredSize(new Dimension(60, 60));
        
        JPanel iconPanel = new JPanel();
        iconPanel.setBackground(new Color(232, 240, 254));
        iconPanel.setPreferredSize(new Dimension(80, 80));
        iconPanel.setLayout(new GridBagLayout());
        iconPanel.setBorder(BorderFactory.createLineBorder(new Color(66, 133, 244, 50), 1));
        iconPanel.add(iconLabel);
        
        headerPanel.add(iconPanel, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Formulaire avec GridBagLayout pour plus de contrôle
        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 30, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 0, 0, 0);
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // Nom d'utilisateur
        JLabel userLabel = createFormLabel("Nom d'utilisateur");
        usernameField = createStyledTextField();
        
        gbc.gridy = 0;
        formPanel.add(userLabel, gbc);
        gbc.gridy = 1;
        formPanel.add(usernameField, gbc);

        // Mot de passe
        JLabel passLabel = createFormLabel("Mot de passe");
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
        JLabel emailLabel = createFormLabel("Email");
        emailField = createStyledTextField();
        
        gbc.gridy = 8;
        formPanel.add(emailLabel, gbc);
        gbc.gridy = 9;
        formPanel.add(emailField, gbc);

        // Indication champs obligatoires
        JLabel requiredLabel = new JLabel("* Champs obligatoires");
        requiredLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        requiredLabel.setForeground(new Color(234, 67, 53));
        
        gbc.gridy = 10;
        gbc.insets = new Insets(20, 0, 0, 0);
        formPanel.add(requiredLabel, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Panel des boutons
        buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        signupButton = createPrimaryButton("S'inscrire");
        cancelButton = createSecondaryButton("Annuler");
        
        // Bouton de connexion existante
        haveAccountLabel = new JLabel("Vous avez déjà un compte ? ");
        haveAccountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        haveAccountLabel.setForeground(new Color(117, 117, 117));
        
        loginButton = new JButton("Se connecter");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
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
        btnGbc.insets = new Insets(20, 0, 0, 0);
        buttonPanel.add(loginPanel, btnGbc);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Actions des boutons
        signupButton.addActionListener(e -> handleSignUp());
        cancelButton.addActionListener(e -> dispose());

        // Touche Entrée = inscription
        getRootPane().setDefaultButton(signupButton);
    }

    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
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
                
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                g2d.dispose();
            }
        };
        
        field.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setOpaque(false);
        field.setPreferredSize(new Dimension(400, 45));
        
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
        field.setPreferredSize(new Dimension(400, 45));
        
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
        button.setPreferredSize(new Dimension(400, 45));
        
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
                
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                // Bordure
                g2d.setColor(borderColor);
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                
                super.paintComponent(g);
                g2d.dispose();
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(new Color(66, 66, 66));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(400, 45));
        
        return button;
    }

    private void handleSignUp() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        String email = emailField.getText().trim();

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

        // Animation de chargement
        signupButton.setText("Inscription en cours...");
        signupButton.setEnabled(false);

        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                UtilisateurService service = new UtilisateurService();
                return service.inscrireUtilisateur(username, password, nom, prenom, email);
            }

            @Override
            protected void done() {
                try {
                    boolean success = get();
                    
                    if (success) {
                        JOptionPane.showMessageDialog(SignUpForm.this,
                            "<html><div style='text-align:center;'>"
                            + "<b>Inscription réussie !</b><br>"
                            + "Votre compte a été créé avec succès.<br>"
                            + "Vous pouvez maintenant vous connecter."
                            + "</div></html>",
                            "Succès", JOptionPane.INFORMATION_MESSAGE);
                        
                        dispose();
                        new LoginForm().setVisible(true);
                    } else {
                        showErrorDialog("Nom d'utilisateur ou email déjà utilisé");
                    }
                } catch (Exception e) {
                    showErrorDialog("Une erreur est survenue lors de l'inscription");
                } finally {
                    signupButton.setText("S'inscrire");
                    signupButton.setEnabled(true);
                }
            }
        };
        
        worker.execute();
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this,
            "<html><div style='text-align:center;'>"
            + "<b>" + message + "</b>"
            + "</div></html>",
            "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}