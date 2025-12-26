/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.IDao;
import model.Stock;
import connexion.MyConnexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockService implements IDao<Stock> {
    
    @Override
    public boolean create(Stock stock) {
        String sql = "INSERT INTO stock (produit_id, quantite, quantite_min, quantite_max, emplacement) "
                   + "VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = MyConnexion.getCnx().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, stock.getProduitId());
            stmt.setInt(2, stock.getQuantite());
            stmt.setInt(3, stock.getQuantiteMin());
            stmt.setInt(4, stock.getQuantiteMax());
            stmt.setString(5, stock.getEmplacement());
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    stock.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du stock: " + e.getMessage());
        }
        return false;
    }
    
public boolean entreeStock(int produitId, int quantite, String motif) {
    Connection conn = null;
    try {
        conn = MyConnexion.getCnx();
        conn.setAutoCommit(false);

        String updateStock = "UPDATE stock SET quantite = quantite + ? WHERE produit_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(updateStock)) {
            stmt.setInt(1, quantite);
            stmt.setInt(2, produitId);
            if (stmt.executeUpdate() == 0) {
                conn.rollback();
                return false;
            }
        }

        enregistrerMouvement(conn, produitId, "ENTREE", quantite, motif);

        conn.commit();
        return true;

    } catch (SQLException e) {
        try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
        System.out.println("Erreur entrée stock : " + e.getMessage());
        return false;
    } finally {
        try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException e) {}
    }
}

public boolean sortieStock(int produitId, int quantite, String motif) {
    Connection conn = null;
    try {
        conn = MyConnexion.getCnx();
        conn.setAutoCommit(false);

        String updateStock = "UPDATE stock SET quantite = quantite - ? WHERE produit_id = ? AND quantite >= ?";
        try (PreparedStatement stmt = conn.prepareStatement(updateStock)) {
            stmt.setInt(1, quantite);
            stmt.setInt(2, produitId);
            stmt.setInt(3, quantite);
            if (stmt.executeUpdate() == 0) {
                conn.rollback();
                return false;
            }
        }

        enregistrerMouvement(conn, produitId, "SORTIE", quantite, motif);

        conn.commit();
        return true;

    } catch (SQLException e) {
        try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
        System.out.println("Erreur sortie stock : " + e.getMessage());
        return false;
    } finally {
        try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException e) {}
    }
}

private void enregistrerMouvement(Connection conn, int produitId, String type, int quantite, String motif)
        throws SQLException {

    String sql = "INSERT INTO mouvement_stock (produit_id, type, quantite, motif) VALUES (?, ?, ?, ?)";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, produitId);
        stmt.setString(2, type);
        stmt.setInt(3, quantite);
        stmt.setString(4, motif);
        stmt.executeUpdate();
    }
}

    
    @Override
    public boolean update(Stock stock) {
        String sql = "UPDATE stock SET quantite=?, quantite_min=?, quantite_max=?, emplacement=? WHERE id=?";
        
        try (PreparedStatement stmt = MyConnexion.getCnx().prepareStatement(sql)) {
            stmt.setInt(1, stock.getQuantite());
            stmt.setInt(2, stock.getQuantiteMin());
            stmt.setInt(3, stock.getQuantiteMax());
            stmt.setString(4, stock.getEmplacement());
            stmt.setInt(5, stock.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification du stock: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public boolean delete(Stock stock) {
        return deleteById(stock.getId());
    }
    
    public boolean deleteById(int id) {
        String sql = "DELETE FROM stock WHERE id = ?";
        try (PreparedStatement stmt = MyConnexion.getCnx().prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du stock: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public Stock findById(int id) {
        String sql = "SELECT s.*, p.nom as produit_nom FROM stock s "
                   + "LEFT JOIN produit p ON s.produit_id = p.id WHERE s.id = ?";
        
        try (PreparedStatement stmt = MyConnexion.getCnx().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return resultSetToStock(rs);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche du stock: " + e.getMessage());
        }
        return null;
    }
    
    public Stock findByProduitId(int produitId) {
        String sql = "SELECT s.*, p.nom as produit_nom FROM stock s "
                   + "LEFT JOIN produit p ON s.produit_id = p.id WHERE s.produit_id = ?";
        
        try (PreparedStatement stmt = MyConnexion.getCnx().prepareStatement(sql)) {
            stmt.setInt(1, produitId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return resultSetToStock(rs);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche du stock par produit: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public List<Stock> findAll() {
        List<Stock> stocks = new ArrayList<>();
        String sql = "SELECT s.*, p.nom as produit_nom FROM stock s "
                   + "LEFT JOIN produit p ON s.produit_id = p.id ORDER BY p.nom";
        
        try (Statement stmt = MyConnexion.getCnx().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                stocks.add(resultSetToStock(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des stocks: " + e.getMessage());
        }
        return stocks;
    }
    
    public List<Stock> getStocksEnAlerte() {
        List<Stock> stocks = new ArrayList<>();
        String sql = "SELECT s.*, p.nom as produit_nom FROM stock s "
                   + "LEFT JOIN produit p ON s.produit_id = p.id "
                   + "WHERE s.quantite <= s.quantite_min ORDER BY s.quantite";
        
        try (Statement stmt = MyConnexion.getCnx().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                stocks.add(resultSetToStock(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche des stocks en alerte: " + e.getMessage());
        }
        return stocks;
    }
    
    private Stock resultSetToStock(ResultSet rs) throws SQLException {
        Stock stock = new Stock();
        stock.setId(rs.getInt("id"));
        stock.setProduitId(rs.getInt("produit_id"));
        stock.setProduitNom(rs.getString("produit_nom"));
        stock.setQuantite(rs.getInt("quantite"));
        stock.setQuantiteMin(rs.getInt("quantite_min"));
        stock.setQuantiteMax(rs.getInt("quantite_max"));
        stock.setEmplacement(rs.getString("emplacement"));
        stock.setDateMaj(rs.getTimestamp("date_maj").toLocalDateTime());
        return stock;
    }
    
    
    public Stock findByProduitNom(String produitNom) {
    String sql = "SELECT s.* FROM stock s " +
                 "JOIN produit p ON s.produit_id = p.id " +
                 "WHERE p.nom = ?";
    
    try (PreparedStatement stmt = MyConnexion.getCnx().prepareStatement(sql)) {
        stmt.setString(1, produitNom);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            Stock stock = new Stock();
            stock.setId(rs.getInt("id"));
            stock.setProduitId(rs.getInt("produit_id"));
            stock.setQuantite(rs.getInt("quantite"));
            stock.setQuantiteMin(rs.getInt("quantite_min"));
            stock.setQuantiteMax(rs.getInt("quantite_max"));
            stock.setEmplacement(rs.getString("emplacement"));
            stock.setDateMaj(rs.getTimestamp("date_maj").toLocalDateTime());
            return stock;
        }
    } catch (SQLException e) {
        System.out.println("Erreur lors de la recherche du stock par nom: " + e.getMessage());
    }
    return null;
}
    
}
