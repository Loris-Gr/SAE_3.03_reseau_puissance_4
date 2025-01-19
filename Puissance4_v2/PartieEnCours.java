import java.io.IOException;
import java.util.Scanner;

public class PartieEnCours extends Thread {
    
    private ModeleJeu modele;
    private Serveur serveur;
    private ClientHandler joueur1;
    private ClientHandler joueur2;

    public PartieEnCours(ClientHandler joueur1, ClientHandler joueur2, Serveur serveur) {
        this.modele = new ModeleJeu(Equipe.JAUNE);
        this.serveur = serveur;
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
    }

    public ClientHandler getJoueur1() {
        return joueur1;
    }

    public ClientHandler getJoueur2() {
        return joueur2;
    }

    public ModeleJeu getModele() {
        return modele;
    }

    @Override
    public void run() {
        while (true) {
            // Affichage de la grille et instructions pour le joueur
            String affichage = modele.getGrille().afficher2();

            joueur1.getOut().println(affichage); joueur2.getOut().println(affichage);
            System.out.println("go");

            Equipe couleur = modele.getJoueur();

            ClientHandler joueurActuel;

            if (couleur == Equipe.JAUNE) {
                joueurActuel = joueur1;
            }
            else {
                joueurActuel = joueur2;
            }
            joueur1.getOut().println("Joueur " + joueurActuel.getPseudo() + ", choisissez une colonne (0-6) :");
            joueur2.getOut().println("Joueur " + joueurActuel.getPseudo() + ", choisissez une colonne (0-6) :");

            // Lecture de la colonne choisie par le joueur
            
            int colonne;
            try {
                colonne = Integer.parseInt(joueurActuel.getIn().readLine());
                // Validation de l'entrée utilisateur
                if (colonne < 0 || colonne >= ModeleJeu.COLONNES) {
                    joueurActuel.getOut().println("Choix invalide, veuillez choisir une colonne entre 0 et 6.");
                    continue;
                }

                // Tentative de déposer le pion
                if (!modele.getGrille().poserPion(colonne, modele.getJoueur())) {
                    joueurActuel.getOut().println("Colonne pleine, choisissez une autre.");
                    continue;
                }

                // Vérification de victoire
                if (modele.estGagnee()) {
                    joueur1.getOut().println(modele.getGrille().afficher2()); joueur2.getOut().println(modele.getGrille().afficher2());
                    joueur1.getOut().println("Le joueur " + joueurActuel.getPseudo() + " a gagné !");
                    joueur2.getOut().println("Le joueur " + joueurActuel.getPseudo() + " a gagné !");

                    System.out.println("partie fini");
                    break;
                }

                // Vérification de match nul
                if (modele.estPerdu()) {
                    joueur1.getOut().println(modele.getGrille().afficher2()); joueur2.getOut().println(modele.getGrille().afficher2());
                    joueur1.getOut().println("Match nul !");
                    joueur2.getOut().println("Match nul !");

                    System.out.println("zebi");
                    break;
                }

                else {
                    joueurActuel.getOut().println("mauvais input");
                }

                // Changement de joueur
                modele.changerJoueur();
                } catch (Exception e) {
                    e.getMessage();
            }
        }
        try {
            this.serveur.finirPartie(this);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
