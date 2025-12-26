package service;

import connexion.MyConnexion;
import java.sql.*;

public class UtilisateurService {

 public boolean authentifier(String username, String password) {
    String sql = "SELECT id, role FROM utilisateur "
               + "WHERE username = ? "
               + "AND password = SHA2(?, 256) "
               + "AND actif = 1";

    try (PreparedStatement stmt = MyConnexion.getCnx().prepareStatement(sql)) {
        stmt.setString(1, username);
        stmt.setString(2, password);

        ResultSet rs = stmt.executeQuery();
        return rs.next();

    } catch (SQLException e) {
        System.out.println("Erreur authentification : " + e.getMessage());
        return false;
    }
}
 public boolean inscrireUtilisateur(String username, String password,
                                   String nom, String prenom, String email) {

    String checkSql = "SELECT id FROM utilisateur WHERE username = ? OR email = ?";
    String insertSql = "INSERT INTO utilisateur "
            + "(username, password, nom, prenom, email, role, actif) "
            + "VALUES (?, SHA2(?, 256), ?, ?, ?, 'VENDEUR', 1)";

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

            return insertStmt.executeUpdate() > 0;
        }

    } catch (SQLException e) {
        System.out.println("Erreur inscription : " + e.getMessage());
        return false;
    }
}


}
