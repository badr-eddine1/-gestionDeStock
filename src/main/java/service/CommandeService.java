/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.IDao;
import model.Commande;
import model.LigneCommande;
import connexion.MyConnexion;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CommandeService implements IDao<Commande> {
    
    @Override

public boolean create(Commande commande) {
    Connection conn = null;
    try {
        conn = MyConnexion.getCnx();
        conn.setAutoCommit(false);
        
        // Recalculer les totaux avant insertion
        commande.calculerTotaux();
        
        // Insérer la commande
        String sqlCommande = "INSERT INTO commande (numero, type, fournisseur_id, client_id, "
                           + "date_commande, date_livraison, statut, total_ht, total_ttc, "
                           + "tva, mode_paiement, remarques) "
                           + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sqlCommande, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, commande.getNumero());
            stmt.setString(2, commande.getType());
            
            // Gérer les valeurs null pour fournisseur/client
            if (commande.getFournisseurId() != null) {
                stmt.setInt(3, commande.getFournisseurId());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            
            if (commande.getClientId() != null) {
                stmt.setInt(4, commande.getClientId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            
            stmt.setDate(5, Date.valueOf(commande.getDateCommande()));
            stmt.setDate(6, commande.getDateLivraison() != null ? 
                Date.valueOf(commande.getDateLivraison()) : null);
            stmt.setString(7, commande.getStatut());
            stmt.setDouble(8, commande.getTotalHt());
            stmt.setDouble(9, commande.getTotalTtc());
            stmt.setDouble(10, commande.getTva());
            stmt.setString(11, commande.getModePaiement());
            stmt.setString(12, commande.getRemarques());
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int commandeId = rs.getInt(1);
                    commande.setId(commandeId);
                    
                    // Insérer les lignes de commande
                    if (commande.getLignes() != null && !commande.getLignes().isEmpty()) {
                        for (LigneCommande ligne : commande.getLignes()) {
                            if (!ajouterLigneCommande(commandeId, ligne, conn)) {
                                conn.rollback();
                                return false;
                            }
                        }
                    }
                    
                    // Si c'est une commande d'achat et qu'elle est confirmée, mettre à jour le stock
                    if ("ACHAT".equals(commande.getType()) && "CONFIRME".equals(commande.getStatut())) {
                        mettreAJourStockCommande(commande, conn);
                    }
                    
                    conn.commit();
                    return true;
                }
            }
        }
        conn.rollback();
        return false;
    } catch (SQLException e) {
        try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
        System.out.println("Erreur lors de l'ajout de la commande: " + e.getMessage());
        e.printStackTrace();
        return false;
    } finally {
        try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException e) {}
    }
}
    
    private boolean ajouterLigneCommande(int commandeId, LigneCommande ligne, Connection conn) throws SQLException {
        String sql = "INSERT INTO ligne_commande (commande_id, produit_id, quantite, prix_unitaire, remise) "
                   + "VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, commandeId);
            stmt.setInt(2, ligne.getProduitId());
            stmt.setInt(3, ligne.getQuantite());
            stmt.setDouble(4, ligne.getPrixUnitaire());
            stmt.setDouble(5, ligne.getRemise());
            
            return stmt.executeUpdate() > 0;
        }
    }
    
private void mettreAJourStockCommande(Commande commande, Connection conn) throws SQLException {
    if (commande.getLignes() == null) return;
    
    for (LigneCommande ligne : commande.getLignes()) {
        // Vérifier si le produit existe en stock
        String checkStock = "SELECT id FROM stock WHERE produit_id = ?";
        boolean stockExists = false;
        
        try (PreparedStatement checkStmt = conn.prepareStatement(checkStock)) {
            checkStmt.setInt(1, ligne.getProduitId());
            ResultSet rs = checkStmt.executeQuery();
            stockExists = rs.next();
        }
        
        if (stockExists) {
            // Mettre à jour le stock existant
            String updateStock = "UPDATE stock SET quantite = quantite + ? WHERE produit_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateStock)) {
                stmt.setInt(1, ligne.getQuantite());
                stmt.setInt(2, ligne.getProduitId());
                stmt.executeUpdate();
            }
        } else {
            // Insérer un nouveau stock
            String insertStock = "INSERT INTO stock (produit_id, quantite) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertStock)) {
                stmt.setInt(1, ligne.getProduitId());
                stmt.setInt(2, ligne.getQuantite());
                stmt.executeUpdate();
            }
        }
        
        // Enregistrer le mouvement
        String mouvement = "INSERT INTO mouvement_stock (produit_id, type, quantite, motif) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(mouvement)) {
            stmt.setInt(1, ligne.getProduitId());
            stmt.setString(2, "ENTREE");
            stmt.setInt(3, ligne.getQuantite());
            stmt.setString(4, "Commande " + commande.getNumero());
            stmt.executeUpdate();
        }
    }
}

    
    @Override
    public boolean update(Commande commande) {
        String sql = "UPDATE commande SET statut=?, date_livraison=?, total_ht=?, total_ttc=?, "
                   + "mode_paiement=?, remarques=? WHERE id=?";
        
        try (PreparedStatement stmt = MyConnexion.getCnx().prepareStatement(sql)) {
            stmt.setString(1, commande.getStatut());
            stmt.setDate(2, commande.getDateLivraison() != null ? 
                Date.valueOf(commande.getDateLivraison()) : null);
            stmt.setDouble(3, commande.getTotalHt());
            stmt.setDouble(4, commande.getTotalTtc());
            stmt.setString(5, commande.getModePaiement());
            stmt.setString(6, commande.getRemarques());
            stmt.setInt(7, commande.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de la commande: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public boolean delete(Commande commande) {
        return deleteById(commande.getId());
    }
    
    public boolean deleteById(int id) {
    String deleteLignes = "DELETE FROM ligne_commande WHERE commande_id = ?";
    String deleteCommande = "DELETE FROM commande WHERE id = ?";

    Connection conn = null;
    try {
        conn = MyConnexion.getCnx();
        conn.setAutoCommit(false);

        try (PreparedStatement stmt1 = conn.prepareStatement(deleteLignes)) {
            stmt1.setInt(1, id);
            stmt1.executeUpdate();
        }

        try (PreparedStatement stmt2 = conn.prepareStatement(deleteCommande)) {
            stmt2.setInt(1, id);
            stmt2.executeUpdate();
        }

        conn.commit();
        return true;

    } catch (SQLException e) {
        try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
        System.out.println("Erreur suppression commande : " + e.getMessage());
        return false;
    } finally {
        try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException e) {}
    }
}

    
    @Override
    public Commande findById(int id) {
        String sql = "SELECT c.*, f.nom as fournisseur_nom, cl.nom as client_nom FROM commande c "
                   + "LEFT JOIN fournisseur f ON c.fournisseur_id = f.id "
                   + "LEFT JOIN client cl ON c.client_id = cl.id WHERE c.id = ?";
        
        try (PreparedStatement stmt = MyConnexion.getCnx().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Commande commande = resultSetToCommande(rs);
                commande.setLignes(getLignesCommande(id));
                return commande;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche de la commande: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public List<Commande> findAll() {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT c.*, f.nom as fournisseur_nom, cl.nom as client_nom FROM commande c "
                   + "LEFT JOIN fournisseur f ON c.fournisseur_id = f.id "
                   + "LEFT JOIN client cl ON c.client_id = cl.id ORDER BY c.date_commande DESC";
        
        try (Statement stmt = MyConnexion.getCnx().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Commande commande = resultSetToCommande(rs);
                commandes.add(commande);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des commandes: " + e.getMessage());
        }
        return commandes;
    }
    
    private List<LigneCommande> getLignesCommande(int commandeId) {
        List<LigneCommande> lignes = new ArrayList<>();
        String sql = "SELECT lc.*, p.nom as produit_nom FROM ligne_commande lc "
                   + "LEFT JOIN produit p ON lc.produit_id = p.id WHERE lc.commande_id = ?";
        
        try (PreparedStatement stmt = MyConnexion.getCnx().prepareStatement(sql)) {
            stmt.setInt(1, commandeId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                LigneCommande ligne = new LigneCommande();
                ligne.setId(rs.getInt("id"));
                ligne.setCommandeId(rs.getInt("commande_id"));
                ligne.setProduitId(rs.getInt("produit_id"));
                ligne.setQuantite(rs.getInt("quantite"));
                ligne.setPrixUnitaire(rs.getDouble("prix_unitaire"));
                ligne.setRemise(rs.getDouble("remise"));
                ligne.setProduitNom(rs.getString("produit_nom"));
                lignes.add(ligne);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des lignes de commande: " + e.getMessage());
        }
        return lignes;
    }
    
    private Commande resultSetToCommande(ResultSet rs) throws SQLException {
        Commande commande = new Commande();
        commande.setId(rs.getInt("id"));
        commande.setNumero(rs.getString("numero"));
        commande.setType(rs.getString("type"));
        commande.setFournisseurId(rs.getInt("fournisseur_id"));
        commande.setClientId(rs.getInt("client_id"));
        commande.setDateCommande(rs.getDate("date_commande").toLocalDate());
        
        Date dateLivraison = rs.getDate("date_livraison");
        if (dateLivraison != null) {
            commande.setDateLivraison(dateLivraison.toLocalDate());
        }
        
        commande.setStatut(rs.getString("statut"));
        commande.setTotalHt(rs.getDouble("total_ht"));
        commande.setTotalTtc(rs.getDouble("total_ttc"));
        commande.setTva(rs.getDouble("tva"));
        commande.setModePaiement(rs.getString("mode_paiement"));
        commande.setRemarques(rs.getString("remarques"));
        commande.setFournisseurNom(rs.getString("fournisseur_nom"));
        commande.setClientNom(rs.getString("client_nom"));
        
        return commande;
    }
}