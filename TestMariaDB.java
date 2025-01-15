import java.sql.*;

public class TestMariaDB {
    public static void main(String[] args) {
        String url = "jdbc:mysql://servinfo-maria:3306/DBvergerolle";
        String user = "vergerolle";
        String password = "vergerolle";

        try {
            Class.forName("org.mariadb.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connexion réussie !");
            conn.close();
        } catch (ClassNotFoundException e) {
            System.err.println("Pilote MariaDB introuvable.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Erreur SQL.");
            e.printStackTrace();
        }
    }
}
