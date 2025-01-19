import java.sql.SQLException;

import bd.ConnexionMySQL;

public class ExecServ {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        ConnexionMySQL connexion = new ConnexionMySQL();
        connexion.connecter("localhost", "puissance4", "hun", "");
        Serveur serveur = new Serveur(4444, connexion);
        serveur.start();
    }
}
