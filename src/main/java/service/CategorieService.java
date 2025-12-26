/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.IDao;
import model.Categorie;
import connexion.MyConnexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategorieService implements IDao<Categorie> {
    
    @Override
    public boolean create(Categorie categorie) {
        String sql = "INSERT INTO categorie (code, nom, description) VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = MyConnexion.getCnx().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, categorie.getCode());
            stmt.setString(2, categorie.getNom());
            stmt.setString(3, categorie.getDescription());
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    categorie.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la catégorie: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public boolean update(Categorie categorie) {
        String sql = "UPDATE categorie SET code=?, nom=?, description=? WHERE id=?";
        
        try (PreparedStatement stmt = MyConnexion.getCnx().prepareStatement(sql)) {
            stmt.setString(1, categorie.getCode());
            stmt.setString(2, categorie.getNom());
            stmt.setString(3, categorie.getDescription());
            stmt.setInt(4, categorie.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de la catégorie: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public boolean delete(Categorie categorie) {
        return deleteById(categorie.getId());
    }
    
    public boolean deleteById(int id) {
        String sql = "DELETE FROM categorie WHERE id = ?";
        try (PreparedStatement stmt = MyConnexion.getCnx().prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la catégorie: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public Categorie findById(int id) {
        String sql = "SELECT c.*, COUNT(p.id) as nombre_produits FROM categorie c "
                   + "LEFT JOIN produit p ON c.id = p.categorie_id WHERE c.id = ? GROUP BY c.id";
        
        try (PreparedStatement stmt = MyConnexion.getCnx().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return resultSetToCategorie(rs);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche de la catégorie: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public List<Categorie> findAll() {
        List<Categorie> categories = new ArrayList<>();
        String sql = "SELECT c.*, COUNT(p.id) as nombre_produits FROM categorie c "
                   + "LEFT JOIN produit p ON c.id = p.categorie_id GROUP BY c.id ORDER BY c.nom";
        
        try (Statement stmt = MyConnexion.getCnx().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                categories.add(resultSetToCategorie(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des catégories: " + e.getMessage());
        }
        return categories;
    }
    
    private Categorie resultSetToCategorie(ResultSet rs) throws SQLException {
        Categorie categorie = new Categorie();
        categorie.setId(rs.getInt("id"));
        categorie.setCode(rs.getString("code"));
        categorie.setNom(rs.getString("nom"));
        categorie.setDescription(rs.getString("description"));
        categorie.setNombreProduits(rs.getInt("nombre_produits"));
        return categorie;
    }
}
