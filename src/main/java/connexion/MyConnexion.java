package connexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnexion {

    private static final String URL = "jdbc:mysql://localhost:3306/gestion_stock_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static Connection cnx;

    private MyConnexion() {}

    public static Connection getCnx() throws SQLException {
        if (cnx == null || cnx.isClosed()) {
            cnx = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connexion BD établie");
        }
        return cnx;
    }

    public static boolean testerConnexion() {
        try {
            return getCnx() != null && !getCnx().isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public static void fermerConnexion() {
        try {
            if (cnx != null && !cnx.isClosed()) {
                cnx.close();
                System.out.println("Connexion BD fermée");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
