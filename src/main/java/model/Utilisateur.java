package model;

import java.sql.Timestamp;

public class Utilisateur {
    private int id;
    private String username;
    private String password;
    private String nom;
    private String prenom;
    private String email;
    private String role; // ADMIN, MANAGER, VENDEUR
    private Timestamp dateCreation;
    private boolean actif;

    // Constructeurs
    public Utilisateur() {
    }

    public Utilisateur(int id, String username, String password, String nom, String prenom, 
                      String email, String role, Timestamp dateCreation, boolean actif) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.role = role;
        this.dateCreation = dateCreation;
        this.actif = actif;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Timestamp getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Timestamp dateCreation) {
        this.dateCreation = dateCreation;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    // Méthode pour obtenir le nom complet
    public String getNomComplet() {
        return prenom + " " + nom;
    }

    // Méthodes de vérification de rôle
    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }

    public boolean isManager() {
        return "MANAGER".equals(role);
    }

    public boolean isVendeur() {
        return "VENDEUR".equals(role);
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", actif=" + actif +
                '}';
    }
}