package Puissance4Terminal.bd;
import java.sql.*;

import Puissance4Terminal.bd.EquipeBD;
import Puissance4Terminal.bd.Puissance4BD;
public class ExecutableBD {
    public static void main(String[] args) {
        try {
            ConnexionMySQL connexion = new ConnexionMySQL();

            connexion.connecter("servinfo-maria", "DBvergerolle", "vergerolle", "vergerolle");

            if (connexion.isConnecte()) {
                Puissance4BD bd = new Puissance4BD(connexion);
                EquipeBD equipeBD = new EquipeBD(connexion);
                
               
            } else {
                System.err.println("Erreur: Connection non établie.");
            }
        } catch (Exception e) {
            System.err.println("Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
