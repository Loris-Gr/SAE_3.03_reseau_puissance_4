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

    public void creerJoueur(String nomJoueur) throws SQLException{

        String query1 = "SELECT score FROM JOUEUR WHERE nomJoueur = '" + nomJoueur + "'";
        Statement stm = this.laConnexionMySQL.createStatement();
        ResultSet rs = stm.executeQuery(query1);

        if (!rs.next()) {
            String query = "INSERT INTO JOUEUR VALUES (?, 0)";
            PreparedStatement ps = this.laConnexionMySQL.prepareStatement(query);
            ps.setString(1, nomJoueur);
            ps.executeUpdate();
            ps.close();
        }
    }

    public void reloadPartieBD(){
        try {
            String query = "DELETE FROM PARTIE";
            PreparedStatement ps = this.laConnexionMySQL.prepareStatement(query);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void enregistrerPartie(String joueur1, String joueur2, String gagnant, Date datePartie) throws SQLException {
        String query = "INSERT INTO PARTIE (datePart, joueur1, joueur2, gagnant) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = this.laConnexionMySQL.prepareStatement(query);
        ps.setDate(1, datePartie);
        ps.setString(2, joueur1);
        ps.setString(3, joueur2);
        ps.setString(4, gagnant);

        ps.executeUpdate();
        ps.close();
    }

    public int getScore(String nomJoueur) throws SQLException {
        String query = "SELECT score FROM JOUEUR WHERE nomJoueur = '" + nomJoueur + "'";
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

    public void setScore(String nomJoueur, int newScore) throws SQLException {
        String query = "UPDATE JOUEUR SET score = ? WHERE nomJoueur = ?";
        PreparedStatement ps = this.laConnexionMySQL.prepareStatement(query);
        ps.setInt(1, newScore);
        ps.setString(2, nomJoueur);
        System.out.println("Score augmenté de 1 pour le joueur " + nomJoueur);

        ps.executeUpdate();
        ps.close();
    }

    public String historique(String nomJoueur) throws SQLException {
        String res = "date, joueur1, joueur2, gagnant \n";
        String query = "SELECT * FROM PARTIE WHERE joueur1 = '" + nomJoueur + "' or joueur2 = '" + nomJoueur + "'";
        Statement stm = this.laConnexionMySQL.createStatement();
        ResultSet rs = stm.executeQuery(query);
        while (rs.next()) {
            String date = rs.getString(2);
            String joueur1 = rs.getString(3);
            String joueur2 = rs.getString(4);
            String gagnant = rs.getString(5);
            res += "(" + date + ", " + joueur1 + ", " + joueur2 + ", " + gagnant +  ")\n";
        }
        return res;
    }
    
}
