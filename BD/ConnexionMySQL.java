package BD;
import java.sql.*;

public class ConnexionMySQL {
    private Connection mysql;
    private boolean connecte = false;

    public ConnexionMySQL() throws ClassNotFoundException {
        this.mysql = null;
        this.connecte = false;
        
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur: Pilote JDBC MariaDB introuvable.");
            e.printStackTrace();
            throw e; 
        }
    }

    public void connecter(String nomServeur, String nomBase, String nomLogin, String motDePasse) throws SQLException {
        this.mysql = DriverManager.getConnection(
                "jdbc:mysql://" + nomServeur + ":3306/" + nomBase, nomLogin , motDePasse);
        this.connecte = true;
    }
    public void close() throws SQLException {
        if (this.mysql != null) {
            this.mysql.close();
        }
        this.connecte = false;
    }

    public boolean isConnecte() {
        return this.connecte;
    }

    public Blob createBlob() throws SQLException {
        return this.mysql.createBlob();
    }

    public Statement createStatement() throws SQLException {
        return this.mysql.createStatement();
    }

    public PreparedStatement prepareStatement(String requete) throws SQLException {
        return this.mysql.prepareStatement(requete);
    }

    public Connection getConnexion() {
        return this.mysql;
    }
}
