package bd;
import java.sql.*;

public class ExecutableBD {
    public static void main(String[] args) {
        try {
            ConnexionMySQL connexion = new ConnexionMySQL();

            connexion.connecter("localhost", "puissance4", "hun", "");

            if (connexion.isConnecte()) {
                Puissance4BD bd = new Puissance4BD(connexion);
                System.out.println("Connexion établie.");
            } else {
                System.err.println("Erreur: Connection non établie.");
            }
        } catch (Exception e) {
            System.err.println("Erreur: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
