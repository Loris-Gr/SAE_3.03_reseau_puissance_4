package Puissance4Terminal.bd;
import java.sql.*;

public class PartieBD {

    ConnexionMySQL laConnexionMySQL;
    Statement st;

    public PartieBD(ConnexionMySQL laConnexionMySQL) {
        this.laConnexionMySQL = laConnexionMySQL;
    }

    public String getEquipeGagnante(int idEqu, int idPart) throws SQLException {
        String query = "SELECT idEquGagn AS equipeGagnante FROM PARTIE WHERE idEqu= ? AND idPart= ?";
        PreparedStatement stm = this.laConnexionMySQL.prepareStatement(query);
        stm.setInt(1, idEqu);   // Paramètre pour idEqu
        stm.setInt(2, idPart);  // Paramètre pour idPart
        ResultSet rs = stm.executeQuery();
        
        String equipeGagnante = "";
        if (rs.next()) {
            equipeGagnante = rs.getString("equipeGagnante");
        }
    
        rs.close();
        stm.close();
        return equipeGagnante;
    }
    public void enregistrerPartie(int idEquGagn, Date datePartie) throws SQLException {
        String query = "INSERT INTO PARTIE (datePart, idEquGagn) VALUES (?, ?)";
        PreparedStatement ps = this.laConnexionMySQL.prepareStatement(query);
        ps.setDate(1, datePartie);
        ps.setInt(2, idEquGagn);

        ps.executeUpdate();
        ps.close();
    }

    
    
}
