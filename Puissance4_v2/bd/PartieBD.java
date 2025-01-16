package bd;
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
        stm.setInt(1, idEqu);   
        stm.setInt(2, idPart); 
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

    public int getScore(String nomEqu) throws SQLException {
        String query = "SELECT (score) AS score FROM EQUIPE WHERE symbole = '" + nomEqu + "'";
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

    public void setScore(String nomEqu, int newScore) throws SQLException {
        String query = "UPDATE EQUIPE SET score = ? WHERE symbole = ?";
        PreparedStatement ps = this.laConnexionMySQL.prepareStatement(query);
        ps.setInt(1, newScore);
        ps.setString(2, nomEqu);
        System.out.println("Score augmenté de 1 pour l'équipe " + nomEqu);

        ps.executeUpdate();
        ps.close();
        
    }

    
    
}
