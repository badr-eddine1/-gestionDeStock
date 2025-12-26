/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.IDao;
import model.Produit;
import connexion.MyConnexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProduitService implements IDao<Produit> {
    
    @Override
    public boolean create(Produit produit) {
        String sql = "INSERT INTO produit (reference, nom, description, prix_achat, prix_vente, "
                   + "categorie_id, fournisseur_id, seuil_alerte, unite_mesure, image) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = MyConnexion.getCnx().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, produit.getReference());
            stmt.setString(2, produit.getNom());
            stmt.setString(3, produit.getDescription());
            stmt.setDouble(4, produit.getPrixAchat());
            stmt.setDouble(5, produit.getPrixVente());
            stmt.setInt(6, produit.getCategorieId());
            stmt.setInt(7, produit.getFournisseurId());
            stmt.setInt(8, produit.getSeuilAlerte());
            stmt.setString(9, produit.getUniteMesure());
            stmt.setString(10, produit.getImage());
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    produit.setId(rs.getInt(1));
                    creerEntreeStock(produit.getId(), 0);
                }
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du produit: " + e.getMessage());
        }
        return false;
    }
    
    private void creerEntreeStock(int produitId, int quantiteInitiale) throws SQLException {
        String sql = "INSERT INTO stock (produit_id, quantite) VALUES (?, ?)";
        try (PreparedStatement stmt = MyConnexion.getCnx().prepareStatement(sql)) {
            stmt.setInt(1, produitId);
            stmt.setInt(2, quantiteInitiale);
            stmt.executeUpdate();
        }
    }
    
    @Override
    public boolean update(Produit produit) {
        String sql = "UPDATE produit SET reference=?, nom=?, description=?, prix_achat=?, prix_vente=?, "
                   + "categorie_id=?, fournisseur_id=?, seuil_alerte=?, unite_mesure=?, image=? WHERE id=?";
        
        try (PreparedStatement stmt = MyConnexion.getCnx().prepareStatement(sql)) {
            stmt.setString(1, produit.getReference());
            stmt.setString(2, produit.getNom());
            stmt.setString(3, produit.getDescription());
            stmt.setDouble(4, produit.getPrixAchat());
            stmt.setDouble(5, produit.getPrixVente());
            stmt.setInt(6, produit.getCategorieId());
            stmt.setInt(7, produit.getFournisseurId());
            stmt.setInt(8, produit.getSeuilAlerte());
            stmt.setString(9, produit.getUniteMesure());
            stmt.setString(10, produit.getImage());
            stmt.setInt(11, produit.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification du produit: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public boolean delete(Produit produit) {
        return deleteById(produit.getId());
    }
    
   public boolean deleteById(int id) {
    String checkSql = "SELECT COUNT(*) FROM ligne_commande WHERE produit_id = ?";
    String deleteStock = "DELETE FROM stock WHERE produit_id = ?";
    String deleteProduit = "DELETE FROM produit WHERE id = ?";

    Connection conn = null;
    try {
        conn = MyConnexion.getCnx();
        conn.setAutoCommit(false);

        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, id);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                conn.rollback();
                System.out.println("Produit utilisé dans une commande");
                return false;
            }
        }

        try (PreparedStatement stmt1 = conn.prepareStatement(deleteStock)) {
            stmt1.setInt(1, id);
            stmt1.executeUpdate();
        }

        try (PreparedStatement stmt2 = conn.prepareStatement(deleteProduit)) {
            stmt2.setInt(1, id);
            stmt2.executeUpdate();
        }

        conn.commit();
        return true;

    } catch (SQLException e) {
        try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
        System.out.println("Erreur suppression produit : " + e.getMessage());
        return false;
    } finally {
        try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException e) {}
    }
}

    
    @Override
    public Produit findById(int id) {
        String sql = "SELECT p.*, c.nom as categorie_nom, f.nom as fournisseur_nom, "
                   + "s.quantite as quantite_stock FROM produit p "
                   + "LEFT JOIN categorie c ON p.categorie_id = c.id "
                   + "LEFT JOIN fournisseur f ON p.fournisseur_id = f.id "
                   + "LEFT JOIN stock s ON p.id = s.produit_id WHERE p.id = ?";
        
        try (PreparedStatement stmt = MyConnexion.getCnx().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return resultSetToProduit(rs);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche du produit: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public List<Produit> findAll() {
        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT p.*, c.nom as categorie_nom, f.nom as fournisseur_nom, "
                   + "s.quantite as quantite_stock FROM produit p "
                   + "LEFT JOIN categorie c ON p.categorie_id = c.id "
                   + "LEFT JOIN fournisseur f ON p.fournisseur_id = f.id "
                   + "LEFT JOIN stock s ON p.id = s.produit_id ORDER BY p.nom";
        
        try (Statement stmt = MyConnexion.getCnx().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                produits.add(resultSetToProduit(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des produits: " + e.getMessage());
        }
        return produits;
    }
    
    public List<Produit> rechercherParNom(String nom) {
        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT p.*, c.nom as categorie_nom, f.nom as fournisseur_nom, "
                   + "s.quantite as quantite_stock FROM produit p "
                   + "LEFT JOIN categorie c ON p.categorie_id = c.id "
                   + "LEFT JOIN fournisseur f ON p.fournisseur_id = f.id "
                   + "LEFT JOIN stock s ON p.id = s.produit_id "
                   + "WHERE p.nom LIKE ? OR p.reference LIKE ? ORDER BY p.nom";
        
        try (PreparedStatement stmt = MyConnexion.getCnx().prepareStatement(sql)) {
            String likeTerm = "%" + nom + "%";
            stmt.setString(1, likeTerm);
            stmt.setString(2, likeTerm);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                produits.add(resultSetToProduit(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche: " + e.getMessage());
        }
        return produits;
    }
    
    public List<Produit> getProduitsEnAlerte() {
        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT p.*, c.nom as categorie_nom, f.nom as fournisseur_nom, "
                   + "s.quantite as quantite_stock FROM produit p "
                   + "LEFT JOIN categorie c ON p.categorie_id = c.id "
                   + "LEFT JOIN fournisseur f ON p.fournisseur_id = f.id "
                   + "LEFT JOIN stock s ON p.id = s.produit_id "
                   + "WHERE s.quantite <= p.seuil_alerte OR s.quantite IS NULL ORDER BY s.quantite";
        
        try (Statement stmt = MyConnexion.getCnx().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                produits.add(resultSetToProduit(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche des produits en alerte: " + e.getMessage());
        }
        return produits;
    }
    
    private Produit resultSetToProduit(ResultSet rs) throws SQLException {
        Produit produit = new Produit();
        produit.setId(rs.getInt("id"));
        produit.setReference(rs.getString("reference"));
        produit.setNom(rs.getString("nom"));
        produit.setDescription(rs.getString("description"));
        produit.setPrixAchat(rs.getDouble("prix_achat"));
        produit.setPrixVente(rs.getDouble("prix_vente"));
        produit.setCategorieId(rs.getInt("categorie_id"));
        produit.setFournisseurId(rs.getInt("fournisseur_id"));
        produit.setSeuilAlerte(rs.getInt("seuil_alerte"));
        produit.setUniteMesure(rs.getString("unite_mesure"));
        produit.setImage(rs.getString("image"));
        produit.setCategorieNom(rs.getString("categorie_nom"));
        produit.setFournisseurNom(rs.getString("fournisseur_nom"));
        produit.setQuantiteStock(rs.getInt("quantite_stock"));
        return produit;
    }
    public int getCategorieIdByName(String nom) {
    String sql = "SELECT id FROM categorie WHERE nom = ?";
    try (PreparedStatement stmt = MyConnexion.getCnx().prepareStatement(sql)) {
        stmt.setString(1, nom);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("id");
        }
    } catch (SQLException e) {
        System.out.println("Erreur lors de la recherche de la catégorie: " + e.getMessage());
    }
    return 0;
}

public int getFournisseurIdByName(String nom) {
    String sql = "SELECT id FROM fournisseur WHERE nom = ?";
    try (PreparedStatement stmt = MyConnexion.getCnx().prepareStatement(sql)) {
        stmt.setString(1, nom);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("id");
        }
    } catch (SQLException e) {
        System.out.println("Erreur lors de la recherche du fournisseur: " + e.getMessage());
    }
    return 0;
}
}
