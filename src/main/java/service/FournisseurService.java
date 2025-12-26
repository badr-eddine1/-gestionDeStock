/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.IDao;
import model.Fournisseur;
import connexion.MyConnexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FournisseurService implements IDao<Fournisseur> {
    
    @Override
    public boolean create(Fournisseur fournisseur) {
        String sql = "INSERT INTO fournisseur (code, nom, telephone, email, adresse, ville, pays) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = MyConnexion.getCnx().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, fournisseur.getCode());
            stmt.setString(2, fournisseur.getNom());
            stmt.setString(3, fournisseur.getTelephone());
            stmt.setString(4, fournisseur.getEmail());
            stmt.setString(5, fournisseur.getAdresse());
            stmt.setString(6, fournisseur.getVille());
            stmt.setString(7, fournisseur.getPays());
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    fournisseur.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du fournisseur: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public boolean update(Fournisseur fournisseur) {
        String sql = "UPDATE fournisseur SET code=?, nom=?, telephone=?, email=?, adresse=?, ville=?, pays=? WHERE id=?";
        
        try (PreparedStatement stmt = MyConnexion.getCnx().prepareStatement(sql)) {
            stmt.setString(1, fournisseur.getCode());
            stmt.setString(2, fournisseur.getNom());
            stmt.setString(3, fournisseur.getTelephone());
            stmt.setString(4, fournisseur.getEmail());
            stmt.setString(5, fournisseur.getAdresse());
            stmt.setString(6, fournisseur.getVille());
            stmt.setString(7, fournisseur.getPays());
            stmt.setInt(8, fournisseur.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification du fournisseur: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public boolean delete(Fournisseur fournisseur) {
        return deleteById(fournisseur.getId());
    }
    
    public boolean deleteById(int id) {
        String sql = "DELETE FROM fournisseur WHERE id = ?";
        try (PreparedStatement stmt = MyConnexion.getCnx().prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du fournisseur: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public Fournisseur findById(int id) {
        String sql = "SELECT * FROM fournisseur WHERE id = ?";
        
        try (PreparedStatement stmt = MyConnexion.getCnx().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return resultSetToFournisseur(rs);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche du fournisseur: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public List<Fournisseur> findAll() {
        List<Fournisseur> fournisseurs = new ArrayList<>();
        String sql = "SELECT * FROM fournisseur ORDER BY nom";
        
        try (Statement stmt = MyConnexion.getCnx().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                fournisseurs.add(resultSetToFournisseur(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des fournisseurs: " + e.getMessage());
        }
        return fournisseurs;
    }
    
    private Fournisseur resultSetToFournisseur(ResultSet rs) throws SQLException {
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setId(rs.getInt("id"));
        fournisseur.setCode(rs.getString("code"));
        fournisseur.setNom(rs.getString("nom"));
        fournisseur.setTelephone(rs.getString("telephone"));
        fournisseur.setEmail(rs.getString("email"));
        fournisseur.setAdresse(rs.getString("adresse"));
        fournisseur.setVille(rs.getString("ville"));
        fournisseur.setPays(rs.getString("pays"));
        return fournisseur;
    }
}
