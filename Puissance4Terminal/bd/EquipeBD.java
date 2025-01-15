package Puissance4Terminal.bd;


import java.sql.*;
import java.util.ArrayList;

import Puissance4Terminal.model.Equipe;

public class EquipeBD {

    ConnexionMySQL laConnexionMySQL;
    Statement st;

    public EquipeBD(ConnexionMySQL laConnexionMySQL) {
        this.laConnexionMySQL = laConnexionMySQL;
    }

    public int getScore(Equipe equipe) throws SQLException {
        String query = "SELECT (score) AS score FROM EQUIPE WHERE symbole = '" + equipe.getSymbole() + "'";
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

    public void setScore(Equipe e) throws SQLException {
        PreparedStatement ps = this.laConnexionMySQL.prepareStatement(
                "update EQUIPE set score = ? where idEqu = ?");
        ps.setInt(1, e.getScore());
        ps.setInt(2, e.getId());
    
        ps.executeUpdate();
    }

    
}
