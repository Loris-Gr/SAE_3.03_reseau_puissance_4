package Puissance4Terminal.bd;
import java.sql.*;

public class Puissance4BD {

    ConnexionMySQL laConnexionMySQL;
    Statement st;

    public Puissance4BD(ConnexionMySQL laConnexionMySQL) {
        this.laConnexionMySQL = laConnexionMySQL;
    }

    public String getEquipeGagnante(int idEqu, int idPart) throws SQLException {
        String query = "SELECT idEquGagn AS equipeGagnante FROM PARTIE WHERE idEqu= " + idEqu + " AND idPart= " + idPart;
        Statement stm = this.laConnexionMySQL.createStatement();
        ResultSet rs = stm.executeQuery(query);
        String equipeGagnante = "";
        if (rs.next()) {
            equipeGagnante = rs.getString("equipeGagnante");
        }

        rs.close();
        stm.close();
        return equipeGagnante;
    }
    
}
