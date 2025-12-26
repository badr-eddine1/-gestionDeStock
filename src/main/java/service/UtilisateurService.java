package service;

import connexion.MyConnexion;
import model.Utilisateur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurService {

    // Méthode d'authentification qui retourne l'utilisateur avec son rôle
    public Utilisateur authentifier(String username, String password) {
        String sql = "SELECT id, username, password, nom, prenom, email, role, date_creation, actif " +
                    "FROM utilisateur " +
                    "WHERE username = ? " +
                    "AND password = SHA2(?, 256) " +
                    "AND actif = 1";
        
        try (PreparedStatement stmt = MyConnexion.getCnx().prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUtilisateur(rs);
            }
        } catch (SQLException e) {
            System.out.println("Erreur authentification : " + e.getMessage());
        }
        return null;
    }

    // Méthode d'inscription
    public boolean inscrireUtilisateur(String username, String password,
                                      String nom, String prenom, String email, String role) {
        String checkSql = "SELECT id FROM utilisateur WHERE username = ? OR email = ?";
        String insertSql = "INSERT INTO utilisateur " +
                          "(username, password, nom, prenom, email, role, actif) " +
                          "VALUES (?, SHA2(?, 256), ?, ?, ?, ?, 1)";
        
        try (PreparedStatement checkStmt = MyConnexion.getCnx().prepareStatement(checkSql)) {
            checkStmt.setString(1, username);
            checkStmt.setString(2, email);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                // username ou email déjà utilisé
                return false;
            }
            
            try (PreparedStatement insertStmt = MyConnexion.getCnx().prepareStatement(insertSql)) {
                insertStmt.setString(1, username);
                insertStmt.setString(2, password);
                insertStmt.setString(3, nom);
                insertStmt.setString(4, prenom);
                insertStmt.setString(5, email);
                insertStmt.setString(6, role != null ? role : "VENDEUR");
                
                return insertStmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println("Erreur inscription : " + e.getMessage());
            return false;
        }
    }

    // Méthodes CRUD supplémentaires pour la gestion des utilisateurs

    public Utilisateur findById(int id) {
        String sql = "SELECT * FROM utilisateur WHERE id = ?";
        
        try (PreparedStatement stmt = MyConnexion.getCnx().prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUtilisateur(rs);
            }
        } catch (SQLException e) {
            System.out.println("Erreur findById : " + e.getMessage());
        }
        return null;
    }

    public List<Utilisateur> findAll() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM utilisateur ORDER BY role, nom";
        
        try (Statement stmt = MyConnexion.getCnx().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                utilisateurs.add(mapResultSetToUtilisateur(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erreur findAll : " + e.getMessage());
        }
        return utilisateurs;
    }

    public boolean update(Utilisateur utilisateur) {
        String sql = "UPDATE utilisateur SET " +
                    "username = ?, nom = ?, prenom = ?, email = ?, " +
                    "role = ?, actif = ? " +
                    "WHERE id = ?";
        
        try (PreparedStatement stmt = MyConnexion.getCnx().prepareStatement(sql)) {
            stmt.setString(1, utilisateur.getUsername());
            stmt.setString(2, utilisateur.getNom());
            stmt.setString(3, utilisateur.getPrenom());
            stmt.setString(4, utilisateur.getEmail());
            stmt.setString(5, utilisateur.getRole());
            stmt.setBoolean(6, utilisateur.isActif());
            stmt.setInt(7, utilisateur.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur update : " + e.getMessage());
            return false;
        }
    }

    public boolean updatePassword(int userId, String newPassword) {
        String sql = "UPDATE utilisateur SET password = SHA2(?, 256) WHERE id = ?";
        
        try (PreparedStatement stmt = MyConnexion.getCnx().prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setInt(2, userId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur updatePassword : " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM utilisateur WHERE id = ?";
        
        try (PreparedStatement stmt = MyConnexion.getCnx().prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur delete : " + e.getMessage());
            return false;
        }
    }

    public boolean desactiver(int id) {
        String sql = "UPDATE utilisateur SET actif = FALSE WHERE id = ?";
        
        try (PreparedStatement stmt = MyConnexion.getCnx().prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur desactiver : " + e.getMessage());
            return false;
        }
    }

    // Méthode pour obtenir les utilisateurs par rôle
    public List<Utilisateur> findByRole(String role) {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM utilisateur WHERE role = ? AND actif = 1 ORDER BY nom";
        
        try (PreparedStatement stmt = MyConnexion.getCnx().prepareStatement(sql)) {
            stmt.setString(1, role);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                utilisateurs.add(mapResultSetToUtilisateur(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erreur findByRole : " + e.getMessage());
        }
        return utilisateurs;
    }

    // Méthode utilitaire pour mapper un ResultSet vers un objet Utilisateur
    private Utilisateur mapResultSetToUtilisateur(ResultSet rs) throws SQLException {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(rs.getInt("id"));
        utilisateur.setUsername(rs.getString("username"));
        utilisateur.setPassword(rs.getString("password"));
        utilisateur.setNom(rs.getString("nom"));
        utilisateur.setPrenom(rs.getString("prenom"));
        utilisateur.setEmail(rs.getString("email"));
        utilisateur.setRole(rs.getString("role"));
        utilisateur.setDateCreation(rs.getTimestamp("date_creation"));
        utilisateur.setActif(rs.getBoolean("actif"));
        return utilisateur;
    }

    // Méthode pour vérifier si un nom d'utilisateur existe déjà
    public boolean usernameExists(String username) {
        String sql = "SELECT id FROM utilisateur WHERE username = ?";
        
        try (PreparedStatement stmt = MyConnexion.getCnx().prepareStatement(sql)) {
            stmt.setString(1, username);
            
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Erreur usernameExists : " + e.getMessage());
            return false;
        }
    }

    // Méthode pour vérifier si un email existe déjà
    public boolean emailExists(String email) {
        String sql = "SELECT id FROM utilisateur WHERE email = ?";
        
        try (PreparedStatement stmt = MyConnexion.getCnx().prepareStatement(sql)) {
            stmt.setString(1, email);
            
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Erreur emailExists : " + e.getMessage());
            return false;
        }
    }
}