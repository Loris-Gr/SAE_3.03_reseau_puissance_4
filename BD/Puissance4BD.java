
package BD;
import java.sql.*;
public class Puissance4BD {
    private ConnexionMySQL laConnexion;

    public Puissance4BD(ConnexionMySQL laConnexion) {
        this.laConnexion = laConnexion;
    }

    
    public int getScore(String tableName) throws SQLException {
        String query = "SELECT (score) AS score FROM " + tableName;
        Statement stm = this.laConnexion.createStatement();
        ResultSet rs = stm.executeQuery(query);
        int score = 0;

        if (rs.next()) {
            score = rs.getInt("score");
        }
        rs.close();
        stm.close();
        return score;
    }

    public void updateScore(String tableName, int score) throws SQLException {
        String query = "UPDATE " + tableName + " SET score = " + score;
        Statement stm = this.laConnexion.createStatement();
        stm.executeUpdate(query);
        stm.close();
    }

}