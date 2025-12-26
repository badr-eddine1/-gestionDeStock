/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.IDao;
import model.Client;
import connexion.MyConnexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientService implements IDao<Client> {
    
    @Override
    public boolean create(Client client) {
        String sql = "INSERT INTO client (code, nom, type, telephone, email, adresse, ville, credit) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = MyConnexion.getCnx().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, client.getCode());
            stmt.setString(2, client.getNom());
            stmt.setString(3, client.getType());
            stmt.setString(4, client.getTelephone());
            stmt.setString(5, client.getEmail());
            stmt.setString(6, client.getAdresse());
            stmt.setString(7, client.getVille());
            stmt.setDouble(8, client.getCredit());
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    client.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du client: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public boolean update(Client client) {
        String sql = "UPDATE client SET code=?, nom=?, type=?, telephone=?, email=?, adresse=?, ville=?, credit=? WHERE id=?";
        
        try (PreparedStatement stmt = MyConnexion.getCnx().prepareStatement(sql)) {
            stmt.setString(1, client.getCode());
            stmt.setString(2, client.getNom());
            stmt.setString(3, client.getType());
            stmt.setString(4, client.getTelephone());
            stmt.setString(5, client.getEmail());
            stmt.setString(6, client.getAdresse());
            stmt.setString(7, client.getVille());
            stmt.setDouble(8, client.getCredit());
            stmt.setInt(9, client.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification du client: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public boolean delete(Client client) {
        return deleteById(client.getId());
    }
    
    public boolean deleteById(int id) {
        String sql = "DELETE FROM client WHERE id = ?";
        try (PreparedStatement stmt = MyConnexion.getCnx().prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du client: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public Client findById(int id) {
        String sql = "SELECT * FROM client WHERE id = ?";
        
        try (PreparedStatement stmt = MyConnexion.getCnx().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return resultSetToClient(rs);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche du client: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM client ORDER BY nom";
        
        try (Statement stmt = MyConnexion.getCnx().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                clients.add(resultSetToClient(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des clients: " + e.getMessage());
        }
        return clients;
    }
    
    public boolean mettreAJourCredit(int clientId, double montant) {
        String sql = "UPDATE client SET credit = credit + ? WHERE id = ?";
        try (PreparedStatement stmt = MyConnexion.getCnx().prepareStatement(sql)) {
            stmt.setDouble(1, montant);
            stmt.setInt(2, clientId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du crédit: " + e.getMessage());
        }
        return false;
    }
    
    private Client resultSetToClient(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setId(rs.getInt("id"));
        client.setCode(rs.getString("code"));
        client.setNom(rs.getString("nom"));
        client.setType(rs.getString("type"));
        client.setTelephone(rs.getString("telephone"));
        client.setEmail(rs.getString("email"));
        client.setAdresse(rs.getString("adresse"));
        client.setVille(rs.getString("ville"));
        client.setCredit(rs.getDouble("credit"));
        return client;
    }
}
