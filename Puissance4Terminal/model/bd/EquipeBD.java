package Puissance4Terminal.model.bd;


import java.sql.*;
import java.util.ArrayList;
import Puissance4Terminal.model.Equipe;
import BD.ConnexionMySQL;

public class EquipeBD {

    ConnexionMySQL laConnexionMySQL;
    Statement st;

    public EquipeBD(ConnexionMySQL laConnexionMySQL) {
        this.laConnexionMySQL = laConnexionMySQL;
    }

    public int getScore(Equipe equipe) throws SQLException {
        String query = "SELECT (score) AS score FROM EQUIPE WHERE nom = '" + equipe.getNom() + "'";
        Statement stm = this.laConnexionMySQL.createStatement();
        ResultSet rs = stm.executeQuery(query);
        int score = 0;

        if (rs.next()) {
            score = rs.getInt("score");
        }
        rs.close();
        stm.close();
        return score;
    }
    
}
