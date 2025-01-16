import java.util.Scanner;

import bd.PartieBD;
import bd.ConnexionMySQL;

public class Puissance4Terminal {
    public static void main(String[] args) {
        ModeleJeu modele = new ModeleJeu(Equipe.JAUNE);
        Scanner scanner = new Scanner(System.in);
        ConnexionMySQL connexion = null;

        try {
            connexion = new ConnexionMySQL();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1); // Exit the program if the class is not found
        }
        try {
            connexion.connecter("servinfo-maria", "DBvergerolle", "vergerolle", "vergerolle");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        PartieBD partieBD = new PartieBD(connexion);


        while (true) {
            // Affichage de la grille et instructions pour le joueur
            modele.getGrille().afficher();
            System.out.println("Joueur " + modele.getJoueur() + ", choisissez une colonne (0-6) :");

            // Lecture de la colonne choisie par le joueur
            int colonne = scanner.nextInt();

            // Validation de l'entrée utilisateur
            if (colonne < 0 || colonne >= ModeleJeu.COLONNES) {
                System.out.println("Choix invalide, veuillez choisir une colonne entre 0 et 6.");
                continue;
            }

            // Tentative de déposer le pion
            if (!modele.getGrille().poserPion(colonne, modele.getJoueur())) {
                System.out.println("Colonne pleine, choisissez une autre.");
                continue;
            }

            // Vérification de victoire
            if (modele.estGagnee()) {
                modele.getGrille().afficher();
                System.out.println("Le joueur " + modele.getJoueur() + " a gagné !");
                break;
            }

            // Vérification de match nul
            if (modele.estPerdu()) {
                modele.getGrille().afficher();
                System.out.println("Match nul !");
                break;
            }

            // Changement de joueur
            modele.changerJoueur();
        }

        try {
            partieBD.enregistrerPartie(modele.getJoueur().getId(), new java.sql.Date(System.currentTimeMillis()));
            int score = partieBD.getScore(modele.getJoueur().getSymbole()) + 1;
            partieBD.setScore(modele.getJoueur().getSymbole(), score);

            int scoreEquipeJaune = partieBD.getScore(Equipe.JAUNE.getSymbole());    
            int scoreEquipeRouge = partieBD.getScore(Equipe.ROUGE.getSymbole());
            System.out.println("Score de l'équipe " + Equipe.JAUNE + " : " + scoreEquipeJaune);
            System.out.println("Score de l'équipe " + Equipe.ROUGE + " : " + scoreEquipeRouge);
            
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }




        scanner.close();
    }
}
